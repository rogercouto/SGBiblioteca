package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.reg.RegViwer;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class GerenciaView extends Composite implements Content {
	
		
	protected RegViwer regViwer;
	protected Composite cmpHeader;
	protected Label lblHeader;
	protected ToolBar toolBarClose;
	protected ToolItem tbtFechar;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GerenciaView(Composite parent) {
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
		tbtFechar.setImage(SWTResourceManager.getImage(GerenciaView.class, "/icon/cancel.png"));
		tbtFechar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tbtFecharWidgetSelected(arg0);
			}
		});
		regViwer = new RegViwer(this, SWT.NONE);
		regViwer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Image[] icons = {SWTResourceManager.getImage(this.getClass(), "/img/ic_add_circle_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_create_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_remove_circle_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/find.png")};
		regViwer.setIcons(icons);
		regViwer.setCloseVisible(false);
		toolBarClose.setToolTipText("Fechar");
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
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	@Override
	public void refresh(){
		//
	}

	protected void tbtFecharWidgetSelected(SelectionEvent arg0) {
		dispose();
	}
}
