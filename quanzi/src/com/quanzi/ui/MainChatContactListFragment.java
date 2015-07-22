package com.quanzi.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.exceptions.EaseMobException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.adapter.ContractListAdapter;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.entities.Conversation;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：MainChatContactListFragment  
 * 类描述：聊天最近联系人列表
 * @author zhangshuai
 * @date 创建时间：2015-4-27 下午12:35:50 
 *
 */
public class MainChatContactListFragment extends BaseV4Fragment {
	private View rootView;// 根View
	private ImageView newMsg;
	private ListView mContactListView;
	private TextView mEmpty;
	private ContractListAdapter mAdapter;
	private UserPreference userPreference;
	//	private ConversationDbService conversationDbService;
	//	private LinkedList<Conversation> conversationList;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	private Vibrator vib;
	private View popBtn;//删除按钮
	private int currentItem = -1;
	private MyAlertDialog myAlertDialog;
	public RelativeLayout errorItem;//无法连接到网络提示
	public TextView errorText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPreference = BaseApplication.getInstance().getUserPreference();
		conversationList.addAll(loadConversationsWithRecentChat());
		LogTool.i("长度" + conversationList.size());

		/**震动服务*/
		vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_chat_contactlist, container, false);

		findViewById();// 初始化views
		initView();

		mAdapter = new ContractListAdapter(getActivity(), conversationList);
		mContactListView.setAdapter(mAdapter);
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//如果有未读心动请求，显示红点
		//		if (FlipperDbService.getInstance(getActivity()).getFlipperCount() > 0) {
		//			showNewMsgTip(true);
		//		} else {
		//			showNewMsgTip(false);
		//		}

		refresh();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mContactListView = (ListView) rootView.findViewById(R.id.recent_listview);
		mEmpty = (TextView) rootView.findViewById(R.id.empty);
		errorItem = (RelativeLayout) rootView.findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		//如果没有对话
		final View popView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
		popBtn = popView.findViewById(R.id.popup_btn);
		final PopupWindow popup = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 需要设置一下此参数，点击外边可消失 
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//设置点击窗口外边窗口消失 
		popup.setOutsideTouchable(true);
		popup.setFocusable(true);

		mContactListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				try {
					Intent toChatIntent = new Intent(getActivity(), ChatActivity.class);
					toChatIntent.putExtra(UserTable.U_ID,
							Integer.parseInt(conversationList.get(position).getUserName()));
					startActivity(toChatIntent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		/**
		 * 长按事件
		 */
		mContactListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				vib.vibrate(30);
				int xoff = view.getWidth() / 2 - popView.getWidth();
				popup.showAsDropDown(view, xoff, 0);
				currentItem = position;
				return false;
			}
		});

		/**
		 * 删除按钮
		 */
		popBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popup.dismiss();
				showDeletDialog();
			}
		});

	}

	/**
	 * 刷新对话列表
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		if (conversationList.size() < 1) {
			mEmpty.setVisibility(View.VISIBLE);
		} else {
			mEmpty.setVisibility(View.GONE);
		}
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
		//		try {
		// 可能会在子线程中调到这方法
		//			getActivity().runOnUiThread(new Runnable() {
		//				public void run() {
		//					conversationList.clear();
		//					conversationList.addAll(conversationDbService.conversationDao.loadAll());
		//					mAdapter.notifyDataSetChanged();
		//					if (conversationList.size() < 1) {
		//						mEmpty.setVisibility(View.VISIBLE);
		//					} else {
		//						mEmpty.setVisibility(View.GONE);
		//					}
		//				}
		//			});
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

	/**
	 * 更新对话列表内容
	 */
	public void refreshConversation() {
		mAdapter = new ContractListAdapter(getActivity(), conversationList);
		mContactListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	//显示删除心动或情侣对话窗口
	void showDeletDialog() {
		//		//如果是心动关系
		//		if (userPreference.getU_stateid() == 3) {
		//			myAlertDialog = new MyAlertDialog(getActivity());
		//			myAlertDialog.setTitle("提示");
		//			myAlertDialog.setMessage("是否解除您和\"" + friendPreference.getName() + "\"的心动关系？解除后你们将不能再取得联系。系统会继续为您牵线搭桥。");
		//			View.OnClickListener comfirm = new OnClickListener() {
		//
		//				@Override
		//				public void onClick(View v) {
		//					// TODO Auto-generated method stub
		//					myAlertDialog.dismiss();
		//					if (currentItem > -1) {
		//
		//						RequestParams params = new RequestParams();
		//						params.put(FlipperTable.F_USERID, userPreference.getU_id());
		//						params.put(FlipperTable.F_FLIPPERID, friendPreference.getF_id());
		//						TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//
		//							@Override
		//							public void onSuccess(int statusCode, Header[] headers, String response) {
		//								// TODO Auto-generated method stub
		//								FlipperDbService flipperDbService = FlipperDbService.getInstance(getActivity());
		//								flipperDbService.deleteFlipperByUserId(conversationList.get(currentItem).getUserID()
		//										.intValue());
		//
		//								conversationDbService.conversationDao.delete(conversationList.get(currentItem));
		//								conversationList.remove(currentItem);
		//								mAdapter.notifyDataSetChanged();
		//								currentItem = -1;
		//								//								new SendNotifyTask(userPreference.getName() + "和您解除了心动关系", userPreference.getName(),
		//								//										friendPreference.getBpush_UserID()).send();
		//
		//								//删除好友
		//								try {
		//									EMContactManager.getInstance().deleteContact("" + friendPreference.getF_id());
		//								} catch (EaseMobException e) {
		//									// TODO Auto-generated catch block
		//									e.printStackTrace();
		//								}
		//								//删除会话
		//								//								EMChatManager.getInstance().deleteConversation("" + friendPreference.getF_id());
		//
		//								friendPreference.clear();
		//								userPreference.setU_stateid(4);
		//
		//								MainActivity activity = (MainActivity) getActivity();
		//								activity.refresh();
		//							}
		//
		//							@Override
		//							public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//								// TODO Auto-generated method stub
		//								ToastTool.showShort(getActivity(), "解除心动关系失败！");
		//							}
		//						};
		//						AsyncHttpClientTool.post("deleteflipper", params, responseHandler);
		//					}
		//				}
		//			};
		//			View.OnClickListener cancle = new OnClickListener() {
		//
		//				@Override
		//				public void onClick(View v) {
		//					// TODO Auto-generated method stub
		//					myAlertDialog.dismiss();
		//				}
		//			};
		//			myAlertDialog.setPositiveButton("解除", comfirm);
		//			myAlertDialog.setNegativeButton("取消", cancle);
		//			myAlertDialog.show();
		//		}
		//		//如果是情侣
		//		if (userPreference.getU_stateid() == 2) {
		//			myAlertDialog = new MyAlertDialog(getActivity());
		//			myAlertDialog.setTitle("提示");
		//			myAlertDialog.setMessage("是否解除情侣关系？解除后您和\"" + friendPreference.getName() + "\"将不能再取得联系。系统会继续为您牵线搭桥。");
		//			View.OnClickListener comfirm = new OnClickListener() {
		//
		//				@Override
		//				public void onClick(View v) {
		//					// TODO Auto-generated method stub
		//					myAlertDialog.dismiss();
		//					if (currentItem > -1) {
		//
		//						RequestParams params = new RequestParams();
		//						params.put(LoversTable.L_USERID, userPreference.getU_id());
		//						params.put(LoversTable.L_LOVERID, friendPreference.getF_id());
		//						TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//
		//							@Override
		//							public void onSuccess(int statusCode, Header[] headers, String response) {
		//								// TODO Auto-generated method stub
		//								conversationDbService.conversationDao.delete(conversationList.get(currentItem));
		//								conversationList.remove(currentItem);
		//								mAdapter.notifyDataSetChanged();
		//								currentItem = -1;
		//
		//								new SendNotifyTask(userPreference.getName() + "和您解除了情侣关系", userPreference.getName(),
		//										friendPreference.getBpush_UserID()).send();
		//
		//								//删除好友
		//								try {
		//									EMContactManager.getInstance().deleteContact("" + friendPreference.getF_id());
		//								} catch (EaseMobException e) {
		//									// TODO Auto-generated catch block
		//									e.printStackTrace();
		//								}
		//
		//								//删除会话
		//								//EMChatManager.getInstance().deleteConversation("" + friendPreference.getF_id());
		//								friendPreference.clear();
		//								userPreference.setU_stateid(4);
		//
		//								MainActivity activity = (MainActivity) getActivity();
		//								activity.refresh();
		//							}
		//
		//							@Override
		//							public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//								// TODO Auto-generated method stub
		//								ToastTool.showShort(getActivity(), "解除情侣失败！");
		//							}
		//						};
		//						AsyncHttpClientTool.post("deletelover", params, responseHandler);
		//					}
		//				}
		//			};
		//			View.OnClickListener cancle = new OnClickListener() {
		//
		//				@Override
		//				public void onClick(View v) {
		//					// TODO Auto-generated method stub
		//					myAlertDialog.dismiss();
		//
		//				}
		//			};
		//			myAlertDialog.setPositiveButton("解除", comfirm);
		//			myAlertDialog.setNegativeButton("取消", cancle);
		//			myAlertDialog.show();
		//		}
	}

	/**
	 * 显示或隐藏新消息提示红点
	 */
	public void showNewMsgTip(final boolean show) {
		// 可能会在子线程中调到这方法
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (show) {
					newMsg.setVisibility(View.VISIBLE);
				} else {
					newMsg.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    +	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
		 * 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 
		 * 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					//if(conversation.getType() != EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),
							conversation));
					//}
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

}
