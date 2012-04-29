/**
 * 
 */
package jp.tacores.mankitu;

import java.util.*;

import android.content.Context;
import android.util.Log;

/**
 * ������f�[�^���Ǘ�����N���X�ł��B
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
	 * �V���O���g���C���X�^���X�̃��t�@�����X���擾���܂��B
	 * @return	���t�@�����X
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
	 * ���������������܂��B
	 * @param context	�R���e�L�X�g
	 */
	public void init(Context context) {
		bf.init(context);
		bf.readFile(unReadList, progressList, completeList);
	}
	
	/**
	 * ������f�[�^���t�@�C���ɔ��f���܂��B
	 */
	public void flush() {
		bf.flush(unReadList, progressList, completeList );
	}
	/**
	 * ���ǃ��X�g��Ԃ��܂��B
	 * @return	���ǃ��X�g
	 */
	public List<Bookmark> getUnreadList() {
		return unReadList;
	}
	/**
	 * �ǂ݂������X�g��Ԃ��܂��B
	 * @return	�ǂ݂������X�g
	 */
	public List<Bookmark> getProgressList() {
		return progressList;
	}
	/**
	 * �������X�g��Ԃ��܂��B
	 * @return	�������X�g
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
	 * ����������X�g����폜���܂��B
	 * @param bm	�폜���邵����
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
	 * ����������X�g�ɒǉ����܂��B
	 * @param bm	�ǉ����邵����
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
	 * ��������X�V���܂��B
	 * @param bm	�X�V���邵����
	 */
	public void updateBookmark(Bookmark bm) {
		removeBookmark(bm);	//���X�g����폜���Ă���ǉ����Ȃ���
		insertBookmark(bm);	//�X�V�����ɕ��ׂ�̂ŁA���X�g�̐擪�ɒǉ�����΂悢
	}
	
	/**
	 * ����̏�Ԃ̃��X�g����S�Ă̂�������폜���܂��B
	 * @param status	���
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
	 * ������f�[�^��SD�J�[�h�ɃG�N�X�|�[�g���܂��B
	 * @return	��������
	 */
	public boolean exportBookmarks() {
		return bf.copyBookmarkFileToSDcard();
	}
	
	/**
	 * ������f�[�^��SD�J�[�h����C���|�[�g���܂��B
	 * @return	��������
	 * @throws Exception
	 */
	public boolean importBookmarks() throws Exception {
		return bf.restoreFileFromSDcard(unReadList, progressList, completeList);
	}
}
