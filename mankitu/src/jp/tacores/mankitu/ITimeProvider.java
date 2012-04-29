package jp.tacores.mankitu;

public interface ITimeProvider {
	int getYear();
	int getMonth();
	int getDay();
	long getCurrentMillis();
}
