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
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�MainPernalFragment  
 * ����������������ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-12 ����8:56:26 
 *
 */
public class MainPersonalFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// ��View
	private TextView leftTextView;//�������������
	private View personImageView;//����ͷ��
	private View circleView;//Ȧ��
	private View followerView;//׷����
	private View favourView;//��
	private View persondataView;//����
	private View settingView;//����
	private ImageView headImageView;//ͷ��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_mainpersonal, container, false);

		findViewById();// ��ʼ��views
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
		leftTextView.setText("����");
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
		case R.id.head_image://�Ŵ�ͷ��
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
