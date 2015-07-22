package com.quanzi.ui;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants.Config;
import com.quanzi.jsonobject.JsonActItem;
import com.quanzi.jsonobject.JsonMyMessage;
import com.quanzi.jsonobject.JsonPostItem;
import com.quanzi.table.ActivityTable;
import com.quanzi.table.PostTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�MainChatMsgFragment  
 * �����������������Ϣ�б�
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-27 ����12:36:30 
 *
 */
public class MainChatMsgFragment extends BaseV4Fragment {
	private View rootView;// ��View
	private PullToRefreshListView messageListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private int pageNow = 0;//����ҳ��
	private MyMessageAdapter mAdapter;
	private UserPreference userPreference;
	private LinkedList<JsonMyMessage> messageList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPreference = BaseApplication.getInstance().getUserPreference();
		messageList = new LinkedList<JsonMyMessage>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_chat_msg, container, false);

		findViewById();// ��ʼ��views
		initView();

		//��ȡ����
		getDataTask(pageNow);

		messageListView.setMode(Mode.BOTH);
		mAdapter = new MyMessageAdapter();
		messageListView.setAdapter(mAdapter);
		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		messageListView = (PullToRefreshListView) rootView.findViewById(R.id.lovebridge_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		//������������ˢ���¼�
		messageListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				getDataTask(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (pageNow >= 0)
					++pageNow;
				getDataTask(pageNow);
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		messageListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	/**
	 * �����ȡ����
	 */
	private void getDataTask(int p) {
		final int page = p;
		RequestParams params = new RequestParams();
		params.put("page", pageNow);
		params.put(UserTable.U_ID, userPreference.getU_id());
		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					List<JsonMyMessage> temp = FastJsonTool.getObjectList(response, JsonMyMessage.class);
					if (temp != null) {
						LogTool.i("��Ϣ", "����" + temp.size());
						//������״λ�ȡ����
						if (page == 0) {
							if (temp.size() < Config.PAGE_NUM) {
								pageNow = -1;
							}
							messageList = new LinkedList<JsonMyMessage>();
							messageList.addAll(temp);
						}
						//����ǻ�ȡ����
						else if (page > 0) {
							if (temp.size() < Config.PAGE_NUM) {
								pageNow = -1;
								ToastTool.showShort(getActivity(), "û�и����ˣ�");
							}
							messageList.addAll(temp);
						}
						mAdapter.notifyDataSetChanged();
					} else {
						LogTool.e("��Ϣ", "��ȡ�б�ʧ�ܷ���Ϊ��" + response);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("��Ϣ", "��ȡ�б�ʧ��");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				messageListView.onRefreshComplete();
			}
		};
		AsyncHttpClientTool.post(getActivity(), "user/getMsg", params, responseHandler);
	}

	/**
	 * �������ӻ�����ҳ��
	 */
	private void goToDetail(final int type, int pa_id, int pa_user_id) {
		RequestParams params = new RequestParams();
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage("���ڼ���...");
		dialog.setCancelable(false);
		if (type == 0) {
			params.put(PostTable.P_POSTID, pa_id);
			params.put(PostTable.P_USERID, pa_user_id);
		} else if (type == 1) {
			params.put(ActivityTable.A_ACTID, pa_id);
			params.put(ActivityTable.A_USERID, pa_user_id);
		} else {
			return;
		}

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
					if (type == 0) {
						JsonPostItem jsonPostItem = FastJsonTool.getObject(response, JsonPostItem.class);
						if (jsonPostItem != null) {
							startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(
									PostDetailActivity.POST_ITEM, jsonPostItem));
							getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						} else {
							LogTool.e("�����������ݳ���" + response);
						}
					} else if (type == 1) {
						JsonActItem jsonActItem = FastJsonTool.getObject(response, JsonActItem.class);
						if (jsonActItem != null) {
							startActivity(new Intent(getActivity(), ActDetailActivity.class).putExtra(
									ActDetailActivity.ACT_ITEM, jsonActItem));
							getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						} else {
							LogTool.e("�����������ݳ���" + response);
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("��ȡѧУ�����б�ʧ��" + errorResponse);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				super.onFinish();
			}

		};
		AsyncHttpClientTool.post(getActivity(), "post/getSchoolPosts", params, responseHandler);
	}

	/**
	 * 
	 * �����ƣ�MessageAdapter
	 * ��������������
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014��9��14�� ����3:41:15
	 *
	 */
	class MyMessageAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
			public ImageView itemImageView;
			public TextView timeTextView;
			public TextView commentTextView;
			public TextView itemTextView;
			public ImageView favorImageView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return messageList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return messageList.get(position);
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
			final JsonMyMessage jsonMyMessage = messageList.get(position);
			if (jsonMyMessage == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.my_msg_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.itemImageView = (ImageView) view.findViewById(R.id.item_image);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.commentTextView = (TextView) view.findViewById(R.id.comment);
				holder.itemTextView = (TextView) view.findViewById(R.id.item_text);
				holder.favorImageView = (ImageView) view.findViewById(R.id.favor);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goToDetail(jsonMyMessage.getType(), jsonMyMessage.getPa_id(), jsonMyMessage.getUserid());
				}
			});

			//����ͷ��
			if (!TextUtils.isEmpty(jsonMyMessage.getSmall_avatar())) {

				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonMyMessage.getSmall_avatar()),
						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));

				if (userPreference.getU_id() != jsonMyMessage.getUserid()) {
					//���ͷ���������ҳ��
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
							intent.putExtra(UserTable.U_ID, jsonMyMessage.getUserid());
							intent.putExtra(UserTable.U_NICKNAME, jsonMyMessage.getUsername());
							intent.putExtra(UserTable.U_SMALL_AVATAR, jsonMyMessage.getSmall_avatar());
							startActivity(intent);
							getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//��������
			holder.nameTextView.setText(jsonMyMessage.getUsername());

			//��������
			if (jsonMyMessage.getType() == 2) {//��
				holder.commentTextView.setVisibility(View.GONE);
				holder.favorImageView.setVisibility(View.VISIBLE);
			} else if (jsonMyMessage.getType() == 0 || jsonMyMessage.getType() == 1) {//���ۻ�ظ�
				holder.commentTextView.setText(jsonMyMessage.getCommentcontent());
				holder.commentTextView.setVisibility(View.VISIBLE);
				holder.favorImageView.setVisibility(View.GONE);
			}

			//��������
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(jsonMyMessage.getCommenttime()));

			//�������ӻ�����Ϣ
			if (jsonMyMessage.getPa_image() != null && !jsonMyMessage.getPa_image().isEmpty()) {//�����ͼƬ
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonMyMessage.getPa_image()),
						holder.itemImageView, ImageLoaderTool.getHeadImageOptions(0));
				holder.itemTextView.setVisibility(View.GONE);
				holder.itemImageView.setVisibility(View.VISIBLE);
			} else {
				holder.itemTextView.setText(jsonMyMessage.getPa_content());
				holder.itemTextView.setVisibility(View.VISIBLE);
				holder.itemImageView.setVisibility(View.GONE);
			}

			return view;
		}
	}

}
