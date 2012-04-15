package jp.tacores.mankitu;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

/**
 * 漫喫のしおりアプリの全てのアクティビティクラスの基底クラスです。
 * @author Tacores
 *
 */
public class MyActivity extends Activity {
    public static final String CCBM_PREFERENCES;
    protected static LinkedList<Bookmark> unReadList;
	protected static LinkedList<Bookmark> progressList;
	protected static LinkedList<Bookmark> completeList;
    protected static BookmarkManager manager;
    
    static {
        CCBM_PREFERENCES = "CcbmPreferences";
        manager = BookmarkManager.getInstance();
        unReadList = manager.getUnreadList();
    	progressList = manager.getProgressList();
    	completeList = manager.getCmpleteList();
    }

    protected static void logDebug(String message) {
    	Log.d("CCBM", message);
    }
    
    protected void showMessage(String message, String title) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.create();
		ad.setMessage(message);
		ad.setPositiveButton("OK",null);
		ad.setTitle(title);
		ad.show();
    }
}