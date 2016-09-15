package gb.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainWindowView {

	protected Shell shell;
	protected Menu menu;
	protected MenuItem mbtLanamento;
	protected Menu menu_1;
	protected MenuItem mbtEmprstimo;
	protected MenuItem mbtReserva;
	protected MenuItem mbtDevoluo;
	protected MenuItem mbtBaixa;
	protected MenuItem mbtGerncia;
	protected Menu menu_2;
	protected MenuItem mbtLivrosEx;
	protected MenuItem mbtUsuarios;
	protected MenuItem mbtTiposDeUsurio;
	protected MenuItem mbtAssuntos;
	protected MenuItem mbtCategorias;
	protected MenuItem mbtEditoras;
	protected MenuItem mbtOrigens;
	protected MenuItem mbtEmprestimos;
	protected MenuItem mbtReservas;
	protected ToolBar toolBar;
	protected ToolItem tbtEmprestimo;
	protected ToolItem tbtReserva;
	protected ToolItem tbtDevolucao;
	protected ToolItem tbtBaixa;
	protected ToolItem toolItem_2;
	protected ToolItem tbtLivrosEx;
	protected ToolItem tbtEmpDev;
	protected ToolItem tbtReservas;
	protected ToolItem tbtUsuarios;
	protected GerenciaView gerenciaView;
	protected Composite cmpConteudo;
	
	public MainWindowView(){
		super();
		createContents();
		addListeners();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 480);
		shell.setText("Sistema de Controle de Biblioteca");
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 0;
		gl_shell.horizontalSpacing = 0;
		gl_shell.marginWidth = 0;
		gl_shell.marginHeight = 0;
		shell.setLayout(gl_shell);
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		mbtLanamento = new MenuItem(menu, SWT.CASCADE);
		mbtLanamento.setText("Lançamento");
		menu_1 = new Menu(mbtLanamento);
		mbtLanamento.setMenu(menu_1);
		mbtEmprstimo = new MenuItem(menu_1, SWT.NONE);
		mbtEmprstimo.setText("Empréstimo...");
		mbtReserva = new MenuItem(menu_1, SWT.NONE);
		mbtReserva.setText("Reserva...");
		mbtDevoluo = new MenuItem(menu_1, SWT.NONE);
		mbtDevoluo.setText("Devolução...");
		mbtBaixa = new MenuItem(menu_1, SWT.NONE);
		mbtBaixa.setText("Baixa...");
		mbtGerncia = new MenuItem(menu, SWT.CASCADE);
		mbtGerncia.setText("Gerência");
		menu_2 = new Menu(mbtGerncia);
		mbtGerncia.setMenu(menu_2);
		mbtLivrosEx = new MenuItem(menu_2, SWT.NONE);
		mbtLivrosEx.setText("Livros/Exemplares...");
		mbtEmprestimos = new MenuItem(menu_2, SWT.NONE);
		mbtEmprestimos.setText("Emprestimos/Devoluções...");
		mbtReservas = new MenuItem(menu_2, SWT.NONE);
		mbtReservas.setText("Reservas...");
		mbtUsuarios = new MenuItem(menu_2, SWT.NONE);
		mbtUsuarios.setText("Usuários...");
		new MenuItem(menu_2, SWT.SEPARATOR);
		mbtEditoras = new MenuItem(menu_2, SWT.NONE);
		mbtEditoras.setText("Editoras...");
		mbtAssuntos = new MenuItem(menu_2, SWT.NONE);
		mbtAssuntos.setText("Assuntos...");
		mbtCategorias = new MenuItem(menu_2, SWT.NONE);
		mbtCategorias.setText("Categorias...");
		mbtTiposDeUsurio = new MenuItem(menu_2, SWT.NONE);
		mbtTiposDeUsurio.setText("Tipos de Usuário...");
		mbtOrigens = new MenuItem(menu_2, SWT.NONE);
		mbtOrigens.setText("Origens...");
		toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tbtEmprestimo = new ToolItem(toolBar, SWT.NONE);
		tbtEmprestimo.setToolTipText("Lançar empréstimo...");
		tbtEmprestimo.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_exit_to_app_black_36dp.png"));
		tbtReserva = new ToolItem(toolBar, SWT.NONE);
		tbtReserva.setToolTipText("Lançar reserva...");
		tbtReserva.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_event_black_36dp.png"));
		tbtDevolucao = new ToolItem(toolBar, SWT.NONE);
		tbtDevolucao.setToolTipText("Lançar devolução");
		tbtDevolucao.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_assignment_return_black_36dp.png"));
		tbtBaixa = new ToolItem(toolBar, SWT.NONE);
		tbtBaixa.setToolTipText("Lançar baixa...");
		tbtBaixa.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_report_problem_black_36dp.png"));
		toolItem_2 = new ToolItem(toolBar, SWT.SEPARATOR);
		tbtLivrosEx = new ToolItem(toolBar, SWT.NONE);
		tbtLivrosEx.setToolTipText("Livros/Exemplares...");
		tbtLivrosEx.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_assignment_black_36dp.png"));
		tbtEmpDev = new ToolItem(toolBar, SWT.NONE);
		tbtEmpDev.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_cached_black_36dp.png"));
		tbtEmpDev.setToolTipText("Empréstimos/Devoluções...");
		tbtReservas = new ToolItem(toolBar, SWT.NONE);
		tbtReservas.setToolTipText("Reservas");
		tbtReservas.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_receipt_black_36dp.png"));
		tbtUsuarios = new ToolItem(toolBar, SWT.NONE);
		tbtUsuarios.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_account_circle_black_36dp.png"));
		tbtUsuarios.setToolTipText("Usuários...");
	}
	
	private void addListeners(){
		SelectionListener getLivrosExListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abreGerenciaLivrosEx();
			}
		};
		mbtLivrosEx.addSelectionListener(getLivrosExListener);
		tbtLivrosEx.addSelectionListener(getLivrosExListener);
	}
	
	protected void clearConteudo(){
		if ((cmpConteudo != null) && (!cmpConteudo.isDisposed())) 
	          cmpConteudo.dispose();
		cmpConteudo = new Composite(shell, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		cmpConteudo.setLayout(gl);
	    cmpConteudo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	protected void setConteudo(Composite composite){
	    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    composite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				cmpConteudo.dispose();
				cmpConteudo = null;
			}
		});
		shell.layout(true);
	}
	
	protected void abreGerenciaLivrosEx(){}
}