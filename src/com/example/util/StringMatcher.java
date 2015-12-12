package com.example.util;


public class StringMatcher
{

	private StringMatcher()
	{
		// 禁止工具类实例化
	}

	public static boolean match(String value,String keyword)
	{
		//i为value指针  j为keyword指针
		int i = 0, j = 0;
		if(value == null || keyword == null || keyword.length() > value.length())
			return false;
		do
		{
			if(keyword.charAt(j) == value.charAt(i))
			{
				i++;
				j++;
			}
			//else if(j > 0) break;
			//else i++;
			else break;
		} while (i < value.length()&&j<keyword.length());
		
		return j == keyword.length();
		
	}
}
