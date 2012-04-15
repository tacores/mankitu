package jp.tacores.mankitu;

public class MyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8470526943596757081L;
	public String errMsg;
	MyException(String msg) { errMsg = msg; }
}
