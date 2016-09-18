package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gb.model.Cidade;
import gb.model.Endereco;
import gb.model.Estado;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;

public class EnderecoDAO {

	private Connection connection;

	public EnderecoDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public EnderecoDAO(Connection connection){
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	private void check(Endereco endereco) throws ValidationException{
		StringBuilder error = new StringBuilder();
		if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty())
			error.append("Logradouro n\u00e3o pode ficar em branco");
		if (endereco.getCidade() == null){
			if (error.length() != 0)
				error.append(";\n");
			error.append("Cidade deve ser selecionada");
		}
		if (endereco.getBairro() == null){
			if (error.length() != 0)
				error.append(";\n");
			error.append("Bairro deve ser informado");
		}
		if (endereco.getCep() == null){
			if (error.length() != 0)
				error.append(";\n");
			error.append("Cep deve ser informado");
		}
		if (error.length() > 0){
			error.append("!");
			throw new ValidationException(error.toString());
		}
	}

	public void insert(Endereco endereco) throws ValidationException{
		try {
			check(endereco);
			String sql = "INSERT INTO endereco(logradouro, numero, cidade_id, bairro, cep,"
					+ " complemento) VALUES(?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, endereco.getLogradouro());
			ps.setObject(2, endereco.getNumero());
			if (endereco.getCidade() != null)
				ps.setObject(3, endereco.getCidade().getId());
			else
				ps.setObject(3, null);
			ps.setString(4, endereco.getBairro());
			ps.setString(5, endereco.getCep());
			ps.setString(6, endereco.getComplemento());
			ps.executeUpdate();
			ps.close();
			endereco.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Endereco endereco) throws ValidationException{
		try {
			if (endereco.getId() == null)
				throw new RuntimeException("Chave prim\u00e1rio indefinida para o endere\u00e7o!");
			check(endereco);
			String sql = "UPDATE endereco SET logradouro = ?, numero = ?, cidade_id = ?, bairro = ?,"
					+ " cep = ?, complemento = ?  WHERE endereco_id = ?";
			System.out.println(sql);
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, endereco.getLogradouro());
			ps.setObject(2, endereco.getNumero());
			if (endereco.getCidade() != null)
				ps.setObject(3, endereco.getCidade().getId());
			else
				ps.setObject(3, null);
			ps.setString(4, endereco.getBairro());
			ps.setString(5, endereco.getCep());
			ps.setString(6, endereco.getComplemento());
			ps.setInt(7, endereco.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Endereco endereco) throws ValidationException{
		if (endereco.getId() == null)
			insert(endereco);
		else
			update(endereco);
	}

	public void checkUse(Endereco endereco) throws ValidationException{
		try {
			String sql = "SELECT count(endereco_id) FROM usuario WHERE endereco_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, endereco.getId());
			ResultSet result = ps.executeQuery();
			if (result.next()){
				if (result.getInt(1) > 0)
					throw new ValidationException("Endere\u00e7o j\u00e1 est\u00e1 em uso e"
							+ " n\u00e3o pode ser removido!");
			}
			result.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(Endereco endereco) throws ValidationException{
		try {
			if (endereco.getId() == null)
				throw new RuntimeException("Falta id do endereco a ser atualizado!");
			checkUse(endereco);
			String sql = "DELETE FROM endereco WHERE endereco_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, endereco.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	private String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM endereco e ");
		builder.append("LEFT OUTER JOIN cidade c ON e.cidade_id = c.cidade_id ");
		builder.append("LEFT OUTER JOIN estado es ON c.sigla_estado = es.sigla_estado ");
		if (filter != null){
			builder.append("WHERE ");
			builder.append(filter);
		}
		return builder.toString();
	}

	private Endereco getEndereco(ResultSet result) throws SQLException{
		Endereco endereco = new Endereco();
		endereco.setId(result.getInt("e.endereco_id"));
		endereco.setLogradouro(result.getString("e.logradouro"));
		if (result.getObject("e.numero") != null)
			endereco.setNumero(result.getInt("e.numero"));
		endereco.setBairro(result.getString("e.bairro"));
		endereco.setCep(result.getString("cep"));
		endereco.setComplemento(result.getString("complemento"));
		Cidade cidade = new Cidade();
		cidade.setId(result.getInt("e.cidade_id"));
		cidade.setNome(result.getString("c.nome"));
		Estado estado = new Estado();
		estado.setSigla(result.getString("c.sigla_estado"));
		estado.setNome(result.getString("es.nome"));
		cidade.setEstado(estado);
		endereco.setCidade(cidade);
		return endereco;
	}

	public Endereco get(int id){
		try {
			String sql = getSelectSql("e.endereco_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Endereco endereco = null;
			if (result.next()){
				endereco = getEndereco(result);
			}
			result.close();
			ps.close();
			return endereco;
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public List<Cidade> findCidades(String nome){
		try {
			String sql = "SELECT * FROM cidade c INNER JOIN estado e ON c.sigla_estado  = e.sigla_estado WHERE c.nome LIKE ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "%"+nome+"%");
			ResultSet result = ps.executeQuery();
			List<Cidade> list = new ArrayList<>();
			while (result.next()){
				Cidade cidade = new Cidade();
				cidade.setId(result.getInt("cidade_id"));
				cidade.setNome(result.getString("c.nome"));
				Estado estado = new Estado();
				estado.setSigla(result.getString("e.sigla_estado"));
				estado.setNome(result.getString("e.nome"));
				cidade.setEstado(estado);
				list.add(cidade);
			}
			return list;
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

}
