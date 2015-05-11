package com.quanzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanzi.R;
import com.quanzi.base.BaseV4Fragment;

/**
 *
 * 项目名称：quanzi  
 * 类名称：MainChatContactListFragment  
 * 类描述：聊天最近联系人列表
 * @author zhangshuai
 * @date 创建时间：2015-4-27 下午12:35:50 
 *
 */
public class MainChatContactListFragment extends BaseV4Fragment {
	private View rootView;// 根View

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_chat_contactlist, container, false);

		findViewById();// 初始化views
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
