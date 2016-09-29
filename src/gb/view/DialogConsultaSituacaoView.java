package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;

public class DialogConsultaSituacaoView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Table table;
	protected Composite composite;
	protected Text txtBusca;
	protected Button btnBuscar;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogConsultaSituacaoView(Shell parent) {
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
		shell.setSize(400, 248);
		shell.setText(getText());
		GridLayout gl_shell = new GridLayout(2, false);
		shell.setLayout(gl_shell);
		txtBusca = new Text(shell, SWT.BORDER);
		txtBusca.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBusca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				textKeyPressed(arg0);
			}
		});
		txtBusca.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				textModifyText(arg0);
			}
		});
		btnBuscar = new Button(shell, SWT.NONE);
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscarWidgetSelected(arg0);
			}
		});
		btnBuscar.setImage(SWTResourceManager.getImage(DialogConsultaSituacaoView.class, "/icon/find.png"));
		composite = new Composite(shell, SWT.NONE);
		composite.setEnabled(false);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.horizontalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table = new Table(composite, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setLinesVisible(true);
		new TableColumn(table, SWT.LEAD).setWidth(0);//filler
		new TableColumn(table, SWT.RIGHT).setWidth(120);
		new TableColumn(table, SWT.LEFT).setWidth(250);
		shell.setText("Consulta situa\u00e7\u00e3o");
		Screen.centralize(shell, getParent());
	}

	protected void addItem(String key, String value){
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, "");//filler
		item.setText(1, key);
		item.setText(2, value);
	}

	protected void addDivisor(){
		addItem("","");
	}

	protected void btnBuscarWidgetSelected(SelectionEvent arg0) {
	}
	protected void textModifyText(ModifyEvent arg0) {
	}
	protected void textKeyPressed(KeyEvent arg0) {
	}
}
