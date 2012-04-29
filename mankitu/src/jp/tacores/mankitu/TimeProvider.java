package jp.tacores.mankitu;

import java.util.Calendar;

public class TimeProvider implements ITimeProvider {

	public int getYear() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.DAY_OF_MONTH);
	}

	public long getCurrentMillis() {
		return System.currentTimeMillis();
	}
	
}
