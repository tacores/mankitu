package jp.tacores.mankitu.test;

import jp.tacores.mankitu.bookmark.BookmarkManager;
import jp.tacores.mankitu.bookmark.IBookmarkSource;
import jp.tacores.mankitu.test.util.BackupStub;
import jp.tacores.mankitu.test.util.NullContextContainer;
import jp.tacores.mankitu.test.util.SourceStub;
import jp.tacores.mankitu.util.IContextContainer;
import junit.framework.TestCase;
import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

public class BookmarkManagerTest extends TestCase {

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
}
