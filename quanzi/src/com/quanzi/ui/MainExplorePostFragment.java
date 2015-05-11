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
import android.widget.ListAdapter;
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
import com.quanzi.config.Constants;
import com.quanzi.jsonobject.JsonPostItem;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：MainExplorePostFragment  
 * 类描述：探索页面的帖子子页面
 * @author zhangshuai
 * @date 创建时间：2015-4-20 下午3:28:52 
 *
 */
public class MainExplorePostFragment extends BaseV4Fragment {
	private View rootView;// 根View

	private PullToRefreshListView postListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private UserPreference userPreference;
	private LinkedList<JsonPostItem> jsonPostItemList;
	private int pageNow = 0;//控制页数
	private PostAdapter mAdapter;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonPostItemList = new LinkedList<JsonPostItem>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main_explore_post, container, false);

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
		postListView = (PullToRefreshListView) rootView.findViewById(R.id.post_list);
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
		JsonPostItem item1 = new JsonPostItem(1, 1, "张帅", "drawable://" + R.drawable.headimage1, "男", 78,
				"一见倾心，再见依然痴迷", "drawable://" + R.drawable.content, new Date(), 20, 2);
		JsonPostItem item2 = new JsonPostItem(2, 2, "叶子", "drawable://" + R.drawable.headimage2, "女", 8, "你的美丽让我情不自禁",
				"drawable://" + R.drawable.content2, new Date(), 45, 20);
		JsonPostItem item3 = new JsonPostItem(3, 3, "荣发", "drawable://" + R.drawable.headimage3, "男", 78,
				"这是一片很寂寞的天下着有些伤心的雨", "drawable://" + R.drawable.content, new Date(), 76, 32);
		JsonPostItem item4 = new JsonPostItem(4, 4, "伟强", "drawable://" + R.drawable.headimage4, "女", 8,
				"爱上你的日子，每天都在想你", "drawable://" + R.drawable.content2, new Date(), 26, 280);
		JsonPostItem item5 = new JsonPostItem(5, 5, "王坤", "drawable://" + R.drawable.headimage5, "女", 8,
				"卡又丢了，快来点开心的事冲冲喜吧！", "drawable://" + R.drawable.content, new Date(), 256, 46);
		jsonPostItemList.add(item1);
		jsonPostItemList.add(item2);
		jsonPostItemList.add(item3);
		jsonPostItemList.add(item4);
		jsonPostItemList.add(item5);
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
			public ImageView headImageView;
			public TextView nameTextView;
			public ImageView genderImageView;
			public CheckBox concernBtn;
			public TextView timeTextView;
			public TextView contentTextView;
			public ImageView contentImageView;
			public CheckBox favorBtn;
			public TextView favorCountTextView;
			public ImageView commentBtn;
			public TextView commentCountTextView;
			public ImageView moreBtn;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonPostItemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonPostItemList.get(position);
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
			final JsonPostItem jsonPostItem = jsonPostItemList.get(position);
			if (jsonPostItem == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.post_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.genderImageView = (ImageView) view.findViewById(R.id.gender);
				holder.concernBtn = (CheckBox) view.findViewById(R.id.concern_btn);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.contentTextView = (TextView) view.findViewById(R.id.content);
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

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonPostItem));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//设置头像
			if (!TextUtils.isEmpty(jsonPostItem.getN_small_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_small_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				imageLoader.displayImage(jsonPostItem.getN_small_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != jsonPostItem.getN_userid()) {
					//点击头像进入详情页面
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
							//							intent.putExtra(PersonDetailActivity.PERSON_TYPE, Constants.PersonDetailType.SINGLE);
							//							intent.putExtra(UserTable.U_ID, jsonPostItem.getN_userid());
							startActivity(intent);
							getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//设置照片
			if (!TextUtils.isEmpty(jsonPostItem.getN_image())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_image()),
				//						holder.contentImageView, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(jsonPostItem.getN_image(), holder.contentImageView,
						ImageLoaderTool.getImageOptions());
				holder.contentImageView.setVisibility(View.VISIBLE);
				holder.contentImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//						Intent intent = new Intent(getActivity(), ImageShowerActivity.class);
						//						intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE,
						//								AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_image()));
						//						startActivity(intent);
						//						getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
					}
				});
			} else {
				holder.contentImageView.setVisibility(View.GONE);
			}

			//设置内容
			holder.contentTextView.setText(jsonPostItem.getN_content());

			//设置姓名
			holder.nameTextView.setText(jsonPostItem.getN_name());

			//显示关注
			holder.concernBtn.setVisibility(View.VISIBLE);

			//设置性别
			if (jsonPostItem.getN_gender().equals(Constants.Gender.MALE)) {
				holder.genderImageView.setImageResource(R.drawable.male);
			} else {
				holder.genderImageView.setImageResource(R.drawable.female);
			}

			//设置日期
			holder.timeTextView.setText(DateTimeTools.getHourAndMin(jsonPostItem.getN_time()));

			//设置被赞次数
			holder.favorCountTextView.setText("" + jsonPostItem.getN_flipcount() + "赞");
			holder.favorCountTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), AllFavorsActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonPostItem));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//设置评论次数
			holder.commentCountTextView.setText("查看全部" + jsonPostItem.getN_commentcount() + "条评论");

			//评论
			holder.commentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonPostItem));
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
