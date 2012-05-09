package jp.tacores.mankitu.test.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jp.tacores.mankitu.bookmark.IXmlStream;

public class XmlStreamStub implements IXmlStream {

	public XmlStreamStub(String str) {
		this.str = str;
	}
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(str.getBytes());
	}

	private String str;
}
