package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import gb.model.Reserva;
import gb.model.Usuario;
import gb.model.data.ConnectionManager;
import gb.util.TemporalUtil;

public class ReservaDAO {

	private Connection connection = null;

	public ReservaDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}

	public ReservaDAO(Connection connection) {
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	private void check(Reserva reserva) throws ValidationException{
		StringBuilder builder = new StringBuilder();
		if (reserva.getDataHora() == null)
			builder.append("Data e hora devem ser informadas");
		if (reserva.getDataRetirada() == null){
			if (builder.length() > 0)
				builder.append(";\n");
			builder.append("Data prevista da retirada deve ser informada");
		}
		if (reserva.getUsuario() == null){
			if (builder.length() > 0)
				builder.append(";\n");
			builder.append("Usuario deve ser informado");
		}
		if (reserva.getExemplar() == null){
			if (builder.length() > 0)
				builder.append(";\n");
			builder.append("Exemplar deve ser selecionado");
		}
		if (builder.length() > 0){
			builder.append('!');
			throw new ValidationException(builder.toString());
		}
	}

	public void insert(Reserva reserva) throws ValidationException{
		try {
			check(reserva);
			String sql = "INSERT INTO reserva(data_hora, data_retirada, usuario_id, num_registro) VALUES(?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, TemporalUtil.getDbDateTime(reserva.getDataHora()));
			ps.setString(2, TemporalUtil.getDbDate(reserva.getDataRetirada()));
			ps.setInt(3, reserva.getUsuario().getId());
			ps.setInt(4, reserva.getExemplar().getNumRegistro());
			ps.executeUpdate();
			ps.close();
			reserva.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void update(Reserva reserva) throws ValidationException{
		try {
			if (reserva.getId() == null)
				throw new RuntimeException("Id da reserva n\u00e3o pode ser null!");
			check(reserva);
			String sql = "UPDATE reserva SET data_hora = ?, data_retirada = ?, usuario_id = ?, num_registro= ?, cancelada = ? WHERE reserva_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, TemporalUtil.getDbDateTime(reserva.getDataHora()));
			ps.setString(2, TemporalUtil.getDbDate(reserva.getDataRetirada()));
			ps.setInt(3, reserva.getUsuario().getId());
			ps.setInt(4, reserva.getExemplar().getNumRegistro());
			ps.setBoolean(5, reserva.isCancelada());
			ps.setInt(6, reserva.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void save(Reserva reserva) throws ValidationException{
		if (reserva.getId() == null)
			insert(reserva);
		else
			update(reserva);
	}

	public void cancela(Reserva reserva){
		try {
			reserva.setCancelada(true);
			if (reserva.getId() == null)
				throw new RuntimeException("Id da reserva n\u00e3o pode ser null!");
			String sql = "UPDATE reserva SET cancelada = ? WHERE reserva_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setBoolean(1, reserva.isCancelada());
			ps.setInt(2, reserva.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public void delete(Reserva reserva){
		try {
			if (reserva.getId() == null)
				throw new RuntimeException("Id da reserva n\u00e3o pode ser null!");
			String sql = "DELETE FROM reserva WHERE reserva_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, reserva.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public String getSelectSql(String filter){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM reserva r");
		builder.append(" INNER JOIN usuario u ON r.usuario_id = u.usuario_id");
		builder.append(" INNER JOIN tipo_usuario tu ON u.tipo_id = tu.tipo_id");
		builder.append(" LEFT OUTER JOIN endereco en ON u.endereco_id = en.endereco_id");
		builder.append(" LEFT OUTER JOIN cidade ci ON en.cidade_id = ci.cidade_id");
		builder.append(" LEFT OUTER JOIN estado es ON ci.sigla_estado = es.sigla_estado");
		builder.append(" INNER JOIN exemplar e ON r.num_registro = e.num_registro");
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

	private Reserva getReserva(ResultSet result) throws SQLException{
		Reserva reserva = new Reserva();
		reserva.setId(result.getInt("r.reserva_id"));
		reserva.setDataHora(TemporalUtil.getLocalDateTime(result.getString("r.data_hora")));
		reserva.setDataRetirada(TemporalUtil.getLocalDate(result.getString("r.data_retirada")));
		reserva.setCancelada(result.getBoolean("r.cancelada"));
		reserva.setUsuario(UsuarioDAO.getUsuario(result));
		reserva.setExemplar(ExemplarDAO.getExemplar(result));
		return reserva;
	}

	public Reserva get(int id){
		try {
			String sql = getSelectSql("reserva_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			Reserva reserva = null;
			if (result.next())
				reserva = getReserva(result);
			result.close();
			ps.close();
			return reserva;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Reserva> getList(){
		try {
			String sql = getSelectSql();
			Statement s = connection.createStatement();
			ResultSet result = s.executeQuery(sql);
			List<Reserva> list = new ArrayList<>();
			while (result.next())
				list.add(getReserva(result));
			result.close();
			s.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public List<Reserva> getList(Usuario usuario){
		try {
			String sql = getSelectSql("u.usuario_id = ?");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ResultSet result = ps.executeQuery();
			List<Reserva> list = new ArrayList<>();
			while (result.next())
				list.add(getReserva(result));
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
}
