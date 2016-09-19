package gb.model;

public enum Situacao {

	DISPONIVEL (1), EMPRESTADO (2), INDISPONIVEL (4);

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
				return EMPRESTADO;
			case 4:
				return INDISPONIVEL;
		}
		return null;
	}

}
