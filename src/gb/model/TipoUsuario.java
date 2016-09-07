package gb.model;

public class TipoUsuario {

	private Integer id;
	private String descricao;
	private Integer numLivrosEmp;
	private Integer numLivrosRes;
	private Integer diasEmprestimo;
	private Integer numRenov;

	public TipoUsuario() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getNumLivrosEmp() {
		return numLivrosEmp;
	}

	public void setNumLivrosEmp(Integer numLivrosEmp) {
		this.numLivrosEmp = numLivrosEmp;
	}

	public Integer getNumLivrosRes() {
		return numLivrosRes;
	}

	public void setNumLivrosRes(Integer numLivrosRes) {
		this.numLivrosRes = numLivrosRes;
	}

	public Integer getDiasEmprestimo() {
		return diasEmprestimo;
	}

	public void setDiasEmprestimo(Integer diasEmprestimo) {
		this.diasEmprestimo = diasEmprestimo;
	}

	public Integer getNumRenov() {
		return numRenov;
	}

	public void setNumRenov(Integer numRenov) {
		this.numRenov = numRenov;
	}

}
