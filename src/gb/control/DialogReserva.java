package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.Main;
import gb.model.Exemplar;
import gb.model.Reserva;
import gb.model.Situacao;
import gb.model.Usuario;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.ReservaDAO;
import gb.model.dao.UsuarioDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.DialogReservaView;
import swt.cw.Customizer;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;
import swt.cw.model.SaveListener;
import swt.cw.util.Dialog;

public class DialogReserva extends DialogReservaView {

	private Reserva reserva = new Reserva();
	
	public DialogReserva(Shell parent) {
		super(parent);
		initialize();
	}

	private void initialize() {
		shell.setText("Nova reserva");
		Customizer.setTemporal(txtDataHora, "dd/MM/yyyy HH:mm");
		Customizer.setTemporal(txtLimite, "dd/MM/yyyy");
		txtDataHora.setText(Main.FORMATADOR_DH.format(LocalDateTime.now()));
		txtLimite.setText(Main.FORMATADOR_D.format(LocalDate.now().plusDays(Reserva.DIAS_RESERVA)));
	}
	
	private void buscaUsuario(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar usuario");
		dialog.addColumn("nome", "Nome", true);
		dialog.setWidth(0, 400);
		dialog.setIcons(Main.ICONS);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				UsuarioDAO dao = new UsuarioDAO();
				List<Usuario> list = dao.findList(text);
				return list;
			}
		});
		dialog.setInsertListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				DialogUsuario dialog = new DialogUsuario(shell);
				return dialog.open();
			}
		});
		Usuario usuario = (Usuario)dialog.open();
		if (usuario != null){
			UsuarioDAO dao = new UsuarioDAO();
			int numReserva = dao.getNumReservas(usuario);
			dao.closeConnection();
			if (numReserva < usuario.getTipo().getNumLivrosRes()){
				txtUsuario.setText(usuario.getNome());
				reserva.setUsuario(usuario);
				txtExemplar.setText("");
				reserva.setExemplar(null);
				txtExemplar.setFocus();
			}else{
				Dialog.warning(shell, "Usuário já está com "+numReserva+" reservas pendentes!");
			}
		}
	}
	
	private void buscaExemplar(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar exemplar");
		dialog.addColumn("numRegistro", "Nº", true);
		dialog.addColumn("livro.titulo", "Título", true);
		dialog.setWidth(1, 300);
		dialog.setIcons(Main.ICONS);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				ExemplarDAO dao = new ExemplarDAO();
				List<Exemplar> list = dao.findList(index, text, Situacao.DISPONIVEL);
				dao.closeConnection();
				return list;
			}
		});
		Exemplar exemplar = (Exemplar)dialog.open();
		if (exemplar != null){
			if (exemplar.getFixo()){
				Dialog.warning(shell, "Livro não pode ser emprestado!");
				return;
			}
			txtExemplar.setText(exemplar.getNumRegistro()+" - "+exemplar.getLivro().getTitulo());
			reserva.setExemplar(exemplar);
			txtDataHora.setFocus();
		}
	}

	@Override
	protected void btnBuscaUsuarioWidgetSelected(SelectionEvent arg0) {
		buscaUsuario();
	}
	
	@Override
	protected void btnBuscaExemplarWidgetSelected(SelectionEvent arg0) {
		buscaExemplar();
	}
	
	@Override
	protected void txtUsuarioKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtUsuario.setText("");
			reserva.setUsuario(null);
			txtExemplar.setText("");
			reserva.setExemplar(null);
		}else{
			buscaUsuario();
		}
		arg0.doit = false;
	}
	
	@Override
	protected void txtExemplarKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtExemplar.setText("");
			reserva.setExemplar(null);
		}else{
			buscaExemplar();
		}
		arg0.doit = false;
	}
	
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
		if (txtDataHora.getText().trim().length() == 16){
			TemporalAccessor ta = Main.FORMATADOR_DH.parse(txtDataHora.getText());
			reserva.setDataHora(LocalDateTime.from(ta));
			reserva.setDataLimite(LocalDate.from(ta).plusDays(Reserva.DIAS_RESERVA));
		}
		Connection connection = ConnectionManager.getConnection();
		ReservaDAO dao = new ReservaDAO(connection);
		try {
			dao.insert(reserva);
			reserva.getExemplar().setSituacao(Situacao.RESERVADO);
			ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
			exemplarDAO.update(reserva.getExemplar());
			ConnectionManager.closeConnection(connection);
			result = reserva;
			shell.close();
		} catch (ValidationException e) {
			ConnectionManager.closeConnection(connection);
			Dialog.error(shell, e.getMessage());
		}
	}
	
}
