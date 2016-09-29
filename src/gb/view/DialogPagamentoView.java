package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.table.DataViwer;
import swt.cw.util.Screen;

public class DialogPagamentoView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Group grpBusca;
	protected Text txtBusca;
	protected Button btnBusca;
	protected DataViwer table;
	protected Button btnConfirma;
	protected Label lblUsurio;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogPagamentoView(Shell parent) {
		super(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		setText("Renovar empr\u00e9stimo");
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
		shell.setImage(SWTResourceManager.getImage(DialogPagamentoView.class, "/img/ic_assignment_turned_in_black_24dp.png"));
		shell.setText("Realizar pagamento");
		shell.setLayout(new GridLayout(1, false));
		grpBusca = new Group(shell, SWT.NONE);
		grpBusca.setText("Busca");
		GridLayout gl_grpBusca = new GridLayout(3, false);
		gl_grpBusca.horizontalSpacing = 0;
		grpBusca.setLayout(gl_grpBusca);
		GridData gd_grpBusca = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_grpBusca.widthHint = 400;
		grpBusca.setLayoutData(gd_grpBusca);
		lblUsurio = new Label(grpBusca, SWT.NONE);
		lblUsurio.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsurio.setText("Usu\u00e1rio: ");
		txtBusca = new Text(grpBusca, SWT.BORDER);
		txtBusca.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				txtBuscaModifyText(arg0);
			}
		});
		txtBusca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtBuscaKeyPressed(arg0);
			}
		});
		txtBusca.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnBusca = new Button(grpBusca, SWT.NONE);
		btnBusca.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscaWidgetSelected(arg0);
			}
		});
		btnBusca.setImage(SWTResourceManager.getImage(DialogPagamentoView.class, "/icon/find.png"));
		table = new DataViwer(shell, SWT.BORDER | SWT.CHECK);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				tableKeyReleased(arg0);
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				tableMouseDown(arg0);
			}
		});
		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tableSelection(arg0);
			}
		});
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 150;
		table.setLayoutData(gd_table);
		btnConfirma = new Button(shell, SWT.NONE);
		btnConfirma.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnConfirmaWidgetSelected(arg0);
			}
		});
		btnConfirma.setEnabled(false);
		btnConfirma.setImage(SWTResourceManager.getImage(DialogPagamentoView.class, "/img/ic_done_black_24dp.png"));
		btnConfirma.setText("Confirma");

	}
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtBuscaKeyPressed(KeyEvent arg0) {
	}
	protected void btnBuscaWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtBuscaModifyText(ModifyEvent arg0) {
	}
	protected void tableMouseDown(MouseEvent arg0) {
	}
	protected void tableSelection(Event arg0){
	}
	protected void tableKeyReleased(KeyEvent arg0) {
	}
}
