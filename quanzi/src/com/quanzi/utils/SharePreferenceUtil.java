package com.quanzi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �����ƣ�SharePreferenceUtil 
 * ��������SharedPreferences��һ�������࣬����setParam���ܱ���String,
 * Integer, Boolean, Float, Long���͵Ĳ��� ͬ������getParam���ܻ�ȡ���������ֻ���������� 
 * �����ˣ� ��˧
 * ����ʱ�䣺2015��4��13�� ����9:00:37
 * 
 */
public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static final String USE_COUNT = "count";// ��¼���ʹ�ô���

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// ��¼���ʹ�ô���
	public int getUseCount() {
		return sp.getInt("count", 0);
	}

	public void setUseCount(int count) {
		editor.putInt("count", count);
		editor.commit();
	}

}
