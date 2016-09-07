package gb.model;

import java.time.LocalDateTime;

public class Emprestimo {

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

	//TODO
	public Double calculaMulta(){
		return null;
	}

}
