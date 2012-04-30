package jp.tacores.mankitu.bookmark;

import java.util.List;

import jp.tacores.mankitu.util.IContextContainer;

public interface IBookmarkSource {
	void retrieve(IContextContainer context);
	List<Bookmark> getUnReadList();
	List<Bookmark> getProgressList();
	List<Bookmark> getCompleteList();
	
	void flush(IContextContainer container,
			List<Bookmark> unReadList, List<Bookmark> progressList,
			List<Bookmark> completeList);
}
