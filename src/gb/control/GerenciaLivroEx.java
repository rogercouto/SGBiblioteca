package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import gb.model.Autor;
import gb.model.Livro;
import gb.model.dao.AutorDAO;
import gb.model.dao.LivroDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.GerenciaView;
import swt.cw.model.RegSource;
import swt.cw.model.SaveListener;
import swt.cw.util.Dialog;

public class GerenciaLivroEx extends GerenciaView {

	
	public GerenciaLivroEx(Composite parent) {
		super(parent);
		initialize();
	}

	private void initialize(){
		setHeader("Livros/Exemplares", SWT.COLOR_WHITE, SWT.COLOR_DARK_CYAN);
		LivroDAO dao = new LivroDAO();
		List<Livro> livros = dao.getList();
		dao.closeConnection();
		regViwer.addColumn("id", "Nº", false);
		regViwer.addColumn("titulo", "Título", true);
		regViwer.addColumn("nomeAutores", "Autor(es)", true);
		regViwer.addColumn("exemplares.size", "Exemplares", false);
		regViwer.setWidth(0, 50);
		regViwer.setWidth(1, 350);
		regViwer.setWidth(2, 250);
		regViwer.setWidth(3, 50);
		regViwer.setData(livros);
		regViwer.setFilter(false);
		regViwer.setRegSource(new RegSource() {
			@Override
			public List<?> getList(int index, LocalDate di, LocalDate df, String text) {
				Connection connection = ConnectionManager.getConnection();
				LivroDAO livroDAO = new LivroDAO(connection);
				List<Livro> list;
				if (text.trim().length() == 0){
					list = livroDAO.getList();
					ConnectionManager.closeConnection(connection);
					return list;
				}
				switch (index) {
				case 0:
					list = livroDAO.findList("titulo", text);
					break;
				case 1:
					AutorDAO autorDAO = new AutorDAO(connection);
					List<Autor> autores = autorDAO.findList(text);
					list = livroDAO.findList(autores);
					break;
				default:
					list = new ArrayList<>();
					break;
				}
				ConnectionManager.closeConnection(connection);
				return list;
			}
		});
		regViwer.setInsertListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				DialogLivro dialog = new DialogLivro(getShell());
				return dialog.open();
			}
		});
		regViwer.setUpdateListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				DialogLivro dialog = new DialogLivro(getShell(), (Livro)regViwer.getSelection());
				return dialog.open();
			}
		});
		regViwer.setDeleteListener(new SaveListener() {
			@Override
			public Object handleEvent(Object object) {
				Livro livro = (Livro)regViwer.getSelection();
				if (livro == null || !Dialog.questionExclude())
					return false;
				LivroDAO dao = new LivroDAO();
				try {
					dao.delete(livro);
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
