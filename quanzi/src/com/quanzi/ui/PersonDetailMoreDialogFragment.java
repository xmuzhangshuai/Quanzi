package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.table.BlackListTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：PersonDetailMoreDialogFragment  
 * 类描述：他人个人中心更多对话框
 * @author zhangshuai
 * @date 创建时间：2015-5-11 上午10:58:20 
 *
 */
public class PersonDetailMoreDialogFragment extends DialogFragment implements OnItemClickListener {
	private View rootView;
	private ListView menuitemListView;
	private List<String> menuitemList;
	private int userId;
	private UserPreference userPreference;

	/**
	 * 创建实例
	 * @return
	 */
	static PersonDetailMoreDialogFragment newInstance(int userID) {
		PersonDetailMoreDialogFragment f = new PersonDetailMoreDialogFragment();
		f.userId = userID;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		userPreference = BaseApplication.getInstance().getUserPreference();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		menuitemListView = (ListView) rootView.findViewById(R.id.dialog_listview);
		menuitemList = new ArrayList<String>();

		menuitemList.add("加黑名单");
		//		menuitemList.add("取消关注");

		menuitemListView.setAdapter(new PublishDialogAdapter());
		menuitemListView.setOnItemClickListener(this);
		return rootView;
	}

	/**
	 * 显示对话框
	 */
	private void showBlackListDialog(final int userId) {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
		myAlertDialog.setTitle("加入黑名单");
		myAlertDialog.setMessage("你们将自动解除关注/被关注关系，他不能再关注你或者评论、点赞、私信，且你们的私信将会被删除。  ");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
				addToBlackList(userId);
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("加入黑名单", comfirm);
		myAlertDialog.setNegativeButton("取消", cancle);
		myAlertDialog.show();
	}

	/**
	 * 加入黑名单
	 */
	private void addToBlackList(final int userId) {
		RequestParams params = new RequestParams();
		params.put(BlackListTable.BL_USERID, userPreference.getU_id());
		params.put(BlackListTable.BL_SHIELDERID, userId);

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("1")) {
						LogTool.i("加入黑名单成功");
						try {
							EMContactManager.getInstance().addUserToBlackList("" + userId, true);
							EMChatManager.getInstance().deleteConversation("" + userId);
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						LogTool.e("加入黑名单返回错误" + response);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("加入黑名单服务器错误");
			}
		};
		AsyncHttpClientTool.post(getActivity(), "quanzi/addBlackList", params, responseHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			if (userId > 0) {
				showBlackListDialog(userId);
			}
			PersonDetailMoreDialogFragment.this.dismiss();
			break;
		case 1:
			PersonDetailMoreDialogFragment.this.dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：HomeDialogAdapter  
	 * 类描述：
	 * @author zhangshuai
	 * @date 创建时间：2015-4-27 上午11:52:43 
	 *
	 */
	private class PublishDialogAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView itemName;
			public TextView righttext;
			public ImageView imageView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuitemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
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
			final ViewHolder holder;

			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_listview_item, null);
				holder = new ViewHolder();
				holder.itemName = (TextView) view.findViewById(R.id.item_name);
				holder.righttext = (TextView) view.findViewById(R.id.righttext);
				holder.imageView = (ImageView) view.findViewById(R.id.leftimage);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.itemName.setText(menuitemList.get(position));
			if (menuitemList.get(position).equals("加入黑名单")) {
				holder.imageView.setImageResource(R.drawable.balcklist);
			} else {
				holder.imageView.setImageResource(R.drawable.concel_concern);
			}

			holder.imageView.setVisibility(View.VISIBLE);

			return view;
		}
	}
}
