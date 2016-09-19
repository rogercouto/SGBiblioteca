package gb.view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DialogDevolucaoView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Group group;
	protected Composite composite;
	protected Button btnConfirma;
	protected Label lblNewLabel;
	protected Text txtExemplar;
	protected Button btnBuscar;
	protected Label lblNewLabel_1;
	protected Text txtMulta;
	protected Text txtUsuario;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogDevolucaoView(Shell parent) {
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
		shell.setImage(SWTResourceManager.getImage(DialogDevolucaoView.class, "/img/ic_assignment_return_black_24dp.png"));
		shell.setSize(449, 199);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		group = new Group(shell, SWT.NONE);
		GridLayout gl_group = new GridLayout(3, false);
		gl_group.horizontalSpacing = 0;
		group.setLayout(gl_group);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Exemplar:   ");
		txtExemplar = new Text(group, SWT.BORDER);
		txtExemplar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtExemplarKeyPressed(arg0);
			}
		});
		txtExemplar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnBuscar = new Button(group, SWT.NONE);
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscarWidgetSelected(arg0);
			}
		});
		btnBuscar.setImage(SWTResourceManager.getImage(DialogDevolucaoView.class, "/img/find.png"));
		Label lblUsurio = new Label(group, SWT.NONE);
		lblUsurio.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsurio.setText("Usuário:   ");
		txtUsuario = new Text(group, SWT.BORDER);
		txtUsuario.setEditable(false);
		txtUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Multa:   ");
		txtMulta = new Text(group, SWT.BORDER | SWT.RIGHT);
		txtMulta.setText("0,00");
		GridData gd_txtMulta = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMulta.widthHint = 120;
		txtMulta.setLayoutData(gd_txtMulta);
		txtMulta.setEditable(false);
		new Label(group, SWT.NONE);
		composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		btnConfirma = new Button(composite, SWT.NONE);
		btnConfirma.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnConfirmaWidgetSelected(arg0);
			}
		});
		btnConfirma.setImage(SWTResourceManager.getImage(DialogDevolucaoView.class, "/img/ic_assignment_return_black_24dp.png"));
		btnConfirma.setText("Confirma");
		Screen.centralize(shell, getParent());
	}
	
	protected void txtExemplarKeyPressed(KeyEvent arg0) {
	}
	protected void btnBuscarWidgetSelected(SelectionEvent arg0) {
	}
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
	}
}
