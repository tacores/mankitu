package jp.tacores.mankitu.test.util;

import android.content.Context;
import jp.tacores.mankitu.util.IContextContainer;

public class NullContextContainer implements IContextContainer {

	public Context getContext() {
		return null;
	}

}
