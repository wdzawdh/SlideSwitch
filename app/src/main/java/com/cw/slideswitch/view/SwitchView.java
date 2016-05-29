package com.cw.slideswitch.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cw.slideswitch.R;


public class SwitchView extends View {

    private Bitmap background;
    private Bitmap slideButton;
    private int mSildLeft;
    private boolean isOpen;
    private int MAX_LEFT;
    private int startX;
    private int moveX;


    public SwitchView(Context context) {
        super(context);
        init();
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.sildBut);
        isOpen = ta.getBoolean(R.styleable.sildBut_isOpen, false);

        if (this.isOpen) {
            //如果是打开状态
            mSildLeft = MAX_LEFT;
        } else {
            //如果是关闭状态
            mSildLeft = 0;
        }
        int slidbitmapId = ta.getResourceId(R.styleable.sildBut_isOpen, -1);
        if (slidbitmapId > -1) {
            slideButton = BitmapFactory.decodeResource(getResources(), slidbitmapId);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(background.getWidth(), background.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(slideButton, mSildLeft, 0, null);
    }

    private void init() {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slideButton = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button_background);
        slideButton = ThumbnailUtils.extractThumbnail(slideButton, background.getHeight(), background.getHeight());
        mSildLeft = MAX_LEFT;
        MAX_LEFT = background.getWidth() - slideButton.getWidth();

		/*
         * 点击改变开关状态
		 */
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    mSildLeft = 0;
                    isOpen = false;
                } else {
                    mSildLeft = MAX_LEFT;
                    isOpen = true;
                }
                invalidate();
                if (listener != null) {
                    listener.onButtonChange(SwitchView.this, isOpen);
                }
            }
        });
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public void setBackground(int backgroundResours) {
        this.background = BitmapFactory.decodeResource(getResources(), backgroundResours);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isOnClick = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getX();
                //x 手指坐标改变的大小，有正负
                int diffX = endX - startX;
                //记录每次移动的距离
                moveX += diffX;
                //按钮左边距更具手指坐标改变而改变
                mSildLeft += diffX;
                if (mSildLeft > MAX_LEFT) {
                    mSildLeft = MAX_LEFT;
                } else if (mSildLeft < 0) {
                    mSildLeft = 0;
                }
                invalidate();
                startX = endX;
                break;
            case MotionEvent.ACTION_UP:
                //移动距离大于1就进行处理
                int moveX = Math.abs(this.moveX);
                Log.d("moveX", moveX + "");
                if (moveX > 1) {
                    if (Math.abs(this.moveX) > MAX_LEFT / 2) {
                        isOpen = !isOpen;
                        mSildLeft = isOpen ? MAX_LEFT : 0;
                        invalidate();
                    } else {
                        mSildLeft = isOpen ? MAX_LEFT : 0;
                        invalidate();
                    }
                    if (listener != null) {
                        listener.onButtonChange(SwitchView.this, isOpen);
                    }
                    isOnClick = false;
                }
                this.moveX = 0;
                break;
        }
        //如果是点击事件就交给父类处理
        return !isOnClick || super.onTouchEvent(event);
    }

    /**
     * 状态改变的回调
     */
    public OnSlideButtonChangeListener listener;

    public void setOnSlideButtonChangeListener(OnSlideButtonChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSlideButtonChangeListener {
        public abstract void onButtonChange(SwitchView view, boolean isOpen);
    }
}
