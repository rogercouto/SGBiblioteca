package gb.control;

import gb.view.Content;
import gb.view.MainWindowView;

public class MainWindow extends MainWindowView {

	private Content content = null;
	
	public MainWindow() {
		super();
	}
	
	@Override
	protected void lancaEmprestimo(){
		new DialogEmprestimo(shell).open();
		if (content != null)
			content.refresh();
	}
	
	@Override
	protected void lancaReserva(){
		new DialogReserva(shell).open();
		if (content != null)
			content.refresh();
	}
	
	@Override
	protected void lancaDevolucao(){
		new DialogDevolucao(shell).open();
		if (content != null)
			content.refresh();
	}
	
	protected void abreGerenciaLivrosEx(){
		if (content != null)
			content.dispose();
		clearConteudo();
		content = new GerenciaLivroEx(cmpConteudo);
		setConteudo(content);
	}
	
	@Override
	protected void abreListaEmprestimos(){
		if (content != null)
			content.dispose();
		clearConteudo();
		content = new ListaEmprestimos(cmpConteudo);
		setConteudo(content);
	}
	
	@Override
	protected void abreListaReservas(){
		if (content != null)
			content.dispose();
		clearConteudo();
		content = new ListaReservas(cmpConteudo);
		setConteudo(content);
	}
	
	@Override
	protected void abreGerenciaUsuarios() {
		if (content != null)
			content.dispose();
		clearConteudo();
		content = new GerenciaUsuarios(cmpConteudo);
		setConteudo(content);
	}
	
	@Override
	protected void consultaSituacao(){
		new DialogConsultaSituacao(shell).open();
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
