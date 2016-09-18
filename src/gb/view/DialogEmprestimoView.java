package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class DialogEmprestimoView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Group grpUsurio;
	protected Label lblNewLabel;
	protected Text txtUsuario;
	protected Button btnBuscaUsuario;
	protected Label lblNewLabel_1;
	protected Text txtExemplar;
	protected Button btnBuscaExemplar;
	protected Label lblNewLabel_2;
	protected Text txtDataHora;
	protected Label lblNewLabel_3;
	protected Text txtPrevDevolucao;
	protected Composite composite;
	protected Button btnConfirma;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogEmprestimoView(Shell parent) {
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
		shell.setImage(SWTResourceManager.getImage(DialogEmprestimoView.class, "/img/ic_exit_to_app_black_24dp.png"));
		shell.setSize(489, 235);
		shell.setText(getText());
		shell.setLayout(new GridLayout());
		grpUsurio = new Group(shell, SWT.NONE);
		grpUsurio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_grpUsurio = new GridLayout(3, false);
		gl_grpUsurio.horizontalSpacing = 1;
		grpUsurio.setLayout(gl_grpUsurio);
		lblNewLabel = new Label(grpUsurio, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Usuário:   ");
		txtUsuario = new Text(grpUsurio, SWT.BORDER);
		txtUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtUsuarioKeyPressed(arg0);
			}
		});
		txtUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnBuscaUsuario = new Button(grpUsurio, SWT.NONE);
		btnBuscaUsuario.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscaUsuarioWidgetSelected(arg0);
			}
		});
		btnBuscaUsuario.setImage(SWTResourceManager.getImage(DialogEmprestimoView.class, "/img/find.png"));
		lblNewLabel_1 = new Label(grpUsurio, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Exemplar:   ");
		txtExemplar = new Text(grpUsurio, SWT.BORDER);
		txtExemplar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtExemplarKeyPressed(arg0);
			}
		});
		txtExemplar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnBuscaExemplar = new Button(grpUsurio, SWT.NONE);
		btnBuscaExemplar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscaExemplarWidgetSelected(arg0);
			}
		});
		btnBuscaExemplar.setImage(SWTResourceManager.getImage(DialogEmprestimoView.class, "/img/find.png"));
		lblNewLabel_2 = new Label(grpUsurio, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Data/Hora:   ");
		txtDataHora = new Text(grpUsurio, SWT.BORDER);
		txtDataHora.setEditable(false);
		txtDataHora.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				txtDataHoraModifyText(arg0);
			}
		});
		GridData gd_txtDataHora = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtDataHora.widthHint = 150;
		txtDataHora.setLayoutData(gd_txtDataHora);
		new Label(grpUsurio, SWT.NONE);
		lblNewLabel_3 = new Label(grpUsurio, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("Devolução Prev.:   ");
		txtPrevDevolucao = new Text(grpUsurio, SWT.BORDER);
		txtPrevDevolucao.setEditable(false);
		GridData gd_txtPrevDevolucao = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtPrevDevolucao.widthHint = 150;
		txtPrevDevolucao.setLayoutData(gd_txtPrevDevolucao);
		new Label(grpUsurio, SWT.NONE);
		composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		btnConfirma = new Button(composite, SWT.NONE);
		btnConfirma.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnConfirmaWidgetSelected(arg0);
			}
		});
		btnConfirma.setImage(SWTResourceManager.getImage(DialogEmprestimoView.class, "/img/ic_assignment_turned_in_black_24dp.png"));
		btnConfirma.setText("Confirma");
		Screen.centralize(shell, getParent());
	}

	protected void btnBuscaUsuarioWidgetSelected(SelectionEvent arg0) {
	}
	protected void btnBuscaExemplarWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtUsuarioKeyPressed(KeyEvent arg0) {
	}
	protected void txtExemplarKeyPressed(KeyEvent arg0) {
	}
	protected void txtDataHoraModifyText(ModifyEvent arg0) {
	}
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
	}
}
