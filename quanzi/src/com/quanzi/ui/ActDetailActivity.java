package com.quanzi.ui;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.config.Constants;
import com.quanzi.config.Constants.CommentType;
import com.quanzi.config.Constants.Config;
import com.quanzi.jsonobject.JsonActItem;
import com.quanzi.jsonobject.JsonComment;
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
 * 类名称：ActDetailActivity  
 * 类描述：活动的详情页面
 * @author zhangshuai
 * @date 创建时间：2015-4-29 下午10:22:32 
 *
 */
public class ActDetailActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String ACT_ITEM = "act_item";
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	private View headView;//详情区域
	private View footView;
	public TextView titleTextView;
	public ImageView headImageView;
	public TextView nameTextView;
	public TextView timeTextView;
	public ImageView contentImageView;
	public TextView actTimeTextView;//活动时间
	public TextView actLocationTextView;//活动地点
	public TextView actGenderTextView;//活动性别要求
	public TextView actContentTextView;//活动内容介绍
	public TextView participantCountTextView;//参加活动人数
	private ImageView itemImageView;
	private ImageView itemImageView1;
	private ImageView itemImageView2;
	private ImageView itemImageView3;
	private ImageView itemImageView4;
	private ImageView itemImageView5;
	private ImageView itemImageView6;
	private View imageViewGroup2;
	private View imageViewGroup1;
	public TextView applyBtn;//我要报名
	private EditText commentEditText;
	private Button sendBtn;
	private View leftImageButton;//导航栏左侧按钮
	private UserPreference userPreference;
	private PullToRefreshListView actCommentListView;
	private JsonActItem jsonActItem;
	private int pageNow = 0;//控制页数
	private CommentAdapter mAdapter;
	private LinkedList<JsonComment> commentList;
	private InputMethodManager inputMethodManager;

	private boolean isReply = false;
	private int toUserID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_act_detail);
		userPreference = BaseApplication.getInstance().getUserPreference();
		commentList = new LinkedList<JsonComment>();
		commentList.add(new JsonComment());
		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		jsonActItem = (JsonActItem) getIntent().getSerializableExtra(ACT_ITEM);
		if (jsonActItem != null) {
			toUserID = jsonActItem.getA_userid();
		}

		findViewById();
		initView();

		//获取数据
		getDataTask(pageNow);
		actCommentListView.setMode(Mode.BOTH);
		ListView mListView = actCommentListView.getRefreshableView();
		mListView.addHeaderView(headView);
		mListView.addFooterView(footView);
		mAdapter = new CommentAdapter();
		actCommentListView.setAdapter(mAdapter);
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
		actCommentListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		actCommentListView = (PullToRefreshListView) findViewById(R.id.act_comment_list);
		headView = getLayoutInflater().inflate(R.layout.act_detail_headview, null);
		footView = getLayoutInflater().inflate(R.layout.comment_foot_view, null);
		titleTextView = (TextView) headView.findViewById(R.id.act_title);
		actTimeTextView = (TextView) headView.findViewById(R.id.act_time);
		actLocationTextView = (TextView) headView.findViewById(R.id.act_location);
		actGenderTextView = (TextView) headView.findViewById(R.id.act_gender);
		actContentTextView = (TextView) headView.findViewById(R.id.act_content);
		participantCountTextView = (TextView) headView.findViewById(R.id.participants_count);
		applyBtn = (TextView) headView.findViewById(R.id.apply_btn);
		headImageView = (ImageView) headView.findViewById(R.id.head_image);
		nameTextView = (TextView) headView.findViewById(R.id.name);
		timeTextView = (TextView) headView.findViewById(R.id.time);
		contentImageView = (ImageView) headView.findViewById(R.id.item_image);
		commentEditText = (EditText) findViewById(R.id.msg_et);
		sendBtn = (Button) findViewById(R.id.send_btn);
		leftImageButton = (View) findViewById(R.id.left_btn_bg);
		itemImageView = (ImageView) headView.findViewById(R.id.item_image);
		itemImageView1 = (ImageView) headView.findViewById(R.id.item_image1);
		itemImageView2 = (ImageView) headView.findViewById(R.id.item_image2);
		itemImageView3 = (ImageView) headView.findViewById(R.id.item_image3);
		itemImageView4 = (ImageView) headView.findViewById(R.id.item_image4);
		itemImageView5 = (ImageView) headView.findViewById(R.id.item_image5);
		itemImageView6 = (ImageView) headView.findViewById(R.id.item_image6);
		imageViewGroup2 = headView.findViewById(R.id.item_image_group2);
		imageViewGroup1 = headView.findViewById(R.id.item_image_group1);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftImageButton.setOnClickListener(this);
		applyBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		participantCountTextView.setOnClickListener(this);
		if (!jsonActItem.isApply()) {
			applyBtn.setEnabled(true);
		} else {
			applyBtn.setEnabled(false);
		}

		//设置上拉下拉刷新事件
		actCommentListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(ActDetailActivity.this.getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				getDataTask(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(ActDetailActivity.this.getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (pageNow >= 0)
					++pageNow;
				getDataTask(pageNow);
			}
		});

		if (jsonActItem != null) {
			//设置头像
			if (!TextUtils.isEmpty(jsonActItem.getA_small_avatar())) {
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonActItem.getA_small_avatar()),
						headImageView, ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != jsonActItem.getA_userid()) {
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

			//设置姓名
			nameTextView.setText(jsonActItem.getA_username());

			//设置题目
			titleTextView.setText(jsonActItem.getA_title());

			//设置活动日期
			actTimeTextView.setText(DateTimeTools.DateToStringForCN(jsonActItem.getA_act_date()));

			//设置活动地点
			actLocationTextView.setText(jsonActItem.getA_act_location());

			//设置活动对象
			actGenderTextView.setText(jsonActItem.getA_act_target());

			//设置活动详情
			actContentTextView.setText(jsonActItem.getA_detail_content());

			//设置发表日期
			timeTextView.setText(DateTimeTools.getHourAndMin(jsonActItem.getA_time()));

			participantCountTextView.setText("" + jsonActItem.getA_apply_amount() + "人参加");

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

			String[] smallPhotos = null;
			//设置缩略图
			if (!jsonActItem.getA_thumbnail().isEmpty()) {
				smallPhotos = jsonActItem.getA_thumbnail().split("\\|");
			}

			if (smallPhotos != null && smallPhotos.length > 0) {
				switch (smallPhotos.length) {
				case 1://只有一张图片
					imageViewGroup1.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.GONE);
					itemImageView.setVisibility(View.VISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), itemImageView,
							ImageLoaderTool.getImageOptions());
					break;
				case 2://有两张图片
					itemImageView.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.GONE);
					imageViewGroup1.setVisibility(View.VISIBLE);
					itemImageView1.setVisibility(View.VISIBLE);
					itemImageView2.setVisibility(View.VISIBLE);
					itemImageView3.setVisibility(View.INVISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), itemImageView2,
							ImageLoaderTool.getImageOptions());
					break;
				case 3://有三张图片
					itemImageView.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.GONE);
					imageViewGroup1.setVisibility(View.VISIBLE);
					itemImageView1.setVisibility(View.VISIBLE);
					itemImageView2.setVisibility(View.VISIBLE);
					itemImageView3.setVisibility(View.VISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), itemImageView3,
							ImageLoaderTool.getImageOptions());
					break;
				case 4://有四张图片
					itemImageView.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.VISIBLE);
					imageViewGroup1.setVisibility(View.VISIBLE);
					itemImageView1.setVisibility(View.VISIBLE);
					itemImageView2.setVisibility(View.VISIBLE);
					itemImageView3.setVisibility(View.INVISIBLE);
					itemImageView4.setVisibility(View.VISIBLE);
					itemImageView5.setVisibility(View.VISIBLE);
					itemImageView6.setVisibility(View.INVISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), itemImageView4,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), itemImageView5,
							ImageLoaderTool.getImageOptions());
					break;
				case 5://有五张图片
					itemImageView.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.VISIBLE);
					imageViewGroup1.setVisibility(View.VISIBLE);
					itemImageView1.setVisibility(View.VISIBLE);
					itemImageView2.setVisibility(View.VISIBLE);
					itemImageView3.setVisibility(View.VISIBLE);
					itemImageView4.setVisibility(View.VISIBLE);
					itemImageView5.setVisibility(View.VISIBLE);
					itemImageView6.setVisibility(View.INVISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), itemImageView3,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), itemImageView4,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[4]), itemImageView5,
							ImageLoaderTool.getImageOptions());
					break;
				case 6://有六张图片
					itemImageView.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.VISIBLE);
					imageViewGroup1.setVisibility(View.VISIBLE);
					itemImageView1.setVisibility(View.VISIBLE);
					itemImageView2.setVisibility(View.VISIBLE);
					itemImageView3.setVisibility(View.VISIBLE);
					itemImageView4.setVisibility(View.VISIBLE);
					itemImageView5.setVisibility(View.VISIBLE);
					itemImageView6.setVisibility(View.VISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), itemImageView3,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), itemImageView4,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[4]), itemImageView5,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[5]), itemImageView6,
							ImageLoaderTool.getImageOptions());
					break;
				default:
					itemImageView.setVisibility(View.GONE);
					imageViewGroup1.setVisibility(View.GONE);
					imageViewGroup2.setVisibility(View.GONE);
					break;
				}
			} else {
				itemImageView.setVisibility(View.GONE);
				imageViewGroup1.setVisibility(View.GONE);
				imageViewGroup2.setVisibility(View.GONE);
			}

			String[] tempBbigPhotoUrls = null;

			if (!jsonActItem.getA_big_photo().isEmpty()) {
				tempBbigPhotoUrls = jsonActItem.getA_big_photo().split("\\|");
				for (int i = 0; i < tempBbigPhotoUrls.length; i++) {
					tempBbigPhotoUrls[i] = AsyncHttpClientTool.getAbsoluteUrl(tempBbigPhotoUrls[i]);
				}
			}
			final String[] bigPhotoUrls = tempBbigPhotoUrls;

			itemImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ActDetailActivity.this, ImageShowerActivity.class);
					intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, bigPhotoUrls[0]);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
				}
			});

			itemImageView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goBigPhoto(bigPhotoUrls, 0);
				}
			});
			itemImageView2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goBigPhoto(bigPhotoUrls, 1);
				}
			});
			itemImageView3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goBigPhoto(bigPhotoUrls, 2);
				}
			});
			itemImageView4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (bigPhotoUrls.length == 4) {
						goBigPhoto(bigPhotoUrls, 2);
					} else {
						goBigPhoto(bigPhotoUrls, 3);
					}
				}
			});
			itemImageView5.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (bigPhotoUrls.length == 4) {
						goBigPhoto(bigPhotoUrls, 3);
					} else {
						goBigPhoto(bigPhotoUrls, 4);
					}
				}
			});
			itemImageView6.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goBigPhoto(bigPhotoUrls, 5);
				}
			});
		}
	}

	//查看大图
	public void goBigPhoto(String[] urls, int postion) {
		Intent intent = new Intent(ActDetailActivity.this, GalleryPictureActivity.class);
		intent.putExtra(GalleryPictureActivity.IMAGE_URLS, urls);
		intent.putExtra(GalleryPictureActivity.POSITON, postion);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
	}

	/**
	 * 更新数据
	 */
	private void refresh() {
		actCommentListView.setRefreshing();
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
		case R.id.apply_btn:
			applyAct();
			break;
		case R.id.participants_count:
			if (jsonActItem.getA_apply_amount() > 0) {
				startActivity(new Intent(ActDetailActivity.this, AllApplicantsActivity.class)
						.putExtra(AllApplicantsActivity.A_ACTID, jsonActItem.getA_actid())
						.putExtra(AllApplicantsActivity.A_USERID, jsonActItem.getA_userid())
						.putExtra(AllApplicantsActivity.APPLY_COUNT, jsonActItem.getA_apply_amount()));
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 报名参加活动
	 */
	private void applyAct() {
		RequestParams params = new RequestParams();
		params.put(ActivityTable.A_ACTID, jsonActItem.getA_actid());
		params.put(ActivityTable.A_USERID, jsonActItem.getA_userid());
		params.put(UserTable.U_ID, userPreference.getU_id());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				sendBtn.setEnabled(false);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("1")) {
						LogTool.i("报名成功");
						ToastTool.showLong(ActDetailActivity.this, "报名成功");
						applyBtn.setEnabled(false);
						participantCountTextView.setText("" + (jsonActItem.getA_apply_amount() + 1) + "人参加");
						jsonActItem.setA_apply_amount(jsonActItem.getA_apply_amount() + 1);
					} else {
						LogTool.e("报名返回" + response);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("报名失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				sendBtn.setEnabled(true);
			}

		};
		AsyncHttpClientTool.post("activity/join", params, responseHandler);
	}

	/**
	 * 评论
	 */
	private void comment(String content) {
		if (jsonActItem != null && !TextUtils.isEmpty(content)) {

			RequestParams params = new RequestParams();
			params.put(CommentTable.C_USER_ID, userPreference.getU_id());
			params.put(CommentTable.C_CONTENT, content);
			params.put(CommentTable.TO_USER_ID, toUserID);
			params.put(CommentTable.PA_USERID, jsonActItem.getA_userid());
			params.put(CommentTable.PA_ID, jsonActItem.getA_actid());
			if (isReply) {
				params.put(CommentTable.COMMENT_TYPE, CommentType.REPLY);
			} else {
				params.put(CommentTable.COMMENT_TYPE, CommentType.COMMENT);
			}

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					sendBtn.setEnabled(false);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					if (statusCode == 200) {
						LogTool.e("评论成功");
						commentEditText.setText("");
						refresh();
						hideKeyboard();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("评论失败");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					sendBtn.setEnabled(true);
				}

			};
			AsyncHttpClientTool.post("activity/comment_reply", params, responseHandler);
		}
	}

	/**
	 * 网络获取数据
	 */
	private void getDataTask(int p) {
		if (jsonActItem == null) {
			return;
		}
		final int page = p;
		RequestParams params = new RequestParams();
		params.put("page", pageNow);
		params.put(ActivityTable.A_ACTID, jsonActItem.getA_actid());
		params.put(ActivityTable.A_USERID, jsonActItem.getA_userid());
		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					List<JsonComment> temp = FastJsonTool.getObjectList(response, JsonComment.class);
					LogTool.i("活动详情获取评论列表", "长度" + temp.size());
					if (temp != null) {
						//如果是首次获取数据
						if (page == 0) {
							if (temp.size() < Config.PAGE_NUM) {
								pageNow = -1;
							}
							commentList = new LinkedList<JsonComment>();
							commentList.add(new JsonComment());
							commentList.addAll(temp);
						}
						//如果是获取更多
						else if (page > 0) {
							if (temp.size() < Config.PAGE_NUM) {
								pageNow = -1;
								ToastTool.showShort(ActDetailActivity.this, "没有更多了！");
							}
							commentList.addAll(temp);
						}
						mAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("活动详情获取评论列表失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				actCommentListView.onRefreshComplete();
			}
		};
		AsyncHttpClientTool.post(ActDetailActivity.this, "activity/getComments", params, responseHandler);
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
			public TextView lable;
			public TextView toUserName;
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
			if (position == 0) {
				return LayoutInflater.from(ActDetailActivity.this).inflate(R.layout.emptyview, null);
			}
			View view = convertView;
			final JsonComment jsonComment = commentList.get(position);
			if (jsonComment == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(ActDetailActivity.this).inflate(R.layout.comment_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.genderImageView = (ImageView) view.findViewById(R.id.gender);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.contentTextView = (TextView) view.findViewById(R.id.content);
				holder.lable = (TextView) view.findViewById(R.id.label);
				holder.toUserName = (TextView) view.findViewById(R.id.to_user_name);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					commentEditText.setHint("回复 " + jsonComment.getC_user_nickname() + ":");
					isReply = true;
					toUserID = jsonComment.getC_user_id();
				}
			});

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideKeyboard();
					commentEditText.setHint("评论");
					isReply = false;
					toUserID = jsonActItem.getA_userid();
					return false;
				}
			});
			//设置头像
			if (!TextUtils.isEmpty(jsonActItem.getA_small_avatar())) {
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonComment.getC_user_avatar()),
						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != jsonComment.getC_user_id()) {
					//点击头像进入详情页面
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(ActDetailActivity.this, PersonDetailActivity.class);
							intent.putExtra(UserTable.U_ID, jsonComment.getC_user_id());
							intent.putExtra(UserTable.U_NICKNAME, jsonComment.getC_user_nickname());
							startActivity(intent);
							ActDetailActivity.this.overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//设置内容
			holder.contentTextView.setText(jsonComment.getC_content());

			//设置姓名
			holder.nameTextView.setText(jsonComment.getC_user_nickname());

			//设置性别
			if (jsonComment.getC_user_gender().equals(Constants.Gender.MALE)) {
				holder.genderImageView.setImageResource(R.drawable.male);
			} else {
				holder.genderImageView.setImageResource(R.drawable.female);
			}

			if (jsonComment.getComment_type().equals(CommentType.COMMENT)) {
				holder.lable.setVisibility(View.GONE);
				holder.toUserName.setVisibility(View.GONE);
			} else {
				holder.lable.setVisibility(View.VISIBLE);
				holder.toUserName.setVisibility(View.VISIBLE);
				holder.toUserName.setText(jsonComment.getTo_user_nickname() + ":");
			}

			//设置日期
			holder.timeTextView.setText(DateTimeTools.getHourAndMin(jsonComment.getC_time()));

			return view;
		}
	}

}
