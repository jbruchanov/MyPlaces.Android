package com.scurab.android.myplaces.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.util.IntentHelper;

public class MapItemPanel extends LinearLayout implements View.OnClickListener {
    public interface OnMoreButtonClickListener {
        public void onClick(View source, MapItem item);
    }

    private View mContentView;
    private Context mContext;
    private ImageView mCloseView;
    private Animation mAnimationUp;
    private Animation mAnimationDown;
    private boolean mAnimations = true;
    private MapItem mMapItem;

    private TextView mNameTextView;
    private TextView mStreetTextView;
    private TextView mContactTextView;
    private ImageButton mWebImageButton;
    private ImageButton mContactImageButton;
    private ImageButton mShareImageButton;
    private ImageButton mCallImageButton;
    private ImageButton mMoreImageButton;
    private ImageView mIconImageView;
    private ImageButton mStreetViewButton;
    private OnMoreButtonClickListener mOnMoreButtonClickListener;

    public MapItemPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MapItemPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapItemPanel(Context context) {
        super(context);
        init();
    }

    private void init() {
        setVisibility(View.GONE);

        mContext = getContext();
        mContentView = View.inflate(mContext, R.layout.mapitempanel, this);

        mNameTextView = (TextView) mContentView.findViewById(R.id.tvName);
        mStreetTextView = (TextView) mContentView.findViewById(R.id.tvStreet);
        mContactTextView = (TextView) mContentView.findViewById(R.id.tvContact);
        mWebImageButton = (ImageButton) mContentView.findViewById(R.id.ibWeb);
        mContactImageButton = (ImageButton) mContentView.findViewById(R.id.ibPhone);
        mIconImageView = (ImageView) mContentView.findViewById(R.id.ivIcon);
        mShareImageButton = (ImageButton) mContentView.findViewById(R.id.ibShare);
        mCallImageButton = (ImageButton) mContentView.findViewById(R.id.ibPhone);
        mMoreImageButton = (ImageButton) mContentView.findViewById(R.id.ibMore);
        mStreetViewButton = (ImageButton) mContentView.findViewById(R.id.ibStreetView);
        mCloseView = (ImageView) findViewById(R.id.ivClose);
        mAnimationUp = AnimationUtils.loadAnimation(mContext, R.anim.scroll_down_def);
        mAnimationDown = AnimationUtils.loadAnimation(mContext, R.anim.scroll_up_def);

        bind();
    }

    private void bind() {
        mCloseView.setOnClickListener(this);
        mWebImageButton.setOnClickListener(this);
        mContactImageButton.setOnClickListener(this);
        mShareImageButton.setOnClickListener(this);
        mCallImageButton.setOnClickListener(this);
        mMoreImageButton.setOnClickListener(this);
        mStreetViewButton.setOnClickListener(this);

        mAnimationUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        mAnimationDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }
        });
    }

    public void show() {
        if (isVisible()) {
            return;
        }
        if (isAnimations()) {
            this.startAnimation(mAnimationUp);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        mMapItem = null;
        if (!isVisible()) {
            return;
        }
        if (isAnimations()) {
            this.startAnimation(mAnimationDown);
        } else {
            setVisibility(View.GONE);
        }

    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    @Override
    public void onClick(View v) {
        if (v == mCloseView) {
            hide();
        } else if (v == mWebImageButton) {
            IntentHelper.openWebLink(mContext, getMapItem().getWeb());
        } else if (v == mCallImageButton) {
            IntentHelper.callNumber(mContext, getMapItem().getContact());
        } else if (v == mShareImageButton) {
            IntentHelper.sendText(mContext, getMapItem().toString());
        } else if (v == mMoreImageButton) {
            if (mOnMoreButtonClickListener != null) {
                mOnMoreButtonClickListener.onClick(v, getMapItem());
            }
        } else if (v == mStreetViewButton) {
//             Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll=" + lat +"," + lon + "&cbp=1,180,,0,1.0"));
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mMapItem.getStreetViewUriLink()));
            try {
                mContext.startActivity(i);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, R.string.errInstallStreetViewFirst, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isAnimations() {
        return mAnimations;
    }

    public void setAnimations(boolean animations) {
        mAnimations = animations;
    }

    public void setMapItem(MapItem item) {
        mMapItem = item;
        getNameTextView().setText(item.getName());
        getStreetTextView().setText(item.getStreet());
        getContactTextView().setText(item.getContact());

        String web = item.getWeb();
        mWebImageButton.setEnabled(web != null && web.trim().length() > 0);

        String contact = item.getContact();
        mContactImageButton.setEnabled(contact != null && contact.trim().length() > 0);

        mIconImageView.setImageDrawable(getResources().getDrawable(item.getIconResId()));

        boolean showStreetView = mMapItem.getStreetViewLink() != null && mMapItem.getStreetViewLink().length() > 0;
        mStreetViewButton.setVisibility(showStreetView ? View.VISIBLE : View.GONE);
    }

    public TextView getNameTextView() {
        return mNameTextView;
    }

    public TextView getStreetTextView() {
        return mStreetTextView;
    }

    public TextView getContactTextView() {
        return mContactTextView;
    }

    public MapItem getMapItem() {
        return mMapItem;
    }

    public void setOnMoreButtonClickListener(OnMoreButtonClickListener onMoreButtonClickListener) {
        mOnMoreButtonClickListener = onMoreButtonClickListener;
    }

}
