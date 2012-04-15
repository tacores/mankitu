package jp.tacores.mankitu.test;

import jp.tacores.mankitu.*;
import jp.tacores.mankitu.Bookmark;
import junit.framework.TestCase;

public class BookmarkTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConstructor_OK() {
		createAnonBookmark();
	}
	public void testConstructor_TitleNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(null,ANON_STATUS,ANON_VOLUME,
					ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_VolumeNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,ANON_STATUS,null,
					ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_PageNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
					null,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_StoryNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
					ANON_PAGE,null,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_MemoNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
					ANON_PAGE,ANON_STORY,null,ANON_UPDATEDATE,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_UpdateDateNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
					ANON_PAGE,ANON_STORY,ANON_MEMO,null,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_UidNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
					ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,null);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_SetTitle() {
		String title = "title";
		Bookmark sut = new Bookmark(title,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		assertEquals("title is not correctly set.", title, sut.getTitle());
	}
	public void testConstructor_SetVolume() {
		String volume = "2";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,volume,
				ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		assertEquals("volume is not correctly set.", volume, sut.getVolume());
	}
	public void testConstructor_SetPage() {
		String page = "5";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				page,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		assertEquals("page is not correctly set.", page, sut.getPage());
	}
	public void testConstructor_SetStory() {
		String story = "some story...";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,story,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		assertEquals("story is not correctly set.", story, sut.getStory());
	}
	public void testConstructor_SetMemo() {
		String memo = "memo...";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,memo,ANON_UPDATEDATE,ANON_UID);
		assertEquals("memo is not correctly set.", memo, sut.getMemo());
	}
	public void testConstructor_SetUpdateDate() {
		String date = "20120401";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_MEMO,date,ANON_UID);
		assertEquals("updateDate is not correctly set.", date, sut.getUpdateDate());
	}
	
	private Bookmark createAnonBookmark() {
		return new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
	}
	public void testConstructor_SetUid() {
		String uid = "uid";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_UPDATEDATE,ANON_UPDATEDATE,uid);
		assertEquals("page is not correctly set.", uid, sut.getUid());
	}
	
	private String ANON_TITLE = "TITLE";
	private String ANON_VOLUME = "10";
	private String ANON_PAGE = "100";
	private String ANON_STORY = "STORY";
	private String ANON_MEMO = "MEMO";
	private String ANON_UPDATEDATE = "20110430";
	private String ANON_UID = "UNIQ_ID_0001";
	private ReadStatus ANON_STATUS = ReadStatus.READING;
}
