package gb;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

import gb.control.MainWindow;
import gb.model.dao.ReservaDAO;
import gb.model.data.ConnectionManager;
import swt.cw.util.Dialog;

public class Main {

	public static final Image[] ICONS = new Image[]{
			SWTResourceManager.getImage(Main.class, "/img/find.png"),
			SWTResourceManager.getImage(Main.class, "/img/ic_add_circle_black_24dp.png"),
			SWTResourceManager.getImage(Main.class, "/img/ic_done_black_24dp.png"),
			SWTResourceManager.getImage(Main.class, "/img/ic_clear_black_24dp.png")
	};

	public static boolean isLinux(){
		return System.getProperty("os.name").compareTo("Linux") == 0;
	}
	
	public static void main(String[] args) {
		ConnectionManager.setConnection();
		if (!ConnectionManager.isSet())
			return;
		if (!ConnectionManager.testConnection()){
			Dialog.message("Não foi possível realizar a conexão bom o banco de dados!");
			return;
		}
		ReservaDAO dao = new ReservaDAO();
		dao.disponibilizaExpiradas();
		dao.closeConnection();
		MainWindow mainController = new MainWindow();
		mainController.run();
		ConnectionManager.limpaDb();
	}

}
