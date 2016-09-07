package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Exemplar;
import gb.model.Livro;
import gb.model.Origem;
import gb.model.Secao;
import gb.model.Situacao;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.util.TemporalUtil;

public class ExemplarDAO {

	private Connection connection = null;

	public ExemplarDAO() {
		super();
        connection = ConnectionManager.getConnection();
	}

	public ExemplarDAO(Connection connection) {
		super();
		this.connection = connection;
	}

	public void closeConnection(){
        ConnectionManager.closeConnection(connection);
    }

	private void check(Exemplar exemplar) throws ValidationException{
        StringBuilder error = new StringBuilder();
        if (exemplar.getNumRegistro() == null)
            error.append("N\u00famero do registro deve ser informado");
        else if (exemplar.getNumRegistro() == 0)
        	error.append("N\u00famero do registro n\u00e3o pode ser zero");
        if (exemplar.getLivro() == null){
            if (error.length() > 0)
                error.append(";\n");
            error.append("Livro deve ser selecionado");
        }
        if (error.length() > 0)
            throw new ValidationException(error.toString());
        if (exemplar.getFixo() == null)
        	exemplar.setFixo(false);
        if (exemplar.getSituacao() == null)
        	exemplar.setSituacao(Situacao.DISPONIVEL);
    }

	public void insert(Exemplar exemplar) throws ValidationException{
		try {
			check(exemplar);
			String sql = "INSERT INTO exemplar VALUES(?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setObject(1, exemplar.getNumRegistro());
			ps.setObject(2, exemplar.getLivro() != null ? exemplar.getLivro().getId() : null);
			ps.setObject(3, exemplar.getSecao() != null ? exemplar.getSecao().getId() : null);
			ps.setString(4, exemplar.getDataAquisicao() != null ?
						TemporalUtil.getDbDate(exemplar.getDataAquisicao()) :
						null
					);
			ps.setObject(5, exemplar.getOrigem() != null ? exemplar.getOrigem().getId() : null);
			ps.setBoolean(6, exemplar.getFixo());
			ps.setInt(7, exemplar.getSituacao().getValue());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Exemplar exemplar) throws ValidationException{
		try {
			check(exemplar);
			if (exemplar.getNumRegistro() == null)
				throw new RuntimeException("Id do exemplar n\u00e3o pode ser null!");
			String sql = "INSERT INTO exemplar SET num_registro = ?, livro_id = ?, secao_id = ?,"
					+ " data_aquisicao = ?, origem_id, fixo = ?, situacao = ?"
					+ " WHERE num_registro = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setObject(1, exemplar.getNumRegistro());
			ps.setObject(2, exemplar.getLivro() != null ? exemplar.getLivro().getId() : null);
			ps.setObject(3, exemplar.getSecao() != null ? exemplar.getSecao().getId() : null);
			ps.setString(4, exemplar.getDataAquisicao() != null ?
						TemporalUtil.getDbDate(exemplar.getDataAquisicao()) :
						null
					);
			ps.setObject(5, exemplar.getOrigem() != null ? exemplar.getOrigem().getId() : null);
			ps.setBoolean(6, exemplar.getFixo());
			ps.setInt(7, exemplar.getSituacao().getValue());
			ps.setInt(8, exemplar.getNumRegistro());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Exemplar exemplar) throws ValidationException{
		if (exemplar.getNumRegistro() == null)
			insert(exemplar);
		else
			update(exemplar);
	}

	public void checkUse(Exemplar exemplar) throws ValidationException{
		try {
			String sql = "SELECT count(num_registro) FROM reserva WHERE num_registro = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, exemplar.getNumRegistro());
            ResultSet result = ps.executeQuery();
            result = ps.executeQuery();
            if (result.next() && result.getInt(1) > 0){
            	result.close();
            	ps.close();
            	throw new ValidationException("Exemplar j\u00e1 est\u00e1 em uso e"
            			+ " n\u00e3o pode ser excluido!");
            }
            result.close();
        	ps.close();
            sql = "SELECT count(num_registro) FROM emprestimo WHERE num_registro = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, exemplar.getNumRegistro());
            result = ps.executeQuery();
            if (result.next() && result.getInt(1) > 0){
            	result.close();
            	ps.close();
            	throw new ValidationException("Exemplar j\u00e1 est\u00e1 em uso e"
            			+ " n\u00e3o pode ser excluido!");
            }
            result.close();
        	ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(Exemplar exemplar) throws ValidationException{
		try {
			checkUse(exemplar);
			String sql = "DELETE FROM exemplar WHERE num_registro = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, exemplar.getNumRegistro());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM exemplar e");
		builder.append(" INNER JOIN livro l on e.livro_id = l.livro_id");
		builder.append(" LEFT OUTER JOIN editora ed ON l.editora_id = ed.editora_id");
        builder.append(" LEFT OUTER JOIN assunto a ON l.assunto_id = a.assunto_id");
		builder.append(" LEFT OUTER JOIN secao s on e.secao_id = s.secao_id");
		builder.append(" LEFT OUTER JOIN origem o on e.origem_id = o.origem_id");
		if (filter != null){
			builder.append(" WHERE ");
			builder.append(filter);
		}
		return builder.toString();
	}

	public String getSelectSql(){
		return getSelectSql(null);
	}

	protected static Secao getSecao(ResultSet result) throws SQLException{
		if (result.getObject("s.secao_id") == null)
			return null;
		Secao secao = new Secao();
		secao.setId(result.getInt("s.secao_id"));
		secao.setDescricao(result.getString("s.descricao"));
		return secao;
	}

	protected static Origem getOrigem(ResultSet result) throws SQLException{
		if (result.getObject("e.origem_id") == null)
			return null;
		Origem origem = new Origem();
		origem.setId(result.getInt("o.origem_id"));
		origem.setDescricao(result.getString("o.descricao"));
		return origem;
	}

	protected static Exemplar getExemplar(ResultSet result) throws SQLException{
		Exemplar exemplar = new Exemplar();
		exemplar.setNumRegistro(result.getInt("e.num_registro"));
		exemplar.setLivro(LivroDAO.getLivro(result));
		exemplar.setDataAquisicao(TemporalUtil.getLocalDate(result.getString("e.data_aquisicao")));
		exemplar.setOrigem(getOrigem(result));
		exemplar.setFixo(result.getBoolean("fixo"));
		exemplar.setSecao(getSecao(result));
		exemplar.setSituacao(Situacao.getSituacao(result.getInt("e.situacao")));
		return exemplar;
	}

	public Exemplar get(int id){
		try {
			String sql = getSelectSql("e.num_registro = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Exemplar exemplar = null;
			if (result.next())
				exemplar = getExemplar(result);
			result.close();
			ps.close();
			return exemplar;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Exemplar> getList(){
		try {
			String sql = getSelectSql();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			List<Exemplar> list = new ArrayList<>();;
			while (result.next())
				list.add(getExemplar(result));
			result.close();
			stmt.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Exemplar> getList(Livro livro){
		try {
			String sql = getSelectSql("e.livro_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, livro.getId());
			ResultSet result = ps.executeQuery();
			List<Exemplar> list = new ArrayList<>();;
			while (result.next())
				list.add(getExemplar(result));
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}



}
