package gb.model.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

	private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";

	/**
	 * Retorna a conexão com o banco de dados
	 * Cria uma nova conexão se desconectado
	 * @return connection
	 */
	public static Connection getConnection(){
		try {
			return DriverManager.getConnection(URL,"root","admin");
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Encerra uma conexão com o banco
	 * @return
	 */
	public static void closeConnection(Connection connection){
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public static void disableAutoCommit(Connection connection){
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public static void commit(Connection connection){
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public static void rollback(Connection connection){
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public static int getLastInsertId(Connection connection) throws SQLException{
		String sql = "SELECT LAST_INSERT_ID()";
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		int id = 0;
		if (result.next())
			id = result.getInt(1);
		result.close();
		stmt.close();
		return id;
	}
}
