package jp.tacores.mankitu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import jp.tacores.mankitu.bookmark.BackupAccess;
import jp.tacores.mankitu.bookmark.Bookmark;
import jp.tacores.mankitu.bookmark.BookmarkXml;
import jp.tacores.mankitu.bookmark.BookmarkManager;
import jp.tacores.mankitu.bookmark.ReadStatus;
import jp.tacores.mankitu.bookmark.XmlStream;
import jp.tacores.mankitu.util.ContextContainer;

import android.app.AlertDialog;
//import android.content.DialogInterface;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.ArrayAdapter;

/**
 * どのタブを表示しているかを意味するEnumです。
 * @author Tacores
 *
 */
enum ViewStatus { UNREAD, PROGRESS, COMPLETE };

/**
 * しおりの一覧を表示するアクティビティクラスです。
 * @author Tacores
 *
 */
public class BookmarkDisplayActivity extends MyActivity {
	private static final int SHOW_EDIT_FORM = 0;  
	ViewStatus viewStatus = ViewStatus.PROGRESS;
	private BookmarkManager manager;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_EDIT_FORM) {  
            if (resultCode == RESULT_OK) {  
            	refleshAllView();
            }  
        }  
		//super.onActivityResult(requestCode, resultCode, data);
	}
	private final int MENU_CREATE = 1, MENU_DELETE_ALL = 2, MENU_EXPORT = 3, MENU_IMPORT = 4;
	private final int GROUP_DEFAULT = 0;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logDebug("enter setContentView comicList");
        setContentView(R.layout.comiclist);
        
        manager = BookmarkManager.getInstance(new ContextContainer(this),
        		new BookmarkXml(new XmlStream()), new BackupAccess());
        //manager.init(this);
        initializeTab();	//タブの初期化
        
        initializeMenuList();
        refleshAllView();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//メニューを追加
		MenuItem menu_create = menu.add(GROUP_DEFAULT, MENU_CREATE, 0, R.string.menu_create);
		MenuItem menu_delete = menu.add(GROUP_DEFAULT, MENU_DELETE_ALL, 0, R.string.menu_delete);
		MenuItem menu_export = menu.add(GROUP_DEFAULT, MENU_EXPORT, 0, R.string.menu_export);
		MenuItem menu_import = menu.add(GROUP_DEFAULT, MENU_IMPORT, 0, R.string.menu_import);
		//アイコンを設定
		menu_create.setIcon( R.drawable.ic_menu_create );
		menu_delete.setIcon( R.drawable.ic_menu_delete );
		menu_export.setIcon( R.drawable.ic_menu_export );
		menu_import.setIcon( R.drawable.ic_menu_import );
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case MENU_CREATE:
			startComposerActivity(null);
			return true;
		case MENU_DELETE_ALL:
		{
			List<Bookmark> linkedList = getLinkedListByStatus(viewStatus);
			int size = linkedList.size();
			if(size == 0) {	//「表示するデータがありません」が表示されている場合
				return true;
			}
    	
	        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	        alertDialogBuilder.setTitle(R.string.msg_title_confirm_delete);
	        alertDialogBuilder.setMessage(R.string.msg_confirm_delete);
	        alertDialogBuilder.setPositiveButton( R.string.button_yes,  
	        		new DialogInterface.OnClickListener() {   
	        			public void onClick(DialogInterface dialog, int which) {  
	        				removeAllBookmarkInTab();	//リスト内全削除
	        			}  
	        		});  
	        alertDialogBuilder.setNegativeButton( R.string.button_no,  
	        		new DialogInterface.OnClickListener() {   
	        			public void onClick(DialogInterface dialog, int which) {  
	        				//何もしない
	        			}  
	        		});  
	        // ダイアログを表示
	        alertDialogBuilder.create().show();
		}
			return true;
		case MENU_EXPORT:
		{
			boolean existFile = existExportFile();	//エクスポートファイルが存在するか
			if(existFile) {
				//上書き確認
		        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		        alertDialogBuilder.setTitle(R.string.msg_title_confirm_overwrite);
		        alertDialogBuilder.setMessage(R.string.msg_confirm_overwrite);
		        alertDialogBuilder.setPositiveButton( R.string.button_yes,  
		        		new DialogInterface.OnClickListener() {   
		        			public void onClick(DialogInterface dialog, int which) {  
		        				exportBookmarks();	//SDカードへの保存
		        			}  
		        		});  
		        alertDialogBuilder.setNegativeButton( R.string.button_no,  
		        		new DialogInterface.OnClickListener() {   
		        			public void onClick(DialogInterface dialog, int which) {  
		        				//何もしない
		        			}  
		        		});  
		        alertDialogBuilder.create().show();
			} else {
				exportBookmarks();	//SDカードへの保存
			}
		}
			return true;
		case MENU_IMPORT:
		{
			boolean existFile = existExportFile();	//エクスポートファイルが存在するか
			if(existFile == false) {
				showMessage( "SDカードに「siori_export.xml」が存在しません。", "復元" );
				return true;
			}
			
	        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	        alertDialogBuilder.setTitle(R.string.msg_title_confirm_import);
	        alertDialogBuilder.setMessage(R.string.msg_confirm_import);
	        alertDialogBuilder.setPositiveButton( R.string.button_yes,  
	        		new DialogInterface.OnClickListener() {   
	        			public void onClick(DialogInterface dialog, int which) {  
	        				importBookmarks();	//SDカードからの復元
	        			}  
	        		});  
	        alertDialogBuilder.setNegativeButton( R.string.button_no,  
	        		new DialogInterface.OnClickListener() {   
	        			public void onClick(DialogInterface dialog, int which) {  
	        				//何もしない
	        			}  
	        		});  
	        // ダイアログを表示
	        alertDialogBuilder.create().show();
		}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initializeTab() {
        logDebug("enter setContentView TabHost1");
    	TabHost host = (TabHost)findViewById(R.id.TabHost1);
        host.setup();
        
        // Tab1 設定
        TabSpec unreadtab = host.newTabSpec("unread");
        unreadtab.setIndicator(getString(R.string.status_unread));  	// タブに表示する文字列
        unreadtab.setContent(R.id.listviewUnread);	// タブ選択時に表示するビュー
        host.addTab(unreadtab);				// タブホストにタブ追加
  
        TabSpec progresstab = host.newTabSpec("progress");
        progresstab.setIndicator(getString(R.string.status_progress));
        progresstab.setContent(R.id.listviewProgress);
        host.addTab(progresstab);
               
        // Tab2 設定
        TabSpec completetab = host.newTabSpec("complete");
        completetab.setIndicator(getString(R.string.status_complete));  	// タブに表示する文字列
        completetab.setContent(R.id.listviewComplete);	// タブ選択時に表示するビュー
        host.addTab(completetab);				// タブホストにタブ追加
        
        // 初期表示設定
        host.setCurrentTab(1);
        viewStatus = ViewStatus.PROGRESS;
        
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
        	// タブがクリックされた時のハンドラ
        	public void onTabChanged(String tabId) {
        		if(tabId == "unread") {
        			Log.i("CCBM", "selectedtab unread");
        			viewStatus = ViewStatus.UNREAD;
        		} else if(tabId == "progress") {
        			Log.i("CCBM", "selectedtab progress");
        			viewStatus = ViewStatus.PROGRESS;
        		} else if(tabId == "complete") {
        			Log.i("CCBM", "selectedtab complete");
        			viewStatus = ViewStatus.COMPLETE;
        		} else {
        			Log.e("CCBM", "unknowntab selected");
        		}
        	}
        } );
    }
	
	private void startComposerActivity(Bookmark bm) {
		Intent intent = new Intent(BookmarkDisplayActivity.this, ComposeActivity.class);
		if(bm != null) {
			intent.putExtra("Bookmark", bm);
		}
		startActivityForResult(intent, SHOW_EDIT_FORM);
	}

    /**
     * {@code insertScoreRow()} helper method -- Populate a {@code TableRow} with
     * three columns of {@code TextView} data (styled)
     * 
     * @param tableRow
     *            The {@code TableRow} the text is being added to
     * @param text
     *            The text to add
     * @param textColor
     *            The color to make the text
     * @param textSize
     *            The size to make the text
     */
    private CharSequence addTextToRow( Bookmark bm ) {
    	String htmlVolume;
    	String htmlPage;
    	String htmlStatus;
    	String title = bm.getTitle();
    	String volume = bm.getVolume();
    	String page = bm.getPage();
    	String date = bm.getUpdateDate();
    	String status = bm.getReadStatusString();
    	
    	if(volume != null && !volume.equalsIgnoreCase("")) {
    		htmlVolume = volume + getString(R.string.volume);
    	} else {
    		htmlVolume = "";
    	}
    	if(page != null && !page.equalsIgnoreCase("")) {
    		htmlPage = page + getString(R.string.page);
    	} else {
    		htmlPage = "";
    	}
    	if(status != null && !status.equalsIgnoreCase("")) {
    		if(status.equalsIgnoreCase(getString(R.string.status_complete))	//完結または新刊待ち
    				|| status.equalsIgnoreCase(getString(R.string.status_waiting))) {
    			//青色
    			htmlStatus = "<font color=\"#0000FF\">" + status + "  " + "</font>";
    		} else {
    			//赤色
    			htmlStatus = "<font color=\"#FF0000\">" + status + "</font>";
    		}
    	} else {
    		htmlStatus = "";
    	}

    	// HTML タグ付き文字列の作成
        String html = "<font color=\"#FFFFFF\"><big><big><big><b>" + title + "</b></big></big></big><br></font>"
             			+ htmlStatus
        				+ "<font color=\"#FFFFFF\">" + htmlVolume + htmlPage + "</font>"
        				+ "<br><font color=\"#FFFFFF\">" + getString(R.string.last_update_date) + date + "</font>";
        // fromHtml() の引数にタグ付き文字列を渡す
        CharSequence source = Html.fromHtml(html);

        return source;
    }

    private void refleshAllView() {
    	refleshUnreadView();
    	refleshProgressView();
    	refleshCompleteView();
    }
    
    private ListView getListViewByStatus(ViewStatus status) {
    	ListView resultView;
    	switch(status) {
    	case UNREAD:
    		resultView = (ListView) findViewById(R.id.listviewUnread);
    		break;
    	case PROGRESS:
    		resultView = (ListView) findViewById(R.id.listviewProgress);
    		break;
    	case COMPLETE:
    		resultView = (ListView) findViewById(R.id.listviewComplete);
    		break;
    	default:
    		//ASSERT
    		resultView = null;
    	}
    	return resultView;
    }
    
    private List<Bookmark> getLinkedListByStatus(ViewStatus status) {
    	List<Bookmark> returnList;
    	switch(status) {
    	case UNREAD:
    		returnList = manager.getUnreadList();
    		break;
    	case PROGRESS:
    		returnList = manager.getProgressList();
    		break;
    	case COMPLETE:
    		returnList = manager.getCompleteList();
    		break;
    	default:
    		//TODO
    		returnList = null;
    	}
    	return returnList;
    }
    
    private void refleshView(ViewStatus status) {
    	ListView updateView = getListViewByStatus(status);
    	List<Bookmark> linkedList = getLinkedListByStatus(status);
    	
        boolean bInsertRow = false;
        int size = linkedList.size();
        if(size == 0) {
        	size = 1;
        }
      	CharSequence[] mStrings = new CharSequence[size];
      	int i = 0;
    	for(Bookmark bm : linkedList) {
    		CharSequence seq = addTextToRow(bm);
    		mStrings[i] = seq;
    		i++;
    		bInsertRow = true;
    	}
        if (bInsertRow == false) {
            mStrings[0] = getString(R.string.no_bookmark_data);
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
        		R.layout.menu_item, mStrings );
        updateView.setAdapter(adapter);
    }
    
    private void refleshUnreadView() {
    	refleshView(ViewStatus.UNREAD);
    }
    private void refleshProgressView() {
    	refleshView(ViewStatus.PROGRESS);
    }
    private void refleshCompleteView() {
    	refleshView(ViewStatus.COMPLETE);
    }
   
    private void processListItemSelected(int position) {
    	List<Bookmark> linkedList = getLinkedListByStatus(viewStatus);
    	int size = linkedList.size();
    	if(size == 0) {	//「表示するデータがありません」が表示されている場合
    		return;
    	}
    	Bookmark bm = linkedList.get(position);
    	startComposerActivity(bm);
    }
    
    private void initializeMenuList() {
    	ListView unreadMenu = (ListView) findViewById(R.id.listviewUnread);
    	unreadMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
            	processListItemSelected(position);
            }
        });
    	ListView progressMenu = (ListView) findViewById(R.id.listviewProgress);
    	progressMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
            	processListItemSelected(position);
            }
        });
    	ListView completeMenu = (ListView) findViewById(R.id.listviewComplete);
    	completeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
            	processListItemSelected(position);
            }
        });
    }
    
    private void removeAllBookmarkInTab() {
    	switch(viewStatus) {
    	case UNREAD:
    		manager.removeAllOfStatus(ReadStatus.UNREAD);
    		break;
    	case PROGRESS:
    		manager.removeAllOfStatus(ReadStatus.READING);
    		manager.removeAllOfStatus(ReadStatus.WAITING);
    		break;
    	case COMPLETE:
    		manager.removeAllOfStatus(ReadStatus.COMPLETE);
    		break;
    	default:
    		//ASSERT
    		break;
    	}
    	manager.flush(new ContextContainer(this));
    	refleshAllView();
    }
    
    private void importBookmarks() {
		boolean ret;
		//try {
		manager.importBookmarks(new ContextContainer(this));
		/*
		} catch(Exception e) {
			Log.d( "CCBM", e.toString());
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.create();
			alertDialogBuilder.setMessage("SDカードからの復元処理中にエラーが発生しました。しおりデータの消失を防ぐためアプリを終了します。");
	        alertDialogBuilder.setPositiveButton( R.string.button_ok,  
	        		new DialogInterface.OnClickListener() {   
	        			public void onClick(DialogInterface dialog, int which) {  
	        				finish();	//アプリ終了
	        			}  
	        		}); 
	        alertDialogBuilder.setTitle("復元");
	        alertDialogBuilder.show();
			return;
		}
		if(ret == false) {
			showMessage( "SDカードに「siori_export.xml」が存在しません。", "復元" );
		} else {
			refleshAllView();
		}
		*/
    }
    
    private void exportBookmarks() {
    	manager.exportBookmarks(new ContextContainer(this));
    	/*
		boolean ret;
		ret = manager.exportBookmarks();
		if(ret == false) {
			showMessage( "SDカードへの保存に失敗しました。", "エクスポート" );
		} else {
			showMessage( "SDカードへの保存に成功しました。", "エクスポート" );
		}
		*/
    }
    @SuppressWarnings("unused")
	private boolean existExportFile() {	
        String filePath = Environment.getExternalStorageDirectory() + "/siori_export.xml";
        File file = new File(filePath);
        file.getParentFile().mkdir();
        
		FileInputStream is;
        try{
            is = new FileInputStream(file);
        }
        catch(FileNotFoundException e) {
        	Log.d( "CCBM", "file not found");
        	return false;
        }
        return true;
    }
}