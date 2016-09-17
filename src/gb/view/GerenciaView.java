package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.reg.RegViwer;
import org.eclipse.swt.widgets.Label;

public class GerenciaView extends Composite {
	
		
	protected RegViwer regViwer;
	protected Composite cmpHeader;
	protected Label lblHeader;
	
	
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
		GridLayout gl_cmpHeader = new GridLayout(1, false);
		cmpHeader.setLayout(gl_cmpHeader);
		cmpHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblHeader = new Label(cmpHeader, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		regViwer = new RegViwer(this, SWT.NONE);
		regViwer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Image[] icons = {SWTResourceManager.getImage(this.getClass(), "/img/ic_add_circle_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_create_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_remove_circle_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_search_black_18dp.png")};
		regViwer.setIcons(icons);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void refresh(){
		
	}

}
