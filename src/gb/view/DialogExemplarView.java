package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DialogExemplarView extends Dialog {

	protected Object result = null;
	protected Shell shell;
	protected Group group;
	protected Composite composite;
	protected Button btnSalvar;
	protected Button chkFixo;
	protected Combo cmbOrigem;
	protected DateTime dtDataAquis;
	protected Combo cmbSecao;
	protected Text txtNum;
	protected Text txtSituacao;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogExemplarView(Shell parent) {
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
		shell.setSize(370, 307);
		shell.setText(getText());
		shell.setLayout(new GridLayout());
		group = new Group(shell, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Nº:");
		txtNum = new Text(group, SWT.BORDER);
		GridData gd_txtNum = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtNum.widthHint = 100;
		txtNum.setLayoutData(gd_txtNum);
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Seção:");
		cmbSecao = new Combo(group, SWT.NONE);
		cmbSecao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setText("Data aquisição:");
		dtDataAquis = new DateTime(group, SWT.BORDER);
		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("Origem:");
		cmbOrigem = new Combo(group, SWT.NONE);
		cmbOrigem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group, SWT.NONE);
		chkFixo = new Button(group, SWT.CHECK);
		chkFixo.setText("Fixo");
		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setEnabled(false);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("Situação:");
		txtSituacao = new Text(group, SWT.BORDER);
		txtSituacao.setEditable(false);
		txtSituacao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		btnSalvar = new Button(composite, SWT.NONE);
		btnSalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnSalvarWidgetSelected(arg0);
			}
		});
		btnSalvar.setImage(SWTResourceManager.getImage(DialogExemplarView.class, "/img/ic_save_black_24dp.png"));
		btnSalvar.setText("Salvar");
		Screen.centralize(shell, getParent());
	}

	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
	}
}
