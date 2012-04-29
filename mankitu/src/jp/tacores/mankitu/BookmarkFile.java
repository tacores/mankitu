package jp.tacores.mankitu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

class BookmarkData {
	public String title;			//�^�C�g��
	public String readStatus;		//���
	public String volume;			//��
	public String page;			//�y�[�W
	public String story;			//���炷��
	public String memo;			//����
	public String updateDate;		//�X�V��(YYYYMMDD)
	public String uid;			//UID
	
	public void clear() {
		title = null;
		readStatus = null;
		volume = null;
		page = null;
		story = null;
		memo = null;
		updateDate = null;
		uid = null;
	}
};

/**
 * ������f�[�^�̃t�@�C���������N���X�ł��B
 * @author Tacores
 *
 */
public class BookmarkFile {
	
	private static final String dataFilePath = "bookmarks.xml";
	private static final String sdCardPath = "/siori_export.xml";
	private BookmarkData bmData = new BookmarkData();
	private String tagName;
	private Context context;

	/**
	 * ���������������܂��B
	 * @param in_context	�R���e�L�X�g
	 */
	public void init(Context in_context) {
		context = in_context;
	}
	/**
	 * �t�@�C����ǂݍ���ŁA�f�[�^���e���X�g�ɃZ�b�g���܂��B
	 * @param out_unReadList	���ǃ��X�g
	 * @param out_progressList	�ǂ݂������X�g
	 * @param out_completeList	�������X�g
	 */
	public void readFile( List<Bookmark> out_unReadList,
			List<Bookmark> out_progressList,
			List<Bookmark> out_completeList ) {
		FileInputStream is;
        try{
            is = context.openFileInput(dataFilePath);
        }
        catch(FileNotFoundException e) {
        	Log.d( "CCBM", "file not found");
        	return;
        }
        
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput( is, "UTF-8" );
 
            int eventType;
            eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType)
                {
                case XmlPullParser.START_DOCUMENT:
                	//nop
                	break;
                case XmlPullParser.END_DOCUMENT:
                	break;
                case XmlPullParser.START_TAG:
                	processStartTag(xmlPullParser);
                	break;
                case XmlPullParser.END_TAG:
                	processEndTag(xmlPullParser,
                			out_unReadList, out_progressList, out_completeList );
                	break;
                case XmlPullParser.TEXT:
                	processText(xmlPullParser);
                	break;
                default:
                	break;
             
                }
                eventType = xmlPullParser.next();
            }
            is.close();
        }catch( Exception ex ){
            Log.d( "CCBM", ex.toString() );
        }
	}
	
	private void processStartTag(XmlPullParser xmlPullParser) {
    	String startTag = xmlPullParser.getName();
    	Log.d("CCBM", "processStart:" + startTag);
    	if(startTag.equalsIgnoreCase("bookmark")) {
    		bmData.clear();
    	} else {
    		tagName = startTag;
    	}
	}
	private void processEndTag(XmlPullParser xmlPullParser,
			List<Bookmark> out_unReadList,
			List<Bookmark> out_progressList,
			List<Bookmark> out_completeList ) {
    	String endTag = xmlPullParser.getName();
    	Log.d("CCBM", "processEnd:" + endTag);
    	if(endTag.equalsIgnoreCase("bookmark")) {
    		Bookmark bm = new Bookmark(bmData.title, bmData.readStatus, bmData.volume,
    				bmData.page, bmData.story, bmData.memo, bmData.updateDate, bmData.uid );
    		ReadStatus enumStatus = bm.getReadStatus();
    		switch(enumStatus) {
    		case UNREAD:
    			out_unReadList.add(bm);
    			break;
    		case WAITING:
    		case READING:
    			out_progressList.add(bm);
    			break;
    		case COMPLETE:
    			out_completeList.add(bm);
    			break;
    		default:
    			break;
    		}
    	}
	}
	private void processText(XmlPullParser xmlPullParser) {
    	String text = xmlPullParser.getText();
    	Log.d("CCBM", "processText:" + text);
    	if(tagName.equalsIgnoreCase("title")) {
    		bmData.title = text;
    	} else if(tagName.equalsIgnoreCase("status")) {
    		bmData.readStatus = text;
    	} else if(tagName.equalsIgnoreCase("volume")) {
    		bmData.volume = text;
    	} else if(tagName.equalsIgnoreCase("page")) {
    		bmData.page = text;
    	} else if(tagName.equalsIgnoreCase("story")) {
    		bmData.story = text;
    	} else if(tagName.equalsIgnoreCase("memo")) {
    		bmData.memo = text;
    	} else if(tagName.equalsIgnoreCase("date")) {
    		bmData.updateDate = text;
    	} else if(tagName.equalsIgnoreCase("uid")) {
    		bmData.uid = text;
    	} else {
    		//ASSERT
    	}
	}
	private void outputListXml( List<Bookmark> list, XmlSerializer serializer ) {
		for(Bookmark bm: list) {
			try {
			serializer.startTag("", "bookmark");
			Log.d("CCBM", "write starttag bookmark" );
	        serializer.startTag("", "title");
	        Log.d("CCBM", "write starttag title" );
            serializer.text( bm.getTitle() );
            serializer.endTag("", "title");
            Log.d("CCBM", "write endtag title" );
            
	        serializer.startTag("", "uid");
	        Log.d("CCBM", "write starttag uid" );
            serializer.text( bm.getUid() );
            serializer.endTag("", "uid");    
            Log.d("CCBM", "write endtag uid" );

            serializer.startTag("", "status");
	        Log.d("CCBM", "write starttag status" );
            serializer.text( bm.getReadStatusString() );
            serializer.endTag("", "status");    
            Log.d("CCBM", "write endtag status" );
            
            if(bm.getVolume() != null) {
		        serializer.startTag("", "volume");
		        Log.d("CCBM", "write starttag volume" );
	            serializer.text( bm.getVolume() );
	            serializer.endTag("", "volume");    
	            Log.d("CCBM", "write endtag volume" );
            }
            
            if(bm.getPage() != null) {
		        serializer.startTag("", "page");
		        Log.d("CCBM", "write starttag page" );
	            serializer.text( bm.getPage() );
	            serializer.endTag("", "page");
	            Log.d("CCBM", "write endtag page" );
            }

            if(bm.getStory() != null) {
		        serializer.startTag("", "story");
		        Log.d("CCBM", "write starttag story" );
	            serializer.text( bm.getStory() );
	            serializer.endTag("", "story");
	            Log.d("CCBM", "write endtag story" );
            }
            
            if(bm.getMemo() != null) {
		        serializer.startTag("", "memo");
		        Log.d("CCBM", "write starttag memo" );
	            serializer.text( bm.getMemo() );
	            serializer.endTag("", "memo"); 
	            Log.d("CCBM", "write endtag memo" );
            }
            
            if(bm.getUpdateDate() != null) {
		        serializer.startTag("", "date");
		        Log.d("CCBM", "write starttag date" );
	            serializer.text( bm.getUpdateDate() );
	            serializer.endTag("", "date");
	            Log.d("CCBM", "write endtag date" );
            }
            serializer.endTag("", "bookmark"); 
            Log.d("CCBM", "write endtag bookmark" );
			}
			catch(Exception e){
				//ASSERT
				Log.e("CCBM", e.toString() );
			}
		}
	}
	
	/**
	 * ������f�[�^���t�@�C���ɔ��f���܂��B
	 * @param unReadList	���ǃ��X�g
	 * @param progressList	�ǂ݂������X�g
	 * @param completeList	�������X�g
	 */
	public void flush( List<Bookmark> unReadList,
			List<Bookmark> progressList,
			List<Bookmark> completeList ) {
        XmlSerializer serializer = Xml.newSerializer();
        try {
            //FileOutputStream os = new FileOutputStream( fPath );
        	FileOutputStream os =  context.openFileOutput(dataFilePath, Context.MODE_PRIVATE);
            serializer.setOutput( os, "UTF-8" );
            serializer.startDocument("UTF-8", true);    // �w�b�_�[
 
            serializer.startTag("", "bookmarks");
            outputListXml(unReadList, serializer);
            outputListXml(progressList, serializer);
            outputListXml(completeList, serializer);
            serializer.endTag("", "bookmarks");
            serializer.endDocument();    // �t�b�^�[
 
            serializer.flush();    // �o��
            os.close();
        } catch (Exception e) {
        	Log.e("CCBM", e.toString() );
        	return;
        }
	}
	
	private boolean copyFile(FileInputStream fis, FileOutputStream fos) {
		boolean ret = false;
        FileChannel ifc = null;
        FileChannel ofc = null;
        try {
            // �t�@�C���̃X�g���[������`���l�����擾
            ifc = fis.getChannel();
            ofc = fos.getChannel();
            // �o�C�g��]��
            ifc.transferTo(0, ifc.size(), ofc);
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ifc != null) {
                try {
                    ifc.close();
                } catch (IOException e) {
                }
            }
            if (ofc != null) {
                try {
                    ofc.close();
                } catch (IOException e) {
                }
            }
        }
        return ret;
	}
	
	/**
	 * ������f�[�^�t�@�C����SD�J�[�h�ɃR�s�[���܂��B
	 * @return	��������
	 */
	public boolean copyBookmarkFileToSDcard() {
		Log.d("CCBM", "copyBookmarkFileToSDcard");
        String filePath = Environment.getExternalStorageDirectory() + sdCardPath;
        File file = new File(filePath);
        file.getParentFile().mkdir();
 
        FileOutputStream fos;	//output:export file in SDcard.
        try {
            fos = new FileOutputStream(file, false);
        } catch (Exception e) {
        	Log.d( "CCBM", e.toString());
        	return false;
        }
        
		FileInputStream fis;
        try{
            fis = context.openFileInput(dataFilePath);	//input:data file in local.
        } catch(FileNotFoundException e) {
        	try {	
        		fos.close();
        	} catch(IOException ioe) {
        		Log.d("CCBM", ioe.toString());
        	}
        	Log.d( "CCBM", e.toString());
        	return false;
        }
        
        boolean ret = copyFile(fis, fos);
        try {
        	fos.close();
        } catch (IOException ioe) {
        	Log.d("CCBM", ioe.toString());
        }
        try {
        	fis.close();
        } catch (IOException ioe) {
        	Log.d("CCBM", ioe.toString());
        }
        return ret;
	}
	
	/**
	 * SD�J�[�h�̃f�[�^���炵����f�[�^�����X�g�A���܂��B
	 * @param out_unReadList	���ǃ��X�g
	 * @param out_progressList	�ǂ݂������X�g
	 * @param out_completeList	�������X�g
	 * @return	��������
	 * @throws Exception
	 */
	public boolean restoreFileFromSDcard( List<Bookmark> out_unReadList,
			List<Bookmark> out_progressList,
			List<Bookmark> out_completeList ) throws Exception {
		Log.d("CCBM", "restoreFileFromSDcard");
        String filePath = Environment.getExternalStorageDirectory() + sdCardPath;
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
        
        out_unReadList.clear();
        out_progressList.clear();
        out_completeList.clear();
        
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput( is, "UTF-8" );
 
            int eventType;
            eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType)
                {
                case XmlPullParser.START_DOCUMENT:
                	//nop
                	break;
                case XmlPullParser.END_DOCUMENT:
                	break;
                case XmlPullParser.START_TAG:
                	processStartTag(xmlPullParser);
                	break;
                case XmlPullParser.END_TAG:
                	processEndTag(xmlPullParser,
                			out_unReadList, out_progressList, out_completeList );
                	break;
                case XmlPullParser.TEXT:
                	processText(xmlPullParser);
                	break;
                default:
                	break;
                }
                eventType = xmlPullParser.next();
            }
            is.close();
        }catch( Exception ex ){
            Log.d( "CCBM", ex.toString() );
            is.close();
            throw ex;
        }
        flush(out_unReadList, out_progressList, out_completeList);
		return true;
	}
}
