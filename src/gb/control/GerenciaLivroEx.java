package gb.control;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import gb.model.Livro;
import gb.model.dao.LivroDAO;
import gb.view.GerenciaView;
import swt.cw.model.RegSource;
import swt.cw.model.SaveListener;

public class GerenciaLivroEx extends GerenciaView {

	List<Livro> livros;
	
	public GerenciaLivroEx(Composite parent) {
		super(parent);
		initialize();
	}

	private void initialize(){
		LivroDAO dao = new LivroDAO();
		livros = dao.getList();
		dao.closeConnection();
		regViwer.addColumn("id", "Nº", false);
		regViwer.addColumn("titulo", "Título", true);
		regViwer.addColumn("resumo", "Resumo", true);
		regViwer.addColumn("numExemplares", "Exemplares", false);
		regViwer.setWidth(0, 50);
		regViwer.setWidth(1, 350);
		regViwer.setWidth(2, 250);
		regViwer.setWidth(3, 50);
		regViwer.setData(livros);
		regViwer.setFilter(false);
		regViwer.setRegSource(new RegSource() {
			@Override
			public List<?> getList(int index, LocalDate di, LocalDate df, String text) {
				LivroDAO dao = new LivroDAO();
				List<Livro> list = dao.findList(index == 0?"titulo":"resumo", text);
				dao.closeConnection();
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
		regViwer.setOrientation(SWT.UP);
		
		regViwer.open();
	}
	
	
}
