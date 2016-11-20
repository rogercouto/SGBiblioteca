package gb.model;

import java.time.LocalDate;

public class Exemplar {

	private Integer numRegistro;
	private Livro livro;
	private Secao secao;
	private LocalDate dataAquisicao;
	private Origem origem;
	private boolean fixo = false;
	private Situacao situacao;

	public Exemplar() {
		super();
	}

	public Exemplar(int numRegistro) {
		super();
		this.numRegistro = numRegistro;
	}

	public Integer getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(Integer numRegistro) {
		this.numRegistro = numRegistro;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}

	public LocalDate getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(LocalDate dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public boolean isFixo() {
		return fixo;
	}

	public void setFixo(boolean fixo) {
		this.fixo = fixo;
	}

	public boolean isReservado() {
		return (situacao.equals(Situacao.RESERVADO) || situacao.equals(Situacao.EMPRESTADO_RESERVADO));
	}
	public boolean isEmprestado(){
		return (situacao.equals(Situacao.EMPRESTADO) || situacao.equals(Situacao.EMPRESTADO_RESERVADO));
	}

	public void setReservado(){
		if (situacao.equals(Situacao.DISPONIVEL))
			situacao = Situacao.RESERVADO;
		else if (situacao.equals(Situacao.EMPRESTADO))
			situacao = Situacao.EMPRESTADO_RESERVADO;
		else
			throw new RuntimeException("Livro não pode ser reservado!");
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

}
