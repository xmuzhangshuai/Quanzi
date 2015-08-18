package com.quanzi.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
import com.quanzi.config.Constants.CommentType;
import com.quanzi.config.Constants.Config;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.jsonobject.JsonActItem;
import com.quanzi.table.ActivityTable;
import com.quanzi.table.CommentTable;
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
 * 项目名称：quanzi  
 * 类名称：MainExploreActFragment  
 * 类描述：探索页面的活动子页面
 * @author zhangshuai
 * @date 创建时间：2015-4-20 下午3:28:05 
 *
 */
public class MainExploreActFragment extends BaseV4Fragment {
	public final static String TAG = "MainExploreActFragment";

	private View rootView;// 根View
	private PullToRefreshListView actListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private UserPreference userPreference;
	private LinkedList<JsonActItem> jsonActItemList;
	private int pageNow = 0;// 控制页数
	private PostAdapter mAdapter;
	private String actType = "全部";
	private static MainExploreActFragment mainExploreActFragment;

	// 创建实例
	static MainExploreActFragment newInstance() {
		if (mainExploreActFragment == null) {
			return new MainExploreActFragment();
		} else {
			return mainExploreActFragment;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonActItemList = new LinkedList<JsonActItem>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_explore_act, container, false);

		findViewById();// 初始化views
		initView();
		// 获取数据
		getDataTask(pageNow);

		actListView.setMode(Mode.BOTH);
		mAdapter = new PostAdapter();
		actListView.setAdapter(mAdapter);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		actListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2) {
			LogTool.e("已报名");
			int position = data.getIntExtra("position", -1);
			if (position > -1) {
				jsonActItemList.get(position).setApply(true);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		actListView = (PullToRefreshListView) rootView.findViewById(R.id.act_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		// 设置上拉下拉刷新事件
		actListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				getDataTask(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (pageNow >= 0)
					++pageNow;
				getDataTask(pageNow);
			}
		});
	}

	/**
	 * 筛选刷新
	 */
	public void screenType(String type) {
		this.actType = type;
		pageNow = 0;
		if (actListView != null) {
			actListView.setRefreshing();
		}
	}

	public void refresh() {
		pageNow = 0;
		if (actListView != null) {
			actListView.setRefreshing();
		}
	}

	/**
	 * 显示更多操作的对话窗口
	 */
	void showMoreDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		PostMoreDialogFragment newFragment = PostMoreDialogFragment.newInstance();
		newFragment.show(ft, "dialog");
	}

	/**
	 * 网络获取数据
	 */
	private void getDataTask(int p) {
		final int page = p;
		RequestParams params = new RequestParams();
		params.put("page", pageNow);
		params.put(UserTable.U_SCHOOLID, userPreference.getU_schoolid());
		params.put(UserTable.U_ID, userPreference.getU_id());
		params.put(ActivityTable.A_ACT_TYPE, actType);
		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					List<JsonActItem> temp = FastJsonTool.getObjectList(response, JsonActItem.class);
					if (temp != null) {
						LogTool.i("获取学校活动列表长度" + temp.size());
						// 如果是首次获取数据
						if (page == 0) {
							if (temp.size() < Config.PAGE_NUM) {
								pageNow = -1;
							}
							jsonActItemList = new LinkedList<JsonActItem>();
							jsonActItemList.addAll(temp);
						}
						// 如果是获取更多
						else if (page > 0) {
							if (temp.size() < Config.PAGE_NUM) {
								pageNow = -1;
								ToastTool.showShort(getActivity(), "没有更多了！");
							}
							jsonActItemList.addAll(temp);
						}
						mAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("获取学校活动列表失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				actListView.onRefreshComplete();
			}
		};
		AsyncHttpClientTool.post(getActivity(), "activity/getSchoolActivities", params, responseHandler);
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：PostAdapter  
	 * 类描述：圈子内帖子的适配器
	 * @author zhangshuai
	 * @date 创建时间：2015-4-19 下午8:55:46 
	 *
	 */
	class PostAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView titleTextView;
			public ImageView headImageView;
			public TextView nameTextView;
			public TextView timeTextView;
			public ImageView contentImageView;
			public TextView actTimeTextView;// 活动时间
			public TextView actLocationTextView;// 活动地点
			public TextView actGenderTextView;// 活动性别要求
			public TextView actContentTextView;// 活动内容介绍
			public TextView actTypeTextView;// 活动类型
			public CheckBox favorBtn;
			public TextView favorCountTextView;
			public ImageView moreBtn;
			public ImageView commentBtn;
			public TextView commentCountTextView;
			public View comment1Container;
			public View comment2Container;
			public TextView commentUser1;
			public TextView label1;
			public TextView toUser1;
			public TextView commentContent1;
			public TextView commentUser2;
			public TextView label2;
			public TextView toUser2;
			public TextView commentContent2;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonActItemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonActItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final JsonActItem jsonActItem = jsonActItemList.get(position);
			if (jsonActItem == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.act_list_item, null);
				holder = new ViewHolder();
				holder.titleTextView = (TextView) view.findViewById(R.id.act_title);
				holder.actTimeTextView = (TextView) view.findViewById(R.id.act_time);
				holder.actLocationTextView = (TextView) view.findViewById(R.id.act_location);
				holder.actGenderTextView = (TextView) view.findViewById(R.id.act_gender);
				holder.actContentTextView = (TextView) view.findViewById(R.id.act_content);
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.contentImageView = (ImageView) view.findViewById(R.id.item_image);
				holder.actTypeTextView = (TextView) view.findViewById(R.id.act_type);
				holder.favorBtn = (CheckBox) view.findViewById(R.id.favor_btn);
				holder.favorCountTextView = (TextView) view.findViewById(R.id.favor_count);
				holder.commentBtn = (ImageView) view.findViewById(R.id.comment_btn);
				holder.commentCountTextView = (TextView) view.findViewById(R.id.comment_count);
				holder.moreBtn = (ImageView) view.findViewById(R.id.more);
				holder.comment1Container = view.findViewById(R.id.comment1_container);
				holder.comment2Container = view.findViewById(R.id.comment2_container);
				holder.commentUser1 = (TextView) view.findViewById(R.id.comment_user_name1);
				holder.label1 = (TextView) view.findViewById(R.id.labe1);
				holder.toUser1 = (TextView) view.findViewById(R.id.to_user_name1);
				holder.commentContent1 = (TextView) view.findViewById(R.id.comment_content1);
				holder.commentUser2 = (TextView) view.findViewById(R.id.comment_user_name2);
				holder.label2 = (TextView) view.findViewById(R.id.labe2);
				holder.toUser2 = (TextView) view.findViewById(R.id.to_user_name2);
				holder.commentContent2 = (TextView) view.findViewById(R.id.comment_content2);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivityForResult(new Intent(getActivity(), ActDetailActivity.class).putExtra(ActDetailActivity.ACT_ITEM, jsonActItem), 1);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			view.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					if (userPreference.getU_id() == jsonActItem.getA_userid()) {
						deleteAct(position);
					}
					return false;
				}
			});
			holder.titleTextView.setText(jsonActItem.getA_title());

			// 设置头像
			if (!TextUtils.isEmpty(jsonActItem.getA_small_avatar())) {
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonActItem.getA_small_avatar()), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(6));
				if (userPreference.getU_id() != jsonActItem.getA_userid()) {
					// 点击头像进入详情页面
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
							intent.putExtra(UserTable.U_ID, jsonActItem.getA_userid());
							startActivity(intent);
							getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			// 设置姓名
			holder.nameTextView.setText(jsonActItem.getA_username());

			// 设置日期
			holder.timeTextView.setText(DateTimeTools.getInterval(jsonActItem.getA_time()));

			// 设置活动日期
			holder.actTimeTextView.setText(DateTimeTools.DateToStringForCN(jsonActItem.getA_act_date()));

			// 设置活动地点
			holder.actLocationTextView.setText(jsonActItem.getA_act_location());

			// 设置活动类型
			holder.actTypeTextView.setText(jsonActItem.getA_act_type());

			// 设置活动对象
			holder.actGenderTextView.setText(jsonActItem.getA_act_target());

			// 设置活动详情
			holder.actContentTextView.setText(jsonActItem.getA_detail_content());

			String[] smallPhotos = null;
			// 设置缩略图
			if (!jsonActItem.getA_thumbnail().isEmpty()) {
				smallPhotos = jsonActItem.getA_thumbnail().split("\\|");
			}

			// 设置照片
			if (smallPhotos != null && !smallPhotos[0].isEmpty()) {
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.contentImageView, ImageLoaderTool.getImageOptions());
				holder.contentImageView.setVisibility(View.VISIBLE);
				holder.contentImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String[] tempBbigPhotoUrls = null;

						if (!jsonActItem.getA_big_photo().isEmpty()) {
							tempBbigPhotoUrls = jsonActItem.getA_big_photo().split("\\|");
							for (int i = 0; i < tempBbigPhotoUrls.length; i++) {
								tempBbigPhotoUrls[i] = AsyncHttpClientTool.getAbsoluteUrl(tempBbigPhotoUrls[i]);
							}
						}
						goBigPhoto(tempBbigPhotoUrls, 0);
					}
				});
			} else {
				holder.contentImageView.setVisibility(View.GONE);
			}

			List<Map<String, String>> comments = jsonActItem.getCommentList();

			// 设置评论
			if (comments != null) {
				if (comments.size() == 0) {
					holder.comment1Container.setVisibility(View.GONE);
					holder.comment2Container.setVisibility(View.GONE);
				} else if (comments.size() == 1) {
					holder.comment1Container.setVisibility(View.VISIBLE);
					holder.commentUser1.setText(comments.get(0).get(CommentTable.C_USER_NICKNAME) + ":");
					holder.commentContent1.setText(comments.get(0).get(CommentTable.C_CONTENT));
					if (comments.get(0).get(CommentTable.COMMENT_TYPE).equals(CommentType.COMMENT)) {// 如果是评论
						holder.toUser1.setVisibility(View.GONE);
						holder.label1.setVisibility(View.GONE);
					} else {// 如果是回复
						holder.toUser1.setVisibility(View.VISIBLE);
						holder.label1.setVisibility(View.VISIBLE);
						holder.toUser1.setText(comments.get(0).get(CommentTable.TO_USER_NICKNAME));
					}
				} else if (comments.size() == 2) {
					holder.comment1Container.setVisibility(View.VISIBLE);
					holder.commentUser1.setText(comments.get(0).get(CommentTable.C_USER_NICKNAME) + ":");
					holder.commentContent1.setText(comments.get(0).get(CommentTable.C_CONTENT));
					if (comments.get(0).get(CommentTable.COMMENT_TYPE).equals(CommentType.COMMENT)) {// 如果是评论
						holder.toUser1.setVisibility(View.GONE);
						holder.label1.setVisibility(View.GONE);
					} else {// 如果是回复
						holder.toUser1.setVisibility(View.VISIBLE);
						holder.label1.setVisibility(View.VISIBLE);
						holder.toUser1.setText(comments.get(0).get(CommentTable.TO_USER_NICKNAME));
					}
					holder.comment2Container.setVisibility(View.VISIBLE);
					holder.commentUser2.setText(comments.get(1).get(CommentTable.C_USER_NICKNAME) + ":");
					holder.commentContent2.setText(comments.get(1).get(CommentTable.C_CONTENT));
					if (comments.get(1).get(CommentTable.COMMENT_TYPE).equals(CommentType.COMMENT)) {// 如果是评论
						holder.toUser2.setVisibility(View.GONE);
						holder.label2.setVisibility(View.GONE);
					} else {// 如果是回复
						holder.toUser2.setVisibility(View.VISIBLE);
						holder.label2.setVisibility(View.VISIBLE);
						holder.toUser2.setText(comments.get(1).get(CommentTable.TO_USER_NICKNAME));
					}
				}
			} else {
				holder.comment1Container.setVisibility(View.GONE);
				holder.comment2Container.setVisibility(View.GONE);
			}

			// 设置评论次数
			if (jsonActItem.getA_comment_count() == 0) {
				holder.commentCountTextView.setVisibility(View.GONE);
			} else {
				holder.commentCountTextView.setVisibility(View.VISIBLE);
				holder.commentCountTextView.setText("查看全部" + jsonActItem.getA_comment_count() + "条评论");
			}

			// 评论
			holder.commentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), ActDetailActivity.class).putExtra(ActDetailActivity.ACT_ITEM, jsonActItem));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			// 设置被赞次数
			if (jsonActItem.getA_favor_count() > 0) {
				holder.favorCountTextView.setText("" + jsonActItem.getA_favor_count() + "赞");
				holder.favorCountTextView.setVisibility(View.VISIBLE);
			} else {
				holder.favorCountTextView.setVisibility(View.GONE);
			}
			holder.favorCountTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), AllFavorsActivity.class).putExtra(AllFavorsActivity.PA_ID, jsonActItem.getA_actid())
							.putExtra(AllFavorsActivity.PA_USERID, jsonActItem.getA_userid())
							.putExtra(AllFavorsActivity.FAVOR_COUNT, jsonActItem.getA_favor_count()).putExtra(AllFavorsActivity.TYPE, "act"));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			// 设置是否赞过
			holder.favorBtn.setChecked(jsonActItem.isLike());

			holder.favorBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RequestParams params = new RequestParams();
					params.put(ActivityTable.A_ACTID, jsonActItem.getA_actid());
					params.put(ActivityTable.A_USERID, jsonActItem.getA_userid());
					params.put(UserTable.U_ID, userPreference.getU_id());

					TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
							if (!jsonActItem.isLike()) {// 喜欢
								holder.favorCountTextView.setText("" + (jsonActItem.getA_favor_count() + 1) + "赞");
								jsonActItem.setA_favor_count(jsonActItem.getA_favor_count() + 1);
								jsonActItem.setLike(true);

							} else {// 喜欢变成不喜欢
								holder.favorCountTextView.setText("" + (jsonActItem.getA_favor_count() - 1) + "赞");
								jsonActItem.setA_favor_count(jsonActItem.getA_favor_count() - 1);
								jsonActItem.setLike(false);
							}
							// 设置被赞次数
							if (jsonActItem.getA_favor_count() > 0) {
								holder.favorCountTextView.setVisibility(View.VISIBLE);
							} else {
								holder.favorCountTextView.setVisibility(View.GONE);
							}
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers, String response) {
							// TODO Auto-generated method stub
							if (statusCode == 200) {
								if (response.equals("1")) {
									LogTool.i("赞成功！");
								} else {
									LogTool.e("学校活动赞返回错误" + response);
								}
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
							// TODO Auto-generated method stub
							LogTool.e("学校活动赞失败");
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							super.onFinish();
						}

					};
					if (!jsonActItem.isLike()) {
						AsyncHttpClientTool.post(getActivity(), "activity/like", params, responseHandler);
					} else {
						AsyncHttpClientTool.post(getActivity(), "activity/unlike", params, responseHandler);
					}
				}
			});

			holder.moreBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showMoreDialog();
				}
			});
			//
			// if (userPreference.getU_id() == jsonPostItem.getN_userid() ||
			// holder.flipperBtn.isChecked()) {
			// holder.flipperBtn.setEnabled(false);
			// } else {
			// //心动
			// holder.flipperBtn.setOnCheckedChangeListener(new
			// OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView, boolean
			// isChecked) {
			// // TODO Auto-generated method stub
			// if (userPreference.getU_id() != jsonPostItem.getN_userid()) {
			// if (isChecked) {
			// flipper(jsonPostItem.getN_id());
			// sendLoveReuest(jsonPostItem.getN_userid());
			// holder.flipperCountTextView.setText("" +
			// (jsonPostItem.getN_flipcount() + 1));
			// holder.flipperBtn.setEnabled(false);
			// }
			// } else {
			// ToastTool.showShort(getActivity(), "不能对自己心动哦~");
			// }
			// }
			// });
			// }
			return view;
		}

		// 查看大图
		public void goBigPhoto(String[] urls, int postion) {
			Intent intent = new Intent(getActivity(), GalleryPictureActivity.class);
			intent.putExtra(GalleryPictureActivity.IMAGE_URLS, urls);
			intent.putExtra(GalleryPictureActivity.POSITON, postion);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
		}

		/**
		 * 删活动
		 */
		private void deleteAct(final int position) {

			final MyAlertDialog dialog = new MyAlertDialog(getActivity());
			dialog.setTitle("删除");
			dialog.setMessage("删除活动不可逆");
			View.OnClickListener comfirm = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();

					jsonActItemList.remove(position);
					mAdapter.notifyDataSetChanged();
					// RequestParams params = new RequestParams();
					// params.put(UserTable.U_ID, userPreference.getU_id());
					// TextHttpResponseHandler responseHandler = new
					// TextHttpResponseHandler("utf-8") {
					//
					// @Override
					// public void onSuccess(int statusCode, Header[] headers,
					// String response) {
					// // TODO Auto-generated method stub
					// if (statusCode == 200) {
					// if (response.equals("1")) {
					// jsonPostItemList.remove(position);
					// mAdapter.notifyDataSetChanged();
					// }
					// }
					// }
					//
					// @Override
					// public void onFailure(int statusCode, Header[] headers,
					// String errorResponse, Throwable e) {
					// // TODO Auto-generated method stub
					// }
					//
					// };
					// AsyncHttpClientTool.post(getActivity(), "", params,
					// responseHandler);
				}
			};
			View.OnClickListener cancle = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			};
			dialog.setPositiveButton("确定", comfirm);
			dialog.setNegativeButton("取消", cancle);
			dialog.show();
		}
	}

}
