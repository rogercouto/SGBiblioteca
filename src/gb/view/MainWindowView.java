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

import swt.cw.util.Screen;

public class MainWindowView {

	protected Content content = null;
	
	protected Shell shell;
	protected Menu menu;
	protected MenuItem mbtLanamento;
	protected Menu menu_1;
	protected MenuItem mbtEmprstimo;
	protected MenuItem mbtReserva;
	protected MenuItem mbtDevoluo;
	protected MenuItem mbtPagamento;
	protected MenuItem mbtGerncia;
	protected Menu menu_2;
	protected MenuItem mbtLivrosEx;
	protected MenuItem mbtUsuarios;
	protected MenuItem mbtEmprestimos;
	protected MenuItem mbtReservas;
	protected ToolBar toolBar;
	protected ToolItem tbtEmprestimo;
	protected ToolItem tbtReserva;
	protected ToolItem tbtDevolucao;
	protected ToolItem tbtPagamento;
	protected ToolItem toolItem_2;
	protected ToolItem tbtLivrosEx;
	protected ToolItem tbtEmprestimos;
	protected ToolItem tbtReservas;
	protected ToolItem tbtUsuarios;
	protected GerenciaView gerenciaView;
	protected Composite cmpConteudo;
	protected ToolItem tbtConsultaSituacao;
	protected MenuItem mbtConsultaSituacao;
	protected ToolItem tbtRenovacao;
	protected MenuItem mbtRenovao;
	
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
		shell.setSize(960, 600);
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
		mbtRenovao = new MenuItem(menu_1, SWT.NONE);
		mbtRenovao.setText("Renovação...");
		mbtReserva = new MenuItem(menu_1, SWT.NONE);
		mbtReserva.setText("Reserva...");
		mbtDevoluo = new MenuItem(menu_1, SWT.NONE);
		mbtDevoluo.setText("Devolução...");
		mbtPagamento = new MenuItem(menu_1, SWT.NONE);
		mbtPagamento.setText("Pagamento...");
		mbtGerncia = new MenuItem(menu, SWT.CASCADE);
		mbtGerncia.setText("Gerência");
		menu_2 = new Menu(mbtGerncia);
		mbtGerncia.setMenu(menu_2);
		mbtConsultaSituacao = new MenuItem(menu_2, SWT.NONE);
		mbtConsultaSituacao.setText("Consulta situação...");
		new MenuItem(menu_2, SWT.SEPARATOR);
		mbtLivrosEx = new MenuItem(menu_2, SWT.NONE);
		mbtLivrosEx.setText("Livros/Exemplares...");
		mbtEmprestimos = new MenuItem(menu_2, SWT.NONE);
		mbtEmprestimos.setText("Emprestimos/Devoluções...");
		mbtReservas = new MenuItem(menu_2, SWT.NONE);
		mbtReservas.setText("Reservas...");
		mbtUsuarios = new MenuItem(menu_2, SWT.NONE);
		mbtUsuarios.setText("Usuários...");
		toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tbtEmprestimo = new ToolItem(toolBar, SWT.NONE);
		tbtEmprestimo.setToolTipText("Lançar empréstimo...");
		tbtEmprestimo.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_exit_to_app_black_36dp.png"));
		tbtRenovacao = new ToolItem(toolBar, SWT.NONE);
		tbtRenovacao.setToolTipText("Lançar renovação...");
		tbtRenovacao.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_alarm_add_black_36dp.png"));
		tbtReserva = new ToolItem(toolBar, SWT.NONE);
		tbtReserva.setToolTipText("Lançar reserva...");
		tbtReserva.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_event_black_36dp.png"));
		tbtDevolucao = new ToolItem(toolBar, SWT.NONE);
		tbtDevolucao.setToolTipText("Lançar devolução");
		tbtDevolucao.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_assignment_return_black_36dp.png"));
		tbtPagamento = new ToolItem(toolBar, SWT.NONE);
		tbtPagamento.setToolTipText("Lançar pagamento...");
		tbtPagamento.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_assignment_turned_in_black_36dp.png"));
		toolItem_2 = new ToolItem(toolBar, SWT.SEPARATOR);
		tbtConsultaSituacao = new ToolItem(toolBar, SWT.NONE);
		tbtConsultaSituacao.setToolTipText("Consulta situaçao...");
		tbtConsultaSituacao.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_find_in_page_black_36dp.png"));
		tbtLivrosEx = new ToolItem(toolBar, SWT.NONE);
		tbtLivrosEx.setToolTipText("Livros/Exemplares...");
		tbtLivrosEx.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_assignment_black_36dp.png"));
		tbtEmprestimos = new ToolItem(toolBar, SWT.NONE);
		tbtEmprestimos.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_cached_black_36dp.png"));
		tbtEmprestimos.setToolTipText("Empréstimos/Devoluções...");
		tbtReservas = new ToolItem(toolBar, SWT.NONE);
		tbtReservas.setToolTipText("Reservas");
		tbtReservas.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_receipt_black_36dp.png"));
		tbtUsuarios = new ToolItem(toolBar, SWT.NONE);
		tbtUsuarios.setImage(SWTResourceManager.getImage(MainWindowView.class, "/img/ic_account_circle_black_36dp.png"));
		tbtUsuarios.setToolTipText("Usuários...");
		Screen.centralize(shell);
	}
	
	private void addListeners(){
		SelectionListener lancaEmprestimoListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lancaEmprestimo();
			}
		};
		mbtEmprstimo.addSelectionListener(lancaEmprestimoListener);
		tbtEmprestimo.addSelectionListener(lancaEmprestimoListener);
		SelectionListener lancaRenovacaoListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lancaRenovacao();
			}
		};
		mbtRenovao.addSelectionListener(lancaRenovacaoListener);
		tbtRenovacao.addSelectionListener(lancaRenovacaoListener);
		SelectionListener lancaReservaListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lancaReserva();
			}
		};
		mbtReserva.addSelectionListener(lancaReservaListener);
		tbtReserva.addSelectionListener(lancaReservaListener);
		SelectionListener lancaDevolucaoListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lancaDevolucao();
			}
		};
		mbtDevoluo.addSelectionListener(lancaDevolucaoListener);
		tbtDevolucao.addSelectionListener(lancaDevolucaoListener);
		SelectionListener lancaPagamentoListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lancaPagamento();
			}
		};
		mbtPagamento.addSelectionListener(lancaPagamentoListener);
		tbtPagamento.addSelectionListener(lancaPagamentoListener);
		SelectionListener consultaSituacaoListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				consultaSituacao();
			}
		};
		mbtConsultaSituacao.addSelectionListener(consultaSituacaoListener);
		tbtConsultaSituacao.addSelectionListener(consultaSituacaoListener);
		SelectionListener gerLivrosExListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abreGerenciaLivrosEx();
			}
		};
		mbtLivrosEx.addSelectionListener(gerLivrosExListener);
		tbtLivrosEx.addSelectionListener(gerLivrosExListener);
		SelectionListener gerEmprestimosListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abreListaEmprestimos();
			}
		};
		mbtEmprestimos.addSelectionListener(gerEmprestimosListener);
		tbtEmprestimos.addSelectionListener(gerEmprestimosListener);
		SelectionListener gerReservasListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abreListaReservas();
			}
		};
		mbtReservas.addSelectionListener(gerReservasListener);
		tbtReservas.addSelectionListener(gerReservasListener);
		SelectionListener gerUsuariosListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abreGerenciaUsuarios();
			}
		};
		mbtUsuarios.addSelectionListener(gerUsuariosListener);
		tbtUsuarios.addSelectionListener(gerUsuariosListener);
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
	
	protected void setConteudo(Content conteudo){
		Composite composite = (Composite)conteudo;
	    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    composite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				cmpConteudo.dispose();
				cmpConteudo = null;
				content = null;
			}
		});
		shell.layout(true);
	}
	
	protected void lancaEmprestimo(){}
	
	protected void lancaRenovacao(){}
	
	protected void lancaReserva(){}
	
	protected void lancaDevolucao(){}
	
	protected void lancaPagamento(){}
	
	
	protected void consultaSituacao(){}
	
	protected void abreGerenciaLivrosEx(){}
	
	protected void abreListaEmprestimos(){}
	
	protected void abreListaReservas(){}
	
	protected void abreGerenciaUsuarios(){}
}
