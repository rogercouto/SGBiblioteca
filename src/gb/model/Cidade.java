package gb.model;

public class Cidade {

	private Integer id;
	private String nome;
	private Estado estado;
	
	//Transient
	private String siglaEstado;

	public Cidade() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
		if (estado != null)
			siglaEstado = estado.getSigla();
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

}
