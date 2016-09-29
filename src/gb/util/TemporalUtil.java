package gb.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TemporalUtil {

	private static final DateTimeFormatter FORMATADOR_D = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATADOR_DH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public static String getDbDate(LocalDate localDate){
		if (localDate == null)
			return null;
		StringBuilder builder = new StringBuilder();
		builder.append(localDate.getYear());
		builder.append('-');
		builder.append(localDate.getMonthValue());
		builder.append('-');
		builder.append(localDate.getDayOfMonth());
		return builder.toString();
	}

	public static String getDbDateTime(LocalDateTime localDateTime){
		if (localDateTime == null)
			return null;
		StringBuilder builder = new StringBuilder();
		builder.append(localDateTime.getYear());
		builder.append('-');
		builder.append(localDateTime.getMonthValue());
		builder.append('-');
		builder.append(localDateTime.getDayOfMonth());
		builder.append(' ');
		builder.append(localDateTime.getHour());
		builder.append(':');
		builder.append(localDateTime.getMinute());
		builder.append(':');
		builder.append(localDateTime.getSecond());
		return builder.toString();
	}

	public static LocalDate getLocalDate(String dbDate){
		if (dbDate != null){
			String[] amd = dbDate.split("-");
			if (amd.length < 3)
				return null;
			return LocalDate.of(NumericUtil.toInteger(amd[0]),
					NumericUtil.toInteger(amd[1]),
					NumericUtil.toInteger(amd[2]));
		}
		return null;
	}

	public static LocalDateTime getLocalDateTime(String dbDateTime){
		if (dbDateTime != null){
			String[] amdhms = dbDateTime.split("[- :.]");
			if (amdhms.length < 6)
				return null;
			return LocalDateTime.of(NumericUtil.toInteger(amdhms[0]),
					NumericUtil.toInteger(amdhms[1]),
					NumericUtil.toInteger(amdhms[2]),
					NumericUtil.toInteger(amdhms[3]),
					NumericUtil.toInteger(amdhms[4]),
					NumericUtil.toInteger(amdhms[5]));
		}
		return null;
	}

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
