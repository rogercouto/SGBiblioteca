package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Origem;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class OrigemDAO {

	private Connection connection = null;

	public OrigemDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public OrigemDAO(Connection connection){
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	public void insert(Origem origem) throws ValidationException{
		try {
			if (origem.getDescricao() == null || origem.getDescricao().isEmpty())
				throw new ValidationException("Descri\u00e7\u00e3o n\u00e3o pode ficar em branco!");
			String sql = "INSERT INTO origem(descricao) VALUES(?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, origem.getDescricao());
			ps.executeUpdate();
			ps.close();
			origem.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Origem origem) throws ValidationException{
		try {
			if (origem.getId() == null)
				throw new RuntimeException("Id da se\u00e7\u00e3o n\u00e3o pode ser null!");
			if (origem.getDescricao() == null || origem.getDescricao().isEmpty())
				throw new ValidationException("Descri\u00e7\u00e3o n\u00e3o pode ficar em branco!");
			String sql = "UPDATE origem SET descricao = ? WHERE origem_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, origem.getDescricao());
			ps.setInt(2, origem.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Origem origem) throws ValidationException{
		if (origem.getId() == null)
			insert(origem);
		else
			update(origem);
	}

	public void delete(Origem origem) throws ValidationException{
		try {
			String sql = "SELECT count(num_registro) FROM exemplar WHERE origem_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, origem.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				int n = result.getInt(1);
				if (n > 0){
					result.close();
					ps.close();
					throw new ValidationException("Origem est\u00e1 em uso e n\u00e3o pode ser excluido");
				}
			}
			result.close();
			ps.close();
			sql = "DELETE FROM origem WHERE origem_id = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, origem.getId());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public Origem getOrigem(int id){
		try {
			String sql = "SELECT * FROM origem WHERE origem_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Origem origem = null;
			if (result.next()){
				origem = new Origem();
				origem.setId(result.getInt("origem_id"));
				origem.setDescricao(result.getString("descricao"));
			}
			result.close();
			ps.close();
			return origem;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Origem> getList(){
		try {
			String sql = "SELECT * FROM origem";
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			List<Origem> list = new ArrayList<>();
			while (result.next()){
				Origem origem = new Origem();
				origem.setId(result.getInt("origem_id"));
				origem.setDescricao(result.getString("descricao"));
				list.add(origem);
			}
			result.close();
			stmt.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Origem> findList(String fieldName, String text){
		try {
			String sql = "SELECT * FROM origem WHERE UPPER("+fieldName+") LIKE ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+text.toUpperCase()+"%");
			ResultSet result = ps.executeQuery();
			List<Origem> list = new ArrayList<>();
			while (result.next()){
				Origem origem = new Origem();
				origem.setId(result.getInt("origem_id"));
				origem.setDescricao(result.getString("descricao"));
				list.add(origem);
			}
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}


}
