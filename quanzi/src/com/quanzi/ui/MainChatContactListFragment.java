package com.quanzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanzi.R;
import com.quanzi.base.BaseV4Fragment;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�MainChatContactListFragment  
 * �����������������ϵ���б�
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-27 ����12:35:50 
 *
 */
public class MainChatContactListFragment extends BaseV4Fragment {
	private View rootView;// ��View

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_chat_contactlist, container, false);

		findViewById();// ��ʼ��views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

}
