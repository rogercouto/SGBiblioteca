package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.table.DataViwer;
import swt.cw.util.Screen;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class DialogLivroView extends Dialog {

	protected Object result;
	protected Shell shell;
	protected CTabFolder tabFolder;
	protected Composite composite;
	protected Button btnSalvar;
	protected CTabItem tbtmBsico;
	protected Composite tab1;
	protected Text txtTitulo;
	protected Text txtResumo;
	protected Text txtISBN;
	protected Text txtCutter;
	protected Combo cmbEditora;
	protected Text txtEdicao;
	protected Text txtVolume;
	protected Text txtNumPag;
	protected Combo cmbAssunto;
	protected CTabItem tbtmAvanado;
	protected Composite tab3;
	protected CTabItem tbtmAutorescategorias;
	protected Composite tab2;
	protected Group grpPublicao;
	protected Text txtLocalPublicacao;
	protected Text txtBuscaAutor;
	protected Button btnBuscaAutor;
	protected List lstAutores;
	protected Button btnRemAutor;
	protected Button btnAddAutor;
	protected List lstCategorias;
	protected Button btnAddCategoria;
	protected Button btnRemCategoria;
	protected DataViwer tableExemplares;
	protected ToolBar toolBar;
	protected ToolItem tbtAddExemplar;
	protected ToolItem tbtEdtExemplar;
	protected ToolItem tbtRemExemplar;
	protected Text txtDataPublicacao;
	protected Combo cmbAddCategoria;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogLivroView(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
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
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent arg0) {
				shellShellClosed(arg0);
			}
		});
		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				shellKeyTraversed(arg0);
			}
		});
		shell.setSize(489, 576);
		shell.setText(getText());
		shell.setLayout(new GridLayout());
		tabFolder = new CTabFolder(shell, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tbtmBsico = new CTabItem(tabFolder, SWT.NONE);
		tbtmBsico.setText("Dados");
		tab1 = new Composite(tabFolder, SWT.NONE);
		tbtmBsico.setControl(tab1);
		GridLayout gl_tab1 = new GridLayout(4, false);
		gl_tab1.marginHeight = 15;
		gl_tab1.marginWidth = 15;
		tab1.setLayout(gl_tab1);
		Label lblNewLabel = new Label(tab1, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Título:");
		txtTitulo = new Text(tab1, SWT.BORDER);
		txtTitulo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label lblNewLabel_1 = new Label(tab1, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblNewLabel_1.setText("Resumo:");
		txtResumo = new Text(tab1, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_txtResumo = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_txtResumo.heightHint = 60;
		txtResumo.setLayoutData(gd_txtResumo);
		Label lblNewLabel_2 = new Label(tab1, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("ISBN:");
		txtISBN = new Text(tab1, SWT.BORDER);
		txtISBN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtISBN.setText("");
		new Label(tab1, SWT.NONE);
		new Label(tab1, SWT.NONE);
		Label lblNewLabel_3 = new Label(tab1, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("Cutter:");
		txtCutter = new Text(tab1, SWT.BORDER);
		txtCutter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(tab1, SWT.NONE);
		new Label(tab1, SWT.NONE);
		Label lblNewLabel_4 = new Label(tab1, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("Editora:");
		cmbEditora = new Combo(tab1, SWT.NONE);
		cmbEditora.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label lblNewLabel_5 = new Label(tab1, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("Edição:");
		txtEdicao = new Text(tab1, SWT.BORDER);
		txtEdicao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(tab1, SWT.NONE);
		new Label(tab1, SWT.NONE);
		Label lblNewLabel_6 = new Label(tab1, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_6.setText("Volume:");
		txtVolume = new Text(tab1, SWT.BORDER);
		txtVolume.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_7 = new Label(tab1, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setText("Nº paginas:");
		txtNumPag = new Text(tab1, SWT.BORDER);
		txtNumPag.setTextLimit(5);
		txtNumPag.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblNewLabel_8 = new Label(tab1, SWT.NONE);
		lblNewLabel_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_8.setText("Assunto:");
		cmbAssunto = new Combo(tab1, SWT.NONE);
		cmbAssunto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				cmbAssuntoKeyPressed(arg0);
			}
		});
		cmbAssunto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label lblNewLabel_11 = new Label(tab1, SWT.NONE);
		lblNewLabel_11.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		grpPublicao = new Group(tab1, SWT.NONE);
		grpPublicao.setText("Publicação:");
		grpPublicao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		grpPublicao.setLayout(new GridLayout(2, false));
		Label lblNewLabel_9 = new Label(grpPublicao, SWT.NONE);
		lblNewLabel_9.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel_9 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_9.widthHint = 75;
		lblNewLabel_9.setLayoutData(gd_lblNewLabel_9);
		lblNewLabel_9.setText("Data:");
		txtDataPublicacao = new Text(grpPublicao, SWT.BORDER);
		GridData gd_txtDataPublicacao = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtDataPublicacao.widthHint = 150;
		txtDataPublicacao.setLayoutData(gd_txtDataPublicacao);
		Label lblNewLabel_10 = new Label(grpPublicao, SWT.NONE);
		lblNewLabel_10.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_10.setText("Local:");
		txtLocalPublicacao = new Text(grpPublicao, SWT.BORDER);
		txtLocalPublicacao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tbtmAutorescategorias = new CTabItem(tabFolder, SWT.NONE);
		tbtmAutorescategorias.setText("Autores/Categorias");
		tab2 = new Composite(tabFolder, SWT.NONE);
		tbtmAutorescategorias.setControl(tab2);
		GridLayout gl_tab2 = new GridLayout(3, false);
		gl_tab2.horizontalSpacing = 0;
		gl_tab2.marginHeight = 15;
		gl_tab2.marginWidth = 15;
		tab2.setLayout(gl_tab2);
		Label lblNewLabel_12 = new Label(tab2, SWT.NONE);
		lblNewLabel_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_12.setText("Adicionar autor:   ");
		txtBuscaAutor = new Text(tab2, SWT.BORDER);
		txtBuscaAutor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtBuscaAutorKeyPressed(arg0);
			}
		});
		txtBuscaAutor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnBuscaAutor = new Button(tab2, SWT.NONE);
		btnBuscaAutor.setToolTipText("Buscar autor...");
		btnBuscaAutor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnBuscaAutorWidgetSelected(arg0);
			}
		});
		btnBuscaAutor.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/find.png"));
		Label lblNewLabel_14 = new Label(tab2, SWT.NONE);
		lblNewLabel_14.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_14.setText("Autores:   ");
		lstAutores = new List(tab2, SWT.BORDER);
		lstAutores.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				lstAutoresKeyPressed(arg0);
			}
		});
		lstAutores.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lstAutoresWidgetSelected(arg0);
			}
		});
		lstAutores.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		btnAddAutor = new Button(tab2, SWT.NONE);
		btnAddAutor.setToolTipText("Adicionar autor");
		btnAddAutor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnAddAutorWidgetSelected(arg0);
			}
		});
		btnAddAutor.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_add_box_black_18dp.png"));
		new Label(tab2, SWT.NONE);
		btnRemAutor = new Button(tab2, SWT.NONE);
		btnRemAutor.setToolTipText("Remover autor");
		btnRemAutor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnRemAutorWidgetSelected(arg0);
			}
		});
		btnRemAutor.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnRemAutor.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_remove_circle_black_18dp.png"));
		Label lblNewLabel_13 = new Label(tab2, SWT.NONE);
		lblNewLabel_13.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_13.setText("Adicionar categoria:   ");
		cmbAddCategoria = new Combo(tab2, SWT.NONE);
		cmbAddCategoria.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				cmbAddCategoriaModifyText(arg0);
			}
		});
		cmbAddCategoria.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				cmbAddCategoriaWidgetSelected(arg0);
			}
		});
		cmbAddCategoria.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				cmbAddCategoriaKeyPressed(arg0);
			}
		});
		cmbAddCategoria.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label lblNewLabel_15 = new Label(tab2, SWT.NONE);
		lblNewLabel_15.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_15.setText("Categorias:   ");
		lstCategorias = new List(tab2, SWT.BORDER);
		lstCategorias.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lstCategoriasWidgetSelected(arg0);
			}
		});
		lstCategorias.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				lstCategoriaKeyPressed(arg0);
			}
		});
		lstCategorias.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		btnAddCategoria = new Button(tab2, SWT.NONE);
		btnAddCategoria.setToolTipText("Adicionar categoria");
		btnAddCategoria.setEnabled(false);
		btnAddCategoria.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnAddCategoriaWidgetSelected(arg0);
			}
		});
		btnAddCategoria.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_add_box_black_18dp.png"));
		new Label(tab2, SWT.NONE);
		btnRemCategoria = new Button(tab2, SWT.NONE);
		btnRemCategoria.setToolTipText("Remover categoria");
		btnRemCategoria.setEnabled(false);
		btnRemCategoria.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnRemCategoriaWidgetSelected(arg0);
			}
		});
		btnRemCategoria.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnRemCategoria.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_remove_circle_black_18dp.png"));
		tbtmAvanado = new CTabItem(tabFolder, SWT.NONE);
		tbtmAvanado.setText("Exemplares");
		tab3 = new Composite(tabFolder, SWT.NONE);
		tbtmAvanado.setControl(tab3);
		tab3.setLayout(new GridLayout(1, false));
		toolBar = new ToolBar(tab3, SWT.FLAT | SWT.RIGHT);
		tbtAddExemplar = new ToolItem(toolBar, SWT.NONE);
		tbtAddExemplar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tbtAddExemplarWidgetSelected(arg0);
			}
		});
		tbtAddExemplar.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_add_circle_black_24dp.png"));
		tbtEdtExemplar = new ToolItem(toolBar, SWT.NONE);
		tbtEdtExemplar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tbtEdtExemplarWidgetSelected(arg0);
			}
		});
		tbtEdtExemplar.setEnabled(false);
		tbtEdtExemplar.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_create_black_24dp.png"));
		tbtRemExemplar = new ToolItem(toolBar, SWT.NONE);
		tbtRemExemplar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tbtRemExemplarWidgetSelected(arg0);
			}
		});
		tbtRemExemplar.setEnabled(false);
		tbtRemExemplar.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_clear_black_24dp.png"));
		tableExemplares = new DataViwer(tab3, SWT.BORDER);
		tableExemplares.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
		btnSalvar.setImage(SWTResourceManager.getImage(DialogLivroView.class, "/img/ic_save_black_24dp.png"));
		btnSalvar.setText("Salvar");
		btnAddAutor.setEnabled(false);
		btnRemAutor.setEnabled(false);
		tableExemplares.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tableExemplaresSelectionEvent(arg0);
			}
		});
		tableExemplares.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tableExemplaresMouseDownEvent(arg0);
			}
		});
		tableExemplares.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tableExemplaresMouseDoubleClickEvent(arg0);
			}
		});
		tableExemplares.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tableExemplaresKeyDownEvent(arg0);
			}
		});
		Screen.centralize(shell, getParent());
	}
	
	protected void lstAutoresWidgetSelected(SelectionEvent arg0) {}
	
	protected void btnBuscaAutorWidgetSelected(SelectionEvent arg0) {}
	
	protected void btnAddAutorWidgetSelected(SelectionEvent arg0) {}
	
	protected void btnRemAutorWidgetSelected(SelectionEvent arg0) {}
	
	protected void txtBuscaAutorKeyPressed(KeyEvent arg0) {}
	
	protected void lstAutoresKeyPressed(KeyEvent arg0) {}
	
	protected void cmbAddCategoriaKeyPressed(KeyEvent arg0) {}
	
	protected void btnAddCategoriaWidgetSelected(SelectionEvent arg0) {}
	
	protected void btnRemCategoriaWidgetSelected(SelectionEvent arg0) {}
	
	protected void lstCategoriaKeyPressed(KeyEvent arg0) {}
	
	protected void cmbAddCategoriaWidgetSelected(SelectionEvent arg0) {}
	
	protected void cmbAddCategoriaModifyText(ModifyEvent arg0) {}
	
	protected void lstCategoriasWidgetSelected(SelectionEvent arg0) {}
	
	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {}
	
	protected void cmbAssuntoKeyPressed(KeyEvent arg0) {}
	
	protected void shellKeyTraversed(TraverseEvent arg0) {}
	
	protected void shellShellClosed(ShellEvent arg0) {}
	
	protected void tableExemplaresSelectionEvent(Event arg0){}
	
	protected void tableExemplaresMouseDownEvent(Event arg0){}
	
	protected void tableExemplaresMouseDoubleClickEvent(Event arg0){}
	
	protected void tableExemplaresKeyDownEvent(Event arg0){}
	
	protected void tbtAddExemplarWidgetSelected(SelectionEvent arg0) {}
	
	protected void tbtEdtExemplarWidgetSelected(SelectionEvent arg0) {}
	
	protected void tbtRemExemplarWidgetSelected(SelectionEvent arg0) {}
	
}
