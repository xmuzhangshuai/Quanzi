package com.quanzi.ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragment;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.config.Constants;
import com.quanzi.config.Constants.Config;
import com.quanzi.jsonobject.JsonPostComment;
import com.quanzi.jsonobject.JsonPostItem;
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
 * 类名称：PostDetailActivity  
 * 类描述：帖子的详情页面
 * @author zhangshuai
 * @date 创建时间：2015-4-29 下午10:22:00 
 *
 */
public class PostDetailActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String POST_ITEM = "post_item";
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	private View headView;//详情区域
	private View footView;
	private ImageView headImageView;
	private TextView nameTextView;
	private ImageView genderImageView;
	private TextView timeTextView;
	private TextView contentTextView;
	private ImageView contentImageView;
	private CheckBox favorBtn;
	private TextView favorCountTextView;
	//	private TextView commentCountTextView;
	private ImageView moreBtn;
	public ImageView commentBtn;
	private EditText commentEditText;
	private Button sendBtn;
	//	private TextView topNavigation;//导航栏文字
	private View leftImageButton;//导航栏左侧按钮
	//	private View rightBtnBg;//导航栏右侧按钮
	//	private ImageView rightBtn;

	private View actionBar;//操作条
	private View inputBar;//输入评论条

	private UserPreference userPreference;
	private PullToRefreshListView loveBridgeListView;
	private JsonPostItem jsonPostItem;
	private int pageNow = 0;//控制页数
	private CommentAdapter mAdapter;
	private LinkedList<JsonPostComment> commentList;
	private InputMethodManager inputMethodManager;
	private int commentCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post_detail);
		userPreference = BaseApplication.getInstance().getUserPreference();
		commentList = new LinkedList<JsonPostComment>();
		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		jsonPostItem = (JsonPostItem) getIntent().getSerializableExtra(POST_ITEM);

		findViewById();
		initView();

		//获取数据
		getDataTask(pageNow);
		loveBridgeListView.setMode(Mode.BOTH);
		ListView mListView = loveBridgeListView.getRefreshableView();
		mListView.addHeaderView(headView);
		mListView.addFooterView(footView);
		mAdapter = new CommentAdapter();
		loveBridgeListView.setAdapter(mAdapter);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loveBridgeListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		loveBridgeListView = (PullToRefreshListView) findViewById(R.id.comment_list);
		headView = getLayoutInflater().inflate(R.layout.post_detail_headview, null);
		footView = getLayoutInflater().inflate(R.layout.comment_foot_view, null);
		headImageView = (ImageView) headView.findViewById(R.id.head_image);
		nameTextView = (TextView) headView.findViewById(R.id.name);
		genderImageView = (ImageView) headView.findViewById(R.id.gender);
		timeTextView = (TextView) headView.findViewById(R.id.time);
		contentTextView = (TextView) headView.findViewById(R.id.content);
		contentImageView = (ImageView) headView.findViewById(R.id.item_image);
		favorBtn = (CheckBox) findViewById(R.id.favor_btn);
		favorCountTextView = (TextView) findViewById(R.id.favor_count);
		//		commentCountTextView = (TextView) findViewById(R.id.comment_count);
		moreBtn = (ImageView) findViewById(R.id.more);
		commentEditText = (EditText) findViewById(R.id.msg_et);
		commentBtn = (ImageView) findViewById(R.id.comment_btn);
		sendBtn = (Button) findViewById(R.id.send_btn);
		//		topNavigation = (TextView) findViewById(R.id.nav_text);
		leftImageButton = (View) findViewById(R.id.left_btn_bg);
		//		rightBtnBg = (View) findViewById(R.id.right_btn_bg);
		//		rightBtn = (ImageView) findViewById(R.id.nav_right_btn);
		actionBar = findViewById(R.id.actionbar);
		inputBar = findViewById(R.id.inputBar);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		//		rightBtn.setImageResource(R.drawable.refresh);
		leftImageButton.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		inputBar.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					inputBar.setVisibility(View.GONE);
					actionBar.setVisibility(View.VISIBLE);
				}
			}
		});

		//		rightBtnBg.setOnClickListener(this);
		//		topNavigation.setText("评论");
		//		favorBtn.setChecked(true);
		//		favorBtn.setEnabled(false);

		//设置上拉下拉刷新事件
		loveBridgeListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(PostDetailActivity.this.getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				getDataTask(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(PostDetailActivity.this.getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (pageNow >= 0)
					++pageNow;
				getDataTask(pageNow);
			}
		});

		if (jsonPostItem != null) {
			//设置头像
			if (!TextUtils.isEmpty(jsonPostItem.getN_small_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_small_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				imageLoader.displayImage(jsonPostItem.getN_small_avatar(), headImageView,
						ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != jsonPostItem.getN_userid()) {
					//点击头像进入详情页面
					headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//							Intent intent = new Intent(LoveBridgeDetailActivity.this, PersonDetailActivity.class);
							//							intent.putExtra(PersonDetailActivity.PERSON_TYPE, Constants.PersonDetailType.SINGLE);
							//							intent.putExtra(UserTable.U_ID, loveBridgeItem.getN_userid());
							//							startActivity(intent);
							//							LoveBridgeDetailActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//设置照片
			if (!TextUtils.isEmpty(jsonPostItem.getN_image())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_image()),
				//						holder.contentImageView, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage("drawable://" + R.drawable.content, contentImageView,
						ImageLoaderTool.getImageOptions());
				contentImageView.setVisibility(View.VISIBLE);
				contentImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//						Intent intent = new Intent(LoveBridgeDetailActivity.this, ImageShowerActivity.class);
						//						intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE,
						//								AsyncHttpClientImageSound.getAbsoluteUrl(loveBridgeItem.getN_image()));
						//						startActivity(intent);
						//						LoveBridgeDetailActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
					}
				});
			} else {
				contentImageView.setVisibility(View.GONE);
			}

			moreBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showMoreDialog();
				}
			});

			//设置内容
			contentTextView.setText(jsonPostItem.getN_content());

			//设置姓名
			nameTextView.setText(jsonPostItem.getN_name());

			//设置性别
			if (jsonPostItem.getN_gender().equals(Constants.Gender.MALE)) {
				genderImageView.setImageResource(R.drawable.male);
			} else {
				genderImageView.setImageResource(R.drawable.female);
			}

			//设置日期
			timeTextView.setText(DateTimeTools.getHourAndMin(jsonPostItem.getN_time()));

			//设置赞的次数
			favorCountTextView.setText("" + jsonPostItem.getN_flipcount() + "赞");

			//			commentCount = jsonPostItem.getN_commentcount();
			//			//设置评论次数
			//			commentCountTextView.setText("" + commentCount);

			commentEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (s.length() > 0) {
						sendBtn.setEnabled(true);
					} else {
						sendBtn.setEnabled(false);
					}
				}
			});
			sendBtn.setOnClickListener(this);
		}
	}

	/**
	 * 更新数据
	 */
	private void refresh() {
		loveBridgeListView.setRefreshing();
		pageNow = 0;
		getDataTask(pageNow);
	}

	/**
	 * 显示更多操作的对话窗口
	 */
	void showMoreDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		PostMoreDialogFragment newFragment = PostMoreDialogFragment.newInstance();
		newFragment.show(ft, "dialog");
	}

	/**
	* 隐藏软键盘
	*/
	private void hideKeyboard() {
		inputMethodManager.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.right_btn_bg:
			refresh();
			break;
		case R.id.send_btn:
			comment(commentEditText.getText().toString().trim());
			break;
		case R.id.comment_btn:
			actionBar.setVisibility(View.GONE);
			inputBar.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 评论
	 */
	private void comment(String content) {
		//		if (jsonPostItem != null && !TextUtils.isEmpty(content)) {
		//			RequestParams params = new RequestParams();
		//
		//			//如果是单身
		//			params.put(BridgeCommentTable.N_ID, jsonPostItem.getN_id());
		//			params.put(BridgeCommentTable.C_USERID, userPreference.getU_id());
		//			params.put(BridgeCommentTable.C_CONTENT, content);
		//			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//
		//				@Override
		//				public void onSuccess(int statusCode, Header[] headers, String response) {
		//					// TODO Auto-generated method stub
		//					if (statusCode == 200) {
		//						commentEditText.setText("");
		//						//设置评论次数
		//						commentCountTextView.setText("" + (++commentCount));
		//						refresh();
		//						hideKeyboard();
		//					}
		//				}
		//
		//				@Override
		//				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//					// TODO Auto-generated method stub
		//					LogTool.e("LoveBridgeDetail", "评论失败");
		//				}
		//			};
		//			AsyncHttpClientTool.post(LoveBridgeDetailActivity.this, "addbridgecomment", params, responseHandler);
		//		}
	}

	/**
	 * 网络获取数据
	 */
	private void getDataTask(int p) {
		//		if (jsonPostItem == null) {
		//			return;
		//		}
		//		final int page = p;
		//		RequestParams params = new RequestParams();
		//		params.put("page", pageNow);
		//		//如果是单身
		//		params.put(LoveBridgeItemTable.N_ID, jsonPostItem.getN_id());
		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//				if (statusCode == 200) {
		//					LogTool.i("LoveBridgeDetail", "长度" + commentList.size());
		//					List<JsonBridgeComment> temp = FastJsonTool.getObjectList(response, JsonBridgeComment.class);
		//					if (temp != null) {
		//						//如果是首次获取数据
		//						if (page == 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//							}
		//							commentList = new LinkedList<JsonBridgeComment>();
		//							commentList.addAll(temp);
		//						}
		//						//如果是获取更多
		//						else if (page > 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//								ToastTool.showShort(LoveBridgeDetailActivity.this, "没有更多了！");
		//							}
		//							commentList.addAll(temp);
		//						}
		//						mAdapter.notifyDataSetChanged();
		//					}
		//				}
		//				loveBridgeListView.onRefreshComplete();
		//			}
		//
		//			@Override
		//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//				// TODO Auto-generated method stub
		//				LogTool.e("LoveBridgeDetail", "获取列表失败");
		//				loveBridgeListView.onRefreshComplete();
		//			}
		//		};
		//		AsyncHttpClientTool.post(PostDetailActivity.this, "getbridgecommentlist", params, responseHandler);

		JsonPostComment postComment1 = new JsonPostComment(1, 1, 3, "drawable://" + R.drawable.headimage5, "蔡卓妍", "女",
				"好美丽", "", new Date());
		JsonPostComment postComment2 = new JsonPostComment(2, 2, 4, "drawable://" + R.drawable.headimage4, "黄晓明", "男",
				"小妞，来一个~！", "", new Date());
		JsonPostComment postComment3 = new JsonPostComment(3, 1, 3, "drawable://" + R.drawable.headimage1, "刘德华", "女",
				"好美丽", "", new Date());
		JsonPostComment postComment4 = new JsonPostComment(4, 2, 4, "drawable://" + R.drawable.headimage3, "黄秋生", "男",
				"小妞，来一个~！", "", new Date());
		JsonPostComment postComment5 = new JsonPostComment(5, 1, 3, "drawable://" + R.drawable.headimage6, "SHECKO",
				"女", "好美丽", "", new Date());
		JsonPostComment postComment6 = new JsonPostComment(6, 2, 4, "drawable://" + R.drawable.headimage7, "曾志伟", "男",
				"小妞，来一个~！", "", new Date());
		commentList.add(postComment1);
		commentList.add(postComment2);
		commentList.add(postComment3);
		commentList.add(postComment4);
		commentList.add(postComment5);
		commentList.add(postComment6);
		loveBridgeListView.onRefreshComplete();
	}

	/**
	 * 
	 * 类名称：CommentAdapter
	 * 类描述：适配器
	 * 创建人： 张帅
	 * 创建时间：2014年9月14日 上午10:46:10
	 *
	 */
	class CommentAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
			public ImageView genderImageView;
			public TextView timeTextView;
			public TextView contentTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return commentList.get(position);
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
			final JsonPostComment postComment = commentList.get(position);
			if (postComment == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(PostDetailActivity.this).inflate(R.layout.comment_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.genderImageView = (ImageView) view.findViewById(R.id.gender);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.contentTextView = (TextView) view.findViewById(R.id.content);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			//设置头像
			if (!TextUtils.isEmpty(jsonPostItem.getN_small_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(postComment.getC_small_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				imageLoader.displayImage(postComment.getC_small_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != postComment.getC_userid()) {
					//点击头像进入详情页面
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//							Intent intent = new Intent(PostDetailActivity.this, PersonDetailActivity.class);
							//							intent.putExtra(PersonDetailActivity.PERSON_TYPE, Constants.PersonDetailType.SINGLE);
							//							intent.putExtra(UserTable.U_ID, postComment.getC_userid());
							//							startActivity(intent);
							//							PostDetailActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//设置内容
			holder.contentTextView.setText(postComment.getC_content());

			//设置姓名
			holder.nameTextView.setText(postComment.getC_nickname());

			//设置性别
			if (postComment.getC_gender().equals(Constants.Gender.MALE)) {
				holder.genderImageView.setImageResource(R.drawable.male);
			} else {
				holder.genderImageView.setImageResource(R.drawable.female);
			}

			//设置日期
			holder.timeTextView.setText(DateTimeTools.getHourAndMin(postComment.getC_time()));

			return view;
		}
	}
}
