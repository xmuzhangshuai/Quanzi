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
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�MainChatContactListFragment  
 * �����������������ϵ���б�
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-27 ����12:35:50 
 *
 */
public class MainChatContactListFragment extends BaseV4Fragment {
	private View rootView;// ��View
	private ImageView newMsg;
	private ListView mContactListView;
	private TextView mEmpty;
	private ContractListAdapter mAdapter;
	private UserPreference userPreference;
	//	private ConversationDbService conversationDbService;
	//	private LinkedList<Conversation> conversationList;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	private Vibrator vib;
	private View popBtn;//ɾ����ť
	private int currentItem = -1;
	private MyAlertDialog myAlertDialog;
	public RelativeLayout errorItem;//�޷����ӵ�������ʾ
	public TextView errorText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPreference = BaseApplication.getInstance().getUserPreference();
		conversationList.addAll(loadConversationsWithRecentChat());
		LogTool.i("����" + conversationList.size());

		/**�𶯷���*/
		vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_chat_contactlist, container, false);

		findViewById();// ��ʼ��views
		initView();

		mAdapter = new ContractListAdapter(getActivity(), conversationList);
		mContactListView.setAdapter(mAdapter);
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//�����δ���Ķ�������ʾ���
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
		//���û�жԻ�
		final View popView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
		popBtn = popView.findViewById(R.id.popup_btn);
		final PopupWindow popup = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// ��Ҫ����һ�´˲����������߿���ʧ 
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//���õ��������ߴ�����ʧ 
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
		 * �����¼�
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
		 * ɾ����ť
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
	 * ˢ�¶Ի��б�
	 */
	public void refresh() {
		try {
			//		 ���ܻ������߳��е����ⷽ��
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					conversationList.clear();
					conversationList.addAll(loadConversationsWithRecentChat());
					mAdapter.notifyDataSetChanged();
					if (conversationList.size() < 1) {
						mEmpty.setVisibility(View.VISIBLE);
					} else {
						mEmpty.setVisibility(View.GONE);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���¶Ի��б�����
	 */
	public void refreshConversation() {
		mAdapter = new ContractListAdapter(getActivity(), conversationList);
		mContactListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	//��ʾɾ���Ķ������¶Ի�����
	void showDeletDialog() {
		//		//���������
		//		if (userPreference.getU_stateid() == 2) {
		//			myAlertDialog = new MyAlertDialog(getActivity());
		//			myAlertDialog.setTitle("��ʾ");
		//			myAlertDialog.setMessage("�Ƿ������¹�ϵ�����������\"" + friendPreference.getName() + "\"��������ȡ����ϵ��ϵͳ�����Ϊ��ǣ�ߴ��š�");
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
		//								new SendNotifyTask(userPreference.getName() + "������������¹�ϵ", userPreference.getName(),
		//										friendPreference.getBpush_UserID()).send();
		//
		//								//ɾ������
		//								try {
		//									EMContactManager.getInstance().deleteContact("" + friendPreference.getF_id());
		//								} catch (EaseMobException e) {
		//									// TODO Auto-generated catch block
		//									e.printStackTrace();
		//								}
		//
		//								//ɾ���Ự
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
		//								ToastTool.showShort(getActivity(), "�������ʧ�ܣ�");
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
		//			myAlertDialog.setPositiveButton("���", comfirm);
		//			myAlertDialog.setNegativeButton("ȡ��", cancle);
		//			myAlertDialog.show();
		//		}
	}

	/**
	 * ��ʾ����������Ϣ��ʾ���
	 */
	public void showNewMsgTip(final boolean show) {
		// ���ܻ������߳��е����ⷽ��
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
	 * ��ȡ���лỰ
	 * 
	 * @param context
	 * @return
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    +	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// ��ȡ���лỰ������İ����
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// ���˵�messages sizeΪ0��conversation
		/**
		 * ��������������������Ϣ�յ���lastMsgTime�ᷢ���仯
		 * Ӱ��������̣�Collection.sort������쳣
		 * ��֤Conversation��Sort���������һ����Ϣ��ʱ�䲻�� 
		 * ���Ⲣ������
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
	 * �������һ����Ϣ��ʱ������
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
