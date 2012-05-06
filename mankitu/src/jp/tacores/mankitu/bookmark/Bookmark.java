/**
 * 
 */
package jp.tacores.mankitu.bookmark;

import java.io.Serializable;

import jp.tacores.mankitu.util.ITimeProvider;

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
	public Bookmark( String in_title, String in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo, String in_updateDate, String in_uid ) {
		if(in_title == null) throw new IllegalArgumentException("title");
		if(in_readStatus == null) throw new IllegalArgumentException("readStatus");
		if(in_volume == null) throw new IllegalArgumentException("volume");
		if(in_page == null) throw new IllegalArgumentException("page");
		if(in_story == null) throw new IllegalArgumentException("story");
		if(in_memo == null) throw new IllegalArgumentException("memo");
		if(in_updateDate == null) throw new IllegalArgumentException("updateDate");
		if(in_uid == null) throw new IllegalArgumentException("uid");

		title = in_title;
		readStatus = convertReadStatusToEnum(in_readStatus);
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
	 */
	public Bookmark( String in_title, String in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo, ITimeProvider time) {
		this( in_title, in_readStatus, in_volume,
				in_page, in_story, in_memo, getTodaysString(time), generateUid(time) );
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
			String in_page, String in_story, String in_memo, ITimeProvider time) {
		if(in_title == null) throw new IllegalArgumentException("title");
		if(in_readStatus == null) throw new IllegalArgumentException("readStatus");
		if(in_volume == null) throw new IllegalArgumentException("volume");
		if(in_page == null) throw new IllegalArgumentException("page");
		if(in_story == null) throw new IllegalArgumentException("story");
		if(in_memo == null) throw new IllegalArgumentException("memo");
		if(time == null) throw new IllegalArgumentException("timeProvider");
		
		title = in_title;
		readStatus = convertReadStatusToEnum(in_readStatus);
		volume = in_volume;
		page = in_page;
		story = in_story;
		memo = in_memo;
		updateDate = getTodaysString(time);
		trim();
	}
	
	static private String generateUid(ITimeProvider time) {
		return getTodaysString(time) + getCurrentTimeMillis(time);
	}
	
	static private String getCurrentTimeMillis(ITimeProvider time) {
		return Long.toString(time.getCurrentMillis());
	}
	
	static private String getTodaysString(ITimeProvider time) {
		int today = time.getYear()*10000 + time.getMonth()*100 + time.getDay();
		return String.valueOf(today);
	}
	
	public String getTitle() {
		return title;
	}

	public ReadStatus getReadStatus() {
		return readStatus;
	}

	public String getReadStatusString() {
		return convertReadStatusToString(readStatus);
	}

	public String getVolume() {
		return volume;
	}

	public String getPage() {
		return page;
	}

	public String getStory() {
		return story;
	}

	public String getMemo() {
		return memo;
	}

	public String getUpdateDate() {
		return String.valueOf(updateDate);
	}

	public String getUid() {
		return uid;
	}

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
			throw new IllegalArgumentException("Illegal ReadState." + stringStatus);
		}
		return enumStatus;
	}
	
	static public String convertReadStatusToString(ReadStatus enumStatus) {
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
			throw new IllegalArgumentException("Illegal EnumStatus");
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

