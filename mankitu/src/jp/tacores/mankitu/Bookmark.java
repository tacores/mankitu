/**
 * 
 */
package jp.tacores.mankitu;

import java.io.Serializable;
import java.util.Calendar;

/**
 * しおりをカプセル化したクラスです。
 * @author Tacores
 *
 */
public class Bookmark implements Serializable {
	private static final long serialVersionUID = 8843651541969761207L;
	private String title;			//タイトル
	private ReadStatus readStatus;	//状態
	private String volume;			//巻
	private String page;			//ページ
	private String story;			//あらすじ
	private String memo;			//メモ
	private String updateDate;		//更新日(YYYYMMDD)
	private String uid;				//UID
	
	/**
	 * コンストラクタ
	 * @param in_title	タイトル
	 * @param in_readStatus		状態
	 * @param in_volume		巻数
	 * @param in_page	ページ数
	 * @param in_story	あらすじ
	 * @param in_memo	メモ
	 * @param in_updateDate		最終更新日
	 * @param in_uid	UID
	 */
	public Bookmark( String in_title, ReadStatus in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo, String in_updateDate, String in_uid ) {
		if(in_title == null) throw new IllegalArgumentException("title");
		if(in_volume == null) throw new IllegalArgumentException("volume");
		if(in_page == null) throw new IllegalArgumentException("page");
		if(in_story == null) throw new IllegalArgumentException("story");
		if(in_memo == null) throw new IllegalArgumentException("memo");
		if(in_updateDate == null) throw new IllegalArgumentException("updateDate");
		if(in_uid == null) throw new IllegalArgumentException("uid");

		title = in_title;
		readStatus = in_readStatus;
		volume = in_volume;
		page = in_page;
		story = in_story;
		memo = in_memo;
		updateDate = in_updateDate;
		uid = in_uid;
		trim();
	}
	/**
	 * コンストラクタ
	 * @param in_title	タイトル
	 * @param in_readStatus		状態
	 * @param in_volume		巻数
	 * @param in_page	ページ数
	 * @param in_story	あらすじ
	 * @param in_memo	メモ
	 * @param in_updateDate		最終更新日
	 * @param in_uid	UID
	 */
	public Bookmark( String in_title, String in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo, String in_updateDate, String in_uid ) {
		this( in_title, convertReadStatusToEnum(in_readStatus), in_volume,
				in_page, in_story, in_memo, in_updateDate, in_uid );
	}
	/**
	 * コンストラクタ
	 * @param in_title	タイトル
	 * @param in_readStatus		状態
	 * @param in_volume		巻数
	 * @param in_page	ページ数
	 * @param in_story	あらすじ
	 * @param in_memo	メモ
	 */
	public Bookmark( String in_title, String in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo ) {
		this( in_title, in_readStatus, in_volume,
				in_page, in_story, in_memo, getTodaysString(),
				getTodaysString() + System.currentTimeMillis() );
	}

	/**
	 * しおりの内容を更新します。
	 * @param in_title	タイトル
	 * @param in_readStatus		状態
	 * @param in_volume		巻数
	 * @param in_page	ページ数
	 * @param in_story	あらすじ
	 * @param in_memo	メモ
	 */
	public void update( String in_title, String in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo ) {
		title = in_title;
		readStatus = convertReadStatusToEnum(in_readStatus);
		volume = in_volume;
		page = in_page;
		story = in_story;
		memo = in_memo;
		updateDate = getTodaysString();
		trim();
	}

	static private String getTodaysString() {
		int today;	//YYYYMMDD
		Calendar now = Calendar.getInstance();
		today = now.get(Calendar.YEAR) * 10000
				+ (now.get(Calendar.MONTH) + 1) * 100 + now.get(Calendar.DAY_OF_MONTH);
		String todaysString = String.valueOf(today);
		return todaysString;
	}
	
	/**
	 * タイトルを返します。
	 * @return	タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * しおりの状態を返します。
	 * @return	しおりの状態（ReadStatus型）
	 */
	public ReadStatus getReadStatus() {
		return readStatus;
	}
	/**
	 * しおりの状態を返します。
	 * @return	しおりの状態（String型）
	 */
	public String getReadStatusString() {
		String strStatus = convertReadStatusToString(readStatus);
		return strStatus;
	}

	/**
	 * 巻数を返します。
	 * @return	巻数
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * ページ数を返します。
	 * @return	ページ数
	 */
	public String getPage() {
		return page;
	}

	/**
	 * あらすじを返します。
	 * @return	あらすじ
	 */
	public String getStory() {
		return story;
	}

	/**
	 * メモを返します。
	 * @return	メモ
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 最終更新日を返します。
	 * @return	最終更新日
	 */
	public String getUpdateDate() {
		return String.valueOf(updateDate);
	}
	
	/**
	 * UIDを返します。
	 * @return	UID
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Enumの状態をStringの状態に変換します。
	 * @param stringStatus	Enum型の状態
	 * @return	String型の状態
	 */
	static public ReadStatus convertReadStatusToEnum(String stringStatus) {
		ReadStatus enumStatus;
		if (stringStatus.equals("新刊待ち")) {
			enumStatus = ReadStatus.WAITING;
		} else if (stringStatus.equals("読みかけ")) {
			enumStatus = ReadStatus.READING;
		} else if (stringStatus.equals("完結")) {
			enumStatus = ReadStatus.COMPLETE;
		} else if (stringStatus.equals("未読")) {
			enumStatus = ReadStatus.UNREAD;
		} else {
			//ASSERT
			enumStatus = ReadStatus.UNREAD;
		}
		return enumStatus;
	}
	static private String convertReadStatusToString(ReadStatus enumStatus) {
		String stringStatus;
		switch(enumStatus)
		{
		case UNREAD:
			stringStatus = "未読";
			break;
		case WAITING:
			stringStatus = "新刊待ち";
			break;
		case READING:
			stringStatus = "読みかけ";
			break;
		case COMPLETE:
			stringStatus = "完結";
			break;
		default:
			//ASSERT
			stringStatus = "未読";
			break;		
		}
		return stringStatus;
	}
	private void trim() {
		if(title != null){
			title = title.trim();
		}
		if(volume != null) {
			volume = volume.trim();
		}
		if(page != null) {
			page = page.trim();
		}
		if(story != null) {
			story = story.trim();
		}
		if(memo != null) {
			memo = memo.trim();
		}
	}
}

