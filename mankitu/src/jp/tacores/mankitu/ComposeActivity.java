package jp.tacores.mankitu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * しおりを編集するアクティビティクラスです。
 * @author Tacores
 *
 */
public class ComposeActivity extends MyActivity {
	Bookmark bm;
	private String oldTitle;			//タイトル
	private ReadStatus oldStatus;	//状態
	private String oldVolume;			//巻
	private String oldPage;			//ページ
	private String oldStory;			//あらすじ
	private String oldMemo;			//メモ
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.compose);
        Intent intent = getIntent();
        bm = (Bookmark)intent.getSerializableExtra("Bookmark");
       
        initStatusSpinner();
        if (bm != null) {
        	initEditText();
        }
        setOldValue();

        Button saveButton = (Button)findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveBookmark();
			}
		});
        
        Button deleteButton = (Button)findViewById(R.id.buttonDelete);
        if(bm == null) {
        	deleteButton.setEnabled(false);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(bm != null) {
					confirmAndDelete();
				}
			}
		});
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(isEditValueChanged()) {	//編集されている
				confirmAndFinish();
				return false;
			} else {	//編集されていない
				return super.onKeyDown(keyCode, event);
			}
		} else { 
			return super.onKeyDown(keyCode, event);
		}
	}

	private int getSpinnerIndex(String strStatus) {
    	int index;
    	if(strStatus.equalsIgnoreCase(getString(R.string.status_unread))) {
    		index = 0;
    	} else if(strStatus.equalsIgnoreCase(getString(R.string.status_waiting))) {
    		index = 1;
    	} else if(strStatus.equalsIgnoreCase(getString(R.string.status_progress))) {
    		index = 2;
    	} else if(strStatus.equalsIgnoreCase(getString(R.string.status_complete))) {
    		index = 3;
    	} else {
    		//ASSERT
    		index = 0;
    	}
    	return index;
    }
    
    /**
     * Initialize the spinner
     */
    private void initStatusSpinner() {
        // Populate Spinner control with genders
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.status,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if ( bm != null ) {
        	int index = getSpinnerIndex(bm.getReadStatusString());
            spinner.setSelection(index);
        }
    }
    
    private void initEditText() {
    	EditText editTitle = (EditText)findViewById(R.id.editTextTitle);
    	EditText editVolume = (EditText)findViewById(R.id.editTextVolume);
    	EditText editPage = (EditText)findViewById(R.id.editTextPage);
    	EditText editStory = (EditText)findViewById(R.id.editTextStory);
    	EditText editMemo = (EditText)findViewById(R.id.editTextMemo);
    	
    	editTitle.setText(bm.getTitle());
    	editVolume.setText(bm.getVolume());
    	editPage.setText(bm.getPage());
    	editStory.setText(bm.getStory());
    	editMemo.setText(bm.getMemo());
    }
    
    private void saveBookmark() {
    	String title;
    	String status;
    	String volume;
    	String page;
    	String story;
    	String memo;
    	ReadStatus enumStatus;
        
    	EditText editTitle = (EditText)findViewById(R.id.editTextTitle);
    	EditText editVolume = (EditText)findViewById(R.id.editTextVolume);
    	EditText editPage = (EditText)findViewById(R.id.editTextPage);
    	Spinner spinStatus = (Spinner)findViewById(R.id.spinner1);
    	EditText editStory = (EditText)findViewById(R.id.editTextStory);
    	EditText editMemo = (EditText)findViewById(R.id.editTextMemo);
    	
    	title = editTitle.getText().toString();
    	status = (String)spinStatus.getSelectedItem();
    	volume = editVolume.getText().toString();
    	page = editPage.getText().toString();
    	story = editStory.getText().toString();
    	memo = editMemo.getText().toString();
    	enumStatus = Bookmark.convertReadStatusToEnum(status);

    	if (title != null && title.equalsIgnoreCase("")) {
    		showMessage( "タイトルが未入力のため保存できません。", "編集" );
            return;
    	}
    	if ( enumStatus == ReadStatus.WAITING || enumStatus == ReadStatus.READING ) {
    		if (volume.equalsIgnoreCase("")) {
    			showMessage( "「新刊待ち」または「読みかけ」の場合、巻数の入力が必要です。", "編集" );
    			return;
    		}
    	}
    	if (volume != null && volume.equalsIgnoreCase("") == false) {
    		try {
    			Integer.valueOf(volume);
    		} catch (NumberFormatException e) {
    			showMessage( "「巻数」には数字を入力してください。", "編集" );
    			return;
    		}
    	}
    	if (page != null && page.equalsIgnoreCase("") == false) {
    		try {
    			Integer.valueOf(page);
    		} catch (NumberFormatException e) {
    			showMessage( "「ページ」には数字を入力してください。", "編集" );
    			return;
    		}
    	}
   
    	if(bm != null) {	//編集
    		bm.update(title, status, volume, page, story, memo, new TimeProvider());
    		manager.updateBookmark(bm);
    	} else {	//新規
    		Bookmark bm_new = new Bookmark( title, status, volume, page, story, memo, new TimeProvider() );
    		manager.insertBookmark(bm_new);
    	}
    	manager.flush();	//ファイル保存
    	
    	sendResultAndFinish();
    }
    
    private void deleteBookmark() {
		manager.removeBookmark(bm);
		manager.flush();
    }
    
    private void sendResultAndFinish() {
        Intent intent = new Intent();  
        setResult(RESULT_OK, intent);  
    	this.finish();
    }
    
    private void finishActivity() {
    	this.finish();
    }
    
    private void confirmAndDelete() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.msg_title_confirm_delete);
        alertDialogBuilder.setMessage(R.string.msg_confirm_one_delete);
        alertDialogBuilder.setPositiveButton( R.string.button_yes,  
        		new DialogInterface.OnClickListener() {   
        			public void onClick(DialogInterface dialog, int which) {  
						deleteBookmark();
						sendResultAndFinish();
        			}  
        		});  
        alertDialogBuilder.setNegativeButton( R.string.button_no,  
        		new DialogInterface.OnClickListener() {   
        			public void onClick(DialogInterface dialog, int which) {  
        				//何もしない
        			}  
        		});  
        alertDialogBuilder.create().show();
    }
    
    /**
     * 編集開始時の設定内容を保存します。
     */
    private void setOldValue() {
    	if(bm != null) {
    		oldTitle = bm.getTitle();
    		oldStatus = bm.getReadStatus();
    		oldVolume = bm.getVolume();
    		oldPage = bm.getPage();
    		oldStory = bm.getStory();
    		oldMemo = bm.getMemo();
    		
    		if(oldTitle == null) {
    			oldTitle = "";
    		}
    		if(oldVolume == null) {
    			oldVolume = "";
    		}
    		if(oldPage == null) {
    			oldPage = "";
    		}
    		if(oldStory == null) {
    			oldStory = "";
    		}
    		if(oldMemo == null) {
    			oldMemo = "";
    		}
    	} else {
    		oldTitle = "";			//タイトル
    		oldStatus = ReadStatus.UNREAD;	//状態
    		oldVolume = "";			//巻
    		oldPage = "";			//ページ
    		oldStory = "";			//あらすじ
    		oldMemo = "";			//メモ
    	}
    }
    
    private boolean isEditValueChanged() {
    	String title;
    	String status;
    	String volume;
    	String page;
    	String story;
    	String memo;
    	ReadStatus enumStatus;
        
    	EditText editTitle = (EditText)findViewById(R.id.editTextTitle);
    	EditText editVolume = (EditText)findViewById(R.id.editTextVolume);
    	EditText editPage = (EditText)findViewById(R.id.editTextPage);
    	Spinner spinStatus = (Spinner)findViewById(R.id.spinner1);
    	EditText editStory = (EditText)findViewById(R.id.editTextStory);
    	EditText editMemo = (EditText)findViewById(R.id.editTextMemo);
    	
    	title = editTitle.getText().toString();
    	status = (String)spinStatus.getSelectedItem();
    	volume = editVolume.getText().toString();
    	page = editPage.getText().toString();
    	story = editStory.getText().toString();
    	memo = editMemo.getText().toString();
    	enumStatus = Bookmark.convertReadStatusToEnum(status);
    	
    	boolean changed = false;
    	if(title.equalsIgnoreCase(oldTitle)
    			&& volume.equalsIgnoreCase(oldVolume)
    			&& page.equalsIgnoreCase(oldPage)
    			&& story.equalsIgnoreCase(oldStory)
    			&& memo.equalsIgnoreCase(oldMemo)
    			&& enumStatus == oldStatus )
    	{
    		changed = false;
    	} else {
    		changed = true;
    	}
    	return changed;
    }
    
    void confirmAndFinish() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.msg_title_confirm_back);
        alertDialogBuilder.setMessage(R.string.msg_confirm_back);
        alertDialogBuilder.setPositiveButton( R.string.button_yes,  
        		new DialogInterface.OnClickListener() {   
        			public void onClick(DialogInterface dialog, int which) {  
						finishActivity();
        			}  
        		});  
        alertDialogBuilder.setNegativeButton( R.string.button_no,  
        		new DialogInterface.OnClickListener() {   
        			public void onClick(DialogInterface dialog, int which) {  
        				//何もしない
        			}  
        		});  
        alertDialogBuilder.create().show();
    }
}