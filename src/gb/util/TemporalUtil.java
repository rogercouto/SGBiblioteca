package gb.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TemporalUtil {

	private static final DateTimeFormatter FORMATADOR_D = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATADOR_DH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public static String formatDate(LocalDate localDate){
		return FORMATADOR_D.format(localDate);
	}
	
	public static String formatDateTime(LocalDateTime localDateTime){
		return FORMATADOR_DH.format(localDateTime);
	}
	
	public static LocalDateTime parseDateTime(String text){
		if (text.trim().length() != 16)
			throw new RuntimeException("Erro tentando converter "+text+" para LocalDateTime");
		TemporalAccessor ta = FORMATADOR_DH.parse(text);
		return LocalDateTime.from(ta);
	}
	
	public static LocalDate parseDate(String text){
		if (text.trim().length() != 10)
			throw new RuntimeException("Erro tentando converter "+text+" para LocalDate");
		TemporalAccessor ta = FORMATADOR_D.parse(text);
		return LocalDate.from(ta);
	}
	
}
