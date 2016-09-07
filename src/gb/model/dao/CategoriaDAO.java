package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Categoria;
import gb.model.Livro;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class CategoriaDAO {

	private Connection connection;

	public CategoriaDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public CategoriaDAO(Connection connection){
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	public void insert(Categoria categoria) throws ValidationException{
		try {
			if (categoria.getDescricao() == null || categoria.getDescricao().isEmpty())
				throw new ValidationException("Descri\u00e7\u00e3o n\u00e3o pode ficar em branco");
			String sql = "INSERT INTO categoria(descricao) VALUES(?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, categoria.getDescricao());
			ps.executeUpdate();
			categoria.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Categoria categoria) throws ValidationException{
		try {
			if (categoria.getId() == null)
				throw new RuntimeException("Id da categoria n\u00e3o pode ser null!");
			String sql = "UPDATE categoria SET descricao = ? WHERE categoria_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, categoria.getDescricao());
			ps.setInt(2, categoria.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Categoria categoria) throws ValidationException{
		if (categoria.getId() == null)
			insert(categoria);
		else
			update(categoria);
	}

	public void checkUse(Categoria categoria) throws ValidationException{
		try {
			String sql = "SELECT count(livro_id) FROM categoria_livro WHERE livro_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, categoria.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				if (result.getInt(1) > 0)
					throw new ValidationException("Categoria j\u00e1 est\u00e1 em uso e"
							+ " n\u00e3o pode ser excluido!");
			}
			result.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(Categoria categoria) throws ValidationException{
		try {
			checkUse(categoria);
			String sql = "DELETE FROM categoria WHERE categoria_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, categoria.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM categoria");
		if (filter != null){
			builder.append(" WHERE ");
			builder.append(filter);
		}
		return builder.toString();
	}

	public String getSelectSql(){
		return getSelectSql(null);
	}

	public Categoria getCategoria(ResultSet result) throws SQLException{
		Categoria categoria = new Categoria();
		categoria.setId(result.getInt("categoria_id"));
		categoria.setDescricao(result.getString("descricao"));
		return categoria;
	}

	public Categoria get(int id){
		Categoria categoria = null;
		try {
			String sql = getSelectSql("categoria_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if (result.next())
				categoria = getCategoria(result);
			result.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return categoria;
	}

	public List<Categoria> getCategorias(){
		List<Categoria> list = new ArrayList<>();
		try {
			String sql = getSelectSql();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next())
				list.add(getCategoria(result));
			result.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

	public List<Categoria> findCategorias(String text){
		List<Categoria> list = new ArrayList<>();
		try {
			String sql = getSelectSql("UPPER(descricao) LIKE ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+text.toUpperCase()+"%");
			ResultSet result = ps.executeQuery();
			while (result.next())
				list.add(getCategoria(result));
			result.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

	/**
	 * Retorna a lista de categorias de um livro
	 * @param livro
	 * @return Lista de categoriaes
	 */
	protected List<Categoria> getCategorias(Livro livro){
		try {
			List<Categoria> list = new ArrayList<>();
			String sql = "SELECT * FROM categoria_livro al"
					+ " INNER JOIN categoria a ON al.categoria_id = a.categoria_id"
					+ " WHERE livro_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, livro.getId());
			ResultSet result = ps.executeQuery();
			while (result.next())
				list.add(getCategoria(result));
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
}
