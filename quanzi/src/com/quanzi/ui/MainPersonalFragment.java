package com.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseV4Fragment;

/**
 *
 * 项目名称：quanquan  
 * 类名称：MainPernalFragment  
 * 类描述：个人中心页面
 * @author zhangshuai
 * @date 创建时间：2015-4-12 下午8:56:26 
 *
 */
public class MainPersonalFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// 根View
	private TextView leftTextView;//导航栏左侧文字
	private View personImageView;//个人头像
	private View circleView;//圈子
	private View followerView;//追随者
	private View favourView;//赞
	private View persondataView;//资料
	private View settingView;//设置
	private ImageView headImageView;//头像

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_mainpersonal, container, false);

		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) rootView.findViewById(R.id.left_text1);
		personImageView = rootView.findViewById(R.id.person_image);
		circleView = rootView.findViewById(R.id.circle);
		followerView = rootView.findViewById(R.id.follower);
		favourView = rootView.findViewById(R.id.favour);
		persondataView = rootView.findViewById(R.id.person_data);
		settingView = rootView.findViewById(R.id.setting);
		headImageView = (ImageView) rootView.findViewById(R.id.head_image);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("个人");
		personImageView.setOnClickListener(this);
		circleView.setOnClickListener(this);
		followerView.setOnClickListener(this);
		favourView.setOnClickListener(this);
		persondataView.setOnClickListener(this);
		settingView.setOnClickListener(this);
		headImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.head_image://放大头像
			Intent intent = new Intent(getActivity(), ImageShowerActivity.class);
			//			intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, AsyncHttpClientImageSound
			//					.getAbsoluteUrl(singleTimeCapsuleList.get(position).getStc_photo()));
			intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, "drawable://" + R.drawable.headimage);

			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			break;
		case R.id.circle:
			startActivity(new Intent(getActivity(), MyQuanziActivity.class));
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.follower:
			startActivity(new Intent(getActivity(), MyFollowerActivity.class));
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.favour:
			startActivity(new Intent(getActivity(), MyFavoredActivity.class));
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.person_data:
			startActivity(new Intent(getActivity(), ModifyDataActivity.class));
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.setting:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.person_image:
			startActivity(new Intent(getActivity(), MyPersonDetailActivity.class));
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		default:
			break;
		}
	}

}
