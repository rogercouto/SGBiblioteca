package gb.control;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.model.Autor;
import gb.model.dao.AutorDAO;
import gb.model.exceptions.ValidationException;
import gb.view.DialogAutorView;
import swt.cw.util.Dialog;

public class DialogAutor extends DialogAutorView{

	private Autor autor;
	
	public DialogAutor(Shell parent) {
		super(parent);
		autor = new Autor();
		shell.setText("Inserir autor");
		initialize();
	}

	public DialogAutor(Shell parent, Autor autor) {
		super(parent);
		this.autor = autor;
		shell.setText("Editar autor");
		initialize();
	}
	
	private void initialize(){
		if (autor.getNome() != null)
			txtNome.setText(autor.getNome());
		if (autor.getSobrenome() != null)
			txtSobrenome.setText(autor.getSobrenome());
		if (autor.getInfo() != null)
			txtInfo.setText(autor.getInfo());
	}
	
	@Override
	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
		if (txtNome.getText().trim().length() > 0)
			autor.setNome(txtNome.getText().trim());
		if (txtSobrenome.getText().trim().length() > 0)
			autor.setSobrenome(txtSobrenome.getText().trim());
		if (txtInfo.getText().trim().length() > 0)
			autor.setInfo(txtInfo.getText().trim());
		AutorDAO dao = new AutorDAO();
		try {
			dao.insert(autor);
			dao.closeConnection();
			result = autor;
			shell.close();
		} catch (ValidationException e) {
			dao.closeConnection();
			Dialog.error(shell, e.getMessage());
		}
	}

}
