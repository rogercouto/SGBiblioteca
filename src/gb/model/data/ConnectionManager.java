package gb.model.data;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import gb.control.DialogLogin;

public class ConnectionManager {

	private static String url = "jdbc:mysql://localhost:3306/";
	private static String database = "biblioteca"; 
	private static String username = null;
	private static String password = null;
	
	private static boolean set = false;
	
	public static void main(String[] args) {
		setConnection();
	}
	
	public static void setConnection(){
		try {
			File fileProp = new File("db.properties");
			if (fileProp.exists()){
				Properties prop = new Properties();
				FileInputStream in = new FileInputStream(fileProp);
				prop.load(in);
				url = prop.getProperty("jdbc.url");
				database = prop.getProperty("jdbc.database");
		        username = prop.getProperty("jdbc.username");
		        password = prop.getProperty("jdbc.password");
		        if (username == null || password == null){
		        	DialogLogin dialogLogin = new DialogLogin(username);
		        	String[] ls = (String[])dialogLogin.open();
		        	if (ls != null){
		        		username = ls[0];
		        		password = ls[1];
		        		set = true;
		        	}
		        }else{
	        		set = true;
		        }
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean testConnection(){
		try {
			Connection connection = DriverManager.getConnection(url+database, username, password);
			if (connection != null){
				connection.close();
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean testConnection(String username, String password){
		try {
			Connection connection = DriverManager.getConnection(url+database, username, password);
			if (connection != null){
				connection.close();
				return true;
			}
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean isSet(){
		return set;
	}
	/**
	 * Retorna a conexão com o banco de dados
	 * Cria uma nova conexão se desconectado
	 * @return connection
	 */
	public static Connection getConnection(){
		try {
			return DriverManager.getConnection(url+database,username,password);
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
	
	public static void limpaDb(){
		Connection connection = getConnection();
		disableAutoCommit(connection);
		try {
			String sql = "SELECT DISTINCT editora_id FROM livro WHERE editora_id IS NOT NULL";
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM editora WHERE editora_id NOT IN(");
			int count = 0;
			while (result.next()){
				if (count > 0)
					builder.append(", ");
				builder.append(result.getInt(1));
				count++;
			}
			result.close();
			stmt.close();
			builder.append(")");
			if (count > 0){
				sql = builder.toString();
				stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
			}
			sql = "SELECT DISTINCT categoria_id FROM categoria_livro";
			stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			builder = new StringBuilder();
			builder.append("DELETE FROM categoria WHERE categoria_id NOT IN(");
			count = 0;
			while (result.next()){
				if (count > 0)
					builder.append(", ");
				builder.append(result.getInt(1));
				count++;
			}
			builder.append(")");
			if (count > 0){
				sql = builder.toString();
				stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
			}
			sql = "SELECT DISTINCT origem_id FROM exemplar";
			stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			builder = new StringBuilder();
			builder.append("DELETE FROM origem WHERE origem_id NOT IN(");
			count = 0;
			while (result.next()){
				if (count > 0)
					builder.append(", ");
				builder.append(result.getInt(1));
				count++;
			}
			builder.append(")");
			if (count > 0){
				sql = builder.toString();
				stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
			}
		} catch (SQLException e) {
			rollback(connection);
			closeConnection(connection);
			throw new RuntimeException(e.getMessage());
		}
		commit(connection);
		closeConnection(connection);
	}
	
}
