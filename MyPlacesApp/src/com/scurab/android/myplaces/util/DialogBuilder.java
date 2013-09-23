package com.scurab.android.myplaces.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MapItemActivity;
import com.scurab.android.myplaces.datamodel.Detail;
import com.scurab.android.myplaces.datamodel.Star;
import com.scurab.android.myplaces.widget.dialog.EditTextDialog;
import com.scurab.android.myplaces.widget.dialog.ListDialog;
import com.scurab.android.myplaces.widget.dialog.MapItemDetailDialog;

import java.util.List;

public class DialogBuilder {
    public interface OnAddMapItenContextButtonClickListener {
        public final static int PRO = 1;
        public final static int CON = 2;
        public final static int DETAIL = 4;

        public void onClick(View v, int type);
    }

    /**
     * @param context
     * @param mEditText as contentview
     * @param s
     * @return
     */
    public static AlertDialog getStarDialog(Context context, EditText mEditText, Star s) {
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }
        if (s == null) {
            throw new NullPointerException("Star is null!");
        }

        mEditText.setSingleLine(false);
        mEditText.setLines(5);
        mEditText.setGravity(Gravity.TOP | Gravity.LEFT);
        AlertDialog.Builder b = new Builder(context);
        b.setTitle(R.string.lblNote);
        b.setView(mEditText);
        mEditText.setText(s.getNote());
        return b.create();
    }

    public static AlertDialog getAddMapItemContextDialog(Context context, final OnAddMapItenContextButtonClickListener listener) {
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }
        if (listener == null) {
            throw new NullPointerException("OnAddMapItenContextButtonClickListener is null!");
        }

        View v = View.inflate(context, R.layout.dialog_addcontext, null);
        View b = v.findViewById(R.id.ibPro);
        AlertDialog.Builder bldr = new Builder(context);
        bldr.setView(v);
        final AlertDialog ad = bldr.create();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                listener.onClick(v, OnAddMapItenContextButtonClickListener.PRO);
            }
        });
        b = v.findViewById(R.id.ibCon);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                listener.onClick(v, OnAddMapItenContextButtonClickListener.CON);
            }
        });
        b = v.findViewById(R.id.ibDetail);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                listener.onClick(v, OnAddMapItenContextButtonClickListener.DETAIL);
            }
        });

        return ad;
    }

    public static EditTextDialog getMapItemContextDialog(Context context, int headerIcon, DialogInterface.OnClickListener listener, String content) {
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }

        EditTextDialog dtd = new EditTextDialog(context, headerIcon);
        if (content != null) {
            dtd.setText(content);
        }
        dtd.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.lblOK), listener);
        dtd.setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.lblCancel), listener);
        return dtd;
    }

    public static MapItemDetailDialog getMapItemContextDialog(Context context, DialogInterface.OnClickListener listener, Detail content) {
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }

        MapItemDetailDialog dtd = new MapItemDetailDialog(context);
        if (content != null) {
            dtd.setDetail(content);
        }
        dtd.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.lblOK), listener);
        dtd.setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.lblCancel), listener);
        return dtd;
    }

    public static AlertDialog getSimpleQuestionDialog(Context context, String title, String question, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(question);
        builder.setPositiveButton(context.getString(R.string.lblOK), okListener);
        builder.setNegativeButton(context.getString(R.string.lblCancel), null);
        AlertDialog a = builder.create();
        return a;
    }

    public static AlertDialog getSimpleRUSureDialog(Context context, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.txtRUSure));
        builder.setPositiveButton(context.getString(R.string.lblOK), okListener);
        builder.setNegativeButton(context.getString(R.string.lblCancel), null);
        AlertDialog a = builder.create();
        return a;
    }

    public static ListDialog<Address> getAddressDialog(MapItemActivity context, List<Address> data, ListDialog.OnItemSelectListener<Address> listener) {
        ListDialog.AddressAdapter adapter = new ListDialog.AddressAdapter(context, data);
        ListDialog<Address> ld = new ListDialog<Address>(context, data, adapter);
        ld.setOnItemClickListener(listener);
        ld.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.lblCancel), (DialogInterface.OnClickListener) null);
        return ld;
    }
}
