package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Assunto;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class AssuntoDAO {

	private Connection connection;

	public AssuntoDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public AssuntoDAO(Connection connection){
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	private void check(Assunto assunto) throws ValidationException{
		if (assunto.getDescricao() == null || assunto.getDescricao().isEmpty())
			throw new ValidationException("Descri\u00e7\u00e3o n\u00e3o pode ficar em branco!");
	}

	public void insert(Assunto assunto) throws ValidationException{
		try {
			check(assunto);
			String sql = "INSERT INTO assunto(descricao, cores, cdu) VALUES(?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, assunto.getDescricao());
			ps.setString(2, assunto.getDBCores());
			ps.setString(3, assunto.getCdu());
			ps.executeUpdate();
			ps.close();
			assunto.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Assunto assunto) throws ValidationException{
		try {
			check(assunto);
			String sql = "UPDATE assunto SET descricao = ?, cores = ?, cdu = ? "
					+ "WHERE assunto_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, assunto.getDescricao());
			ps.setString(2, assunto.getDBCores());
			ps.setString(3, assunto.getCdu());
			ps.setInt(4, assunto.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Assunto assunto) throws ValidationException{
		if (assunto.getId() == null)
			insert(assunto);
		else
			update(assunto);
	}

	private void checkUse(Assunto assunto) throws ValidationException{
		try{
			String sql = "SELECT count(assunto_id) FROM livro WHERE assunto_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, assunto.getId());
			ResultSet result = ps.executeQuery();
			boolean inUse = false;
			if (result.next())
				if (result.getInt(1) > 0)
					inUse = true;
			ps.close();
			result.close();
			if (inUse)
				throw new ValidationException("Usu\u00e1rio j\u00e1 est\u00e1 em uso e"
						+ " n\u00e3o pode ser removido!");
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(Assunto assunto) throws ValidationException{
		try{
			checkUse(assunto);
			String sql = "DELETE FROM assunto WHERE assunto_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, assunto.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	private Assunto getAssunto(ResultSet result){
		try{
			Assunto assunto = new Assunto();
			assunto.setId(result.getInt("assunto_id"));
			assunto.setDescricao(result.getString("descricao"));
			assunto.setDBCores(result.getString("cores"));
			assunto.setCdu(result.getString("cdu"));
			return assunto;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public Assunto getAssunto(int id){
		try{
			String sql = "SELECT * FROM assunto WHERE assunto_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			Assunto assunto = null;
			ResultSet result = ps.executeQuery();
			if (result.next())
				assunto = getAssunto(result);
			result.close();
			ps.close();
			return assunto;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Assunto> getList(){
		try{
			String sql = "SELECT * FROM assunto";
			Statement stmt = connection.createStatement();
			List<Assunto> list = new ArrayList<>();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next())
				list.add(getAssunto(result));
			result.close();
			stmt.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Assunto> findList(String fieldName,String text){
		try{
			String sql = "SELECT * FROM assunto WHERE UPPER("+fieldName+") like ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+text.toUpperCase()+"%");
			List<Assunto> list = new ArrayList<>();
			ResultSet result = ps.executeQuery();
			while (result.next())
				list.add(getAssunto(result));
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

}
