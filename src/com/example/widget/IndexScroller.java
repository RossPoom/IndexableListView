package com.example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class IndexScroller
{
	private float mIndexBarWidth;/* 索引条宽度 */
	private float mIndexBarMargin;/* 索引条到右边的距离 */
	private float mPreviewPadding;/* 中央显示的字母到边框距离 */
	private float mDensity;/* 当前屏幕密度除以160 */
	private float mScaledDensity;/* 当前字体密度除以160 */
	private float mAlphaRate;/* 索引条透明度 */
	private int mState = STATE_HIDDEN;/* 索引条状态 */
	private int mListViewWidth;
	private int mListViewHeight;
	private int mCurrentSection = -1;/* 索引条当前位置 */
	private boolean mIsIndexing = false;
	private ListView mListView = null;
	private SectionIndexer mIndexer = null;
	private String[] mSections = null;
	private RectF mIndexBarRect;

	private static final int STATE_HIDDEN = 0;
	private static final int STATE_SHOWING = 1;
	private static final int STATE_SHOWN = 2;
	private static final int STATE_HIDING = 3;

	public IndexScroller(Context context, ListView lv)
	{
		mDensity = context.getResources().getDisplayMetrics().density;
		mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		mListView = lv;
		setAdapter(mListView.getAdapter());
		mIndexBarWidth = 20 * mDensity;/* 根据屏幕密度计算索引条宽度 */
		mIndexBarMargin = 10 * mDensity;/* 根据屏幕密度计算索引条距右边框宽度 */
		mPreviewPadding = 5 * mDensity;/* 根据屏幕密度计算中心字母到中心框四周的距离 */
	}

	public void setAdapter(Adapter adapter)
	{
		if (adapter instanceof SectionIndexer)
		{
			mIndexer = (SectionIndexer) adapter;
			mSections = (String[]) mIndexer.getSections();
		}
	}

	public void draw(Canvas canvas)
	{
		// 1.绘制索引条文本和背景
		// 2.绘制预览框文本及背景
		// 如果索引条状态为隐藏则不进行绘制
		if (mState == STATE_HIDDEN)
			return;
		// 绘制索引条背景
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.BLACK);
		indexbarPaint.setAlpha((int) (64 * mAlphaRate));
		canvas.drawRoundRect(mIndexBarRect, 5 * mDensity, 5 * mDensity,
				indexbarPaint);
		// 绘制索引条文本Sections
		if (mSections != null && mSections.length > 0)
		{
			if (mCurrentSection >= 0)
			{
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.BLACK);
				previewPaint.setAlpha(96);
				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.WHITE);
				previewTextPaint.setTextSize(50 * mScaledDensity);
				float previewTextWidth = previewTextPaint
						.measureText(mSections[mCurrentSection]);
				float previewSize = 2 * mPreviewPadding
						+ previewTextPaint.descent()
						- previewTextPaint.ascent();
				RectF previewRect = new RectF(
						(mListViewWidth - previewSize) / 2,
						(mListViewHeight - previewSize) / 2,
						(mListViewWidth + previewSize) / 2,
						(mListViewHeight + previewSize) / 2);
				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity,
						previewPaint);
				canvas.drawText(
						mSections[mCurrentSection],
						previewRect.left + (previewSize - previewTextWidth) / 2
								- 1,
						previewRect.top + mPreviewPadding
								- previewTextPaint.ascent() + 1,
						previewTextPaint);
			}

		}
		Paint indexTextPaint = new Paint();
		indexTextPaint.setColor(Color.WHITE);
		indexTextPaint.setAlpha((int) (255 * mAlphaRate));
		indexTextPaint.setTextSize(12 * mScaledDensity);
		float sectionHeight = (mIndexBarRect.height() - 2 * mIndexBarMargin)
				/ mSections.length;
		float paddingTop = (sectionHeight - (indexTextPaint.descent() - indexTextPaint
				.ascent())) / 2;

		for (int i = 0; i < mSections.length; i++)
		{
			float paddingLeft = (mIndexBarWidth - indexTextPaint
					.measureText(mSections[i])) / 2;
			canvas.drawText(mSections[i], mIndexBarRect.left + paddingLeft,
					mIndexBarRect.top + mIndexBarMargin + sectionHeight * i
							+ paddingTop - indexTextPaint.ascent(),
					indexTextPaint);
		}

	}

	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mListViewWidth = w;
		mListViewHeight = h;
		Log.d("old", w+"+"+h+" mlistviewwidth="+mListViewWidth+" mlistviewheight="+mListViewHeight);
		mIndexBarRect = new RectF(mListViewWidth - mIndexBarWidth
				- mIndexBarMargin, mIndexBarMargin, w - mIndexBarMargin, h
				- mIndexBarMargin);
	}
	
	private void fade(long delay)
	{
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageAtTime(0,SystemClock.uptimeMillis()+delay);
	}
	
	private void setState(int state)
	{
		if(state<STATE_HIDDEN||state>STATE_HIDING)
			return;
		mState=state;
		switch (mState)
		{
		case STATE_HIDDEN:
			mHandler.removeMessages(0);
		case STATE_SHOWING:
			mAlphaRate=0;
			fade(0);
			break;
		case STATE_SHOWN:
			mHandler.removeMessages(0);
			break;
		case STATE_HIDING:
			mAlphaRate=1;
			fade(3000);
			break;
		default:
			break;
		}
	}
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (mState)
			{
			case STATE_SHOWING:
				mAlphaRate+=(1-mAlphaRate)*0.2;
				if(mAlphaRate>=0.9)
				{
					mAlphaRate=1;
					setState(STATE_SHOWN);
				}
				mListView.invalidate();
				fade(10);
				break;
			case STATE_SHOWN:
				setState(STATE_HIDING);
				break;
			case STATE_HIDING:
				mAlphaRate-=mAlphaRate*0.2;
				if(mAlphaRate>=0.9)
				{
					mAlphaRate=0;
					setState(STATE_HIDDEN);
				}
				mListView.invalidate();
				fade(10);
				break;
			default:
				break;
			}
		}
	};
	
	public boolean onTouchEvent(MotionEvent ev) {  
        switch (ev.getAction()) {  
        case MotionEvent.ACTION_DOWN: // 按下，开始索引  
            // If down event occurs inside index bar region, start indexing  
            if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {  
                setState(STATE_SHOWN);  
  
                // It demonstrates that the motion event started from index bar  
                mIsIndexing = true;  
                // Determine which section the point is in, and move the list to  
                // that section  
                mCurrentSection = getSectionByPoint(ev.getY()); 
                Log.d("old", mCurrentSection+"");
                mListView.setSelection(mIndexer  
                        .getPositionForSection(mCurrentSection));  
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_MOVE: // 移动  
            if (mIsIndexing) {  
                // If this event moves inside index bar  
                if (contains(ev.getX(), ev.getY())) {  
                    // Determine which section the point is in, and move the  
                    // list to that section  
                    mCurrentSection = getSectionByPoint(ev.getY());  
                    mListView.setSelection(mIndexer  
                            .getPositionForSection(mCurrentSection));  
                }  
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_UP: // 抬起  
            if (mIsIndexing) {  
                mIsIndexing = false;  
                mCurrentSection = -1;  
            }  
            if (mState == STATE_SHOWN)  
                setState(STATE_HIDING);  
            break;  
        }  
        return false;  
    }  
	// 显示  
    public void show() {  
        if (mState == STATE_HIDDEN)  
            setState(STATE_SHOWING);  
        else if (mState == STATE_HIDING)  
            setState(STATE_HIDING);  
    }  
  
    // 隐藏  
    public void hide() {  
        if (mState == STATE_SHOWN)  
            setState(STATE_HIDING);  
    }  
    
    private boolean contains(float x, float y) {  
        // Determine if the point is in index bar region, which includes the  
        // right margin of the bar  
        return (x >= mIndexBarRect.left && y >= mIndexBarRect.top && y <= mIndexBarRect.top  
                + mIndexBarRect.height());  
    }  
  
    private int getSectionByPoint(float y) {  
        if (mSections == null || mSections.length == 0)  
            return 0;  
        if (y < mIndexBarRect.top + mIndexBarMargin)  
            return 0;  
        if (y >= mIndexBarRect.top + mIndexBarRect.height() - mIndexBarMargin)  
            return mSections.length - 1;  
        return (int) ((y - mIndexBarRect.top - mIndexBarMargin) / ((mIndexBarRect  
                .height() - 2 * mIndexBarMargin) / mSections.length));  
    }  
}
