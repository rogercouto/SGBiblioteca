package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.reg.RegViwer;

public class GerenciaView extends Composite {
	
		
	protected RegViwer regViwer;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GerenciaView(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
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

}
