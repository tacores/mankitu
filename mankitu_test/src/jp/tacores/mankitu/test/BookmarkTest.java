package jp.tacores.mankitu.test;

import jp.tacores.mankitu.bookmark.Bookmark;
import jp.tacores.mankitu.bookmark.ReadStatus;
import jp.tacores.mankitu.util.ITimeProvider;
import jp.tacores.mankitu.util.TimeProvider;
import junit.framework.TestCase;

public class BookmarkTest extends TestCase {
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
	public void testConstructor_StatusNull_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,null,ANON_VOLUME,
					ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testConstructor_StatusIllegal_ThrowsIllegalArgumentException() {
		try {
			new Bookmark(ANON_TITLE,"不正状態",ANON_VOLUME,
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

	public void testConstructor_SetUid() {
		String uid = "uid";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,uid);
		assertEquals("page is not correctly set.", uid, sut.getUid());
	}
	public void testConstructor_GenerateUid() {
		ITimeProvider time = new ITimeProvider() {
			public int getYear() { return 2012; }
			public int getMonth() { return 5; }
			public int getDay() { return 1; }
			public long getCurrentMillis() { return 12345678; }
		};
		String expectedUid = "2012050112345678";
		Bookmark sut = new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_MEMO,time);
		assertEquals("Generated uid is not correct.", expectedUid, sut.getUid());
	}
	public void testUpdate_Title() {
		Bookmark sut = createAnonBookmark();
		String newTitle = "新タイトル";
		sut.update(newTitle, ANON_STATUS, ANON_VOLUME,
				ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		assertEquals("Updated title is not correct.", newTitle, sut.getTitle());
	}
	public void testUpdate_TitleNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(null, ANON_STATUS, ANON_VOLUME,
					ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_Status() {
		Bookmark sut = createAnonBookmark();
		String newStatus = "完結";
		sut.update(ANON_TITLE, newStatus, ANON_VOLUME,
				ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		assertEquals("Updated status is not correct.", newStatus, sut.getReadStatusString());
	}
	public void testUpdate_StatusNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, null, ANON_VOLUME,
					ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_StatusIllegal_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, "不正", ANON_VOLUME,
					ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_Volume() {
		Bookmark sut = createAnonBookmark();
		String newVolume = "22";
		sut.update(ANON_TITLE, ANON_STATUS, newVolume,
				ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		assertEquals("Updated volume is not correct.", newVolume, sut.getVolume());
	}
	public void testUpdate_VolumeNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, ANON_STATUS, null,
					ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_Page() {
		Bookmark sut = createAnonBookmark();
		String newPage = "22";
		sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
				newPage, ANON_STORY, ANON_MEMO, new TimeProvider());
		assertEquals("Updated page is not correct.", newPage, sut.getPage());
	}
	public void testUpdate_PageNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
					null, ANON_STORY, ANON_MEMO, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_Story() {
		Bookmark sut = createAnonBookmark();
		String newStory = "更新後のあらすじ";
		sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
				ANON_PAGE, newStory, ANON_MEMO, new TimeProvider());
		assertEquals("Updated page is not correct.", newStory, sut.getStory());
	}
	public void testUpdate_StoryNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
					ANON_PAGE, null, ANON_MEMO, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_Memo() {
		Bookmark sut = createAnonBookmark();
		String newMemo = "更新後のメモ";
		sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
				ANON_PAGE, ANON_STORY, newMemo, new TimeProvider());
		assertEquals("Updated page is not correct.", newMemo, sut.getMemo());
	}
	public void testUpdate_MemoNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
					ANON_PAGE, ANON_STORY, null, new TimeProvider());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testUpdate_TimeProviderNull_ThrowsIllegalArgumentException() {
		Bookmark sut = createAnonBookmark();
		try {
			sut.update(ANON_TITLE, ANON_STATUS, ANON_VOLUME,
					ANON_PAGE, ANON_STORY, ANON_MEMO, null);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	
	public void testConvertStatusToEnum_UnRead() {
		ReadStatus status = Bookmark.convertReadStatusToEnum("未読");
		assertEquals("Status is not UnRead.", ReadStatus.UNREAD, status);
	}
	public void testConvertStatusToEnum_Reading() {
		ReadStatus status = Bookmark.convertReadStatusToEnum("読みかけ");
		assertEquals("Status is not Reading.", ReadStatus.READING, status);
	}
	public void testConvertStatusToEnum_Waiting() {
		ReadStatus status = Bookmark.convertReadStatusToEnum("新刊待ち");
		assertEquals("Status is not Waiting.", ReadStatus.WAITING, status);
	}
	public void testConvertStatusToEnum_Complete() {
		ReadStatus status = Bookmark.convertReadStatusToEnum("完結");
		assertEquals("Status is not Complete.", ReadStatus.COMPLETE, status);
	}
	public void testConvertStatusToEnum_ThrowIllegalArgumentException() {
		try {
			Bookmark.convertReadStatusToEnum("不正");
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	
	public void testConvertStatusToString_UnRead() {
		String status = Bookmark.convertReadStatusToString(ReadStatus.UNREAD);
		assertEquals("Status is not 未読.", "未読", status);
	}
	public void testConvertStatusToString_Reading() {
		String status = Bookmark.convertReadStatusToString(ReadStatus.READING);
		assertEquals("Status is not 読みかけ.", "読みかけ", status);
	}
	public void testConvertStatusToString_Waiting() {
		String status = Bookmark.convertReadStatusToString(ReadStatus.WAITING);
		assertEquals("Status is not 新刊待ち.", "新刊待ち", status);
	}
	public void testConvertStatusToString_Complete() {
		String status = Bookmark.convertReadStatusToString(ReadStatus.COMPLETE);
		assertEquals("Status is not 完結.", "完結", status);
	}
	
	private Bookmark createAnonBookmark() {
		return new Bookmark(ANON_TITLE,ANON_STATUS,ANON_VOLUME,
				ANON_PAGE,ANON_STORY,ANON_MEMO,ANON_UPDATEDATE,ANON_UID);
	}
	
	private String ANON_TITLE = "TITLE";
	private String ANON_VOLUME = "10";
	private String ANON_PAGE = "100";
	private String ANON_STORY = "STORY";
	private String ANON_MEMO = "MEMO";
	private String ANON_UPDATEDATE = "20110430";
	private String ANON_UID = "UNIQ_ID_0001";
	private String ANON_STATUS = "未読";
}
