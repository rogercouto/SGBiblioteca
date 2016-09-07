package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Autor;
import gb.model.Livro;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class AutorDAO{

	private Connection connection;

	public AutorDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public AutorDAO(Connection connection){
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	private void check(Autor autor) throws ValidationException{
		StringBuilder error = new StringBuilder();
		if (autor.getNome() == null || autor.getNome().isEmpty())
			error.append("Nome n\u00e3o pode ficar em branco");
		if (autor.getSobrenome() == null || autor.getSobrenome().isEmpty()){
			if (error.length() > 0)
				error.append(";\n");
			error.append("Sobrenome n\u00e3o pode ficar em branco");
		}
		if (error.length() > 0){
			error.append("!");
			throw new ValidationException(error.toString());
		}
	}

	public void insert(Autor autor) throws ValidationException{
		try {
			check(autor);
			String sql = "INSERT INTO autor(nome, sobrenome, info) VALUES(?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, autor.getNome());
			ps.setString(2, autor.getSobrenome());
			ps.setString(3, autor.getInfo());
			ps.executeUpdate();
			autor.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Autor autor) throws ValidationException{
		try {
			if (autor.getId() == null)
				throw new RuntimeException("autor.getId() == null");
			String sql = "UPDATE autor SET nome = ?, sobrenome = ?, info = ? WHERE autor_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, autor.getNome());
			ps.setString(2, autor.getSobrenome());
			ps.setString(3, autor.getInfo());
			ps.setInt(4, autor.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Autor autor) throws ValidationException{
		if (autor.getId() == null)
			insert(autor);
		else
			update(autor);
	}

	public void checkUse(Autor autor) throws ValidationException{
		try {
			String sql = "SELECT count(livro_id) FROM autor_livro WHERE livro_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, autor.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				if (result.getInt(1) > 0)
					throw new ValidationException("Autor j\u00e1 est\u00e1 em uso e "
							+ "n\u00e3o pode ser excluido!");
			}
			result.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(Autor autor) throws ValidationException{
		try {
			checkUse(autor);
			String sql = "DELETE FROM autor WHERE autor_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, autor.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM autor");
		if (filter != null){
			builder.append(" WHERE ");
			builder.append(filter);
		}
		return builder.toString();
	}

	public String getSelectSql(){
		return getSelectSql(null);
	}

	public Autor getAutor(ResultSet result) throws SQLException{
		Autor autor = new Autor();
		autor.setId(result.getInt("autor_id"));
		autor.setNome(result.getString("nome"));
		autor.setSobrenome(result.getString("sobrenome"));
		autor.setInfo(result.getString("info"));
		return autor;
	}

	public Autor get(int id){
		Autor autor = null;
		try {
			String sql = getSelectSql("autor_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if (result.next())
				autor = getAutor(result);
			result.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return autor;
	}

	public List<Autor> getList(){
		List<Autor> list = new ArrayList<>();
		try {
			String sql = getSelectSql();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next())
				list.add(getAutor(result));
			result.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

	public List<Autor> findList(String fieldName, String text){
		List<Autor> list = new ArrayList<>();
		try {
			String sql = getSelectSql("UPPER("+fieldName+") LIKE ?");
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%"+text.toUpperCase()+"%");
			ResultSet result = stmt.executeQuery();
			while (result.next())
				list.add(getAutor(result));
			result.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

	/**
	 * Retorna a lista de autores de um livro
	 * @param livro
	 * @return Lista de autores
	 */
	protected List<Autor> getList(Livro livro){
		try {
			List<Autor> list = new ArrayList<>();
			String sql = "SELECT * FROM autor_livro al"
					+ " INNER JOIN autor a ON al.autor_id = a.autor_id"
					+ " WHERE livro_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, livro.getId());
			ResultSet result = ps.executeQuery();
			while (result.next())
				list.add(getAutor(result));
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

}
