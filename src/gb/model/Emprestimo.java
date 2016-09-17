package gb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

	public static final double MULTA_DIA = 1.0;
	
	private Integer id;
	private LocalDateTime dataHora;
	private Usuario usuario;
	private Exemplar exemplar;
	private Integer numRenovacoes = 0;
	private LocalDateTime dataHoraDevolucao;
	private Double multaPaga = 0.0;
	
	public Emprestimo() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public LocalDate getPrevisaoDevolucao(){
		if (usuario == null || usuario.getTipo() == null)
			return null;
		LocalDateTime ldt = dataHora.plus(usuario.getTipo().getDiasEmprestimo(), ChronoUnit.DAYS);
		return LocalDate.from(ldt);
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Exemplar getExemplar() {
		return exemplar;
	}

	public void setExemplar(Exemplar exemplar) {
		this.exemplar = exemplar;
	}

	public Integer getNumRenovacoes() {
		return numRenovacoes;
	}

	public void setNumRenovacoes(Integer numRenovacoes) {
		this.numRenovacoes = numRenovacoes;
	}

	public LocalDateTime getDataHoraDevolucao() {
		return dataHoraDevolucao;
	}

	public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) {
		this.dataHoraDevolucao = dataHoraDevolucao;
	}

	public Double getMultaPaga() {
		return multaPaga;
	}

	public void setMultaPaga(Double multaPaga) {
		this.multaPaga = multaPaga;
	}

}
