/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quanzi.huanxin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.quanzi.config.DefaultKeys;
import com.quanzi.entities.User;
import com.quanzi.huanxin.HXNotifier.HXNotificationInfoProvider;
import com.quanzi.table.UserTable;
import com.quanzi.ui.ChatActivity;
import com.quanzi.ui.MainActivity;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * @author easemob
 *
 */
public class MyHXSDKHelper extends HXSDKHelper {

	private static final String TAG = "MyHXSDKHelper";

	/**
	 * EMEventListener
	 */
	protected EMEventListener eventListener = null;

	/**
	 * contact list in cache
	 */
	private Map<String, User> contactList;

	/**
	 * robot list in cache
	 */
	//    private Map<String, RobotUser> robotList;
	//	private CallReceiver callReceiver;

	/**
	 * 用来记录foreground Activity
	 */
	private List<Activity> activityList = new ArrayList<Activity>();

	public void pushActivity(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(0, activity);
		}
	}

	public void popActivity(Activity activity) {
		activityList.remove(activity);
	}

	@Override
	protected void initListener() {
		super.initListener();
		//注册消息事件监听
		initEventListener();
	}

	/**
	 * 全局事件监听
	 * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
	 * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
	 */
	protected void initEventListener() {
		eventListener = new EMEventListener() {
			private BroadcastReceiver broadCastReceiver = null;

			@Override
			public void onEvent(EMNotifierEvent event) {
				EMMessage message = null;
				if (event.getData() instanceof EMMessage) {
					message = (EMMessage) event.getData();
					EMLog.d(TAG, "receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
				}

				switch (event.getEvent()) {
				case EventNewMessage:
					//应用在后台，不需要刷新UI,通知栏提示新消息
					if (activityList.size() <= 0) {
						HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
					}
					break;
				case EventOfflineMessage:
					if (activityList.size() <= 0) {
						EMLog.d(TAG, "received offline messages");
						List<EMMessage> messages = (List<EMMessage>) event.getData();
						HXSDKHelper.getInstance().getNotifier().onNewMesg(messages);
					}
					break;
				// below is just giving a example to show a cmd toast, the app should not follow this
				// so be careful of this
				case EventNewCMDMessage: {

					EMLog.d(TAG, "收到透传消息");
					//获取消息body
					CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
					final String action = cmdMsgBody.action;//获取自定义action

					//获取扩展属性 此处省略
					//message.getStringAttribute("");
					EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));
					final String str = "收到透传：action：";

					final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
					IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

					if (broadCastReceiver == null) {
						broadCastReceiver = new BroadcastReceiver() {

							@Override
							public void onReceive(Context context, Intent intent) {
								// TODO Auto-generated method stub
								Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT)
										.show();
							}
						};

						//注册广播接收者
						appContext.registerReceiver(broadCastReceiver, cmdFilter);
					}

					Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
					broadcastIntent.putExtra("cmd_value", str + action);
					appContext.sendBroadcast(broadcastIntent, null);

					break;
				}
				case EventDeliveryAck:
					message.setDelivered(true);
					break;
				case EventReadAck:
					message.setAcked(true);
					break;
				// add other events in case you are interested in
				default:
					break;
				}

			}
		};

		EMChatManager.getInstance().registerEventListener(eventListener);
	}

	/**
	 * 自定义通知栏提示内容
	 * @return
	 */
	@Override
	protected HXNotificationInfoProvider getNotificationListener() {
		//可以覆盖默认的设置
		return new HXNotificationInfoProvider() {

			@Override
			public String getTitle(EMMessage message) {
				//修改标题,这里使用默认
				return null;
			}

			@Override
			public int getSmallIcon(EMMessage message) {
				//设置小图标，这里为默认
				return 0;
			}

			@Override
			public String getDisplayedText(EMMessage message) {
				// 设置状态栏的消息提示，可以根据message的类型做相应提示
				//				String ticker = CommonUtils.getMessageDigest(message, appContext);
				//				if (message.getType() == Type.TXT) {
				//					ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
				//				}
				//				Map<String, RobotUser> robotMap = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getRobotList();
				//				if (robotMap != null && robotMap.containsKey(message.getFrom())) {
				//					String nick = robotMap.get(message.getFrom()).getNick();
				//					if (!TextUtils.isEmpty(nick)) {
				//						return nick + ": " + ticker;
				//					} else {
				//						return message.getFrom() + ": " + ticker;
				//					}
				//				} else {
				//					return message.getFrom() + ": " + ticker;
				//				}
				return message.getStringAttribute("username", "") + "发来了一条消息哦~";
			}

			@Override
			public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
				return fromUsersNum + "个联系人，发来了" + messageNum + "条消息";
			}

			@Override
			public Intent getLaunchIntent(EMMessage message) {
				//设置点击通知栏跳转事件
				Intent intent = new Intent(appContext, ChatActivity.class);
				//有电话时优先跳转到通话页面
				//                if(isVideoCalling){
				//                    intent = new Intent(appContext, VideoCallActivity.class);
				//                }else if(isVoiceCalling){
				//                    intent = new Intent(appContext, VoiceCallActivity.class);
				//                }else{
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra(UserTable.U_ID, Integer.parseInt(message.getFrom()));
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);

				}
				//                }
				return intent;
			}
		};
	}

	@Override
	protected void onConnectionConflict() {
		Intent intent = new Intent(appContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("conflict", true);
		appContext.startActivity(intent);
	}

	@Override
	protected void onCurrentAccountRemoved() {
		Intent intent = new Intent(appContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(DefaultKeys.ACCOUNT_REMOVED, true);
		appContext.startActivity(intent);
	}

	@Override
	protected HXSDKModel createModel() {
		return new DefaultHXSDKModel(appContext);
	}

	@Override
	public HXNotifier createNotifier() {
		return new HXNotifier() {
			public synchronized void onNewMsg(final EMMessage message) {
				if (EMChatManager.getInstance().isSlientMessage(message)) {
					return;
				}

				String chatUsename = null;
				List<String> notNotifyIds = null;

				if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
					// 判断app是否在后台
					if (!EasyUtils.isAppRunningForeground(appContext)) {
						EMLog.d(TAG, "app is running in backgroud");
						sendNotification(message, false);
					} else {
						sendNotification(message, true);
					}
					viberateAndPlayTone(message);
				}
			}
		};
	}

	/**
	 * get demo HX SDK Model
	 */
	public DefaultHXSDKModel getModel() {
		return (DefaultHXSDKModel) hxModel;
	}

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	//	public Map<String, User> getContactList() {
	//		if (getHXId() != null && contactList == null) {
	//			contactList = ((DemoHXSDKModel) getModel()).getContactList();
	//		}
	//
	//		return contactList;
	//	}

	//	public Map<String, RobotUser> getRobotList() {
	//		if (getHXId() != null && robotList == null) {
	//			robotList = ((DemoHXSDKModel) getModel()).getRobotList();
	//		}
	//		return robotList;
	//	}

	//	public boolean isRobotMenuMessage(EMMessage message) {
	//
	//		try {
	//			JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
	//			if (jsonObj.has("choice")) {
	//				return true;
	//			}
	//		} catch (Exception e) {
	//		}
	//		return false;
	//	}

	//	public String getRobotMenuMessageDigest(EMMessage message) {
	//		String title = "";
	//		try {
	//			JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
	//			if (jsonObj.has("choice")) {
	//				JSONObject jsonChoice = jsonObj.getJSONObject("choice");
	//				title = jsonChoice.getString("title");
	//			}
	//		} catch (Exception e) {
	//		}
	//		return title;
	//	}

	//	public void setRobotList(Map<String, RobotUser> robotList) {
	//		this.robotList = robotList;
	//	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	@Override
	public void logout(final EMCallBack callback) {
		super.logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				setContactList(null);
				//				setRobotList(null);
				//				getModel().closeDB();
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

		});
	}

}
