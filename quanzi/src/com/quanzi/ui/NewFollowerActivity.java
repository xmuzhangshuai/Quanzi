package com.quanzi.ui;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.utils.DateTimeTools;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�NewFollowerActivity  
 * ���������µ�׷����ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-30 ����2:12:50 
 *
 */
public class NewFollowerActivity extends BaseActivity {

	/***********VIEWS************/
	private ListView myQuanziListView;
	private TextView leftTextView;//�������������
	private View leftButton;//��������ఴť

	private String[] names = new String[] { "��˧", "��ΰǿ", "���ط�", "����" };
	private int[] headimages = new int[] { R.drawable.headimage, R.drawable.headimage1, R.drawable.headimage2,
			R.drawable.headimage3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_quanzi);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		myQuanziListView = (ListView) findViewById(R.id.myquanzi_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("�µ�׷����");
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});

		myQuanziListView.setAdapter(new QuaiziAdapter());
	}

	/**
	 *
	 * ��Ŀ���ƣ�quanzi  
	 * �����ƣ�MyFavorsAdapter  
	 * ��������
	 * @author zhangshuai
	 * @date ����ʱ�䣺2015-4-29 ����2:37:23 
	 *
	 */
	private class QuaiziAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
			public CheckBox concernBtn;
			public TextView timeTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final String name = names[position];
			if (name == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(NewFollowerActivity.this).inflate(R.layout.new_follower_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.concernBtn = (CheckBox) view.findViewById(R.id.concern_btn);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.headImageView.setImageResource(headimages[position]);
			holder.nameTextView.setText(name);
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(new Date()));

			return view;
		}
	}

}
