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
 * 项目名称：quanzi  
 * 类名称：MainChatMsgFragment  
 * 类描述：聊天界面消息列表
 * @author zhangshuai
 * @date 创建时间：2015-4-27 下午12:36:30 
 *
 */
public class MainChatMsgFragment extends BaseV4Fragment {
	private View rootView;// 根View
	private PullToRefreshListView messageListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private int pageNow = 0;//控制页数
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

		findViewById();// 初始化views
		initView();

		//获取数据
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
		//设置上拉下拉刷新事件
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
	 * 网络获取数据
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
		//					LogTool.i("LoveBridgeMsgFragment", "长度" + messageList.size());
		//					List<JsonBridgeCommentMessage> temp = FastJsonTool.getObjectList(response,
		//							JsonBridgeCommentMessage.class);
		//					if (temp != null) {
		//						//如果是首次获取数据
		//						if (page == 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//							}
		//							messageList = new LinkedList<JsonBridgeCommentMessage>();
		//							messageList.addAll(temp);
		//						}
		//						//如果是获取更多
		//						else if (page > 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//								ToastTool.showShort(getActivity(), "没有更多了！");
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
		//				LogTool.e("LoveBridgeMsgFragment", "获取列表失败");
		//				messageListView.onRefreshComplete();
		//			}
		//		};
		//		AsyncHttpClientTool.post(getActivity(), "getbridgemessagelist", params, responseHandler);
		JsonMyMessage msg1 = new JsonMyMessage(1, "一见倾心，再见依然痴迷", 1, "帅哥", "一见倾心，再见依然痴迷", "drawable://"
				+ R.drawable.headimage2, "很唯美", new Date());
		JsonMyMessage msg2 = new JsonMyMessage(2, "这是一片很寂寞的天下着有些伤心的雨", 2, "荣发", "这是一片很寂寞的天下着有些伤心的雨", "drawable://"
				+ R.drawable.headimage3, "嗯，很不错", new Date());
		JsonMyMessage msg3 = new JsonMyMessage(3, "爱上你的日子，每天都在想你", 3, "伟强", "爱上你的日子，每天都在想你", "drawable://"
				+ R.drawable.headimage4, "我是篮球宝贝被我是篮球宝贝被我是", new Date());
		JsonMyMessage msg4 = new JsonMyMessage(4, "卡又丢了，快来点开心的事冲冲喜吧！", 4, "旗子", "卡又丢了，快来点开心的事冲冲喜吧！", "drawable://"
				+ R.drawable.headimage7, "快来参加吧", new Date());
		JsonMyMessage msg5 = new JsonMyMessage(5, "爱上你的日子，每天都在想你", 5, "赵默笙", "爱上你的日子，每天都在想你", "drawable://"
				+ R.drawable.headimage6, "很唯美", new Date());
		JsonMyMessage msg6 = new JsonMyMessage(6, "一见倾心，再见依然痴迷", 1, "帅哥", "一见倾心，再见依然痴迷", "drawable://"
				+ R.drawable.headimage5, "我是篮球宝贝被我是篮球宝贝被我是篮球", new Date());
		JsonMyMessage msg7 = new JsonMyMessage(7, "这是一片很寂寞的天下着有些伤心的雨", 2, "荣发", "这是一片很寂寞的天下着有些伤心的雨", "drawable://"
				+ R.drawable.headimage1, "嗯，很不错", new Date());
		JsonMyMessage msg8 = new JsonMyMessage(8, "爱上你的日子，每天都在想你", 3, "伟强", "爱上你的日子，每天都在想你", "drawable://"
				+ R.drawable.headimage4, "快来参加吧", new Date());
		JsonMyMessage msg9 = new JsonMyMessage(9, "卡又丢了，快来点开心的事冲冲喜吧！", 4, "旗子", "卡又丢了，快来点开心的事冲冲喜吧！", "drawable://"
				+ R.drawable.headimage4, "快来参加吧", new Date());
		JsonMyMessage msg10 = new JsonMyMessage(10, "爱上你的日子，每天都在想你", 5, "赵默笙", "爱上你的日子，每天都在想你", "drawable://"
				+ R.drawable.headimage2, "很唯美", new Date());
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
	 * 根据ID获取JsonLoveBridgeItem数据
	 */
	private void goToDetail(int loveItemId) {
		//		RequestParams params = new RequestParams();
		//		LogTool.e("携带" + loveItemId);
		//		params.put(LoveBridgeItemTable.N_ID, loveItemId);
		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//			ProgressDialog dialog = new ProgressDialog(getActivity());
		//
		//			@Override
		//			public void onStart() {
		//				// TODO Auto-generated method stub
		//				super.onStart();
		//				dialog.setMessage("请稍后...");
		//				dialog.setCancelable(false);
		//				dialog.show();
		//			}
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//				if (statusCode == 200) {
		//					LogTool.e("结果" + response);
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
		//				LogTool.e("LoveBridgeMsgFragment", "获取详情失败");
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
	 * 类名称：MessageAdapter
	 * 类描述：适配器
	 * 创建人： 张帅
	 * 创建时间：2014年9月14日 下午3:41:15
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
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(

					PostDetailActivity.POST_ITEM, new JsonPostItem(5, 5, "王坤", "drawable://" + R.drawable.headimage5,
							"drawable://" + R.drawable.headimage5, "女", "卡又丢了，快来点开心的事冲冲喜吧！", "drawable://"
									+ R.drawable.content, "drawable://" + R.drawable.content, new Date(), 256, 46)));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//设置头像
			if (!TextUtils.isEmpty(message.getSmall_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(message.getSmall_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));

				imageLoader.displayImage(message.getSmall_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(10));

				if (userPreference.getU_id() != message.getUserid()) {
					//点击头像进入详情页面
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

			//设置内容
			if (position % 3 == 0) {
				holder.commentTextView.setVisibility(View.GONE);
				holder.favorImageView.setVisibility(View.VISIBLE);
			} else {
				holder.commentTextView.setText(message.getCommentcontent());
				holder.commentTextView.setVisibility(View.VISIBLE);
				holder.favorImageView.setVisibility(View.GONE);
			}

			//设置姓名
			holder.nameTextView.setText(message.getUsername());

			//设置性别
			//			if (message.getGender().equals(Constants.Gender.MALE)) {
			//				holder.genderImageView.setImageResource(R.drawable.male);
			//			} else {
			//				holder.genderImageView.setImageResource(R.drawable.female);
			//			}

			//设置日期
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
