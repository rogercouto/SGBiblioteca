package gb.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;

import gb.model.data.ConnectionManager;
import gb.view.DialogLoginView;
import swt.cw.util.Dialog;

public class DialogLogin extends DialogLoginView {

	public DialogLogin(String username) {
		super();
		if (username != null){
			txtUsuario.setText(username);
			txtSenha.setFocus();
		}
		
	}

	private void login(){
		if (!ConnectionManager.testConnection(txtUsuario.getText(), txtSenha.getText())){
			Dialog.warning(shell, "Usuário ou senha invalido(s)");
			return;
		}
		String[] li = new String[2];
		li[0] = txtUsuario.getText();
		li[1] = txtSenha.getText();
		result = li;
		shell.close();
	}
	
	protected void txtUsuarioKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
			txtSenha.setFocus();
	}
	
	protected void txtSenhaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
			login();
	}
	
	protected void btnOkWidgetSelected(SelectionEvent arg0) {
		login();
	}
	
}
