package gb.control;

import gb.view.GerenciaView;
import gb.view.MainWindowView;

public class MainWindow extends MainWindowView {

	private GerenciaView gerencia = null;
	
	public MainWindow() {
		super();
	}
	
	protected void abreGerenciaLivrosEx(){
		if (gerencia != null)
			gerencia.dispose();
		clearConteudo();
		gerencia = new GerenciaLivroEx(cmpConteudo);
		setConteudo(gerencia);
	}
	
	@Override
	protected void abreGerenciaUsuarios() {
		if (gerencia != null)
			gerencia.dispose();
		clearConteudo();
		gerencia = new GerenciaUsuarios(cmpConteudo);
		setConteudo(gerencia);
	}
	
	@Override
	protected void lancaEmprestimo(){
		new DialogEmprestimo(shell).open();
		if (gerencia != null)
			gerencia.refresh();
	}
	
	@Override
	protected void lancaReserva(){
		new DialogReserva(shell).open();
		if (gerencia != null)
			gerencia.refresh();
	}
	
	@Override
	protected void lancaDevolucao(){
		new DialogDevolucao(shell).open();
		if (gerencia != null)
			gerencia.refresh();
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public void run() {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
