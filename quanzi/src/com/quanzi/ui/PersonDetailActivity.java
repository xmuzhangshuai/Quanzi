package com.quanzi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.customewidget.MyAlertDialog;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�PersonDetailActivity  
 * �����������˵ĸ�������ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-5-8 ����3:08:37 
 *
 */
public class PersonDetailActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView leftTextView;//�������������
	private View leftButton;//��������ఴť
	private View contactBtn;//˽�Ű�ť
	private View moreBtn;//���ఴť
	private int index;
	private int currentTabIndex;
	private View[] mTabs;

	private Fragment[] fragments;
	private PersonDetailPostFragment personDetailPostFragment;
	private PersonDataFragment personDataFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person_detail);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		contactBtn = findViewById(R.id.nav_right_btn2);
		moreBtn = findViewById(R.id.nav_right_btn1);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("׿��");
		leftButton.setOnClickListener(this);
		contactBtn.setOnClickListener(this);
		moreBtn.setOnClickListener(this);

		personDataFragment = new PersonDataFragment();
		personDetailPostFragment = new PersonDetailPostFragment();
		fragments = new Fragment[] { personDetailPostFragment, personDataFragment };

		mTabs = new View[2];
		mTabs[0] = (View) findViewById(R.id.postBtn);
		mTabs[1] = (View) findViewById(R.id.dataBtn);
		// �ѵ�һ��tab��Ϊѡ��״̬
		mTabs[0].setSelected(true);

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, personDetailPostFragment)
				.show(personDetailPostFragment).commit();

	}

	
	

	/**
	 * �˵���ʾ
	 */
	void showMoreDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		PersonDetailMoreDialogFragment newFragment = PersonDetailMoreDialogFragment.newInstance();
		newFragment.show(ft, "dialog");
	}

	/**
	 * button����¼�
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.postBtn:
			index = 0;
			break;
		case R.id.dataBtn:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// �ѵ�ǰtab��Ϊѡ��״̬
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.nav_right_btn2://˽��
			break;
		case R.id.nav_right_btn1://����
			showMoreDialog();
			break;
		default:
			break;
		}
	}

}
