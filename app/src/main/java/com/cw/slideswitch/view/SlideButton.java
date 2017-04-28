package com.cw.slideswitch.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.cw.slideswitch.R;


/**
 * 滑动按钮,可以控制打开关闭的背景,和滑块的图片
 *
 * @author Cw
 * @date 16/7/18
 */
public class SlideButton extends View {

    private Scroller mScroller;
    private Bitmap mBackground;
    private Bitmap mSlideButton;
    private boolean mIsOpen;
    private int mOpenBackground = R.drawable.widget_icon_slidebutton_yellow_bg;
    private int mCloseBackground = R.drawable.widget_icon_slidebutton_write_bg;
    private int mSlideImgId = R.drawable.widget_icon_slidebutton_write_slider;
    private int mSildLeft;
    private int mMaxLeft;
    private int mStartX;
    private int mMoveX;


    public SlideButton(Context context) {
        super(context);
        init();
    }

    public SlideButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideButton);
        mIsOpen = ta.getBoolean(R.styleable.SlideButton_isOpen, false);
        int openResourceId = ta.getResourceId(R.styleable.SlideButton_openBackground, -1);
        int closeResourceId = ta.getResourceId(R.styleable.SlideButton_closeBackground, -1);
        int slideImageId = ta.getResourceId(R.styleable.SlideButton_slideImage, -1);

        if (openResourceId != -1) {
            mOpenBackground = openResourceId;
        }
        if (closeResourceId != -1) {
            mCloseBackground = closeResourceId;
        }
        if (slideImageId != -1) {
            mSlideImgId = slideImageId;
        }
        init();
        ta.recycle();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBackground.getWidth(), mBackground.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawBitmap(mSlideButton, mSildLeft, 0, null);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mSildLeft = mScroller.getCurrX();
            invalidate();
        }
    }

    private void init() {
        setBackground(mIsOpen);
        setSlideImgId(mSlideImgId);
        //保证高度相同
        mSlideButton = ThumbnailUtils.extractThumbnail(mSlideButton, mBackground.getHeight(), mBackground.getHeight());
        mMaxLeft = mBackground.getWidth() - mSlideButton.getWidth();

        if (this.mIsOpen) {
            //如果是打开状态
            mSildLeft = mMaxLeft;
        } else {
            //如果是关闭状态
            mSildLeft = 0;
        }

        //点击改变开关状态
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOpen) {
                    mScroller.startScroll(mSildLeft, 0, -mSildLeft, 0, 500);
                    mIsOpen = false;
                } else {
                    mScroller.startScroll(mSildLeft, 0, mMaxLeft - mSildLeft, 0, 500);
                    mIsOpen = true;
                }
                setBackground(mIsOpen);
                invalidate();
                if (listener != null) {
                    listener.onButtonChange(SlideButton.this, mIsOpen);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isOnClick = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getX();
                //x 手指坐标改变的大小，有正负
                int diffX = endX - mStartX;
                //记录每次移动的距离
                mMoveX += diffX;
                //按钮左边距更具手指坐标改变而改变
                mSildLeft += diffX;
                //不能超过最大值
                if (mSildLeft > mMaxLeft) {
                    mSildLeft = mMaxLeft;
                } else if (mSildLeft < 0) {
                    mSildLeft = 0;
                }
                invalidate();
                mStartX = endX;
                break;
            case MotionEvent.ACTION_UP:
                //移动距离大于2就进行处理
                int moveX = Math.abs(this.mMoveX);
                if (moveX > 2) {
                    if (Math.abs(this.mMoveX) > mMaxLeft / 2) {
                        mIsOpen = !mIsOpen;
                    }
                    if (mIsOpen) {
                        mScroller.startScroll(mSildLeft, 0, mMaxLeft - mSildLeft, 0, 500);
                    } else {
                        mScroller.startScroll(mSildLeft, 0, -mSildLeft, 0, 500);
                    }
                    setBackground(mIsOpen);
                    invalidate();
                    if (listener != null) {
                        listener.onButtonChange(SlideButton.this, mIsOpen);
                    }
                    isOnClick = false;
                }
                this.mMoveX = 0;
                break;
        }
        //如果是点击事件就交给父类处理
        return !isOnClick || super.onTouchEvent(event);
    }

    /**
     * 设置背景
     */
    private void setBackground(boolean isOpen) {
        mBackground = BitmapFactory.decodeResource(getResources()
                , isOpen ? mOpenBackground : mCloseBackground);
    }

    /**
     * 设置滑块图片
     */
    private void setSlideImgId(int backgroundResours) {
        mSlideButton = BitmapFactory.decodeResource(getResources(), backgroundResours);
    }

    /**
     * 状态改变的回调
     */
    public OnSlideButtonChangeListener listener;

    public void setOnSlideButtonChangeListener(OnSlideButtonChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSlideButtonChangeListener {
        void onButtonChange(SlideButton view, boolean isOpen);
    }

}
