package com.quanzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�ModifyIndustryActivity  
 * ���������޸���ҵ
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-30 ����6:45:45 
 *
 */
public class ModifyIndustryActivity extends BaseActivity {
	/***********VIEWS************/
	private ListView industryListView;
	private TextView leftTextView;//�������������
	private View leftButton;//��������ఴť

	private String[] industry = new String[] { "����", "���򿪷�", "����", "����", "������", "��Ӱ", "ҽ��" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_industry);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		industryListView = (ListView) findViewById(R.id.myquanzi_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("������ҵ");
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});

		industryListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
				industry));

		int position = industryListView.getCheckedItemPosition(); // ����ȡѡ��λ��  
		if (ListView.INVALID_POSITION != position) {
			Toast.makeText(ModifyIndustryActivity.this, industry[position], 0).show();
		}
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
			public TextView nameTextView;
			public TextView countTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return industry.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return industry[position];
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
			final String name = industry[position];
			if (name == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(ModifyIndustryActivity.this).inflate(R.layout.simple_list_item, null);
				holder = new ViewHolder();
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.countTextView = (TextView) view.findViewById(R.id.count);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.nameTextView.setText(name);

			return view;
		}
	}
}
