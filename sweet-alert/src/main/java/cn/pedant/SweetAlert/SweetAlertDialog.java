package cn.pedant.SweetAlert;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

public class SweetAlertDialog extends Dialog implements View.OnClickListener {
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mVendorTextView; //110618
    private TextView mModelTextView; //110618
    private TextView mOSTextView; //110618
    private TextView mImei1TextView; //110618
    private TextView mImei2TextView; //110618
    private TextView mUnduh; //241018
    private EditText mPassCode; //120618
    private GifImageView gifImageView; //120618
    private GifImageView gifSmallImageView; //260618
    private String mTitleText;
    private String mContentText;
    private String mVendorText; //110618
    private String mModelText; //110618
    private String mOSText; //110618
    private String mImei1Text; //110618
    private String mImei2Text; //110618
    private String mPassCodeText; //120618
    private String mUnduhText; //241018
    private LinearLayout layoutHome; //260618
    private LinearLayout layoutHandset; //110618
    private LinearLayout layoutButton; //260618
    private LinearLayout layoutUnduh; //241018
    private boolean mShowCancel;
    private boolean mShowContent;
    private boolean mShowTitle;
    private boolean mShowPasscode;
    private String mCancelText;
    private String mConfirmText;
    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mErrorLargeFrame; //300618
    private FrameLayout mSuccessFrame;
    private FrameLayout mSuccessLargeFrame; //130618
    private FrameLayout mProgressFrame;
    private FrameLayout mNewProgressFrame; //130618
    private FrameLayout mNewProgressSmallFrame; //260618
    private SuccessTickView mSuccessTick;
    private SuccessTickLargeView mSuccessLargeTick; //130618
    private ImageView mErrorX;
    private ImageView mErrorLargeX; //300618
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private View mSuccessLargeLeftMask; //130618
    private View mSuccessLargeRightMask; //130618
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private Button mConfirmButton;
    private Button mCancelButton;
    private ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;

    private Context mContext; //300618

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int UPDATE_TYPE = 6; //110618
    public static final int HANDSET_TYPE = 7; //110618
    public static final int UPDATE_PASSWORD = 8; //120618
    public static final int LOADING_TYPE = 9; //130618
    public static final int SUCCESS_LARGE_TYPE = 10; //130618
    public static final int LOADING_SMALL_TYPE = 11; //130618
    public static final int ERROR_LARGE_TYPE = 12; //300618
    public static final int CONFIRM_TYPE = 13; //140718
    public static final int UNDUH_TYPE = 14; //241018

    Typeface fonts, fontsBold;

    public static interface OnSweetClickListener {
        public void onClick (SweetAlertDialog sweetAlertDialog);
    }

    public SweetAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public SweetAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        this.mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet)OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            List<Animation> childAnims = mErrorXInAnim.getAnimations();
            int idx = 0;
            for (;idx < childAnims.size();idx++) {
                if (childAnims.get(idx) instanceof AlphaAnimation) {
                    break;
                }
            }
            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet)OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            SweetAlertDialog.super.cancel();
                        } else {
                            SweetAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        /* Custom fonts */
        fonts               = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsBold           = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/OpenSans-Bold.ttf");

        mDialogView         = getWindow().getDecorView().findViewById(android.R.id.content);
        layoutHome          = findViewById(R.id.loading); //110618
        layoutHandset       = findViewById(R.id.layoutHandset); //110618
        layoutButton        = findViewById(R.id.layoutButton); //260618
        layoutUnduh         = findViewById(R.id.layoutUnduh); //241018
        mTitleTextView      = (TextView)findViewById(R.id.title_text);
        mContentTextView    = (TextView)findViewById(R.id.content_text);
        mVendorTextView     = (TextView)findViewById(R.id.vendor_text); //110618
        mModelTextView      = (TextView)findViewById(R.id.model_text); //110618
        mOSTextView         = (TextView)findViewById(R.id.os_text); //110618
        mImei1TextView      = (TextView)findViewById(R.id.imei1_text); //110618
        mImei2TextView      = (TextView)findViewById(R.id.imei2_text); //110618
        mUnduh              = (TextView)findViewById(R.id.unduh_text); //241018
        mPassCode           = (EditText) findViewById(R.id.etPasscode); //120618
        gifImageView        = (GifImageView) findViewById(R.id.GifImageView); //120618
        gifSmallImageView   = (GifImageView) findViewById(R.id.GifSmallImageView); //120618
        mErrorFrame         = (FrameLayout)findViewById(R.id.error_frame);
        mErrorLargeFrame    = (FrameLayout)findViewById(R.id.error_large_frame); //300618
        mErrorX             = (ImageView)mErrorFrame.findViewById(R.id.error_x);
        mErrorLargeX        = (ImageView)mErrorLargeFrame.findViewById(R.id.error_large_x); //300618
        mSuccessFrame       = (FrameLayout)findViewById(R.id.success_frame);
        mSuccessLargeFrame  = (FrameLayout)findViewById(R.id.successlarge_frame); //130618
        mProgressFrame      = (FrameLayout)findViewById(R.id.progress_dialog);
        mNewProgressFrame   = (FrameLayout)findViewById(R.id.loading_dialog); //130618
        mNewProgressSmallFrame   = (FrameLayout)findViewById(R.id.loading_small_dialog); //260618
        mSuccessTick        = (SuccessTickView)mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLargeTick   = (SuccessTickLargeView)mSuccessLargeFrame.findViewById(R.id.successlarge_tick); //130618
        mSuccessLeftMask    = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask   = mSuccessFrame.findViewById(R.id.mask_right);
        mSuccessLargeLeftMask   = mSuccessLargeFrame.findViewById(R.id.masklarge_left); //130618
        mSuccessLargeRightMask  = mSuccessLargeFrame.findViewById(R.id.masklarge_right); //130618
        mCustomImage        = (ImageView)findViewById(R.id.custom_image);
        mWarningFrame       = (FrameLayout)findViewById(R.id.warning_frame);
        mConfirmButton      = (Button)findViewById(R.id.confirm_button);
        mCancelButton       = (Button)findViewById(R.id.cancel_button);

        mProgressHelper.setProgressWheel((ProgressWheel)findViewById(R.id.progressWheel));
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setUnduhText(mUnduhText); //241018
        setContentText(mContentText);
        setVendorText(mVendorText);
        setModelText(mModelText);
        setOSText(mOSText);
        setImei1Text(mImei1Text);
        setImei2Text(mImei2Text);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);

        mContentTextView.setTypeface(fonts);
        mCancelButton.setTypeface(fonts);
        mConfirmButton.setTypeface(fonts);
        mUnduh.setTypeface(fonts);
        mPassCode.setTypeface(fonts);

        gifImageView.setGifImageResource(R.drawable.spinner);
        gifSmallImageView.setGifImageResource(R.drawable.spinner_small);


        mPassCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Toast.makeText(mContext, "Done", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

    }

    private void restore () {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mErrorLargeFrame.setVisibility(View.GONE); //300618
        mSuccessFrame.setVisibility(View.GONE);
        mSuccessLargeFrame.setVisibility(View.GONE); //130618
        mWarningFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.GONE);
        mNewProgressFrame.setVisibility(View.GONE); //130618
        mNewProgressSmallFrame.setVisibility(View.GONE); //130618
        mConfirmButton.setVisibility(View.VISIBLE);

        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        mErrorFrame.clearAnimation();
        mErrorLargeFrame.clearAnimation(); //300618
        mErrorX.clearAnimation();
        mErrorLargeX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
        mSuccessLargeTick.clearAnimation(); //130618
        mSuccessLargeLeftMask.clearAnimation(); //130618
        mSuccessLargeRightMask.clearAnimation(); //130618
    }

    private void playAnimation () {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        } else if (mAlertType == SUCCESS_LARGE_TYPE) {
            mSuccessLargeTick.startTickAnim();
            mSuccessLargeRightMask.startAnimation(mSuccessBowAnim);
        } else if (mAlertType == ERROR_LARGE_TYPE) { //300618
            mErrorLargeFrame.startAnimation(mErrorInAnim); //300618
            mErrorLargeX.startAnimation(mErrorXInAnim); //300618
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            switch (mAlertType) {
                case ERROR_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    // initial rotate layout of success mask
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case WARNING_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mWarningFrame.setVisibility(View.GONE);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    break;
                case UPDATE_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mConfirmButton.setBackgroundResource(R.drawable.completeblue_button_background);
                    mWarningFrame.setVisibility(View.GONE);
                    break;
                case HANDSET_TYPE:
                    ViewGroup.LayoutParams paramsHandset = layoutHome.getLayoutParams();
                    // Changes the height and width to the specified *pixels*
                    paramsHandset.width = dpToPx(300);
                    layoutHome.setLayoutParams(paramsHandset);
                    mTitleTextView.setTypeface(fonts);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    layoutHandset.setVisibility(View.VISIBLE);
                    mWarningFrame.setVisibility(View.GONE);
                    break;
                case UPDATE_PASSWORD:
                    mTitleTextView.setTypeface(fonts);
                    mConfirmButton.setBackgroundResource(R.drawable.completeblue_button_background);
                    mPassCode.setVisibility(View.VISIBLE);
                    mWarningFrame.setVisibility(View.GONE);
                    break;
                case LOADING_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mNewProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    break;
                case SUCCESS_LARGE_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mSuccessLargeFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    // initial rotate layout of success mask
                    mSuccessLargeLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessLargeRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case LOADING_SMALL_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    ViewGroup.LayoutParams params = layoutHome.getLayoutParams();
                    // Changes the height and width to the specified *pixels*
                    params.height = 150;
                    params.width = 150;
                    layoutHome.setLayoutParams(params);
                    mNewProgressSmallFrame.setVisibility(View.VISIBLE);
                    mTitleTextView.setVisibility(View.GONE);
                    layoutButton.setVisibility(View.GONE);
                    break;
                case ERROR_LARGE_TYPE:
                    mTitleTextView.setTypeface(fonts);
                    mErrorLargeFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    break;
                case CONFIRM_TYPE:
                    mTitleTextView.setTypeface(fontsBold);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    break;
                case UNDUH_TYPE: //241018
                    ViewGroup.LayoutParams paramsUnduh = layoutHome.getLayoutParams();
                    // Changes the height and width to the specified *pixels*
                    paramsUnduh.width = dpToPx(300);
                    layoutHome.setLayoutParams(paramsUnduh);
                    mTitleTextView.setTypeface(fontsBold);
                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    layoutUnduh.setVisibility(View.VISIBLE);

                    mWarningFrame.setVisibility(View.GONE);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public int getAlerType () {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }

    public String getPassCode () {
        mPassCodeText = mPassCode.getText().toString();
        return mPassCodeText;
    }

    public void hidePasscode(){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPassCode.getWindowToken(), 0);
    }

    public String getTitleText () {
        return mTitleText;
    }

    public SweetAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    //241018
    public SweetAlertDialog setUnduhText (String text) {
        mUnduhText = text;
        if (mUnduh != null && mUnduhText != null) {
            mUnduh.setText(mUnduhText);
        }
        return this;
    }

    public SweetAlertDialog setCustomImage (Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public SweetAlertDialog setCustomImage (int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    public String getContentText () {
        return mContentText;
    }

    public SweetAlertDialog setContentText (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public String getVendorText () { return mVendorText; } //110618

    public SweetAlertDialog setVendorText (String text) { //110618
        mVendorText = text;
        if (mVendorTextView != null && mVendorText != null) {
            mVendorTextView.setText(mVendorText);
        }
        return this;
    }

    public String getModelText () { return mModelText; } //110618

    public SweetAlertDialog setModelText (String text) { //110618
        mModelText = text;
        if (mModelTextView != null && mModelText != null) {
            mModelTextView.setText(mModelText);
        }
        return this;
    }

    public String getOSText () { return mOSText; } //110618

    public SweetAlertDialog setOSText (String text) { //110618
        mOSText = text;
        if (mOSTextView != null && mOSText != null) {
            mOSTextView.setText(mOSText);
        }
        return this;
    }

    public String getImei1Text () { return mImei1Text; } //110618

    public SweetAlertDialog setImei1Text (String text) { //110618
        mImei1Text = text;
        if (mImei1TextView != null && mImei1Text != null) {
            mImei1TextView.setText(mImei1Text);
        }
        return this;
    }

    public String getImei2Text () { return mImei2Text; } //110618

    public SweetAlertDialog setImei2Text (String text) { //110618
        mImei2Text = text;
        if (mImei2TextView != null && mImei2Text != null) {
            mImei2TextView.setText(mImei2Text);
        }
        return this;
    }

    public boolean isShowPasscode () {
        return mShowPasscode;
    }

    public SweetAlertDialog showPasscode (boolean isShow) {
        mShowPasscode = isShow;
        if (mShowPasscode) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return this;
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public SweetAlertDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText () {
        return mShowContent;
    }

    public SweetAlertDialog showContentText (boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowTitleText () {
        return mShowTitle;
    }

    public SweetAlertDialog showTitleText (boolean isShow) {
        mShowTitle = isShow;
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(mShowTitle ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText () {
        return mCancelText;
    }

    public SweetAlertDialog setCancelText (String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public SweetAlertDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public SweetAlertDialog setCancelClickListener (OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public SweetAlertDialog setConfirmClickListener (OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressHelper getProgressHelper () {
        return mProgressHelper;
    }

    public class MyPasswordTransformationMethod extends PasswordTransformationMethod {

        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {

            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }

            public char charAt(int index) {
                return 11044;
            }

            public int length() {
                return mSource.length();
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = mContext.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}