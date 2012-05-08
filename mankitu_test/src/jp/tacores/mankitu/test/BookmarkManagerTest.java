package jp.tacores.mankitu.test;

import java.util.LinkedList;
import java.util.List;

import jp.tacores.mankitu.bookmark.*;
import jp.tacores.mankitu.test.util.BackupStub;
import jp.tacores.mankitu.test.util.NullContextContainer;
import jp.tacores.mankitu.test.util.SourceStub;
import jp.tacores.mankitu.test.util.TimeProviderStub;
import junit.framework.TestCase;

public class BookmarkManagerTest extends TestCase {
	private final String ANON_TITLE = "title"; 
	private final String ANON_VOLUME = "1";
	private final String ANON_PAGE = "100"; 
	private final String ANON_STORY = "story"; 
	private final String ANON_MEMO = "memo";
	private final String ANON_UPDATEDATE = "20120507";
	private final String ANON_UID = "1234567890"; 

	protected void setUp() throws Exception {
		super.setUp();
		BookmarkManager.deleteInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetInstance_OK() {
		BookmarkManager sut = BookmarkManager.getInstance(new NullContextContainer(),
				new SourceStub(), new BackupStub());
		assertNotNull(sut);
	}
	public void testGetInstance_ReturnSameInstances() {
		BookmarkManager sut1 = BookmarkManager.getInstance(new NullContextContainer(),
				new SourceStub(), new BackupStub());
		BookmarkManager sut2 = BookmarkManager.getInstance(new NullContextContainer(),
				new SourceStub(), new BackupStub());
		assertEquals("Return different instances.", sut1, sut2);
	}

	public void testGetInstance_ContextNull_ThrowsIllegalArgument() {
		try {
			BookmarkManager.getInstance(null, new SourceStub(), new BackupStub());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testGetInstance_SourceNull_ThrowsIllegalArgument() {
		try {
			BookmarkManager.getInstance(new NullContextContainer(), null, new BackupStub());
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testGetInstance_BackupNull_ThrowsIllegalArgument() {
		try {
			BookmarkManager.getInstance(new NullContextContainer(), new SourceStub(), null);
		} catch(IllegalArgumentException e) {
			return;
		}
		fail("not throws IllegalArgumentException.");
	}
	public void testGetInstance_CallRetrieve() {
		//TODO mock
	}
	
	public void testGetUnreadList() {
		final List<Bookmark> list = new LinkedList<Bookmark>();
		IBookmarkSource source = new SourceStub() {
			public List<Bookmark> getUnReadList() { return list; }
			public List<Bookmark> getProgressList() { return null; }
			public List<Bookmark> getCompleteList() { return null; }
		};
		BookmarkManager sut = BookmarkManager.getInstance(new NullContextContainer(),
				source, new BackupStub());
		
		assertEquals("Returned UnReadList is wrong.", list, sut.getUnreadList());
	}
	public void testGetProgressList() {
		final List<Bookmark> list = new LinkedList<Bookmark>();
		IBookmarkSource source = new SourceStub() {
			public List<Bookmark> getUnReadList() { return null; }
			public List<Bookmark> getProgressList() { return list; }
			public List<Bookmark> getCompleteList() { return null; }
		};
		BookmarkManager sut = BookmarkManager.getInstance(new NullContextContainer(),
				source, new BackupStub());
		
		assertEquals("Returned ProgressList is wrong.", list, sut.getProgressList());
	}
	public void testGetCompleteList() {
		final List<Bookmark> list = new LinkedList<Bookmark>();
		IBookmarkSource source = new SourceStub() {
			public List<Bookmark> getUnReadList() { return null; }
			public List<Bookmark> getProgressList() { return null; }
			public List<Bookmark> getCompleteList() { return list; }
		};
		BookmarkManager sut = BookmarkManager.getInstance(new NullContextContainer(),
				source, new BackupStub());
		
		assertEquals("Returned CompleteList is wrong.", list, sut.getCompleteList());
	}
	
	public void testInsert_UnRead() {
		BookmarkManager sut = createAnonBookmarkManager();
		sut.insertBookmark(createAnonUnReadBookmark());		
		assertEquals("UnRead Bookmark is not inserted.", 1, sut.getUnreadList().size());
	}
	public void testInsert_Reading() {
		BookmarkManager sut = createAnonBookmarkManager();
		sut.insertBookmark(createAnonReadingBookmark());		
		assertEquals("Progress Bookmark is not inserted.", 1, sut.getProgressList().size());
	}
	public void testInsert_Waiting() {
		BookmarkManager sut = createAnonBookmarkManager();
		sut.insertBookmark(createAnonWaitingBookmark());		
		assertEquals("Progress Bookmark is not inserted.", 1, sut.getProgressList().size());
	}
	public void testInsert_Complete() {
		BookmarkManager sut = createAnonBookmarkManager();
		sut.insertBookmark(createAnonCompleteBookmark());		
		assertEquals("Complete Bookmark is not inserted.", 1, sut.getCompleteList().size());
	}
	public void testInsert_ToHead() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonUnReadBookmark();
		
		sut.insertBookmark(createAnonUnReadBookmark());
		sut.insertBookmark(bm);
		Bookmark head = sut.getUnreadList().get(0);
		assertEquals("Bookmark is not inserted to list's head.", head, bm);
	}
	
	public void testUpdate_UnReadToWaiting_UnReadListRemoved() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonUnReadBookmark();

		sut.insertBookmark(bm);
		bm.update(ANON_TITLE, "êVäßë“Çø", ANON_VOLUME, ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProviderStub());
		sut.updateBookmark(bm);
		
		assertEquals("not removed from unread list.", 0, sut.getUnreadList().size());
	}
	public void testUpdate_UnReadToWaiting_ProgressListInserted() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonUnReadBookmark();

		sut.insertBookmark(bm);
		bm.update(ANON_TITLE, "êVäßë“Çø", ANON_VOLUME, ANON_PAGE, ANON_STORY, ANON_MEMO, new TimeProviderStub());
		sut.updateBookmark(bm);
		
		assertEquals("not inserted to progress list.", 1, sut.getProgressList().size());
	}
	
	public void testRemove_FromUnRead() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonUnReadBookmark();
		sut.insertBookmark(bm);
		sut.removeBookmark(bm);
		
		assertEquals("not removed from unread list.", 0, sut.getUnreadList().size());
	}
	public void testRemove_FromProgress() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonWaitingBookmark();
		sut.insertBookmark(bm);
		sut.removeBookmark(bm);
		
		assertEquals("not removed from progress list.", 0, sut.getProgressList().size());
	}
	public void testRemove_FromComplete() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonCompleteBookmark();
		sut.insertBookmark(bm);
		sut.removeBookmark(bm);
		
		assertEquals("not removed from complete list.", 0, sut.getCompleteList().size());
	}
	
	public void testRemoveAllStatus_UnRead() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonUnReadBookmark();
		sut.insertBookmark(bm);
		sut.removeAllOfStatus(ReadStatus.UNREAD);
		
		assertEquals("not removed from unread list.", 0, sut.getUnreadList().size());
	}
	public void testRemoveAllStatus_Waiting() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonWaitingBookmark();
		sut.insertBookmark(bm);
		sut.removeAllOfStatus(ReadStatus.WAITING);
		
		assertEquals("not removed from progress list.", 0, sut.getProgressList().size());
	}
	public void testRemoveAllStatus_Reading() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonReadingBookmark();
		sut.insertBookmark(bm);
		sut.removeAllOfStatus(ReadStatus.READING);
		
		assertEquals("not removed from reading list.", 0, sut.getProgressList().size());
	}
	public void testRemoveAllStatus_Complete() {
		BookmarkManager sut = createAnonBookmarkManager();
		Bookmark bm = createAnonCompleteBookmark();
		sut.insertBookmark(bm);
		sut.removeAllOfStatus(ReadStatus.COMPLETE);
		
		assertEquals("not removed from complete list.", 0, sut.getCompleteList().size());
	}
	
	private BookmarkManager createAnonBookmarkManager() {
		return BookmarkManager.getInstance(new NullContextContainer(),
				new SourceStub(), new BackupStub());
	}
	private Bookmark createAnonUnReadBookmark() {
		return new Bookmark(ANON_TITLE, "ñ¢ì«", ANON_VOLUME, ANON_PAGE, ANON_STORY, ANON_MEMO, ANON_UPDATEDATE, ANON_UID);
	}
	private Bookmark createAnonReadingBookmark() {
		return new Bookmark(ANON_TITLE, "ì«Ç›Ç©ÇØ", ANON_VOLUME, ANON_PAGE, ANON_STORY, ANON_MEMO, ANON_UPDATEDATE, ANON_UID);
	}
	private Bookmark createAnonWaitingBookmark() {
		return new Bookmark(ANON_TITLE, "êVäßë“Çø", ANON_VOLUME, ANON_PAGE, ANON_STORY, ANON_MEMO, ANON_UPDATEDATE, ANON_UID);
	}
	private Bookmark createAnonCompleteBookmark() {
		return new Bookmark(ANON_TITLE, "äÆåã", ANON_VOLUME, ANON_PAGE, ANON_STORY, ANON_MEMO, ANON_UPDATEDATE, ANON_UID);
	}
}
