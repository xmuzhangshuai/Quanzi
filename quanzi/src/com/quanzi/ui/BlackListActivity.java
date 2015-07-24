package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
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

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.jsonobject.JsonConcern;
import com.quanzi.table.BlackListTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�BlackListActivity  
 * ���������鿴�ҵĺ�����
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-30 ����7:43:18 
 *
 */
public class BlackListActivity extends BaseActivity implements OnClickListener {

	/***********VIEWS************/

	private ListView allFavorListView;
	private TextView leftTextView;//�������������
	private View leftButton;//��������ఴť
	List<JsonConcern> jsonConcernList;
	private MyFavorsAdapter myAdapter;
	private UserPreference userPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all_favors);
		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonConcernList = new ArrayList<JsonConcern>();

		getUserList();

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
		leftTextView.setText("������");
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
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("���ڼ���...");

		RequestParams params = new RequestParams();
		params.put(UserTable.U_ID, userPreference.getU_id());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				dialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("-1")) {
					} else {
						jsonConcernList = FastJsonTool.getObjectList(response, JsonConcern.class);
						if (jsonConcernList != null && jsonConcernList.size() > 0) {
							myAdapter = new MyFavorsAdapter();
							allFavorListView.setAdapter(myAdapter);
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("��ȡ�б�ʧ��");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		};
		AsyncHttpClientTool.post(BlackListActivity.this, "quanzi/getBlackList", params, responseHandler);
	}

	/**
	 * ��ʾ�Ի���
	 */
	private void showBlackListDialog(final JsonConcern jsonConcern) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(BlackListActivity.this);
		myAlertDialog.setTitle("�Ƴ�������");
		myAlertDialog.setMessage("�����ѴӺ��������Ƴ�  ");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
				removeFromBlackList(jsonConcern);
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("�Ƴ�", comfirm);
		myAlertDialog.setNegativeButton("ȡ��", cancle);
		myAlertDialog.show();
	}

	/**
	 * �Ƴ�������
	 */
	private void removeFromBlackList(final JsonConcern jsonConcern) {
		RequestParams params = new RequestParams();
		params.put(BlackListTable.BL_USERID, userPreference.getU_id());
		params.put(BlackListTable.BL_SHIELDERID, jsonConcern.getUser_id());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("1")) {
						LogTool.i("�Ƴ��������ɹ�");
						try {
							EMContactManager.getInstance().deleteUserFromBlackList("" + jsonConcern.getUser_id());
							jsonConcernList.remove(jsonConcern);
							myAdapter.notifyDataSetChanged();
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						LogTool.e("�Ƴ����������ش���" + response);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("�Ƴ�����������������");
			}
		};
		AsyncHttpClientTool.post(BlackListActivity.this, "quanzi/removeBlackList", params, responseHandler);
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
			return jsonConcernList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonConcernList.get(position);
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
			final JsonConcern jsonConcern = jsonConcernList.get(position);
			if (jsonConcern == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(BlackListActivity.this).inflate(R.layout.followers_list_item, null);
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
					showBlackListDialog(jsonConcern);
				}
			});

			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonConcern.getUser_small_avatar()),
					holder.headImageView, ImageLoaderTool.getHeadImageOptions(0));
			holder.nameTextView.setText(jsonConcern.getUser_name());

			return view;
		}
	}
}
