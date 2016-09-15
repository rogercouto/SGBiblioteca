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
