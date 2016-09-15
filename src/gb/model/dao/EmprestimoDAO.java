package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import gb.model.Emprestimo;
import gb.model.Situacao;
import gb.model.Usuario;
import gb.model.data.ConnectionManager;
import gb.util.TemporalUtil;

public class EmprestimoDAO {

    private Connection connection = null;

    public EmprestimoDAO() {
        super();
        connection = ConnectionManager.getConnection();
    }

    public EmprestimoDAO(Connection connection) {
        super();
        this.connection = connection;
    }

    public void closeConnection(){
        ConnectionManager.closeConnection(connection);
    }

    private void check(Emprestimo emprestimo) throws ValidationException{
        StringBuilder builder = new StringBuilder();
        if (emprestimo.getDataHora() == null)
            builder.append("Data e hora devem ser informadas");
        if (emprestimo.getUsuario() == null){
            if (builder.length() > 0)
                builder.append(";\n");
            builder.append("Usuario deve ser informado");
        }
        if (emprestimo.getExemplar() == null){
            if (builder.length() > 0)
                builder.append(";\n");
            builder.append("Exemplar deve ser selecionado");
        }
        if (builder.length() > 0){
            builder.append('!');
            throw new ValidationException(builder.toString());
        }
    }

    public void insert(Emprestimo emprestimo) throws ValidationException{
        try {
            check(emprestimo);
            String sql = "INSERT INTO emprestimo(data_hora, usuario_id, num_registro, renovacoes,"
            		+ " data_hora_devolucao, multa) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, TemporalUtil.getDbDateTime(emprestimo.getDataHora()));
            ps.setInt(2, emprestimo.getUsuario().getId());
            ps.setInt(3, emprestimo.getExemplar().getNumRegistro());
            ps.setInt(4, emprestimo.getNumRenovacoes());
            ps.setString(5, TemporalUtil.getDbDateTime(emprestimo.getDataHoraDevolucao()));
            ps.setDouble(6, emprestimo.getMultaPaga());
            ps.executeUpdate();
            ps.close();
            emprestimo.setId(ConnectionManager.getLastInsertId(connection));
            sql = "UPDATE exemplar SET situacao = ? WHERE num_registro = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, Situacao.EMPRESTADO.getValue());
			ps.setInt(2, emprestimo.getExemplar().getNumRegistro());
			ps.executeUpdate();
			ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void update(Emprestimo emprestimo) throws ValidationException{
        try {
            if (emprestimo.getId() == null)
                throw new RuntimeException("Id da emprestimo n\u00e3o pode ser null!");
            check(emprestimo);
            String sql = "UPDATE emprestimo SET data_hora = ?, usuario_id = ?, num_registro = ?,"
            		+ " renovacoes = ?, data_hora_devolucao = ?, multa = ?"
            		+ " WHERE emprestimo_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, TemporalUtil.getDbDateTime(emprestimo.getDataHora()));
            ps.setInt(2, emprestimo.getUsuario().getId());
            ps.setInt(3, emprestimo.getExemplar().getNumRegistro());
            ps.setInt(4, emprestimo.getNumRenovacoes());
            ps.setString(5, TemporalUtil.getDbDateTime(emprestimo.getDataHoraDevolucao()));
            ps.setDouble(6, emprestimo.getMultaPaga());
            ps.setInt(7, emprestimo.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void save(Emprestimo emprestimo) throws ValidationException{
        if (emprestimo.getId() == null)
            insert(emprestimo);
        else
            update(emprestimo);
    }

    public void delete(Emprestimo emprestimo){
        try {
            if (emprestimo.getId() == null)
                throw new RuntimeException("Id da emprestimo n\u00e3o pode ser null!");
            String sql = "DELETE FROM emprestimo WHERE emprestimo_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, emprestimo.getId());
            ps.executeUpdate();
            ps.close();
            if (emprestimo.getDataHoraDevolucao() == null){
            	sql = "UPDATE exemplar SET situacao = ? WHERE num_registro = ?";
            	ps = connection.prepareStatement(sql);
            	ps.setInt(1, Situacao.DISPONIVEL.getValue());
            	ps.setInt(2, emprestimo.getExemplar().getNumRegistro());
            	ps.executeUpdate();
            	ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void registraDevolucao(Emprestimo emprestimo){
    	try {
            if (emprestimo.getId() == null)
                throw new RuntimeException("Id da empr\u00e9stimo n\u00e3o pode ser null!");
            if (emprestimo.getDataHoraDevolucao() == null)
            	throw new RuntimeException("Data/Hora da devolu\u00e7\u00e3o n\u00e3o pode ser null!");
            String sql = "UPDATE emprestimo SET data_hora_devolucao = ? WHERE emprestimo_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, TemporalUtil.getDbDateTime(emprestimo.getDataHoraDevolucao()));
            ps.setInt(2, emprestimo.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public String getSelectSql(String filter){
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM emprestimo em");
        builder.append(" INNER JOIN usuario u ON em.usuario_id = u.usuario_id");
        builder.append(" INNER JOIN tipo_usuario tu ON u.tipo_id = tu.tipo_id");
        builder.append(" LEFT OUTER JOIN endereco en ON u.endereco_id = en.endereco_id");
        builder.append(" LEFT OUTER JOIN cidade ci ON en.cidade_id = ci.cidade_id");
        builder.append(" LEFT OUTER JOIN estado es ON ci.sigla_estado = es.sigla_estado");
        builder.append(" INNER JOIN exemplar e ON e.num_registro = e.num_registro");
        builder.append(" INNER JOIN livro l ON e.livro_id = l.livro_id");
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

    private Emprestimo getEmprestimo(ResultSet result) throws SQLException{
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(result.getInt("em.emprestimo_id"));
        emprestimo.setDataHora(TemporalUtil.getLocalDateTime(result.getString("em.data_hora")));
        emprestimo.setDataHoraDevolucao(
        		TemporalUtil.getLocalDateTime(result.getString("em.data_hora_devolucao"))
        		);
        emprestimo.setMultaPaga(result.getDouble("em.multa"));
        emprestimo.setUsuario(UsuarioDAO.getUsuario(result));
        emprestimo.setExemplar(ExemplarDAO.getExemplar(result));
        return emprestimo;
    }

    public Emprestimo get(int id){
        try {
            String sql = getSelectSql("em.emprestimo_id = ?");
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            Emprestimo emprestimo = null;
            if (result.next())
                emprestimo = getEmprestimo(result);
            result.close();
            ps.close();
            return emprestimo;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public List<Emprestimo> getList(){
        try {
            String sql = getSelectSql();
            Statement s = connection.createStatement();
            ResultSet result = s.executeQuery(sql);
            List<Emprestimo> list = new ArrayList<>();
            while (result.next())
                list.add(getEmprestimo(result));
            result.close();
            s.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public List<Emprestimo> getList(Usuario usuario){
        try {
            String sql = getSelectSql("u.usuario_id = ?");
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, usuario.getId());
            ResultSet result = ps.executeQuery();
            List<Emprestimo> list = new ArrayList<>();
            while (result.next())
                list.add(getEmprestimo(result));
            result.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

}
