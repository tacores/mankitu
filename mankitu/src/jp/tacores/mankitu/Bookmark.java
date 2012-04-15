/**
 * 
 */
package jp.tacores.mankitu;

import java.io.Serializable;
import java.util.Calendar;

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
		this( in_title, convertReadStatusToEnum(in_readStatus), in_volume,
				in_page, in_story, in_memo, in_updateDate, in_uid );
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
			String in_page, String in_story, String in_memo ) {
		this( in_title, in_readStatus, in_volume,
				in_page, in_story, in_memo, getTodaysString(),
				getTodaysString() + System.currentTimeMillis() );
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
	 * �^�C�g����Ԃ��܂��B
	 * @return	�^�C�g��
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ������̏�Ԃ�Ԃ��܂��B
	 * @return	������̏�ԁiReadStatus�^�j
	 */
	public ReadStatus getReadStatus() {
		return readStatus;
	}
	/**
	 * ������̏�Ԃ�Ԃ��܂��B
	 * @return	������̏�ԁiString�^�j
	 */
	public String getReadStatusString() {
		String strStatus = convertReadStatusToString(readStatus);
		return strStatus;
	}

	/**
	 * ������Ԃ��܂��B
	 * @return	����
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * �y�[�W����Ԃ��܂��B
	 * @return	�y�[�W��
	 */
	public String getPage() {
		return page;
	}

	/**
	 * ���炷����Ԃ��܂��B
	 * @return	���炷��
	 */
	public String getStory() {
		return story;
	}

	/**
	 * ������Ԃ��܂��B
	 * @return	����
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * �ŏI�X�V����Ԃ��܂��B
	 * @return	�ŏI�X�V��
	 */
	public String getUpdateDate() {
		return String.valueOf(updateDate);
	}
	
	/**
	 * UID��Ԃ��܂��B
	 * @return	UID
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Enum�̏�Ԃ�String�̏�Ԃɕϊ����܂��B
	 * @param stringStatus	Enum�^�̏��
	 * @return	String�^�̏��
	 */
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
			//ASSERT
			stringStatus = "����";
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

