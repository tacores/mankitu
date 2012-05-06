package jp.tacores.mankitu.util;

public interface ITimeProvider {
	int getYear();
	int getMonth();
	int getDay();
	long getCurrentMillis();
}
