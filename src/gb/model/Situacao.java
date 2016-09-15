package gb.model;

public enum Situacao {

	DISPONIVEL (1), RESERVADO (2), EMPRESTADO (3), INDISPONIVEL (4);

	private final Integer value;
	Situacao(Integer value){
		this.value = value;
	}
	public Integer getValue(){
		return value;
	}

	public static Situacao getSituacao(int value){
		switch (value) {
			case 1:
				return DISPONIVEL;
			case 2:
				return RESERVADO;
			case 3:
				return EMPRESTADO;
			case 4:
				return INDISPONIVEL;
		}
		return null;
	}

}
