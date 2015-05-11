package com.quanzi.ui;

import java.util.Date;
import java.util.LinkedList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
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
import com.quanzi.config.Constants;
import com.quanzi.jsonobject.JsonPostItem;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.DensityUtil;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanquan  
 * 类名称：MainHomeFragment  
 * 类描述：圈子动态的页面，也是主页面
 * @author zhangshuai
 * @date 创建时间：2015-4-12 下午8:53:06 
 *
 */
public class MainHomeFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// 根View
	private TextView leftTextView;//导航栏左侧文字
	private View searchBtn;//查找按钮
	private View publishBtn;//发布按钮

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
		rootView = inflater.inflate(R.layout.fragment_mainhome, container, false);

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
		leftTextView = (TextView) rootView.findViewById(R.id.left_text1);
		searchBtn = rootView.findViewById(R.id.nav_right_btn2);
		publishBtn = rootView.findViewById(R.id.nav_right_btn1);
		postListView = (PullToRefreshListView) rootView.findViewById(R.id.post_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("圈子");
		searchBtn.setOnClickListener(this);
		publishBtn.setOnClickListener(this);

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nav_right_btn1://发布
			showPublishDialog();
			break;
		case R.id.nav_right_btn2://查找
			getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
			getActivity().overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
			break;
		default:
			break;
		}
	}

	/**
	 * 菜单显示
	 */
	void showPublishDialog() {

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
		PublishDialogFragment newFragment = PublishDialogFragment.newInstance();
		newFragment.show(ft, "dialog");
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
		jsonPostItemList.add(item2);
		jsonPostItemList.add(item1);
		jsonPostItemList.add(item2);
		jsonPostItemList.add(item3);
		jsonPostItemList.add(item4);
		jsonPostItemList.add(item5);
		jsonPostItemList.add(item2);
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
			public TextView timeTextView;
			public TextView contentTextView;
			public ImageView itemImageView;
			public ImageView itemImageView1;
			public ImageView itemImageView2;
			public ImageView itemImageView3;
			public ImageView itemImageView4;
			public ImageView itemImageView5;
			public ImageView itemImageView6;
			public View imageViewGroup2;
			public View imageViewGroup1;
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
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.contentTextView = (TextView) view.findViewById(R.id.content);
				holder.itemImageView = (ImageView) view.findViewById(R.id.item_image);
				holder.itemImageView1 = (ImageView) view.findViewById(R.id.item_image1);
				holder.itemImageView2 = (ImageView) view.findViewById(R.id.item_image2);
				holder.itemImageView3 = (ImageView) view.findViewById(R.id.item_image3);
				holder.itemImageView4 = (ImageView) view.findViewById(R.id.item_image4);
				holder.itemImageView5 = (ImageView) view.findViewById(R.id.item_image5);
				holder.itemImageView6 = (ImageView) view.findViewById(R.id.item_image6);
				holder.imageViewGroup2 = view.findViewById(R.id.item_image_group2);
				holder.imageViewGroup1 = view.findViewById(R.id.item_image_group1);
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

			if (position == 0) {//只有一张图片
				//				holder.itemImageView1.setVisibility(View.VISIBLE);
				//				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.itemImageView.setVisibility(View.VISIBLE);
				imageLoader.displayImage("drawable://" + R.drawable.content, holder.itemImageView,
						ImageLoaderTool.getImageOptions());
			} else if (position == 1) {//有两张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.INVISIBLE);
				imageLoader.displayImage("drawable://" + R.drawable.content, holder.itemImageView1,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content2, holder.itemImageView2,
						ImageLoaderTool.getImageOptions());
			} else if (position == 2) {//有三张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.VISIBLE);
				imageLoader.displayImage("drawable://" + R.drawable.content, holder.itemImageView1,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content2, holder.itemImageView2,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content3, holder.itemImageView3,
						ImageLoaderTool.getImageOptions());
			} else if (position == 3) {//有四张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.VISIBLE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.INVISIBLE);
				holder.itemImageView4.setVisibility(View.VISIBLE);
				holder.itemImageView5.setVisibility(View.VISIBLE);
				holder.itemImageView6.setVisibility(View.INVISIBLE);
				imageLoader.displayImage("drawable://" + R.drawable.content, holder.itemImageView1,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content2, holder.itemImageView2,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content3, holder.itemImageView4,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content4, holder.itemImageView5,
						ImageLoaderTool.getImageOptions());
			} else if (position == 4) {//有五张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.VISIBLE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.VISIBLE);
				holder.itemImageView4.setVisibility(View.VISIBLE);
				holder.itemImageView5.setVisibility(View.VISIBLE);
				holder.itemImageView6.setVisibility(View.INVISIBLE);
				imageLoader.displayImage("drawable://" + R.drawable.content, holder.itemImageView1,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content2, holder.itemImageView2,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content3, holder.itemImageView3,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content4, holder.itemImageView4,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content5, holder.itemImageView5,
						ImageLoaderTool.getImageOptions());
			} else if (position == 5) {//有六张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.VISIBLE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.VISIBLE);
				holder.itemImageView4.setVisibility(View.VISIBLE);
				holder.itemImageView5.setVisibility(View.VISIBLE);
				holder.itemImageView6.setVisibility(View.VISIBLE);
				imageLoader.displayImage("drawable://" + R.drawable.content, holder.itemImageView1,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content2, holder.itemImageView2,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content3, holder.itemImageView3,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content4, holder.itemImageView4,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content5, holder.itemImageView5,
						ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content6, holder.itemImageView6,
						ImageLoaderTool.getImageOptions());
			} else {
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
			}

			//设置照片
			//			if (!TextUtils.isEmpty(jsonPostItem.getN_image())) {
			//				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_image()),
			//				//						holder.contentImageView, ImageLoaderTool.getImageOptions());
			//				imageLoader.displayImage(jsonPostItem.getN_image(), holder.contentImageView,
			//						ImageLoaderTool.getImageOptions());
			//				holder.contentImageView.setVisibility(View.VISIBLE);
			//				holder.contentImageView.setOnClickListener(new OnClickListener() {
			//
			//					@Override
			//					public void onClick(View v) {
			//						// TODO Auto-generated method stub
			//						//						Intent intent = new Intent(getActivity(), ImageShowerActivity.class);
			//						//						intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE,
			//						//								AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_image()));
			//						//						startActivity(intent);
			//						//						getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			//					}
			//				});
			//			} else {
			//				holder.contentImageView.setVisibility(View.GONE);
			//			}

			//设置内容
			holder.contentTextView.setText(jsonPostItem.getN_content());

			//设置姓名
			holder.nameTextView.setText(jsonPostItem.getN_name());

			//设置性别
			if (jsonPostItem.getN_gender().equals(Constants.Gender.MALE)) {
				holder.genderImageView.setImageResource(R.drawable.male);
			} else {
				holder.genderImageView.setImageResource(R.drawable.female);
			}

			//设置日期
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(jsonPostItem.getN_time()));

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
