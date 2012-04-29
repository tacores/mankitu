/**
 * 
 */
package jp.tacores.mankitu;

import java.util.*;

import android.content.Context;
import android.util.Log;

/**
 * しおりデータを管理するクラスです。
 * @author Tacores
 *
 */
public final class BookmarkManager {
	private List<Bookmark> unReadList;
	private List<Bookmark> progressList;
	private List<Bookmark> completeList;
	
	private BookmarkFile bf;
	
	static private BookmarkManager instance;
	/**
	 * シングルトンインスタンスのリファレンスを取得します。
	 * @return	リファレンス
	 */
	static public BookmarkManager getInstance() {
		if(instance == null) {
			instance = new BookmarkManager();
		}
		return instance;
	}
	private BookmarkManager() {
		unReadList = new LinkedList<Bookmark>();
		progressList = new LinkedList<Bookmark>();
		completeList = new LinkedList<Bookmark>();
		
		bf = new BookmarkFile();
		//bf.readFile(unReadList, progressList, completeList);
	}
	/**
	 * 初期化処理をします。
	 * @param context	コンテキスト
	 */
	public void init(Context context) {
		bf.init(context);
		bf.readFile(unReadList, progressList, completeList);
	}
	
	/**
	 * しおりデータをファイルに反映します。
	 */
	public void flush() {
		bf.flush(unReadList, progressList, completeList );
	}
	/**
	 * 未読リストを返します。
	 * @return	未読リスト
	 */
	public List<Bookmark> getUnreadList() {
		return unReadList;
	}
	/**
	 * 読みかけリストを返します。
	 * @return	読みかけリスト
	 */
	public List<Bookmark> getProgressList() {
		return progressList;
	}
	/**
	 * 完結リストを返します。
	 * @return	完結リスト
	 */
	public List<Bookmark> getCmpleteList() {
		return completeList;
	}
	
	private int getIndexOfList(List<Bookmark> list, Bookmark in_bm) {
		int i = 0;
		for(Bookmark bm: list) {
			String uid = bm.getUid();
			if(uid.equalsIgnoreCase(in_bm.getUid())) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	/**
	 * しおりをリストから削除します。
	 * @param bm	削除するしおり
	 */
	public void removeBookmark(Bookmark bm) {
		Log.d("CCBM", "removeBookmark");
		int index;
		//index = unReadList.indexOf(bm);
		index = getIndexOfList(unReadList, bm);
		Log.d("CCBM", "unread index:" + index);
		if ( 0 <= index ) {
			unReadList.remove(index);
			return;
		}
		//index = progressList.indexOf(bm);
		index = getIndexOfList(progressList, bm);
		Log.d("CCBM", "progress index:" + index);
		if ( 0 <= index ) {
			progressList.remove(index);
			return;
		}
		//index = completeList.indexOf(bm);
		index = getIndexOfList(completeList, bm);
		Log.d("CCBM", "complete index:" + index);
		if ( 0 <= index ) {
			completeList.remove(index);
			return;
		}
	}
	
	/**
	 * しおりをリストに追加します。
	 * @param bm	追加するしおり
	 */
	public void insertBookmark(Bookmark bm) {
		ReadStatus eStatus = bm.getReadStatus();
		List<Bookmark> insertList;
		switch(eStatus) {
		case UNREAD:
			insertList = unReadList;
			break;
		case WAITING:
		case READING:
			insertList = progressList;
			break;
		case COMPLETE:
			insertList = completeList;
			break;
		default:
			//ASSERT
			return;
		}
		insertList.add(0, bm);
	}

	/**
	 * しおりを更新します。
	 * @param bm	更新するしおり
	 */
	public void updateBookmark(Bookmark bm) {
		removeBookmark(bm);	//リストから削除してから追加しなおす
		insertBookmark(bm);	//更新日順に並べるので、リストの先頭に追加すればよい
	}
	
	/**
	 * 特定の状態のリストから全てのしおりを削除します。
	 * @param status	状態
	 */
	public void removeAllOfStatus(ReadStatus status) {
		List<Bookmark> removeList;
		switch(status) {
		case UNREAD:
			removeList = unReadList;
			break;
		case WAITING:
		case READING:
			removeList = progressList;
			break;
		case COMPLETE:
			removeList = completeList;
			break;
		default:
			//ASSERT
			return;
		}
		removeList.clear();
	}
	
	/**
	 * しおりデータをSDカードにエクスポートします。
	 * @return	処理結果
	 */
	public boolean exportBookmarks() {
		return bf.copyBookmarkFileToSDcard();
	}
	
	/**
	 * しおりデータをSDカードからインポートします。
	 * @return	処理結果
	 * @throws Exception
	 */
	public boolean importBookmarks() throws Exception {
		return bf.restoreFileFromSDcard(unReadList, progressList, completeList);
	}
}
