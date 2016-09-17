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
import gb.model.Reserva;
import gb.model.Secao;
import gb.model.Situacao;
import gb.model.Usuario;
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
			ps.setBoolean(5, exemplar.getFixo());
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
	
	private List<Exemplar> findListNaoFixo(int numRegistro){
		try {
			String sql = getSelectSql("e.fixo = 0 AND numRegistro = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, numRegistro);
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
	
	private List<Exemplar> getListDisponivel(){
		try {
			String sql = getSelectSql("e.fixo = 0 AND situacao = 1");
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
	
	private List<Exemplar> getListEmprestado(){
		try {
			String sql = getSelectSql("e.fixo = 0 AND situacao = 3");
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
	
	private List<Exemplar> findListDisponivel(int numRegistro){
		try {
			String sql = getSelectSql("e.fixo = 0 AND situacao = 1 AND numRegistro = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, numRegistro);
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
	
	private List<Exemplar> findListEmprestado(int numRegistro){
		try {
			String sql = getSelectSql("e.fixo = 0 AND situacao = 1 AND numRegistro = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, numRegistro);
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

	private static boolean isIn(Exemplar exemplar, List<Reserva> reservas){
		for (Reserva reserva : reservas) {
			if (exemplar.getNumRegistro().intValue() == reserva.getExemplar().getNumRegistro().intValue())
				return true;
		}
		return false;
	}
	
	private static boolean isNumeric(String string){
		char[] ca = string.toCharArray();
		for (char c : ca) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
	
	private List<Exemplar> getDisp(List<Exemplar> fullList, List<Reserva> resList){
		List<Exemplar> list = new ArrayList<>();
		for (Exemplar exemplar : fullList) {
			if (exemplar.getSituacao().equals(Situacao.DISPONIVEL) || 
			(exemplar.getSituacao().equals(Situacao.RESERVADO) &&	isIn(exemplar, resList)))
			{
				list.add(exemplar);
			}
		}
		return list;
	}
	
	public List<Exemplar> findFromDialogEmprestimo(int index, String text, Usuario usuario){
		ReservaDAO dao = new ReservaDAO(connection);
		List<Reserva> reservas = null;
		if (usuario != null)
			reservas = dao.getList(usuario);
		if (text.trim().length() == 0){
			if (usuario == null){
				return getListDisponivel();
			}else{
				return getDisp(getList(), reservas);
			}
		}else{
			if (index == 0){
				if (isNumeric(text)){
					if (usuario == null){
						return findListDisponivel(Integer.parseInt(text));
					}else{
						return getDisp(findListNaoFixo(Integer.parseInt(text)), reservas);
					}
				}
			}else if (index == 1){
				LivroDAO livroDAO = new LivroDAO(connection);
				List<Livro> livros = livroDAO.findList("titulo", text);
				List<Exemplar> result = new ArrayList<>();
				if (usuario == null){
					for (Livro livro : livros) {
						for (Exemplar exemplar : livro.getExemplares()) {
							if (!exemplar.getFixo() && exemplar.getSituacao() == Situacao.DISPONIVEL)
								result.add(exemplar);
						}
					}
				}else{
					for (Livro livro : livros) {
						result.addAll(getDisp(livro.getExemplares(), reservas));
					}
				}
				Collections.sort(result, new Comparator<Exemplar>() {
					@Override
					public int compare(Exemplar e1, Exemplar e2) {
						return e1.getNumRegistro().compareTo(e2.getNumRegistro());
					}
				});
				return result;
			}
		}
		return new ArrayList<>();
	}
	
	public List<Exemplar> findFromDialogReserva(int index, String text){
		if (text.trim().length() == 0){
			return getListDisponivel();
		}else{
			if (index == 0){
				if (isNumeric(text)){
					return findListDisponivel(Integer.parseInt(text));
				}
			}else if (index == 1){
				LivroDAO livroDAO = new LivroDAO(connection);
				List<Livro> livros = livroDAO.findList("titulo", text);
				List<Exemplar> result = new ArrayList<>();
				for (Livro livro : livros) {
					for (Exemplar exemplar : livro.getExemplares()) {
						if (!exemplar.getFixo() && exemplar.getSituacao() == Situacao.DISPONIVEL)
							result.add(exemplar);
					}
				}
				Collections.sort(result, new Comparator<Exemplar>() {
					@Override
					public int compare(Exemplar e1, Exemplar e2) {
						return e1.getNumRegistro().compareTo(e2.getNumRegistro());
					}
				});
				return result;
			}
		}
		return new ArrayList<>();
	}
	
	public List<Exemplar> findFromDialogDevolucao(int index, String text){
		if (text.trim().length() == 0){
			return getListEmprestado();
		}else{
			if (index == 0){
				if (isNumeric(text)){
					return findListEmprestado(Integer.parseInt(text));
				}
			}else if (index == 1){
				LivroDAO livroDAO = new LivroDAO(connection);
				List<Livro> livros = livroDAO.findList("titulo", text);
				List<Exemplar> result = new ArrayList<>();
				for (Livro livro : livros) {
					for (Exemplar exemplar : livro.getExemplares()) {
						if (exemplar.getSituacao() == Situacao.EMPRESTADO)
							result.add(exemplar);
					}
				}
				Collections.sort(result, new Comparator<Exemplar>() {
					@Override
					public int compare(Exemplar e1, Exemplar e2) {
						return e1.getNumRegistro().compareTo(e2.getNumRegistro());
					}
				});
				return result;
			}
		}
		return new ArrayList<>();
	}
	
}
