package com.scurab.android.myplaces.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.google.android.maps.GeoPoint;
import com.scurab.android.myplaces.R;

public class SmileyDialog extends Dialog {
    private OnClickListener mClickListener;
    private GeoPoint mGeoPoint;

    public SmileyDialog(Context context, GeoPoint gp) {
        super(context);
        if (gp == null) {
            throw new NullPointerException("GeoPoint must be set!");
        }

        mGeoPoint = gp;
        ViewGroup v = (ViewGroup) View.inflate(context, R.layout.smileys, null);
        setContentView(v);
        setTitle(R.string.lblChooseStar);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        bind(v);
    }

    private void bind(ViewGroup vg) {
        for (int i = 0, n = vg.getChildCount(); i < n; i++) {
            View v = vg.getChildAt(i);
            if (v instanceof ImageButton) {
                ImageButton b = (ImageButton) v;
                bind(b);
            } else if (v instanceof ViewGroup) {
                ViewGroup vgin = (ViewGroup) v;
                bind(vgin);
            }
        }
    }

    private void bind(final ImageButton b) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(SmileyDialog.this, v.getId());
                }
                dismiss();
            }
        });
    }

    public GeoPoint getGeoPoint() {
        return mGeoPoint;
    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }
}
