package jp.tacores.mankitu.test.util;

import java.util.LinkedList;
import java.util.List;

import jp.tacores.mankitu.bookmark.Bookmark;
import jp.tacores.mankitu.bookmark.IBookmarkSource;
import jp.tacores.mankitu.util.IContextContainer;

public class SourceStub implements IBookmarkSource {

	public void retrieve(IContextContainer context) {
	}

	public List<Bookmark> getUnReadList() {
		return new LinkedList<Bookmark>();
	}

	public List<Bookmark> getProgressList() {
		return new LinkedList<Bookmark>();
	}

	public List<Bookmark> getCompleteList() {
		return new LinkedList<Bookmark>();
	}

	public void flush(IContextContainer container, List<Bookmark> unReadList,
			List<Bookmark> progressList, List<Bookmark> completeList) {
	}

}
