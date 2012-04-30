package jp.tacores.mankitu.bookmark;

import java.util.List;

import jp.tacores.mankitu.util.IContextContainer;

public interface IBackupAccess {
	void importBackup(IContextContainer context);
	List<Bookmark> getUnReadList();
	List<Bookmark> getProgressList();
	List<Bookmark> getCompleteList();
	
	void exportBackup(IContextContainer context,
			List<Bookmark> unReadList, List<Bookmark> progressList,
			List<Bookmark> completeList);
}
