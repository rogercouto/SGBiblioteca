package gb.util;

public class NumericUtil {

	public static boolean isInteger(String string){
		char[] ca = string.toCharArray();
		for (char c : ca) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
	
	public static Integer toInteger(String string){
		if (!isInteger(string))
			throw new RuntimeException("Valor não inteiro!");
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			Long l = Long.parseLong(string);
			if (l.longValue() > Integer.MAX_VALUE)
				throw new RuntimeException("Valor muito grande para o tipo inteiro!");
		}
		return null;
	}
	
	public static Long toLong(String string){
		if (!isInteger(string))
			throw new RuntimeException("Valor não inteiro!");
		return Long.parseLong(string);
	}

}
