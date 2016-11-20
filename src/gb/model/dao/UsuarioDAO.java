package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Cidade;
import gb.model.Endereco;
import gb.model.Estado;
import gb.model.TipoUsuario;
import gb.model.Usuario;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class UsuarioDAO {

	private Connection connection;

	public UsuarioDAO(){
		super();
		connection = ConnectionManager.getConnection();
	}

	public UsuarioDAO(Connection connection){
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	public Connection getConnection() {
		return connection;
	}

	private void check(Usuario usuario) throws ValidationException{
		StringBuilder error = new StringBuilder();
		if (usuario.getNome() == null || usuario.getNome().isEmpty())
			error.append("Nome n\u00e3o pode ficar em branco");
		if (usuario.getCpf() == null || usuario.getCpf().isEmpty()){
			if (error.length() != 0)
				error.append(";\n");
			error.append("CPF deve ser informado");
		}
		if (usuario.getTipo() == null){
			if (error.length() != 0)
				error.append(";\n");
			error.append("Tipo deve ser selecionado");
		}
		if (error.length() > 0){
			error.append("!");
			throw new ValidationException(error.toString());
		}
	}

	public void insert(Usuario usuario) throws ValidationException{
		try {
			check(usuario);
			String sql = "INSERT INTO usuario(nome,cpf,tipo_id,endereco_id,telefone,celular,email,login,senha)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ? ,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, usuario.getNome());
			ps.setString(2, usuario.getCpf());
			ps.setInt(3, usuario.getTipo().getId());
			if (usuario.getEndereco() != null)
				ps.setInt(4, usuario.getEndereco().getId());
			else
				ps.setObject(4, null);
			ps.setString(5, usuario.getTelefone());
			ps.setString(6, usuario.getCelular());
			ps.setString(7, usuario.geteMail());
			ps.setString(8, usuario.getLogin());
			ps.setString(9, usuario.getSenha());
			ps.executeUpdate();
			Statement st = connection.createStatement();
			sql = "SELECT LAST_INSERT_ID() AS id";
			ResultSet result = st.executeQuery(sql);
			if (result.next())
				usuario.setId(result.getInt(1));
			ps.close();
			result.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Usuario usuario)throws ValidationException{
		try {
			check(usuario);
			if (usuario.getId() == null)
				throw new RuntimeException("Falta id do usu\u00e1rio a ser atualizado!");
			String sql = "UPDATE usuario SET nome = ?, cpf = ?, tipo_id = ?, endereco_id = ?,"
					+ " telefone = ?, celular = ?, email = ?, login = ?, senha = ? WHERE usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, usuario.getNome());
			ps.setString(2, usuario.getCpf());
			ps.setInt(3, usuario.getTipo().getId());
			if (usuario.getEndereco() != null)
				ps.setInt(4, usuario.getEndereco().getId());
			else
				ps.setObject(4, null);
			ps.setString(5, usuario.getTelefone());
			ps.setString(6, usuario.getCelular());
			ps.setString(7, usuario.geteMail());
			ps.setString(8, usuario.getLogin());
			ps.setString(9, usuario.getSenha());
			ps.setInt(10, usuario.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Usuario usuario)throws ValidationException{
		if (usuario.getId() == null)
			insert(usuario);
		else
			update(usuario);
	}

	private void checkUse(Usuario usuario) throws ValidationException{
		try {
			String sql = "SELECT count(usuario_id) FROM emprestimo WHERE usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ResultSet result = ps.executeQuery();
			boolean inUse = false;
			if (result.next()){
				if (result.getInt(1) > 0)
					inUse = true;
			}
			if (!inUse){
				sql = "SELECT count(usuario_id) FROM reserva WHERE usuario_id = ?";
				ps = connection.prepareStatement(sql);
				ps.setInt(1, usuario.getId());
				result = ps.executeQuery();
				if (result.next()){
					if (result.getInt(1) > 0)
						inUse = true;
				}
			}
			result.close();
			ps.close();
			if (inUse)
				throw new ValidationException("Usu\u00e1rio j\u00e1 est\u00e1 em uso e"
						+ " n\u00e3o pode ser removido!");
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}

	}

	public void delete(Usuario usuario) throws ValidationException{
		try {
			if (usuario.getId() == null)
				throw new RuntimeException("Falta id do usu\u00e1rio a ser removido!");
			checkUse(usuario);
			Integer idEndereco = null;
			if (usuario.getEndereco() != null)
				idEndereco = usuario.getEndereco().getId();
			String sql = "DELETE FROM usuario WHERE usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ps.executeUpdate();
			if (idEndereco != null){
				sql = "DELETE FROM endereco WHERE endereco_id = ?";
				ps = connection.prepareStatement(sql);
				ps.setInt(1, idEndereco);
				ps.executeUpdate();
			}
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	private String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM usuario u ");
		builder.append("INNER JOIN tipo_usuario tu ON u.tipo_id = tu.tipo_id ");
		builder.append("LEFT OUTER JOIN endereco en ON u.endereco_id = en.endereco_id ");
		builder.append("LEFT OUTER JOIN cidade ci ON en.cidade_id = ci.cidade_id ");
		builder.append("LEFT OUTER JOIN estado es ON ci.sigla_estado = es.sigla_estado ");
		if (filter != null){
			builder.append("WHERE ");
			builder.append(filter);
		}
		return builder.toString();
	}

	private String getSelectSql(){
		return getSelectSql(null);
	}

 	protected static Usuario getUsuario(ResultSet result) throws SQLException{
		Usuario usuario = new Usuario();
		usuario.setId(result.getInt("u.usuario_id"));
		usuario.setNome(result.getString("u.nome"));
		usuario.setCpf(result.getString("u.cpf"));
		usuario.setTelefone(result.getString("u.telefone"));
		usuario.setCelular(result.getString("u.celular"));
		usuario.seteMail(result.getString("u.email"));
		usuario.setLogin(result.getString("u.login"));
		usuario.setSenha(result.getString("u.senha"));
		TipoUsuario tipo = new TipoUsuario();
		tipo.setId(result.getInt("u.tipo_id"));
		tipo.setDescricao(result.getString("tu.descricao"));
		tipo.setDiasEmprestimo(result.getInt("tu.dias_emprestimo"));
		tipo.setNumLivrosEmp(result.getInt("tu.num_livros_emp"));
		tipo.setNumLivrosRes(result.getInt("tu.num_livros_res"));
		tipo.setNumRenov(result.getInt("tu.num_renov"));
		usuario.setTipo(tipo);
		if (result.getObject("u.endereco_id") != null){
			Endereco endereco = new Endereco();
			endereco.setId(result.getInt("u.endereco_id"));
			endereco.setLogradouro(result.getString("en.logradouro"));
			if (result.getObject("en.numero") != null)
				endereco.setNumero(result.getInt("en.numero"));
			endereco.setBairro(result.getString("en.bairro"));
			endereco.setCep(result.getString("en.cep"));
			endereco.setComplemento(result.getString("en.complemento"));
			Cidade cidade = new Cidade();
			cidade.setId(result.getInt("en.cidade_id"));
			cidade.setNome(result.getString("ci.nome"));
			Estado estado = new Estado();
			estado.setSigla(result.getString("ci.sigla_estado"));
			estado.setNome(result.getString("es.nome"));
			cidade.setEstado(estado);
			endereco.setCidade(cidade);
			usuario.setEndereco(endereco);
		}
		return usuario;
	}

	public Usuario get(int id){
		try {
			String sql = getSelectSql("usuario_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Usuario usuario = null;
			if (result.next()){
				usuario = getUsuario(result);
			}
			result.close();
			ps.close();
			return usuario;
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Usuario> getList(){
		List<Usuario> list = new ArrayList<>();
		try {
			String sql = getSelectSql();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()){
				list.add(getUsuario(result));
			}
			result.close();
			stmt.close();
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

	public List<Usuario> findList(int index, String text){
		List<Usuario> list = new ArrayList<>();
		try {
			String sql = getSelectSql(index == 0 ? "UPPER(u.nome) LIKE ?" : "UPPER(u.login) LIKE ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+text.toUpperCase()+"%");

			ResultSet result = ps.executeQuery();
			while (result.next()){
				list.add(getUsuario(result));
			}
			result.close();
			ps.close();
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return list;
	}

	public int getNumReservas(Usuario usuario){
		try {
			int numReservas = 0;
			String sql = "select count(reserva_id) from reserva where data_hora_retirada IS NULL AND usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ResultSet result = ps.executeQuery();
			if (result.next())
				numReservas = result.getInt(1);
			result.close();
			ps.close();
			return numReservas;
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public int getNumEmprestimosAtivos(Usuario usuario){
		try {
			int numReservas = 0;
			String sql = "select count(emprestimo_id) from emprestimo where data_hora_devolucao IS NULL AND usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ResultSet result = ps.executeQuery();
			if (result.next())
				numReservas = result.getInt(1);
			result.close();
			ps.close();
			return numReservas;
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
}
