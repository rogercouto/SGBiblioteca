package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Editora;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class EditoraDAO {

	private Connection connection = null;

	public EditoraDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public EditoraDAO(Connection connection){
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	public void insert(Editora editora) throws ValidationException{
		try {
			if (editora.getNome() == null || editora.getNome().isEmpty())
				throw new ValidationException("Nome da editora deve ser informado!");
			String sql = "INSERT INTO editora(nome) VALUES(?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, editora.getNome());
			ps.executeUpdate();
			ps.close();
			editora.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Editora editora) throws ValidationException{
		try {
			if (editora.getId() == null)
				throw new RuntimeException("Id da editora n\u00e3o pode ser null!");
			if (editora.getNome() == null || editora.getNome().isEmpty())
				throw new ValidationException("Nome da editora deve ser informado!");
			String sql = "UPDATE editora SET nome = ? WHERE editora_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, editora.getNome());
			ps.setInt(2, editora.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Editora editora) throws ValidationException{
		if (editora.getId() == null)
			insert(editora);
		else
			update(editora);
	}

	public void delete(Editora editora) throws ValidationException{
		try {
			String sql = "SELECT count(livro_id) FROM livro WHERE editora_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, editora.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				int n = result.getInt(1);
				if (n > 0){
					result.close();
					ps.close();
					throw new ValidationException("Editora est\u00e1 em uso e n\u00e3o"
							+ " pode ser excluido");
				}
			}
			result.close();
			ps.close();
			ps = connection.prepareStatement(sql);
			sql = "DELETE FROM editora WHERE editora_id = ?";
			ps.setInt(1, editora.getId());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public Editora getEditora(int id){
		try {
			String sql = "SELECT * FROM editora WHERE editora_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Editora editora = null;
			if (result.next()){
				editora = new Editora();
				editora.setId(result.getInt("editora_id"));
				editora.setNome(result.getString("nome"));
			}
			result.close();
			ps.close();
			return editora;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Editora> getList(){
		try {
			String sql = "SELECT * FROM editora";
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			List<Editora> list = new ArrayList<>();
			while (result.next()){
				Editora editora = new Editora();
				editora.setId(result.getInt("editora_id"));
				editora.setNome(result.getString("nome"));
				list.add(editora);
			}
			result.close();
			stmt.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Editora> findList(String fieldName, String text){
		try {
			String sql = "SELECT * FROM editora WHERE UPPER("+fieldName+") = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+text.toUpperCase()+"%");
			ResultSet result = ps.executeQuery();
			List<Editora> list = new ArrayList<>();
			while (result.next()){
				Editora editora = new Editora();
				editora.setId(result.getInt("editora_id"));
				editora.setNome(result.getString("nome"));
				list.add(editora);
			}
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}


}
