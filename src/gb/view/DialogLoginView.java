package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;


public class DialogLoginView {

	protected Object result;
	protected Shell shell;
	protected Group group;
	protected Label lblNewLabel;
	protected Text txtUsuario;
	protected Label lblNewLabel_1;
	protected Text txtSenha;
	protected Button btnOk;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogLoginView() {
		super();
		createContents();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		shell.pack();
		Screen.centralize(shell);
		shell.open();
		shell.layout();
		Display display = Display.getCurrent();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("Sistema de Gerenciamento de Biblioteca - Login");
		shell.setLayout(new GridLayout(1, false));
		group = new Group(shell, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 71, 17);
		lblNewLabel.setText("Usuário:");
		txtUsuario = new Text(group, SWT.BORDER);
		txtUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtUsuarioKeyPressed(arg0);
			}
		});
		GridData gd_txtUsuario = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtUsuario.widthHint = 150;
		txtUsuario.setLayoutData(gd_txtUsuario);
		lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Senha:");
		txtSenha = new Text(group, SWT.BORDER | SWT.PASSWORD);
		txtSenha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtSenhaKeyPressed(arg0);
			}
		});
		txtSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnOkWidgetSelected(arg0);
			}
		});
		btnOk.setImage(SWTResourceManager.getImage(DialogLoginView.class, "/img/ic_done_black_24dp.png"));

	}
	protected void btnOkWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtUsuarioKeyPressed(KeyEvent arg0) {
	}
	protected void txtSenhaKeyPressed(KeyEvent arg0) {
	}
}
