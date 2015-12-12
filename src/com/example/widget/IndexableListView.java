package com.example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

public class IndexableListView extends ListView
{

	private boolean mIsFastScrollEnabled = false;// 是否允许快速滚动
	private IndexScroller mScroller = null;// 封装滚动条相关活动
	private GestureDetector mGestureDetector = null;// 手势检测

	public IndexableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public IndexableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public IndexableListView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean IsFastScrollEnable()
	{
		return mIsFastScrollEnabled;
	}

	public void setFastScrollEnabled(boolean enabled)
	{
		mIsFastScrollEnabled = enabled;
		if (mIsFastScrollEnabled)
		// 如果允许滚动
		{
			if (mScroller == null)
			{
				// 创建滚动条
				mScroller = new IndexScroller(getContext(), this);
				//              IndexScroller(Context context, IndexableListView lv)
			}
		} else
		{
			if (mScroller != null)
			{
				mScroller.hide();
				mScroller = null;
			}
		}
	}

	@Override
	// 用于绘制右侧索引条
	public void draw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.draw(canvas);
		if (mScroller != null)
			// 调用索引条的绘制方法
			mScroller.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (mScroller != null && mScroller.onTouchEvent(ev))
		{
			return true;
		}
		if (mGestureDetector == null)
		{
			mGestureDetector = new GestureDetector(getContext(),
					new GestureDetector.SimpleOnGestureListener()
					{

						@Override
						public boolean onFling(MotionEvent e1, MotionEvent e2,
								float velocityX, float velocityY)
						{
							mScroller.show();
							return super.onFling(e1, e2, velocityX, velocityY);
						}
				
					});
		}
		mGestureDetector.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
		if(mScroller!=null)
		{
			mScroller.setAdapter(adapter);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mScroller.onSizeChanged(w, h, oldw, oldh);
		super.onSizeChanged(w, h, oldw, oldh);
	}
}
