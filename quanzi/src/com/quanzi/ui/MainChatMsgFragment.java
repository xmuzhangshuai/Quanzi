package com.quanzi.ui;

import java.util.Date;
import java.util.LinkedList;

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
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.jsonobject.JsonMyMessage;
import com.quanzi.jsonobject.JsonPostItem;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.ImageLoaderTool;
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
	private MessageAdapter mAdapter;
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
		mAdapter = new MessageAdapter();
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
		//		final int page = p;
		//		RequestParams params = new RequestParams();
		//		params.put("page", pageNow);
		//		params.put(LoveBridgeItemTable.N_USERID, userPreference.getU_id());
		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//				if (statusCode == 200) {
		//					LogTool.i("LoveBridgeMsgFragment", "����" + messageList.size());
		//					List<JsonBridgeCommentMessage> temp = FastJsonTool.getObjectList(response,
		//							JsonBridgeCommentMessage.class);
		//					if (temp != null) {
		//						//������״λ�ȡ����
		//						if (page == 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//							}
		//							messageList = new LinkedList<JsonBridgeCommentMessage>();
		//							messageList.addAll(temp);
		//						}
		//						//����ǻ�ȡ����
		//						else if (page > 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//								ToastTool.showShort(getActivity(), "û�и����ˣ�");
		//							}
		//							messageList.addAll(temp);
		//						}
		//						mAdapter.notifyDataSetChanged();
		//					}
		//				}
		//				messageListView.onRefreshComplete();
		//			}
		//
		//			@Override
		//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//				// TODO Auto-generated method stub
		//				LogTool.e("LoveBridgeMsgFragment", "��ȡ�б�ʧ��");
		//				messageListView.onRefreshComplete();
		//			}
		//		};
		//		AsyncHttpClientTool.post(getActivity(), "getbridgemessagelist", params, responseHandler);
		JsonMyMessage msg1 = new JsonMyMessage(1, "һ�����ģ��ټ���Ȼ����", 1, "˧��", "һ�����ģ��ټ���Ȼ����", "drawable://"
				+ R.drawable.headimage2, "��Ψ��", new Date());
		JsonMyMessage msg2 = new JsonMyMessage(2, "����һƬ�ܼ�į����������Щ���ĵ���", 2, "�ٷ�", "����һƬ�ܼ�į����������Щ���ĵ���", "drawable://"
				+ R.drawable.headimage3, "�ţ��ܲ���", new Date());
		JsonMyMessage msg3 = new JsonMyMessage(3, "����������ӣ�ÿ�춼������", 3, "ΰǿ", "����������ӣ�ÿ�춼������", "drawable://"
				+ R.drawable.headimage4, "�������򱦱����������򱦱�������", new Date());
		JsonMyMessage msg4 = new JsonMyMessage(4, "���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", 4, "����", "���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", "drawable://"
				+ R.drawable.headimage7, "�����μӰ�", new Date());
		JsonMyMessage msg5 = new JsonMyMessage(5, "����������ӣ�ÿ�춼������", 5, "��Ĭ��", "����������ӣ�ÿ�춼������", "drawable://"
				+ R.drawable.headimage6, "��Ψ��", new Date());
		JsonMyMessage msg6 = new JsonMyMessage(6, "һ�����ģ��ټ���Ȼ����", 1, "˧��", "һ�����ģ��ټ���Ȼ����", "drawable://"
				+ R.drawable.headimage5, "�������򱦱����������򱦱�����������", new Date());
		JsonMyMessage msg7 = new JsonMyMessage(7, "����һƬ�ܼ�į����������Щ���ĵ���", 2, "�ٷ�", "����һƬ�ܼ�į����������Щ���ĵ���", "drawable://"
				+ R.drawable.headimage1, "�ţ��ܲ���", new Date());
		JsonMyMessage msg8 = new JsonMyMessage(8, "����������ӣ�ÿ�춼������", 3, "ΰǿ", "����������ӣ�ÿ�춼������", "drawable://"
				+ R.drawable.headimage4, "�����μӰ�", new Date());
		JsonMyMessage msg9 = new JsonMyMessage(9, "���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", 4, "����", "���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", "drawable://"
				+ R.drawable.headimage4, "�����μӰ�", new Date());
		JsonMyMessage msg10 = new JsonMyMessage(10, "����������ӣ�ÿ�춼������", 5, "��Ĭ��", "����������ӣ�ÿ�춼������", "drawable://"
				+ R.drawable.headimage2, "��Ψ��", new Date());
		messageList.add(msg1);
		messageList.add(msg2);
		messageList.add(msg3);
		messageList.add(msg4);
		messageList.add(msg5);
		messageList.add(msg6);
		messageList.add(msg7);
		messageList.add(msg8);
		messageList.add(msg9);
		messageList.add(msg10);
		messageListView.onRefreshComplete();
	}

	/**
	 * ����ID��ȡJsonLoveBridgeItem����
	 */
	private void goToDetail(int loveItemId) {
		//		RequestParams params = new RequestParams();
		//		LogTool.e("Я��" + loveItemId);
		//		params.put(LoveBridgeItemTable.N_ID, loveItemId);
		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//			ProgressDialog dialog = new ProgressDialog(getActivity());
		//
		//			@Override
		//			public void onStart() {
		//				// TODO Auto-generated method stub
		//				super.onStart();
		//				dialog.setMessage("���Ժ�...");
		//				dialog.setCancelable(false);
		//				dialog.show();
		//			}
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//				if (statusCode == 200) {
		//					LogTool.e("���" + response);
		//					if (!TextUtils.isEmpty(response)) {
		//						JsonLoveBridgeItem loveBridgeItem = FastJsonTool.getObject(response, JsonLoveBridgeItem.class);
		//						if (loveBridgeItem != null) {
		//							startActivity(new Intent(getActivity(), LoveBridgeDetailActivity.class).putExtra(
		//									LoveBridgeDetailActivity.LOVE_BRIDGE_ITEM, loveBridgeItem));
		//							getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		//						}
		//					}
		//				}
		//			}
		//
		//			@Override
		//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//				// TODO Auto-generated method stub
		//				LogTool.e("LoveBridgeMsgFragment", "��ȡ����ʧ��");
		//			}
		//
		//			@Override
		//			public void onFinish() {
		//				// TODO Auto-generated method stub
		//				super.onFinish();
		//				if (dialog != null && dialog.isShowing()) {
		//					dialog.dismiss();
		//				}
		//			}
		//		};
		//		AsyncHttpClientTool.post(getActivity(), "getlovebridgeitembyid", params, responseHandler);

	}

	/**
	 * 
	 * �����ƣ�MessageAdapter
	 * ��������������
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014��9��14�� ����3:41:15
	 *
	 */
	class MessageAdapter extends BaseAdapter {
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
			final JsonMyMessage message = messageList.get(position);
			if (message == null) {
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
					startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(

					PostDetailActivity.POST_ITEM, new JsonPostItem(5, 5, "����", "drawable://" + R.drawable.headimage5,
							"drawable://" + R.drawable.headimage5, "Ů", "���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", "drawable://"
									+ R.drawable.content, "drawable://" + R.drawable.content, new Date(), 256, 46)));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//����ͷ��
			if (!TextUtils.isEmpty(message.getSmall_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(message.getSmall_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));

				imageLoader.displayImage(message.getSmall_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(10));

				if (userPreference.getU_id() != message.getUserid()) {
					//���ͷ���������ҳ��
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//							Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
							//							intent.putExtra(PersonDetailActivity.PERSON_TYPE, Constants.PersonDetailType.SINGLE);
							//							intent.putExtra(UserTable.U_ID, message.getUserid());
							//							startActivity(intent);
							//							getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//��������
			if (position % 3 == 0) {
				holder.commentTextView.setVisibility(View.GONE);
				holder.favorImageView.setVisibility(View.VISIBLE);
			} else {
				holder.commentTextView.setText(message.getCommentcontent());
				holder.commentTextView.setVisibility(View.VISIBLE);
				holder.favorImageView.setVisibility(View.GONE);
			}

			//��������
			holder.nameTextView.setText(message.getUsername());

			//�����Ա�
			//			if (message.getGender().equals(Constants.Gender.MALE)) {
			//				holder.genderImageView.setImageResource(R.drawable.male);
			//			} else {
			//				holder.genderImageView.setImageResource(R.drawable.female);
			//			}

			//��������
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(message.getCommenttime()));

			if (position % 2 == 0) {
				holder.itemTextView.setText(message.getMessage());
				holder.itemTextView.setVisibility(View.VISIBLE);
				holder.itemImageView.setVisibility(View.GONE);
			} else {
				holder.itemImageView.setImageResource(R.drawable.content);
				holder.itemTextView.setVisibility(View.GONE);
				holder.itemImageView.setVisibility(View.VISIBLE);
			}

			//			holder.loveItemTextView.setOnClickListener(new OnClickListener() {
			//
			//				@Override
			//				public void onClick(View v) {
			//					// TODO Auto-generated method stub
			//					goToDetail(message.getMessageid());
			//				}
			//			});

			return view;
		}
	}

}
