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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.table.DataViwer;

public class ListaView extends Composite implements Content{
	
	protected Composite cmpHeader;
	protected Label lblHeader;
	protected Composite composite;
	protected DateTime dtIni;
	protected DateTime dtFim;
	protected Button btnDetalhes;
	protected Text txtBusca;
	protected Button btnBuscar;
	protected DataViwer table;
	protected Composite composite_1;
	protected Label lblStatus;
	protected Button btnCancelar;
	protected ToolBar toolBarClose;
	protected ToolItem tbtFechar;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ListaView(Composite parent) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		cmpHeader = new Composite(this, SWT.NONE);
		GridLayout gl_cmpHeader = new GridLayout(2, false);
		cmpHeader.setLayout(gl_cmpHeader);
		cmpHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblHeader = new Label(cmpHeader, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolBarClose = new ToolBar(cmpHeader, SWT.FLAT | SWT.RIGHT);
		tbtFechar = new ToolItem(toolBarClose, SWT.NONE);
		tbtFechar.setImage(SWTResourceManager.getImage(ListaView.class, "/icon/cancel.png"));
		tbtFechar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tbtFecharWidgetSelected(arg0);
			}
		});
		composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(10, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnDetalhes = new Button(composite, SWT.NONE);
		btnDetalhes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnDetalhesWidgetSelected(arg0);
			}
		});
		btnDetalhes.setImage(SWTResourceManager.getImage(ListaView.class, "/img/ic_find_in_page_black_24dp.png"));
		btnCancelar = new Button(composite, SWT.NONE);
		btnCancelar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnCancelarWidgetSelected(arg0);
			}
		});
		btnCancelar.setImage(SWTResourceManager.getImage(ListaView.class, "/img/ic_remove_circle_black_24dp.png"));
		Label lblPerodo = new Label(composite, SWT.NONE);
		lblPerodo.setText("Período:");
		dtIni = new DateTime(composite, SWT.BORDER | SWT.SHORT);
		dtIni.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dtIniWidgetSelected(arg0);
			}
		});
		GridData gd_dtIni = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dtIni.widthHint = 150;
		dtIni.setLayoutData(gd_dtIni);
		Label lblAt = new Label(composite, SWT.NONE);
		lblAt.setText("até");
		dtFim = new DateTime(composite, SWT.BORDER | SWT.SHORT);
		dtFim.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dtFimWidgetSelected(arg0);
			}
		});
		GridData gd_dtFim = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dtFim.widthHint = 150;
		dtFim.setLayoutData(gd_dtFim);
		Label lblBusca = new Label(composite, SWT.NONE);
		lblBusca.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		lblBusca.setText("Usuário:");
		txtBusca = new Text(composite, SWT.BORDER);
		txtBusca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtBuscaKeyPressed(arg0);
			}
		});
		txtBusca.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				txtBuscaModifyText(arg0);
			}
		});
		GridData gd_txtBusca = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtBusca.widthHint = 200;
		txtBusca.setLayoutData(gd_txtBusca);
		btnBuscar = new Button(composite, SWT.NONE);
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscarWidgetSelected(arg0);
			}
		});
		btnBuscar.setImage(SWTResourceManager.getImage(ListaView.class, "/img/ic_search_black_18dp.png"));
		table = new DataViwer(composite, SWT.BORDER);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				tableMouseDoubleClick(arg0);
			}
			@Override
			public void mouseDown(MouseEvent arg0) {
				tableMouseDown(arg0);
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 10, 1));
		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 10, 1));
		lblStatus = new Label(composite_1, SWT.NONE);
		lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnDetalhes.setEnabled(false);
		btnCancelar.setEnabled(false);
		toolBarClose.setToolTipText("Fechar");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void refresh(){
		table.refresh();
	}

	private void tableMouseDown(MouseEvent arg0) {
		btnDetalhes.setEnabled(table.getSelectionIndex() >= 0);
		btnCancelar.setEnabled(table.getSelectionIndex() >= 0);
	}
	
	protected void setHeader(String txt, Color foreground, Color background){
		lblHeader.setText(txt);
		lblHeader.setForeground(foreground);
		cmpHeader.setBackground(background);
		lblHeader.setBackground(cmpHeader.getBackground());
		toolBarClose.setBackground(cmpHeader.getBackground());
	}
	
	protected void setHeader(String txt, int fSystemColorId, int bSystemColorId){
		lblHeader.setText(txt);
		lblHeader.setForeground(SWTResourceManager.getColor(fSystemColorId));
		cmpHeader.setBackground(SWTResourceManager.getColor(bSystemColorId));
		lblHeader.setBackground(cmpHeader.getBackground());
		toolBarClose.setBackground(cmpHeader.getBackground());
	}
	
	protected void tbtFecharWidgetSelected(SelectionEvent arg0) {
		dispose();
	}
	
	protected void btnBuscarWidgetSelected(SelectionEvent arg0) {
	}
	protected void dtIniWidgetSelected(SelectionEvent arg0) {
	}
	protected void dtFimWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtBuscaModifyText(ModifyEvent arg0) {
	}
	protected void tableMouseDoubleClick(MouseEvent arg0) {
	}
	protected void btnDetalhesWidgetSelected(SelectionEvent arg0) {
	}
	protected void txtBuscaKeyPressed(KeyEvent arg0) {
	}
	protected void btnCancelarWidgetSelected(SelectionEvent arg0) {
	}
}
