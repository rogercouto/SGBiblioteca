package gb.control;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import gb.model.Usuario;
import gb.model.dao.UsuarioDAO;
import gb.model.exceptions.ValidationException;
import gb.view.GerenciaView;
import swt.cw.model.RegSource;
import swt.cw.model.SaveListener;
import swt.cw.util.Dialog;

public class GerenciaUsuarios extends GerenciaView {

	public GerenciaUsuarios(Composite parent) {
		super(parent);
		initialize();
	}
	
	private void initialize(){
		setHeader("Usuários", SWT.COLOR_WHITE, SWT.COLOR_DARK_BLUE);
		UsuarioDAO dao = new UsuarioDAO();
		List<Usuario> usuarios = dao.getList();
		dao.closeConnection();
		regViwer.addColumn("id", "Nº", false);
		regViwer.addColumn("nome", "Nome", true);
		regViwer.addColumn("telefone", "Telefone", false);
		regViwer.addColumn("celular", "Celular", false);
		regViwer.addColumn("eMail", "E-mail", false);
		regViwer.setPattern(2, "(##)####-####");
		regViwer.setPattern(3, "(##)####-####");
		regViwer.setWidth(1, 250);
		regViwer.setWidth(4, 200);
		regViwer.setData(usuarios);
		regViwer.setRegSource(new RegSource() {
			@Override
			public List<?> getList(int index, LocalDate di, LocalDate df, String text) {
				UsuarioDAO dao = new UsuarioDAO();
				List<Usuario> usuarios = dao.findList(text);
				dao.closeConnection();
				return usuarios;
			}
		});
		regViwer.setInsertListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				DialogUsuario dialog = new DialogUsuario(getShell());
				return dialog.open();
			}
		});
		regViwer.setUpdateListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				DialogUsuario dialog = new DialogUsuario(getShell(), (Usuario)regViwer.getSelection());
				return dialog.open();
			}
		});
		regViwer.setDeleteListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				Usuario usuario = (Usuario)regViwer.getSelection();
				if (usuario == null || !Dialog.questionExclude())
					return false;
				UsuarioDAO dao = new UsuarioDAO();
				try {
					dao.delete(usuario);
					dao.closeConnection();
					return true;
				} catch (ValidationException e) {
					Dialog.error(getShell(), e.getMessage());
				}
				dao.closeConnection();
				return false;
			}
		});
		regViwer.open();
	}

}
