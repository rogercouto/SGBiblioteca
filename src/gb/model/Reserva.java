package gb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
	
	public static final int DIAS_RESERVA = 7;

	private Integer id;
	private Exemplar exemplar;
	private Usuario usuario;
	private LocalDateTime dataHora;
	private LocalDate dataLimite;
	private LocalDateTime dataHoraRetirada;
	private boolean cancelada = false;

	public Reserva() {
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

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public LocalDate getDataHoraLimite() {
		return dataLimite;
	}

	public void setDataLimite(LocalDate dataLimite) {
		this.dataLimite = dataLimite;
	}

	public LocalDateTime getDataHoraRetirada() {
		return dataHoraRetirada;
	}

	public void setDataHoraRetirada(LocalDateTime dataHoraRetirada) {
		this.dataHoraRetirada = dataHoraRetirada;
	}

	public Exemplar getExemplar() {
		return exemplar;
	}

	public void setExemplar(Exemplar exemplar) {
		this.exemplar = exemplar;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

}
