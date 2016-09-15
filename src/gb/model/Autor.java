package gb.model;

public class Autor {

	private Integer id;
	private String nome;
	private String sobrenome;
	private String info;
	private String nomeCompleto;
	
	public Autor() {
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
		if (nome != null && sobrenome != null)
			nomeCompleto = sobrenome.toUpperCase()+", "+nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
		if (nome != null && sobrenome != null)
			nomeCompleto = sobrenome.toUpperCase()+", "+nome;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

}
