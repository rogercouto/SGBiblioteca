package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.util.Screen;

public class DialogDetalheView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Table table;
	protected Composite composite;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogDetalheView(Shell parent) {
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
		shell.setSize(360, 260);
		shell.setText(getText());
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginHeight = 0;
		gl_shell.marginWidth = 0;
		shell.setLayout(gl_shell);
		composite = new Composite(shell, SWT.NONE);
		composite.setEnabled(false);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table = new Table(composite, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setLinesVisible(true);
		new TableColumn(table, SWT.LEAD).setWidth(0);//filler
		new TableColumn(table, SWT.RIGHT).setWidth(120);
		new TableColumn(table, SWT.LEFT).setWidth(200);
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
}
