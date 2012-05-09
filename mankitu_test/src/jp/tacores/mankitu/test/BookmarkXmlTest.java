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
	public void testRetrieve_OneUnRead() {
		BookmarkXml sut = new BookmarkXml(new XmlStreamStub(HEADER + UNREAD_BOOKMARK + FOOTER));
		
		sut.retrieve(new NullContextContainer());
		
		assertEquals("UnRead list did't have one element.", 1, sut.getUnReadList().size());
	}
	
	private BookmarkXml createAnonBookmarkXml() {
		return new BookmarkXml(new XmlStreamStub(ANON_XML));
	}
	
	final private String HEADER = "<bookmarks>";
	final private String FOOTER = "</bookmarks>";
	final private String ANON_XML = "";
	final private String UNREAD_BOOKMARK = "<bookmark><title>title</title><status>–¢“Ç</status>" +
			"<volume>4</volume><page>99</page><story>story</story><memo>memo</memo>" +
			"<date>20120509</date><uid>1234567890</uid></bookmark>";
}
