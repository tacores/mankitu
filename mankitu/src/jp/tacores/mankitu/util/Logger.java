package jp.tacores.mankitu.util;

public class Logger implements ILogger {
	
	public Logger(String tag) {
		if(tag == null) throw new IllegalArgumentException("tag is null.");
		this.tag = tag;
	}

	public void d(String msg) {
		// TODO Auto-generated method stub

	}

	public void e(String msg) {
		// TODO Auto-generated method stub

	}

	public void i(String msg) {
		// TODO Auto-generated method stub

	}

	public void v(String msg) {
		// TODO Auto-generated method stub

	}

	public void w(String msg) {
		// TODO Auto-generated method stub

	}

	private String tag;
}
