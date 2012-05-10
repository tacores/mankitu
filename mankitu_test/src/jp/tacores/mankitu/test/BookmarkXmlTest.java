package jp.tacores.mankitu.test;

import jp.tacores.mankitu.bookmark.BookmarkXml;
import jp.tacores.mankitu.test.util.*;
import junit.framework.TestCase;

public class BookmarkXmlTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConstructor_OK() {
		new BookmarkXml(new XmlStreamStub(ANON_XML));
	}
	public void testConstructor_NullStream_ThrowsIllegalArgumentException() {
		try {
			new BookmarkXml(null);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("IllegalArgumentException was not thrown.");
	}
	
	public void testGetUnReadList_BeforeRetrieve_ThrowsIllegalStateException() {
		BookmarkXml sut = createAnonBookmarkXml();
		try {
			sut.getUnReadList();
		} catch(IllegalStateException e) {
			return;
		}
		fail("IllegalStateException was not thrown.");
	}
	public void testGetProgressList_BeforeRetrieve_ThrowsIllegalStateException() {
		BookmarkXml sut = createAnonBookmarkXml();
		try {
			sut.getProgressList();
		} catch(IllegalStateException e) {
			return;
		}
		fail("IllegalStateException was not thrown.");
	}
	public void testGetCompleteList_BeforeRetrieve_ThrowsIllegalStateException() {
		BookmarkXml sut = createAnonBookmarkXml();
		try {
			sut.getCompleteList();
		} catch(IllegalStateException e) {
			return;
		}
		fail("IllegalStateException was not thrown.");
	}
	public void testRetrieve_Empty_ReturnEmptyLists() {
		final String EMPTY_STR = "<bookmarks></bookmarks>";
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(EMPTY_STR));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("UnRead list was not empty.", 0, sut.getUnReadList().size());
		assertEquals("Progress list was not empty.", 0, sut.getProgressList().size());
		assertEquals("Complete list was not empty.", 0, sut.getCompleteList().size());
	}
	public void testRetrieve_UnRead_OneUnRead() {
		String xml = HEADER + createUnReadBookmarkString() + FOOTER;
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(xml));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("UnRead list did't have one element.", 1, sut.getUnReadList().size());
	}
	public void testRetrieve_Waiting_OneProgress() {
		String xml = HEADER + createWaitingBookmarkString() + FOOTER;
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(xml));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("Progress list did't have one element.", 1, sut.getProgressList().size());
	}
	public void testRetrieve_Reading_OneProgress() {
		String xml = HEADER + createReadingBookmarkString() + FOOTER;
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(xml));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("Progress list did't have one element.", 1, sut.getProgressList().size());
	}
	public void testRetrieve_Complete_OneComplete() {
		String xml = HEADER + createCompleteBookmarkString() + FOOTER;
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(xml));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("Complete list did't have one element.", 1, sut.getCompleteList().size());
	}
	public void testRetrieve_ContainAllStatus() {
		String xml = HEADER +
				createUnReadBookmarkString() +
				createWaitingBookmarkString() +
				createReadingBookmarkString() +
				createCompleteBookmarkString() +
				FOOTER;
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(xml));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("UnRead list was not 1.", 1, sut.getUnReadList().size());
		assertEquals("Progress list was not 2.", 2, sut.getProgressList().size());
		assertEquals("Complete list was not 1.", 1, sut.getCompleteList().size());
	}
	
	private BookmarkXml createAnonBookmarkXml() {
		return new BookmarkXml(new XmlStreamStub(ANON_XML));
	}
	private String createUnReadBookmarkString() {
		return createAnonBookmarkElementWithoutEndTag() + "<status>ñ¢ì«</status>" + "</bookmark>";
	}
	private String createWaitingBookmarkString() {
		return createAnonBookmarkElementWithoutEndTag() + "<status>êVäßë“Çø</status>" + "</bookmark>";
	}
	private String createReadingBookmarkString() {
		return createAnonBookmarkElementWithoutEndTag() + "<status>ì«Ç›Ç©ÇØ</status>" + "</bookmark>";
	}
	private String createCompleteBookmarkString() {
		return createAnonBookmarkElementWithoutEndTag() + "<status>äÆåã</status>" + "</bookmark>";
	}
	private String createAnonBookmarkElementWithoutEndTag() {
		String str = "<bookmark>";
		str += "<title>title</title>";
		str += "<volume>4</volume><page>99</page>";
		str += "<story>story</story><memo>memo</memo>";
		str += "<date>20120509</date><uid>1234567890</uid>";
		return str;
	}
	
	final private String HEADER = "<bookmarks>";
	final private String FOOTER = "</bookmarks>";
	final private String ANON_XML = "<bookmarks></bookmarks>";
}
