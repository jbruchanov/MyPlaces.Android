package com.scurab.android.myplaces.interfaces;

import android.content.Intent;

public interface OnActivityResultListener
{
	void onActivityResult(int requestCode, int resultCode, Intent data);
}
