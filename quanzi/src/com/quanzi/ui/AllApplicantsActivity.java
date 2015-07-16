package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.ActivityTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�AllApplicants  
 * ���������鿴���б�����
 * @author zhangshuai
 * @date ����ʱ�䣺2015-7-16 ����10:36:38 
 *
 */
public class AllApplicantsActivity extends BaseActivity implements OnClickListener {

	/***********VIEWS************/
	public static final String A_ACTID = "a_act_id";
	public static final String A_USERID = "a_user_id";
	public static final String APPLY_COUNT = "applye_count";

	private ListView allFavorListView;
	private TextView leftTextView;//�������������
	private View leftButton;//��������ఴť
	List<JsonUser> jsonUserList;
	int a_id;
	int a_userid;
	int apply_count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all_favors);
		a_id = getIntent().getIntExtra(A_ACTID, 0);
		a_userid = getIntent().getIntExtra(A_USERID, 0);
		apply_count = getIntent().getIntExtra(APPLY_COUNT, 0);

		jsonUserList = new ArrayList<JsonUser>();

		if (a_id > 0 && a_userid > 0) {
			getUserList();
		}

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		allFavorListView = (ListView) findViewById(R.id.allfovers_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("" + apply_count + "�˱���");
		leftButton.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		default:
			break;
		}
	}

	/**
	 * 	�����ȡUser��Ϣ
	 */
	private void getUserList() {
		RequestParams params = new RequestParams();
		params.put(ActivityTable.A_ACTID, a_id);
		params.put(ActivityTable.A_USERID, a_userid);

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("-1")) {
						LogTool.e("�����б��ش���" + response);
					} else {
						jsonUserList = FastJsonTool.getObjectList(response, JsonUser.class);
						if (jsonUserList != null && jsonUserList.size() > 0) {
							allFavorListView.setAdapter(new MyFavorsAdapter());
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("��ȡ�������б�ʧ��");
			}
		};
		AsyncHttpClientTool.post(AllApplicantsActivity.this, "activity/getApplyUsers", params, responseHandler);

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
	private class MyFavorsAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonUserList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonUserList.get(position);
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
			final JsonUser jsonUser = jsonUserList.get(position);
			if (jsonUser == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(AllApplicantsActivity.this).inflate(R.layout.followers_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AllApplicantsActivity.this, PersonDetailActivity.class);
					intent.putExtra(PersonDetailActivity.JSONUSER, jsonUser);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonUser.getU_small_avatar()),
					holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
			holder.nameTextView.setText(jsonUser.getU_nickname());

			return view;
		}
	}

}
