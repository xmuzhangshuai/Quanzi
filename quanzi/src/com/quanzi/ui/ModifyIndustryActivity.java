package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

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
	private UserPreference userPreference;
	private List<String> industryList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_industry);
		userPreference = BaseApplication.getInstance().getUserPreference();
		industryList = new ArrayList<String>();

		getIndustryList();

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

	}

	/**
	 * 	�����ȡ��ҵ�б�
	 */
	private void getIndustryList() {
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
						industryList = FastJsonTool.getObjectList(response, String.class);
						if (industryList != null && industryList.size() > 0) {
							industryListView.setAdapter(new IndustryAdapter());
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
		AsyncHttpClientTool.post(ModifyIndustryActivity.this, "quanzi/getIndustryList", params, responseHandler);
	}

	/**
	* �޸���ҵ
	* 
	* @param context
	* @param isCrop
	*/
	public void updateIndustry(final String industry) {
		if (!industry.equals(userPreference.getU_industry())) {
			// û�д������޸�
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_INDUSTRY_ITEM, industry);

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("���Ժ�...");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					if (statusCode == 200) {
						if (response.equals("1")) {
							ToastTool.showShort(ModifyIndustryActivity.this, "�޸ĳɹ���");
							userPreference.setU_industry(industry);
							finish();
						} else if (response.equals("-1")) {
							LogTool.e("�޸���ҵ����-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("�޸���ҵ����������");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

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
	private class IndustryAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView nameTextView;
			public TextView countTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return industryList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return industryList.get(position);
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
			final String name = industryList.get(position);
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
			holder.countTextView.setVisibility(View.GONE);
			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					updateIndustry(name);
				}
			});
			return view;
		}
	}
}
