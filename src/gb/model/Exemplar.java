package gb.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Exemplar {

	private Integer numRegistro;
	private Livro livro;
	private Secao secao;
	private LocalDate dataAquisicao;
	private Origem origem;
	private Boolean fixo;
	private Situacao situacao;
	
	//Transient
	private String descrSituacao;
	private String descrOrigem;
	private String descrSecao;
	private String dataAquisForm;
	
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
		if (secao != null)
			descrSecao = secao.getDescricao();
	}

	public LocalDate getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(LocalDate dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
		if (dataAquisicao != null){
			DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			dataAquisForm = formatador.format(dataAquisicao);
		}
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
		if (origem != null)
			descrOrigem = origem.getDescricao();
	}

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
		if (situacao != null)
			descrSituacao = situacao.name();
	}

	public String getDescrSituacao() {
		return descrSituacao;
	}

	public String getDescrOrigem() {
		return descrOrigem;
	}

	public String getDataAquisForm() {
		return dataAquisForm;
	}

	public String getDescrSecao() {
		return descrSecao;
	}


}
