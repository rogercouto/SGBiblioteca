package gb;

import java.time.format.DateTimeFormatter;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

import gb.control.MainWindow;
import gb.model.dao.ReservaDAO;

public class Main {

	public static final DateTimeFormatter FORMATADOR_D = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static final DateTimeFormatter FORMATADOR_DH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public static final Image[] ICONS = new Image[]{
			SWTResourceManager.getImage(Main.class, "/img/ic_search_black_18dp.png"),
			SWTResourceManager.getImage(Main.class, "/img/ic_add_circle_black_24dp.png"),
			SWTResourceManager.getImage(Main.class, "/img/ic_done_black_24dp.png"),
			SWTResourceManager.getImage(Main.class, "/img/ic_clear_black_24dp.png")
	};
	
	public static void main(String[] args) {
		ReservaDAO dao = new ReservaDAO();
		dao.disponibilizaExpiradas();
		dao.closeConnection();
		MainWindow mainController = new MainWindow();
		mainController.run();
	}

}
