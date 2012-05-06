/**
 * 
 */
package jp.tacores.mankitu.bookmark;

import java.io.Serializable;

import jp.tacores.mankitu.util.ITimeProvider;

/**
 * ��������J�v�Z���������N���X�ł��B
 * @author Tacores
 *
 */
public class Bookmark implements Serializable {
	private static final long serialVersionUID = 8843651541969761207L;
	private String title;			//�^�C�g��
	private ReadStatus readStatus;	//���
	private String volume;			//��
	private String page;			//�y�[�W
	private String story;			//���炷��
	private String memo;			//����
	private String updateDate;		//�X�V��(YYYYMMDD)
	private String uid;				//UID

	/**
	 * �R���X�g���N�^
	 * @param in_title	�^�C�g��
	 * @param in_readStatus		���
	 * @param in_volume		����
	 * @param in_page	�y�[�W��
	 * @param in_story	���炷��
	 * @param in_memo	����
	 * @param in_updateDate		�ŏI�X�V��
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
	 * �R���X�g���N�^
	 * @param in_title	�^�C�g��
	 * @param in_readStatus		���
	 * @param in_volume		����
	 * @param in_page	�y�[�W��
	 * @param in_story	���炷��
	 * @param in_memo	����
	 */
	public Bookmark( String in_title, String in_readStatus, String in_volume,
			String in_page, String in_story, String in_memo, ITimeProvider time) {
		this( in_title, in_readStatus, in_volume,
				in_page, in_story, in_memo, getTodaysString(time), generateUid(time) );
	}

	/**
	 * ������̓��e���X�V���܂��B
	 * @param in_title	�^�C�g��
	 * @param in_readStatus		���
	 * @param in_volume		����
	 * @param in_page	�y�[�W��
	 * @param in_story	���炷��
	 * @param in_memo	����
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
		if (stringStatus.equals("�V���҂�")) {
			enumStatus = ReadStatus.WAITING;
		} else if (stringStatus.equals("�ǂ݂���")) {
			enumStatus = ReadStatus.READING;
		} else if (stringStatus.equals("����")) {
			enumStatus = ReadStatus.COMPLETE;
		} else if (stringStatus.equals("����")) {
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
			stringStatus = "����";
			break;
		case WAITING:
			stringStatus = "�V���҂�";
			break;
		case READING:
			stringStatus = "�ǂ݂���";
			break;
		case COMPLETE:
			stringStatus = "����";
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

