package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Secao;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class SecaoDAO {

	private Connection connection = null;

	public SecaoDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public SecaoDAO(Connection connection){
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	public void insert(Secao secao) throws ValidationException{
		try {
			if (secao.getDescricao() == null || secao.getDescricao().isEmpty())
				throw new ValidationException("Descrição não pode ficar em branco!");
			String sql = "INSERT INTO secao(descricao) VALUES(?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, secao.getDescricao());
			ps.executeUpdate();
			ps.close();
			sql = "SELECT LAST_INSERT_ID() AS id";
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if (result.next())
				secao.setId(result.getInt(1));
			result.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Secao secao) throws ValidationException{
		try {
			if (secao.getId() == null)
				throw new RuntimeException("Id da seção não pode ser null!");
			if (secao.getDescricao() == null || secao.getDescricao().isEmpty())
				throw new ValidationException("Descrição não pode ficar em branco!");
			String sql = "UPDATE secao SET descricao = ? WHERE secao_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, secao.getDescricao());
			ps.setInt(2, secao.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Secao secao) throws ValidationException{
		if (secao.getId() == null)
			insert(secao);
		else
			update(secao);
	}

	public void delete(Secao secao) throws ValidationException{
		try {
			String sql = "SELECT count(num_registro) FROM exemplar WHERE secao_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, secao.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				int n = result.getInt(1);
				if (n > 0){
					result.close();
					ps.close();
					throw new ValidationException("Secao está em uso e não pode ser excluido");
				}
			}
			result.close();
			ps.close();
			sql = "DELETE FROM secao WHERE secao_id = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, secao.getId());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public Secao getSecao(int id){
		try {
			String sql = "SELECT * FROM secao WHERE secao_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Secao secao = null;
			if (result.next()){
				secao = new Secao();
				secao.setId(result.getInt("secao_id"));
				secao.setDescricao(result.getString("descricao"));
			}
			result.close();
			ps.close();
			return secao;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Secao> getList(){
		try {
			String sql = "SELECT * FROM secao";
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			List<Secao> list = new ArrayList<>();
			while (result.next()){
				Secao secao = new Secao();
				secao.setId(result.getInt("secao_id"));
				secao.setDescricao(result.getString("descricao"));
				list.add(secao);
			}
			result.close();
			stmt.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Secao> findList(String fieldName, String text){
		try {
			String sql = "SELECT * FROM secao WHERE UPPER("+fieldName+") LIKE ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+text.toUpperCase()+"%");
			ResultSet result = ps.executeQuery();
			List<Secao> list = new ArrayList<>();
			while (result.next()){
				Secao secao = new Secao();
				secao.setId(result.getInt("secao_id"));
				secao.setDescricao(result.getString("descricao"));
				list.add(secao);
			}
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}


}
