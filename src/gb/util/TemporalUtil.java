package gb.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TemporalUtil {

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
			return LocalDate.of(Integer.parseInt(amd[0]),
					Integer.parseInt(amd[1]),
					Integer.parseInt(amd[2]));
		}
		return null;
	}

	public static LocalDateTime getLocalDateTime(String dbDateTime){
		if (dbDateTime != null){
			String[] amdhms = dbDateTime.split("[- :.]");
			return LocalDateTime.of(Integer.parseInt(amdhms[0]),
					Integer.parseInt(amdhms[1]),
					Integer.parseInt(amdhms[2]),
					Integer.parseInt(amdhms[3]),
					Integer.parseInt(amdhms[4]),
					Integer.parseInt(amdhms[5]));
		}
		return null;
	}

}
