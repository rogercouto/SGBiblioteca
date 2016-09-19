package gb.control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.Main;
import gb.model.Emprestimo;
import gb.model.Exemplar;
import gb.model.Reserva;
import gb.model.Situacao;
import gb.model.Usuario;
import gb.model.dao.EmprestimoDAO;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.ReservaDAO;
import gb.model.dao.UsuarioDAO;
import gb.model.exceptions.ValidationException;
import gb.util.TemporalUtil;
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
		txtDataHora.setText(TemporalUtil.formatDateTime(LocalDateTime.now()));
		txtLimite.setText(TemporalUtil.formatDate(LocalDate.now().plusDays(Reserva.DIAS_RESERVA)));
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
		dialog.addColumn("livro.isbn", "ISBN", true);
		dialog.setIcons(Main.ICONS);
		dialog.setWidth(0, 80);
		dialog.setWidth(1, 250);
		dialog.setWidth(2, 80);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				ExemplarDAO dao = new ExemplarDAO();
				List<Exemplar> list = dao.findList(index, text);
				dao.closeConnection();
				return list;
			}
		});
		Exemplar exemplar = (Exemplar)dialog.open();
		if (exemplar != null){
			if (exemplar.isFixo()){
				Dialog.warning(shell, "Livro não pode ser emprestado!");
				return;
			}else if (exemplar.isReservado()){
				Dialog.warning(shell, "Livro já está reservado!");
				return;
			}
			if (exemplar.getSituacao().equals(Situacao.EMPRESTADO)){
				EmprestimoDAO dao = new EmprestimoDAO();
				Emprestimo emprestimo = dao.getLastEmprestimo(exemplar);
				if (emprestimo.getUsuario() != null 
						&& reserva.getUsuario().getId().intValue() == emprestimo.getUsuario().getId().intValue()){
					Dialog.warning(shell, "O livro já está com o usuário!");
					return;
				}
				if (emprestimo != null)
					Dialog.message(shell, "Atenção", "Livro está emprestado, previsão de retorno: "+
						TemporalUtil.formatDate(emprestimo.getPrevisaoDevolucao()));
				if (reserva.getUsuario() != null)
					txtLimite.setText(
							TemporalUtil.formatDate(
									emprestimo.getPrevisaoDevolucao().plusDays(Reserva.DIAS_RESERVA)));
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
		if (txtDataHora.getText().trim().length() == 16)
			reserva.setDataHora(TemporalUtil.parseDateTime(txtDataHora.getText()));
		if (txtLimite.getText().trim().length() == 10)
			reserva.setDataLimite(TemporalUtil.parseDate(txtLimite.getText()));
		ReservaDAO dao = new ReservaDAO();
		try {
			dao.insert(reserva);
			dao.closeConnection();
			result = reserva;
			Dialog.confirmation(shell, "Reserva realizada!");
			shell.close();
		} catch (ValidationException e) {
			dao.closeConnection();
			Dialog.error(shell, e.getMessage());
		}
	}
	
}
