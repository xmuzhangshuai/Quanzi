package com.quanzi.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�ModifySkillActivity  
 * ���������޸ļ���
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-30 ����6:46:26 
 *
 */
public class ModifySkillActivity extends BaseActivity {

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
		setContentView(R.layout.activity_modify_skill);

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
		leftTextView.setText("�ó�����");
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

	}

}
