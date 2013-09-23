package com.scurab.android.myplaces.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.util.DialogBuilder;

public class EditTextDialog extends TagableDialog {
    private Context mContext;
    private EditText mText;
    private int mHeaderIconRes;
    private int mType;

    public EditTextDialog(Context context, int headerIconRes) {
        this(context, headerIconRes,
                (headerIconRes == R.drawable.ico_plus
                        ? DialogBuilder.OnAddMapItenContextButtonClickListener.PRO
                        : DialogBuilder.OnAddMapItenContextButtonClickListener.CON));
    }

    public EditTextDialog(Context context, int headerIconRes, int type) {
        super(context);
        mContext = context;
        mHeaderIconRes = headerIconRes;
        build();
        setIcon(headerIconRes);
//		DatePickerDialog
        mType = type;
    }


    private void build() {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setGravity(Gravity.TOP | Gravity.LEFT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(mContext);
        iv.setPadding(0, 15, 0, 15);
        iv.setImageDrawable(mContext.getResources().getDrawable(mHeaderIconRes));
        mText = new EditText(mContext);
        mText.setId(M.Constants.DIALOG_EDITTEXT_ID);
        mText.setMinLines(3);
        mText.setMaxLines(6);
        mText.setSingleLine(false);
        mText.setGravity(Gravity.TOP | Gravity.LEFT);
        mText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        ll.addView(iv);
        ll.addView(mText);

        setView(ll);
        setCancelable(false);

    }

    public void setText(String value) {
        mText.setText(value);
    }

    public String getText() {
        return mText.getText().toString();
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
