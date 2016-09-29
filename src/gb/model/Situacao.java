package gb.model;

public enum Situacao {

	DISPONIVEL (1), EMPRESTADO (2), RESERVADO (3), EMPRESTADO_RESERVADO (4),INDISPONIVEL (5);

	private final Integer value;
	
	Situacao(Integer value){
		this.value = value;
	}
	
	public Integer getValue(){
		return value;
	}

	public static Situacao getSituacao(int value){
		for (Situacao situacao : Situacao.values()) {
			if (value == situacao.getValue().intValue())
				return situacao;
		}
		return null;
	}

}
