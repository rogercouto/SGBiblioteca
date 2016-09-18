package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DialogAutorView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Group group;
	protected Composite composite;
	protected Text txtSobrenome;
	protected Text txtNome;
	protected Text txtInfo;
	protected Button btnSalvar;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogAutorView(Shell parent) {
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
		shell.setSize(404, 245);
		shell.setText(getText());
		shell.setLayout(new GridLayout());
		group = new Group(shell, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group.setLayout(new GridLayout(2, false));
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Sobrenome:");
		txtSobrenome = new Text(group, SWT.BORDER);
		txtSobrenome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Nome:");
		txtNome = new Text(group, SWT.BORDER);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblNewLabel_2.setText("Info:");
		txtInfo = new Text(group, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		txtInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
		btnSalvar.setImage(SWTResourceManager.getImage(DialogAutorView.class, "/img/ic_save_black_24dp.png"));
		btnSalvar.setText("Salvar");
		Screen.centralize(shell, getParent());
	}

	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
	}
}
