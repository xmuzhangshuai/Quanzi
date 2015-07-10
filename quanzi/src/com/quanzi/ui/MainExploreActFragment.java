package com.quanzi.ui;

import java.util.Date;
import java.util.LinkedList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.jsonobject.JsonActItem;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.ImageLoaderTool;
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
	private View rootView;// 根View

	private PullToRefreshListView postListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private UserPreference userPreference;
	private LinkedList<JsonActItem> jsonActItemList;
	private int pageNow = 0;//控制页数
	private PostAdapter mAdapter;
	private ProgressDialog progressDialog;

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
		//获取数据
		getDataTask(pageNow);

		postListView.setMode(Mode.BOTH);
		mAdapter = new PostAdapter();
		postListView.setAdapter(mAdapter);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		postListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		postListView = (PullToRefreshListView) rootView.findViewById(R.id.act_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		//设置上拉下拉刷新事件
		postListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

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

	/**
	 * 显示更多操作的对话窗口
	 */
	void showMoreDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
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
		//		final int page = p;
		//		RequestParams params = new RequestParams();
		//		params.put("page", pageNow);
		//		params.put(UserTable.U_SCHOOLID, userPreference.getU_schoolid());
		//		params.put(UserTable.U_STATEID, userPreference.getU_stateid());
		//		params.put(UserTable.U_GENDER, userPreference.getU_gender());
		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//				if (statusCode == 200) {
		//					List<JsonPostItem> temp = FastJsonTool.getObjectList(response, JsonPostItem.class);
		//					if (temp != null) {
		//						//如果是首次获取数据
		//						if (page == 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//							}
		//							jsonPostItemList = new LinkedList<JsonPostItem>();
		//							jsonPostItemList.addAll(temp);
		//						}
		//						//如果是获取更多
		//						else if (page > 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//								ToastTool.showShort(getActivity(), "没有更多了！");
		//							}
		//							jsonPostItemList.addAll(temp);
		//						}
		//						mAdapter.notifyDataSetChanged();
		//					}
		//				}
		//				postListView.onRefreshComplete();
		//			}
		//
		//			@Override
		//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//				// TODO Auto-generated method stub
		//				LogTool.e("LoveBridgeSchoolFragment", "获取列表失败");
		//				postListView.onRefreshComplete();
		//			}
		//		};
		//		AsyncHttpClientTool.post(getActivity(), "getlovebridgelist", params, responseHandler);
		JsonActItem item1 = new JsonActItem(1, "一起来看流星雨", 1, "张帅", "drawable://" + R.drawable.headimage1, "drawable://"
				+ R.drawable.headimage1, "男", new Date(), "卡又丢了，快来点开心的事冲冲喜吧！", "drawable://" + R.drawable.content,
				"drawable://" + R.drawable.content, new Date(), "厦门大学三家村广场", "男女不限", "讲座", 32, 56);
		jsonActItemList.add(item1);
		jsonActItemList.add(item1);
		jsonActItemList.add(item1);
		jsonActItemList.add(item1);
		jsonActItemList.add(item1);
		postListView.onRefreshComplete();
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
			public TextView actTimeTextView;//活动时间
			public TextView actLocationTextView;//活动地点
			public TextView actGenderTextView;//活动性别要求
			public TextView actContentTextView;//活动内容介绍
			public CheckBox favorBtn;
			public TextView favorCountTextView;
			public ImageView moreBtn;
			public ImageView commentBtn;
			public TextView commentCountTextView;

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
		public View getView(int position, View convertView, ViewGroup parent) {
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
				holder.favorBtn = (CheckBox) view.findViewById(R.id.favor_btn);
				holder.favorCountTextView = (TextView) view.findViewById(R.id.favor_count);
				holder.commentBtn = (ImageView) view.findViewById(R.id.comment_btn);
				holder.commentCountTextView = (TextView) view.findViewById(R.id.comment_count);
				holder.moreBtn = (ImageView) view.findViewById(R.id.more);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.titleTextView.setText(jsonActItem.getA_title());

			//设置头像
			if (!TextUtils.isEmpty(jsonActItem.getA_small_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_small_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				imageLoader.displayImage(jsonActItem.getA_small_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(6));
				if (userPreference.getU_id() != jsonActItem.getA_userid()) {
					//点击头像进入详情页面
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//							Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
							//							intent.putExtra(PersonDetailActivity.PERSON_TYPE, Constants.PersonDetailType.SINGLE);
							//							intent.putExtra(UserTable.U_ID, jsonPostItem.getN_userid());
							//							startActivity(intent);
							//							getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//设置姓名
			holder.nameTextView.setText(jsonActItem.getA_username());

			//设置日期
			holder.timeTextView.setText(DateTimeTools.getHourAndMin(jsonActItem.getA_time()));

			//设置活动日期
			holder.actTimeTextView.setText(DateTimeTools.DateToStringForCN(jsonActItem.getA_act_date()));

			//设置活动地点
			holder.actLocationTextView.setText(jsonActItem.getA_act_location());

			//设置活动对象
			holder.actGenderTextView.setText(jsonActItem.getA_act_target());

			//设置活动详情
			holder.actContentTextView.setText(jsonActItem.getA_detail_content());

			//

			//设置照片
			if (!TextUtils.isEmpty(jsonActItem.getA_thumbnail())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_image()),
				//						holder.contentImageView, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(jsonActItem.getA_thumbnail(), holder.contentImageView,
						ImageLoaderTool.getImageOptions());
				holder.contentImageView.setVisibility(View.VISIBLE);
				holder.contentImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(), ImageShowerActivity.class);
						intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, jsonActItem.getA_big_photo());
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
					}
				});
			} else {
				holder.contentImageView.setVisibility(View.GONE);
			}

			//设置被赞次数
			holder.favorCountTextView.setText("" + jsonActItem.getA_favor_count() + "赞");
			holder.favorCountTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), AllFavorsActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonActItem));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//设置评论次数
			holder.commentCountTextView.setText("查看全部" + jsonActItem.getA_comment_count() + "条评论");

			//评论
			holder.commentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), ActDetailActivity.class).putExtra(
							ActDetailActivity.ACT_ITEM, jsonActItem));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//			holder.flipperBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			//				@Override
			//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//					// TODO Auto-generated method stub
			//
			//					if (isChecked) {
			//						flipper(jsonPostItem.getN_id());
			//						sendLoveReuest(jsonPostItem.getN_userid());
			//					}
			//				}
			//			});
			//			
			holder.moreBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showMoreDialog();
				}
			});
			//
			//			if (userPreference.getU_id() == jsonPostItem.getN_userid() || holder.flipperBtn.isChecked()) {
			//				holder.flipperBtn.setEnabled(false);
			//			} else {
			//				//心动
			//				holder.flipperBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			//					@Override
			//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//						// TODO Auto-generated method stub
			//						if (userPreference.getU_id() != jsonPostItem.getN_userid()) {
			//							if (isChecked) {
			//								flipper(jsonPostItem.getN_id());
			//								sendLoveReuest(jsonPostItem.getN_userid());
			//								holder.flipperCountTextView.setText("" + (jsonPostItem.getN_flipcount() + 1));
			//								holder.flipperBtn.setEnabled(false);
			//							}
			//						} else {
			//							ToastTool.showShort(getActivity(), "不能对自己心动哦~");
			//						}
			//					}
			//				});
			//			}
			return view;
		}
	}

}
