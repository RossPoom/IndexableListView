package com.example.util;


public class StringMatcher
{

	private StringMatcher()
	{
		// ��ֹ������ʵ����
	}

	public static boolean match(String value,String keyword)
	{
		//iΪvalueָ��  jΪkeywordָ��
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
