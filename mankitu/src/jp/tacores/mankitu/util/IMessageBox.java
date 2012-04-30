package jp.tacores.mankitu.util;

import android.content.DialogInterface;

public interface IMessageBox {
	public void alert(String msg, IContextContainer container);
	public void alertWithListener(String msg, IContextContainer container, DialogInterface.OnClickListener listener);
}
