package com.quanzi.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

import com.quanzi.R;

/**
 *
 * 项目名称：quanzi  
 * 类名称：ScreenDialogFragment  
 * 类描述：在探索页面进行用户筛选
 * @author zhangshuai
 * @date 创建时间：2015-4-29 下午11:37:45 
 *
 */
//public class ScreenDialogFragment extends DialogFragment implements OnClickListener {
//
//	private View rootView;
//
//	private RadioButton allGenderBtn;//性别全部
//	private RadioButton genderMaleBtn;//男性
//	private RadioButton genderFemaleBtn;//女性
//	private RadioButton allStateBtn;//状态全部
//	private RadioButton stateSingleBtn;//单身
//	private TextView confirmBtn;//确定
//	private TextView cancleBtn;//取消
//
//	/**
//	 * 创建实例
//	 * @return
//	 */
//	static ScreenDialogFragment newInstance() {
//		ScreenDialogFragment f = new ScreenDialogFragment();
//		return f;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		rootView = inflater.inflate(R.layout.fragment_dialog_screen, container, false);
//		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//		findViewById();
//		initView();
//		return rootView;
//	}
//
//	private void findViewById() {
//		allGenderBtn = (RadioButton) rootView.findViewById(R.id.all_gender);
//		genderMaleBtn = (RadioButton) rootView.findViewById(R.id.gender_male);
//		genderFemaleBtn = (RadioButton) rootView.findViewById(R.id.gender_female);
//		allStateBtn = (RadioButton) rootView.findViewById(R.id.all_statue);
//		stateSingleBtn = (RadioButton) rootView.findViewById(R.id.single);
//		confirmBtn = (TextView) rootView.findViewById(R.id.confirm);
//		cancleBtn = (TextView) rootView.findViewById(R.id.cancle);
//	}
//
//	private void initView() {
//		allGenderBtn.setOnClickListener(this);
//		genderMaleBtn.setOnClickListener(this);
//		genderFemaleBtn.setOnClickListener(this);
//		allStateBtn.setOnClickListener(this);
//		stateSingleBtn.setOnClickListener(this);
//		confirmBtn.setOnClickListener(this);
//		cancleBtn.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.confirm:
//			ScreenDialogFragment.this.dismiss();
//			break;
//		case R.id.cancle:
//			ScreenDialogFragment.this.dismiss();
//			break;
//		default:
//			break;
//		}
//	}
//}
