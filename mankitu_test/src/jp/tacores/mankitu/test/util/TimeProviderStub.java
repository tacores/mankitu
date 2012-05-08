package jp.tacores.mankitu.test.util;

import jp.tacores.mankitu.util.*;

public class TimeProviderStub implements ITimeProvider {

	public int getYear() {
		return 2012;
	}

	public int getMonth() {
		return 5;
	}

	public int getDay() {
		return 7;
	}

	public long getCurrentMillis() {
		return 1000000;
	}
	
}
