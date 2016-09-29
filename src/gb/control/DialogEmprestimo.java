package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import gb.model.dao.PendenciaDAO;
import gb.model.dao.ReservaDAO;
import gb.model.dao.UsuarioDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.util.TemporalUtil;
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
		txtDataHora.setText(TemporalUtil.formatDateTime(LocalDateTime.now()));
	}
	
	private void buscaUsuario(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar usuario");
		dialog.addColumn("nome", "Nome", true);
		dialog.addColumn("login", "Login", true);
		dialog.setIcons(Main.ICONS);
		dialog.setWidth(0, 200);
		dialog.setWidth(1, 100);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				UsuarioDAO dao = new UsuarioDAO();
				List<Usuario> list = dao.findList(index, text);
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
			int numEmprestimos = dao.getNumEmprestimosAtivos(usuario);
			if (numEmprestimos >= usuario.getTipo().getNumLivrosEmp()){
				Dialog.warning(shell, "Usuário já está com "+numEmprestimos+" livros emprestados!");
				dao.closeConnection();
				return;
			}
			EmprestimoDAO eDao = new EmprestimoDAO(dao.getConnection());
			int numPend = eDao.getNumEmprestimosPendentes(usuario);
			PendenciaDAO pDao = new PendenciaDAO(dao.getConnection());
			numPend += pDao.getCount(usuario);
			if (numPend > 1){
				Dialog.warning(shell, "Usuário já está com "+numPend+" pendências!");
				dao.closeConnection();
				return;
			}
			dao.closeConnection();
			txtUsuario.setText(usuario.getNome());
			emprestimo.setUsuario(usuario);
			txtExemplar.setText("");
			emprestimo.setExemplar(null);
			calculaPrevisao();
			txtExemplar.setFocus();
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
			if (exemplar.getSituacao().equals(Situacao.EMPRESTADO)){
				Dialog.warning(shell, "Livro não disponível!");
				return;
			}
			if (exemplar.isFixo()){
				Dialog.warning(shell, "Livro não pode ser emprestado!");
				return;
			}
			if (exemplar.isReservado()){
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
			LocalDateTime dateTime = TemporalUtil.parseDateTime(txtDataHora.getText()).plusDays(emprestimo.getUsuario().getTipo().getDiasEmprestimo());
			txtPrevDevolucao.setText(TemporalUtil.formatDate(LocalDate.from(dateTime)));
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
		if (txtDataHora.getText().trim().length() == 16)
			emprestimo.setDataHora(TemporalUtil.parseDateTime(txtDataHora.getText()));
		Connection connection = ConnectionManager.getConnection();
		EmprestimoDAO dao = new EmprestimoDAO(connection);
		try {
			dao.insert(emprestimo);
			ReservaDAO reservaDAO = new ReservaDAO(connection);
			List<Reserva> reservas = reservaDAO.getList(emprestimo.getUsuario());
			for (Reserva reserva : reservas) {
				if (reserva.getDataHoraRetirada() != null)
					continue;
				if (emprestimo.getExemplar().getNumRegistro().intValue() == reserva.getExemplar().getNumRegistro().intValue()){
					reserva.setDataHoraRetirada(LocalDateTime.now());
					reservaDAO.update(reserva);
					break;
				}
			}
			ConnectionManager.closeConnection(connection);
			result = emprestimo;
			Dialog.confirmation(shell, "Empréstimo realizado!");
			shell.close();
		} catch (ValidationException e) {
			ConnectionManager.closeConnection(connection);
			Dialog.error(shell, e.getMessage());
		}
	}
	
}
