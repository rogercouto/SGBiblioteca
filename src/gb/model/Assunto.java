package gb.model;

import java.util.ArrayList;
import java.util.List;

public class Assunto {

	private Integer id;
	private String descricao;
	private List<String> cores = new ArrayList<>();
	private String cdu = null;

	public Assunto() {
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

	public List<String> getCores() {
		return cores;
	}

	public String getDBCores() {
		if (cores.isEmpty())
			return null;
		StringBuilder builder = new StringBuilder();
		for (String string : cores) {
			builder.append(string);
			builder.append(';');
		}
		return builder.toString();
	}
	
	public void setCores(List<String> cores) {
		this.cores = cores;
	}

	public void setDBCores(String cores){
		if (cores == null || cores.isEmpty())
			return;
		String[] ca = cores.split(";");
		this.cores.clear();
		for (String string : ca) {
			this.cores.add(string);
		}
	}
	
	public void addCor(String cor){
		cores.add(cor);
	}

	public void setCor(int index, String cor){
		cores.set(index, cor);
	}

	public void removeCor(int index){
		cores.remove(index);
	}


	public String getCdu() {
		return cdu;
	}

	public void setCdu(String cdu) {
		this.cdu = cdu;
	}

}
