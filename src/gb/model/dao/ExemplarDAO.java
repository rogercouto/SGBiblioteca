package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gb.model.Exemplar;
import gb.model.Livro;
import gb.model.Origem;
import gb.model.Secao;
import gb.model.Situacao;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.util.NumericUtil;
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

	private void check(Exemplar exemplar) throws ValidationException, SQLException{
        StringBuilder error = new StringBuilder();
        if (exemplar.getNumRegistro() == null)
            error.append("N\u00famero do registro deve ser informado");
        else if (exemplar.getNumRegistro() == 0)
        	error.append("N\u00famero do registro n\u00e3o pode ser zero");
        else{
        	String sql = "SELECT count(*) FROM exemplar WHERE num_registro = ?";
        	PreparedStatement ps = connection.prepareStatement(sql);
        	ps.setInt(1, exemplar.getNumRegistro());
        	ResultSet result = ps.executeQuery();
        	int nr = 0;
        	if (result.next())
        		nr = result.getInt(1);
        	result.close();
        	ps.close();
        	if (nr > 0){
        		if (error.length() > 0)
                    error.append(";\n");
                error.append("Já existe um livro com esse Número!");
        	}
        }
        if (exemplar.getLivro() == null){
            if (error.length() > 0)
                error.append(";\n");
            error.append("Livro deve ser selecionado");
        }
        if (error.length() > 0)
            throw new ValidationException(error.toString());
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
			ps.setBoolean(6, exemplar.isFixo());
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
			String sql = "UPDATE exemplar SET livro_id = ?, secao_id = ?,"
					+ " data_aquisicao = ?, origem_id = ?, fixo = ?, situacao = ?"
					+ " WHERE num_registro = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setObject(1, exemplar.getLivro() != null ? exemplar.getLivro().getId() : null);
			ps.setObject(2, exemplar.getSecao() != null ? exemplar.getSecao().getId() : null);
			ps.setString(3, exemplar.getDataAquisicao() != null ?
						TemporalUtil.getDbDate(exemplar.getDataAquisicao()) :
						null
					);
			ps.setObject(4, exemplar.getOrigem() != null ? exemplar.getOrigem().getId() : null);
			ps.setBoolean(5, exemplar.isFixo());
			ps.setInt(6, exemplar.getSituacao().getValue());
			ps.setInt(7, exemplar.getNumRegistro());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
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
			if (exemplar != null && exemplar.getLivro() != null){
				LivroDAO livroDAO = new LivroDAO(connection);
				livroDAO.setAutores(exemplar.getLivro());
			}
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
			for (Exemplar exemplar : list) {
				if (exemplar.getLivro() != null){
					LivroDAO livroDAO = new LivroDAO(connection);
					livroDAO.setAutores(exemplar.getLivro());
				}
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	@Deprecated
	public List<Exemplar> getList(boolean fixo, Situacao[] situacoes){
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(getSelectSql());
			builder.append(" WHERE");
			builder.append(" e.fixo = ?");
			if (situacoes.length > 0){
				builder.append(" AND situacao IN(");
				for (int i = 0; i < situacoes.length; i++) {
					if (i > 0)
						builder.append(", ");
					builder.append(situacoes[i].getValue());
				}
				builder.append(")");
			}
			String sql = builder.toString();;
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setBoolean(1, fixo);
			ResultSet result = ps.executeQuery();
			List<Exemplar> list = new ArrayList<>();;
			while (result.next())
				list.add(getExemplar(result));
			result.close();
			ps.close();
			for (Exemplar exemplar : list) {
				if (exemplar.getLivro() != null){
					LivroDAO livroDAO = new LivroDAO(connection);
					livroDAO.setAutores(exemplar.getLivro());
				}
			}
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
			for (Exemplar exemplar : list) {
				if (exemplar.getLivro() != null){
					LivroDAO livroDAO = new LivroDAO(connection);
					livroDAO.setAutores(exemplar.getLivro());
				}
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public List<Exemplar> getList(Situacao situacao){
		try {
			String sql = getSelectSql("e.situacao = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, situacao.getValue());
			ResultSet result = ps.executeQuery();
			List<Exemplar> list = new ArrayList<>();;
			while (result.next())
				list.add(getExemplar(result));
			result.close();
			ps.close();
			for (Exemplar exemplar : list) {
				if (exemplar.getLivro() != null){
					LivroDAO livroDAO = new LivroDAO(connection);
					livroDAO.setAutores(exemplar.getLivro());
				}
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public List<Exemplar> findList(int index, String text, Situacao situacao){
		if (text.trim().length() == 0){
			if (situacao == null)
				return getList();
			else 
				return getList(situacao);
		}else{
			if (index == 0){
				if (!NumericUtil.isInteger(text))
					return new ArrayList<>();
				List<Exemplar> list = new ArrayList<>();
				Exemplar exemplar = get(NumericUtil.toInteger(text));
				if (exemplar != null && (situacao == null || exemplar.getSituacao().equals(situacao)))
					list.add(exemplar);
				return list;
			}else if (index == 1 || index == 2){
				LivroDAO livroDAO = new LivroDAO(connection);
				List<Livro> livros = livroDAO.findList((index == 1)?"titulo":"isbn", text);
				List<Exemplar> list = new ArrayList<>();
				for (Livro livro : livros) {
					for (Exemplar exemplar : livro.getExemplares()) {
						if (situacao == null || exemplar.getSituacao().equals(situacao));
							list.add(exemplar);
					}
				}
				Collections.sort(list, new Comparator<Exemplar>() {
					@Override
					public int compare(Exemplar e1, Exemplar e2) {
						return e1.getNumRegistro().compareTo(e2.getNumRegistro());
					}
				});
				return list;
			}
		}
		return new ArrayList<>();
	}
	
	public List<Exemplar> findList(int index, String text){
		return findList(index, text, null);
	}
}
