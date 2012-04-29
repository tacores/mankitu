package jp.tacores.mankitu;

import java.util.List;

public interface IBookmarkSource {
	List<Bookmark> getUnReadList(IContextContainer context);
	List<Bookmark> getProgressList(IContextContainer context);
	List<Bookmark> getCompleteList(IContextContainer context);
	
	void flush(IContextContainer container,
			List<Bookmark> unReadList, List<Bookmark> progressList,
			List<Bookmark> completeList);
}
