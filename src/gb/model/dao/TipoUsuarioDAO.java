package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.TipoUsuario;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class TipoUsuarioDAO {

	private Connection connection;

	public TipoUsuarioDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public TipoUsuarioDAO(Connection connection) {
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	private void check(TipoUsuario tipo) throws ValidationException{
		if (tipo.getDescricao() == null
			|| tipo.getDescricao().isEmpty()
			|| tipo.getDiasEmprestimo() == null
			|| tipo.getNumLivrosEmp() == null
			|| tipo.getNumLivrosRes() == null
			|| tipo.getNumRenov() == null)
			throw new ValidationException("Todos os campos devem ser preenchidos!");

	}

	public void insert(TipoUsuario tipo) throws ValidationException{
		try {
			String sql = "INSERT INTO tipo_usuario(descricao, num_livros_emp, num_livros_res, dias_emprestimo, num_renov) VALUES(?, ?, ?, ?, ?)";
			check(tipo);
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, tipo.getDescricao());
			if (tipo.getNumLivrosEmp() != null)
				ps.setInt(2, tipo.getNumLivrosEmp());
			if (tipo.getNumLivrosRes() != null)
				ps.setInt(3, tipo.getNumLivrosRes());
			if (tipo.getDiasEmprestimo() != null)
				ps.setInt(4, tipo.getDiasEmprestimo());
			if (tipo.getNumRenov() != null)
				ps.setInt(5, tipo.getNumRenov());
			ps.executeUpdate();
			Statement st = connection.createStatement();
			sql = "SELECT LAST_INSERT_ID() AS id";
			ResultSet result = st.executeQuery(sql);
			if (result.next())
				tipo.setId(result.getInt(1));
			result.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(TipoUsuario tipo) throws ValidationException{
		try {
			if (tipo.getId() == null)
				throw new RuntimeException("Falta id no tipo a ser atualizado!");
			String sql = "UPDATE tipo_usuario set descricao = ?, num_livros_emp = ?,"
					+ " num_livros_res = ?, dias_emprestimo = ?, num_renov = ?"
					+ " WHERE tipo_id = ?";
			check(tipo);
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, tipo.getDescricao());
			if (tipo.getNumLivrosEmp() != null)
				ps.setInt(2, tipo.getNumLivrosEmp());
			if (tipo.getNumLivrosRes() != null)
				ps.setInt(3, tipo.getNumLivrosRes());
			if (tipo.getDiasEmprestimo() != null)
				ps.setInt(4, tipo.getDiasEmprestimo());
			if (tipo.getNumRenov() != null)
				ps.setInt(5, tipo.getNumRenov());
			ps.setInt(6, tipo.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(TipoUsuario tipo) throws ValidationException{
		if (tipo.getId() == null)
			insert(tipo);
		else
			update(tipo);
	}

	private void checkUse(TipoUsuario tipo)throws ValidationException{
		try {
			String sql = "SELECT count(tipo_id) FROM usuario WHERE tipo_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, tipo.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				if (result.getInt(1) > 0)
					throw new ValidationException("Usu\u00e1rio j\u00e1 est\u00e1 em uso e"
							+ " n\u00e3o pode ser removido!");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(TipoUsuario tipo) throws ValidationException{
		try {
			if (tipo.getId() == null)
				throw new RuntimeException("Falta id do tipo de usuario a ser atualizado!");
			checkUse(tipo);
			String sql = "DELETE FROM tipo_usuario WHERE tipo_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, tipo.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	private String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM tipo_usuario ");
		if (filter != null){
			builder.append("WHERE ");
			builder.append(filter);
		}
		return builder.toString();
	}

	private String getSelectSql(){
		return getSelectSql(null);
	}

	private TipoUsuario getTipoUsuario(ResultSet result) throws SQLException{
		TipoUsuario tipo = new TipoUsuario();
		tipo.setId(result.getInt("tipo_id"));
		tipo.setDescricao(result.getString("descricao"));
		tipo.setNumLivrosRes(result.getInt("num_livros_res"));
		tipo.setNumLivrosEmp(result.getInt("num_livros_emp"));
		tipo.setDiasEmprestimo(result.getInt("dias_emprestimo"));
		tipo.setNumRenov(result.getInt("num_renov"));
		return tipo;
	}

	public TipoUsuario get(int id){
		try {
			String sql = getSelectSql("tipo_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			TipoUsuario tipo = null;
			if (result.next())
				 tipo = getTipoUsuario(result);
			result.close();
			return tipo;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<TipoUsuario> getList(){
		List<TipoUsuario> list = new ArrayList<>();
		try {
			String sql = getSelectSql();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next())
				 list.add(getTipoUsuario(result));
			result.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

}
