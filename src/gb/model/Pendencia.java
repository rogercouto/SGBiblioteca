package gb.model;

import java.time.LocalDateTime;

public class Pendencia {

	private Integer id;
	private Usuario usuario;
	private Double valor;
	private LocalDateTime dataHoraLancamento;
	private LocalDateTime dataHoraPagamento = null;
	
	public Pendencia() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public LocalDateTime getDataHoraLancamento() {
		return dataHoraLancamento;
	}
	public void setDataHoraLancamento(LocalDateTime dataHoraLancamento) {
		this.dataHoraLancamento = dataHoraLancamento;
	}
	public LocalDateTime getDataHoraPagamento() {
		return dataHoraPagamento;
	}
	public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
		this.dataHoraPagamento = dataHoraPagamento;
	}

}
