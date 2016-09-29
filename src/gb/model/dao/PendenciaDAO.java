package gb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gb.model.Pendencia;
import gb.model.Usuario;
import gb.model.data.ConnectionManager;
import gb.util.TemporalUtil;

public class PendenciaDAO {

	private Connection connection;
	
	public PendenciaDAO() {
		super();
		connection = ConnectionManager.getConnection();
	}
	
	public PendenciaDAO(Connection connection) {
		super();
		this.connection = connection;
	}

	public void closeConnection(){
		ConnectionManager.closeConnection(connection);
	}

	public void insert(Pendencia pendencia){
		try {
			String sql = "INSERT INTO pendencia(usuario_id, valor, data_hora_lancamento) VALUES(?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, pendencia.getUsuario().getId());
			ps.setDouble(2, pendencia.getValor());
			ps.setString(3, TemporalUtil.getDbDateTime(pendencia.getDataHoraLancamento()));
			ps.executeUpdate();
			ps.close();
			pendencia.setId(ConnectionManager.getLastInsertId(connection));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public void update(Pendencia pendencia){
		try {
			String sql = "UPDATE pendencia set usuario_id = ?, valor = ?, data_hora_lancamento = ?,"
					+ " data_hora_pagamento = ? WHERE pendencia_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, pendencia.getUsuario().getId());
			ps.setDouble(2, pendencia.getValor());
			ps.setString(3, TemporalUtil.getDbDateTime(pendencia.getDataHoraLancamento()));
			ps.setString(4, pendencia.getDataHoraPagamento() != null ?
						TemporalUtil.getDbDateTime(pendencia.getDataHoraPagamento()): null
					);
			ps.setInt(5, pendencia.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public void delete(Pendencia pendencia){
		try {
			String sql = "DELETE FROM pendencia WHERE pendencia_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, pendencia.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public List<Pendencia> getList(Usuario usuario){
		try {
			String sql = "SELECT * FROM pendencia WHERE data_hora_pagamento IS NULL AND usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ResultSet result = ps.executeQuery();
			List<Pendencia> list = new ArrayList<>();
			while (result.next()){
				Pendencia pendencia = new Pendencia();
				pendencia.setId(result.getInt("pendencia_id"));
				pendencia.setUsuario(usuario);
				pendencia.setValor(result.getDouble("valor"));
				pendencia.setDataHoraLancamento(TemporalUtil.getLocalDateTime(result.getString("data_hora_lancamento")));
				list.add(pendencia);
			}
			result.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
	public int getCount(Usuario usuario){
		try {
			String sql = "SELECT count(pendencia_id) FROM pendencia WHERE data_hora_pagamento IS NULL AND usuario_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			ResultSet result = ps.executeQuery();
			int count = 0;
			if (result.next())
				count = result.getInt(1);
			result.close();
			ps.close();
			return count;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
	
}
