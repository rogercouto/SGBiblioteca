package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gb.model.Assunto;
import gb.model.Autor;
import gb.model.Categoria;
import gb.model.Editora;
import gb.model.Livro;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.FindException;
import gb.model.exceptions.ValidationException;

public class LivroDAO {

    private Connection connection = null;

    public LivroDAO() {
        super();
        connection = ConnectionManager.getConnection();
    }

    public LivroDAO(Connection connection){
        super();
        this.connection = connection;
    }

    public void closeConnection(){
        ConnectionManager.closeConnection(connection);
    }

    private void check(Livro livro) throws ValidationException{
    	StringBuilder error = new StringBuilder();
        if (livro.getTitulo() == null || livro.getTitulo().isEmpty())
        	error.append("Título deve ser informado");
        if (livro.getIsbn() == null){
        	if (error.length() > 0)
        		error.append(";\n");
        	error.append("ISBN deve ser informado");
        }else if (livro.getIsbn().toString().length() < 10 || livro.getIsbn().toString().length() > 13){
        	if (error.length() > 0)
        		error.append(";\n");
        	error.append("ISBN deve conter de 10 a 13 caracteres");
        }
        if (error.length() > 0){
        	error.append("!");
        	throw new ValidationException(error.toString());
        }
    }

    private void setValues(Livro livro, PreparedStatement ps) throws SQLException{
        ps.setString(1, livro.getTitulo());
        ps.setString(2, livro.getResumo());
        ps.setObject(3, livro.getIsbn());
        ps.setString(4, livro.getCutter());
        if (livro.getEditora() != null)
            ps.setInt(5, livro.getEditora().getId());
        else
            ps.setObject(5, null);
        ps.setString(6, livro.getEdicao());
        ps.setString(7, livro.getVolume());
        ps.setObject(8, livro.getNumPaginas());
        if (livro.getAssunto() != null)
            ps.setInt(9, livro.getAssunto().getId());
        else
            ps.setObject(9, null);
        ps.setObject(10, livro.getAnoPublicacao());
    }

    private void insertAutores(Livro livro) throws SQLException{
        for (Autor autor : livro.getAutores()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO autor_livro VALUES(?, ?)");
            ps.setInt(1, autor.getId());
            ps.setInt(2, livro.getId());
            ps.executeUpdate();
            ps.close();
        }
    }

    private void insertCategorias(Livro livro) throws SQLException{
        for (Categoria categoria : livro.getCategorias()) {
        	PreparedStatement ps = connection.prepareStatement("INSERT INTO categoria_livro VALUES(?,?)");
            ps.setInt(1, categoria.getId());
            ps.setInt(2, livro.getId());
            ps.executeUpdate();
            ps.close();
        }
    }

    public void insert(Livro livro) throws ValidationException{
        try {
            check(livro);
            String sql = "INSERT INTO livro(titulo, resumo, isbn, cutter, editora_id,"
                    + " edicao, volume, num_paginas, assunto_id,"
                    + " ano_publicacao) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            setValues(livro, ps);
            ps.executeUpdate();
            ps.close();
            livro.setId(ConnectionManager.getLastInsertId(connection));
            insertAutores(livro);
            insertCategorias(livro);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private void deleteAutores(Livro livro) throws SQLException{
        String sql = "DELETE FROM autor_livro WHERE livro_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, livro.getId());
        ps.executeUpdate();
        ps.close();
    }

    private void deleteCategorias(Livro livro) throws SQLException{
        String sql = "DELETE FROM categoria_livro WHERE livro_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, livro.getId());
        ps.executeUpdate();
        ps.close();
    }

    public void update(Livro livro) throws ValidationException{
        try {
            if (livro.getId() == null)
                throw new RuntimeException("Id do livro n\u00e3o pode ser null!");
            check(livro);
            String sql = "UPDATE livro SET titulo = ?, resumo = ?, isbn = ?, cutter= ?, "
                    + "editora_id = ?, edicao= ?, volume = ?, num_paginas = ?, "
                    + "assunto_id = ?, ano_publicacao = ? "
                    + "WHERE livro_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            setValues(livro, ps);
            ps.setInt(11, livro.getId());
            ps.executeUpdate();
            ps.close();
            if (livro.atualizaAutores()){
                deleteAutores(livro);
                insertAutores(livro);
            }
            if (livro.atualizaCategorias()){
                deleteCategorias(livro);
                insertCategorias(livro);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void save(Livro livro) throws ValidationException{
        if (livro.getId() == null)
            insert(livro);
        else
            update(livro);
    }

    public void delete(Livro livro) throws ValidationException{
        try {
            if (livro.getId() == null)
                throw new RuntimeException("Id do livro n\u00e3o pode ser null!");
            String sql = "SELECT count(livro_id) FROM exemplar WHERE livro_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, livro.getId());
            ResultSet result = ps.executeQuery();
            result = ps.executeQuery();
            if (result.next()){
                if (result.getInt(1) > 0){
                    result.close();
                    ps.close();
                    throw new ValidationException("Livro j\u00e1 est\u00e1 em uso e"
                    		+ " n\u00e3o pode ser exclu\u00eddo!");
                }
            }
            result.close();
            ps.close();
            deleteAutores(livro);
            deleteCategorias(livro);
            sql = "DELETE FROM livro WHERE livro_id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, livro.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private String getSelectSql(String filter){
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM livro l ");
        builder.append("LEFT OUTER JOIN editora ed ON l.editora_id = ed.editora_id ");
        builder.append("LEFT OUTER JOIN assunto a ON l.assunto_id = a.assunto_id ");
        if (filter != null){
            builder.append("WHERE ");
            builder.append(filter);
            builder.append(" ");
        }
        builder.append("ORDER BY livro_id");
        return builder.toString();
    }

    private String getSelectSql(){
        return getSelectSql(null);
    }

    protected static Livro getLivro(ResultSet result) throws SQLException{
        Livro livro = new Livro();
        livro.setId(result.getInt("l.livro_id"));
        livro.setTitulo(result.getString("l.titulo"));
        livro.setIsbn((Long)result.getObject("l.isbn"));
        livro.setCutter(result.getString("l.cutter"));
        livro.setResumo(result.getString("l.resumo"));
        livro.setEdicao(result.getString("l.edicao"));
        livro.setVolume(result.getString("l.volume"));
        livro.setNumPaginas((Integer)result.getObject("l.num_paginas"));
        livro.setAnoPublicacao((Integer)result.getObject("l.ano_publicacao"));
        if (result.getObject("l.editora_id") != null){
            Editora editora = new Editora();
            editora.setId(result.getInt("ed.editora_id"));
            editora.setNome(result.getString("ed.nome"));
            livro.setEditora(editora);
        }
        if (result.getObject("l.assunto_id") != null){
            Assunto assunto = new Assunto();
            assunto.setId(result.getInt("a.assunto_id"));
            assunto.setDescricao(result.getString("a.descricao"));
            assunto.setDBCores(result.getString("a.cores"));
            assunto.setCdu(result.getString("a.cdu"));
            livro.setAssunto(assunto);
        }
        return livro;
    }

    public Livro getLivro(int id){
        try {
            String sql = getSelectSql("livro_id = ?");
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            Livro livro = null;
            if (result.next()){
                livro = getLivro(result);
            }
            result.close();
            ps.close();
            AutorDAO autorDao = new AutorDAO(connection);
            CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
            livro.setAutores(autorDao.getList(livro));
            livro.setCategorias(categoriaDAO.getCategorias(livro));
            ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
            livro.setExemplares(exemplarDAO.getList(livro));
            return livro;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public List<Livro> getList(){
        try {
            String sql = getSelectSql();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            List<Livro> list = new ArrayList<>();
            while (result.next()){
            	Livro livro = getLivro(result);
                CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
                AutorDAO autorDao = new AutorDAO(connection);
                livro.setAutores(autorDao.getList(livro));
                livro.setCategorias(categoriaDAO.getCategorias(livro));
                ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
                livro.setExemplares(exemplarDAO.getList(livro));
            	list.add(livro);
            }
            result.close();
            stmt.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public List<Livro> findList(String fieldName, String text){
        try {
            String sql = getSelectSql(fieldName+" like ?");
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%"+text+"%");
            ResultSet result = ps.executeQuery();
            List<Livro> list = new ArrayList<>();
            while (result.next()){
            	Livro livro = getLivro(result);
            	AutorDAO autorDao = new AutorDAO(connection);
                CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
                livro.setAutores(autorDao.getList(livro));
                livro.setCategorias(categoriaDAO.getCategorias(livro));
                ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
                livro.setExemplares(exemplarDAO.getList(livro));
            	list.add(livro);
            }
            result.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private String getFilterAutores(List<Autor> autores) throws FindException{
    	try {
    		StringBuilder builder = new StringBuilder();
    		builder.append("SELECT * FROM autor_livro WHERE autor_id IN(");
    		for (Autor autor : autores) {
    			if (autores.indexOf(autor) != 0)
    				builder.append(',');
				builder.append(autor.getId());
			}
    		builder.append(")");
    		Statement stmt = connection.createStatement();
    		ResultSet result = stmt.executeQuery(builder.toString());
    		builder = new StringBuilder();
    		builder.append("livro_id IN(");
    		boolean first = true;
    		while (result.next()){
    			if (!first)
    				builder.append(',');
    			builder.append(result.getString("livro_id"));
    			first = false;
    		}
    		builder.append(")");
    		result.close();
    		stmt.close();
    		if (first)
    			throw new FindException();
    		return builder.toString();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
    }

    private String getFilterCategorias(List<Categoria> categorias) throws FindException{
    	try {
    		StringBuilder builder = new StringBuilder();
    		builder.append("SELECT * FROM categoria_livro WHERE categoria_id IN(");
    		for (Categoria categoria : categorias) {
    			if (categorias.indexOf(categoria) != 0)
    				builder.append(',');
				builder.append(categoria.getId());
			}
    		builder.append(")");
    		Statement stmt = connection.createStatement();
    		ResultSet result = stmt.executeQuery(builder.toString());
    		builder = new StringBuilder();
    		builder.append("livro_id IN(");
    		boolean first = true;
    		while (result.next()){
    			if (!first)
    				builder.append(',');
    			builder.append(result.getString("livro_id"));
    			first = false;
    		}
    		builder.append(")");
    		result.close();
    		stmt.close();
    		if (first)
    			throw new FindException();
    		return builder.toString();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
    }

    public List<Livro> findList(String fieldName, String text, List<Autor> autores, List<Categoria> categorias){
        try {
        	StringBuilder builder = new StringBuilder();
        	boolean first = true;
        	if (autores != null && !autores.isEmpty()){
        		builder.append(getFilterAutores(autores));
        		first = false;
        	}
        	if (categorias != null && !categorias.isEmpty()){
        		if (!first)
        			builder.append(" AND ");
        		builder.append(getFilterCategorias(categorias));
        	}
        	if (fieldName != null && text != null){
        		if (!first)
        			builder.append(" AND ");
        		builder.append("UPPER(");
        		builder.append(fieldName);
        		builder.append(") LIKE ?");
        	}
            PreparedStatement ps = connection.prepareStatement(getSelectSql(builder.toString()));
            if (fieldName != null && text != null)
            	ps.setString(1, "%"+text.toUpperCase()+"%");
            ResultSet result = ps.executeQuery();
            List<Livro> list = new ArrayList<>();
            while (result.next()){
            	Livro livro = getLivro(result);
            	AutorDAO autorDao = new AutorDAO(connection);
                CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
                livro.setAutores(autorDao.getList(livro));
                livro.setCategorias(categoriaDAO.getCategorias(livro));
                ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
                livro.setExemplares(exemplarDAO.getList(livro));
            	list.add(livro);
            }
            result.close();
            ps.close();
            return list;
        }catch(FindException f){
        	return new ArrayList<>();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public List<Livro> findList(List<Autor> autores){
    	return findList(null, null, autores, null);
    }

    protected void setAutores(Livro livro){
    	AutorDAO autorDAO = new AutorDAO(connection);
    	List<Autor> list = autorDAO.getList(livro.getId());
    	livro.setAutores(list);
    }

}
