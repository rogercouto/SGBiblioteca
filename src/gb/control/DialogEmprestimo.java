package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
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
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.DialogEmprestimoView;
import swt.cw.Customizer;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;
import swt.cw.model.SaveListener;
import swt.cw.util.Dialog;

public class DialogEmprestimo extends DialogEmprestimoView {

	private Emprestimo emprestimo = new Emprestimo();
	
	public DialogEmprestimo(Shell parent) {
		super(parent);
		initialize();
	}

	private void initialize() {
		shell.setText("Novo empréstimo");
		Customizer.setTemporal(txtDataHora, "dd/MM/yyyy HH:mm");
		txtDataHora.setText(Main.FORMATADOR_DH.format(LocalDateTime.now()));
	}
	
	private void buscaUsuario(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar usuario");
		dialog.addColumn("nome", "Nome", true);
		dialog.setIcons(Main.ICONS);
		dialog.setWidth(0, 400);
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
			int numEmprestimos = dao.getNumEmprestimos(usuario);
			dao.closeConnection();
			if (numEmprestimos < usuario.getTipo().getNumLivrosEmp()){
				txtUsuario.setText(usuario.getNome());
				emprestimo.setUsuario(usuario);
				txtExemplar.setText("");
				emprestimo.setExemplar(null);
				calculaPrevisao();
				txtExemplar.setFocus();
			}else{
				Dialog.warning(shell, "Usuário já está com "+numEmprestimos+" livros emprestados!");
			}
		}
	}
	
	private void buscaExemplar(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar exemplar");
		dialog.addColumn("numRegistro", "Nº", true);
		dialog.addColumn("livro.titulo", "Título", true);
		dialog.setIcons(Main.ICONS);
		dialog.setWidth(1, 300);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				ExemplarDAO dao = new ExemplarDAO();
				List<Exemplar> list = dao.findList(index, text, new Situacao[]{Situacao.DISPONIVEL, Situacao.RESERVADO});
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
			if (exemplar.getSituacao().equals(Situacao.RESERVADO)){
				ReservaDAO dao = new ReservaDAO();
				Reserva reserva = dao.getLastReserva(exemplar);
				dao.closeConnection();
				if (reserva != null){
					if (emprestimo.getUsuario() == null){
						Dialog.warning(shell, "Livro reservado!");
						return;
					}else if (emprestimo.getUsuario().getId().intValue() != reserva.getUsuario().getId().intValue()){
						Dialog.warning(shell, "Livro reservado para outro usuário!");
						return;
					}
				}
			}
			txtExemplar.setText(exemplar.getNumRegistro()+" - "+exemplar.getLivro().getTitulo());
			emprestimo.setExemplar(exemplar);
			txtDataHora.setFocus();
		}
	}

	private void calculaPrevisao(){
		if (txtDataHora.getText().trim().length() == 16 && emprestimo.getUsuario() != null){
			TemporalAccessor ta = Main.FORMATADOR_DH.parse(txtDataHora.getText());
			LocalDate date = LocalDate.from(ta).plusDays(emprestimo.getUsuario().getTipo().getDiasEmprestimo());
			txtPrevDevolucao.setText(Main.FORMATADOR_D.format(date));
		}else{
			txtPrevDevolucao.setText("");
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
			emprestimo.setUsuario(null);
			txtExemplar.setText("");
			emprestimo.setExemplar(null);
		}else{
			buscaUsuario();
		}
		arg0.doit = false;
	}
	
	@Override
	protected void txtExemplarKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtExemplar.setText("");
			emprestimo.setExemplar(null);
		}else{
			buscaExemplar();
		}
		arg0.doit = false;
	}
	
	@Override
	protected void txtDataHoraModifyText(ModifyEvent arg0) {
		calculaPrevisao();
	}
	
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
		if (txtDataHora.getText().trim().length() == 16){
			TemporalAccessor ta = Main.FORMATADOR_DH.parse(txtDataHora.getText());
			emprestimo.setDataHora(LocalDateTime.from(ta));
		}
		Connection connection = ConnectionManager.getConnection();
		EmprestimoDAO dao = new EmprestimoDAO(connection);
		try {
			dao.insert(emprestimo);
			emprestimo.getExemplar().setSituacao(Situacao.EMPRESTADO);
			ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
			exemplarDAO.update(emprestimo.getExemplar());
			ReservaDAO reservaDAO = new ReservaDAO(connection);
			List<Reserva> reservas = reservaDAO.getList(emprestimo.getUsuario());
			for (Reserva reserva : reservas) {
				if (emprestimo.getExemplar().getNumRegistro().intValue() == reserva.getExemplar().getNumRegistro().intValue()){
					reserva.setDataHoraRetirada(LocalDateTime.now());
					reservaDAO.update(reserva);
					break;
				}
			}
			ConnectionManager.closeConnection(connection);
			result = emprestimo;
			shell.close();
		} catch (ValidationException e) {
			ConnectionManager.closeConnection(connection);
			Dialog.error(shell, e.getMessage());
		}
	}
	
}
