package com.example.indexablelistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.util.StringMatcher;
import com.example.widget.IndexableListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

public class IndexableListViewActivity extends Activity
{

	private ArrayList<String> mItems;// 存储所有数据
	private IndexableListView mListView;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		 * 1.初始化items 2.根据sections获得position
		 */
		mItems = new ArrayList<String>();
		mItems.add("aaaa");// 添加数据
		mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
		mItems.add("Steve Jobs");
		mItems.add("Inheritance (The Inheritance Cycle)");
		mItems.add("11/22/63: A Novel");
		mItems.add("The Hunger Games");
		mItems.add("The LEGO Ideas Book");
		mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
		mItems.add("Catching Fire (The Second Book of the Hunger Games)");
		mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
		mItems.add("Death Comes to Pemberley");
		mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
		mItems.add("Steve Jobs");
		mItems.add("Inheritance (The Inheritance Cycle)");
		mItems.add("11/22/63: A Novel");
		mItems.add("The Hunger Games");
		mItems.add("The LEGO Ideas Book");
		mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
		mItems.add("Catching Fire (The Second Book of the Hunger Games)");
		mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
		mItems.add("做作");
		mItems.add("wokao");
		Collections.sort(mItems);

		ContentAdapter adapter = new ContentAdapter(this,
				android.R.layout.simple_list_item_1, mItems);
		mListView = (IndexableListView) findViewById(R.id.listview);  
        mListView.setAdapter(adapter);  
        mListView.setFastScrollEnabled(true);
        //数据库测试代码
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this, "BookStore2.db", null, 1);
        dbHelper.getWritableDatabase();
	}

	private class ContentAdapter extends ArrayAdapter<String> implements
			SectionIndexer
	{
		private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public ContentAdapter(Context context, int resource,
				List<String> objects)
		{
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object[] getSections()
		{
			String[] sections = new String[mSections.length()];
			// 将mSections每一个section放到sections中
			for (int i = 0; i < mSections.length(); i++)
			{
				// 将每一个字符都转换成字符串存进sections
				sections[i] = String.valueOf(mSections.charAt(i));
			}
			return sections;
		}

		@Override
		public int getPositionForSection(int sectionIndex)
		{
			// 从当前索引项查起如果没有则查询上一个索引项，如果都没有则不作任何动作
			for (int i = sectionIndex; i >= 0; i--)
			{
				for (int j = 0; j < getCount(); j++)
				{
					// 查询数字
					if (j == 0)
					{
						for (int k = 0; k <= 9; k++)
							if (StringMatcher.match(
									String.valueOf(getItem(j).charAt(0)),
									String.valueOf(k)))
								return j;
					}
					// 查询字母
					else
					{
						if (StringMatcher.match(
								String.valueOf(getItem(j).charAt(0)),
								String.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
