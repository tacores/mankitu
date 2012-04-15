package jp.tacores.mankitu;

import android.content.Context;

public class ContextContainer implements IContextContainer {
	public ContextContainer(Context context) {
		if(context == null) throw new IllegalArgumentException("context is null.");
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	private Context context;
}
