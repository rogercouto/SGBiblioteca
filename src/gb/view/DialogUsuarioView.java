package gb.view;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class DialogUsuarioView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Group group;
	protected Group grpContato;
	protected Group grpEndereo;
	protected Composite composite;
	protected Button btnSalvar;
	protected Text txtNome;
	protected Text txtCpf;
	protected Combo cmbTipo;
	protected Text txtTelefone;
	protected Text txtCel;
	protected Text txtEmail;
	protected Text txtLogradouro;
	protected Text txtCidade;
	protected Text txtNumero;
	protected Button btnCidade;
	protected Text txtBairro;
	protected Text txtCep;
	protected Text txtComplemento;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogUsuarioView(Shell parent) {
		super(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		createContents();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(481, 506);
		shell.setText(getText());
		shell.setLayout(new GridLayout());
		group = new Group(shell, SWT.NONE);
		GridLayout gl_group = new GridLayout(2, false);
		gl_group.marginWidth = 10;
		group.setLayout(gl_group);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 100;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Nome:");
		txtNome = new Text(group, SWT.BORDER);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("CPF:");
		txtCpf = new Text(group, SWT.BORDER);
		GridData gd_txtCpf = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtCpf.widthHint = 180;
		txtCpf.setLayoutData(gd_txtCpf);
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Tipo:");
		cmbTipo = new Combo(group, SWT.READ_ONLY);
		GridData gd_cmbTipo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cmbTipo.widthHint = 200;
		cmbTipo.setLayoutData(gd_cmbTipo);
		grpContato = new Group(shell, SWT.NONE);
		grpContato.setText("Contato");
		GridLayout gl_grpContato = new GridLayout(4, false);
		gl_grpContato.marginWidth = 10;
		grpContato.setLayout(gl_grpContato);
		grpContato.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		Label lblNewLabel_3 = new Label(grpContato, SWT.NONE);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel_3 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_3.widthHint = 100;
		lblNewLabel_3.setLayoutData(gd_lblNewLabel_3);
		lblNewLabel_3.setText("Telefone:");
		txtTelefone = new Text(grpContato, SWT.BORDER);
		txtTelefone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_4 = new Label(grpContato, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("Celular:");
		txtCel = new Text(grpContato, SWT.BORDER);
		txtCel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_5 = new Label(grpContato, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("E-mail:");
		txtEmail = new Text(grpContato, SWT.BORDER);
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		grpEndereo = new Group(shell, SWT.NONE);
		grpEndereo.setText("Endereço");
		GridLayout gl_grpEndereo = new GridLayout(5, false);
		gl_grpEndereo.marginWidth = 10;
		grpEndereo.setLayout(gl_grpEndereo);
		grpEndereo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Label lblNewLabel_6 = new Label(grpEndereo, SWT.NONE);
		lblNewLabel_6.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel_6 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_6.widthHint = 100;
		lblNewLabel_6.setLayoutData(gd_lblNewLabel_6);
		lblNewLabel_6.setText("Logradouro:");
		txtLogradouro = new Text(grpEndereo, SWT.BORDER);
		txtLogradouro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_8 = new Label(grpEndereo, SWT.NONE);
		lblNewLabel_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_8.setText("Nº:");
		txtNumero = new Text(grpEndereo, SWT.BORDER);
		GridData gd_txtNumero = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_txtNumero.widthHint = 60;
		txtNumero.setLayoutData(gd_txtNumero);
		Label lblNewLabel_7 = new Label(grpEndereo, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setText("Cidade:");
		txtCidade = new Text(grpEndereo, SWT.BORDER);
		txtCidade.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtCidadeKeyPressed(arg0);
			}
		});
		txtCidade.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		btnCidade = new Button(grpEndereo, SWT.NONE);
		btnCidade.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnCidadeWidgetSelected(arg0);
			}
		});
		btnCidade.setImage(SWTResourceManager.getImage(DialogUsuarioView.class, "/img/ic_search_black_18dp.png"));
		Label lblNewLabel_9 = new Label(grpEndereo, SWT.NONE);
		lblNewLabel_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_9.setText("Bairro:");
		txtBairro = new Text(grpEndereo, SWT.BORDER);
		txtBairro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(grpEndereo, SWT.NONE);
		new Label(grpEndereo, SWT.NONE);
		Label lblNewLabel_10 = new Label(grpEndereo, SWT.NONE);
		lblNewLabel_10.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_10.setText("Cep:");
		txtCep = new Text(grpEndereo, SWT.BORDER);
		txtCep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpEndereo, SWT.NONE);
		new Label(grpEndereo, SWT.NONE);
		new Label(grpEndereo, SWT.NONE);
		Label lblNewLabel_11 = new Label(grpEndereo, SWT.NONE);
		lblNewLabel_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_11.setText("Complemento:");
		txtComplemento = new Text(grpEndereo, SWT.BORDER);
		txtComplemento.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpEndereo, SWT.NONE);
		new Label(grpEndereo, SWT.NONE);
		new Label(grpEndereo, SWT.NONE);
		composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		btnSalvar = new Button(composite, SWT.NONE);
		btnSalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnSalvarWidgetSelected(arg0);
			}
		});
		btnSalvar.setImage(SWTResourceManager.getImage(DialogUsuarioView.class, "/img/ic_save_black_24dp.png"));
		btnSalvar.setText("Salvar");
	}

	protected void btnCidadeWidgetSelected(SelectionEvent arg0) {
	}
	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtCidadeKeyPressed(KeyEvent arg0) {
	}
}
