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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants;
import com.quanzi.config.Constants.CommentType;
import com.quanzi.jsonobject.JsonPostItem;
import com.quanzi.table.CommentTable;
import com.quanzi.table.PostTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：PersonDetailPostFragment  
 * 类描述：他人个人中心的帖子发布页面
 * @author zhangshuai
 * @date 创建时间：2015-5-11 上午10:55:59 
 *
 */
public class PersonDetailPostFragment extends BaseV4Fragment {
	private View rootView;// 根View
	private TableLayout tableLayout;
	private LinkedList<JsonPostItem> jsonPostItemList;
	private UserPreference userPreference;
	private int userId;

	public PersonDetailPostFragment(int userId) {
		this.userId = userId;
	}

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
		rootView = inflater.inflate(R.layout.fragment_person_detail_post, container, false);
		getDataTask();

		findViewById();// 初始化views

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		tableLayout = (TableLayout) rootView.findViewById(R.id.tablelayout);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		if (jsonPostItemList != null) {
			for (int i = 0; i < jsonPostItemList.size(); i++) {
				tableLayout.addView(getView(i, null), i);
			}
		}
	}

	/**
	 * 网络获取数据
	 */
	private void getDataTask() {
		RequestParams params = new RequestParams();
		params.put(UserTable.U_ID, userId);
		params.put("my_userid", userPreference.getU_id());
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
					List<JsonPostItem> temp = FastJsonTool.getObjectList(response, JsonPostItem.class);
					if (temp != null) {
						jsonPostItemList = new LinkedList<JsonPostItem>();
						jsonPostItemList.addAll(temp);
						initView();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("获取个人帖子列表失败" + errorResponse);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

		};
		AsyncHttpClientTool.post(getActivity(), "post/getPostsByUserID", params, responseHandler);
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

	// 查看大图
	public void goBigPhoto(String[] urls, int postion) {
		Intent intent = new Intent(getActivity(), GalleryPictureActivity.class);
		intent.putExtra(GalleryPictureActivity.IMAGE_URLS, urls);
		intent.putExtra(GalleryPictureActivity.POSITON, postion);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
	}

	/**
	 * 返回单条记录
	 */
	private View getView(int position, View convertView) {
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
				startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(PostDetailActivity.POST_ITEM, jsonPostItem));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		// 设置头像
		if (!TextUtils.isEmpty(jsonPostItem.getP_small_avatar())) {
			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonPostItem.getP_small_avatar()), holder.headImageView,
					ImageLoaderTool.getHeadImageOptions(10));
		}
		if (userPreference.getU_id() != jsonPostItem.getP_userid()) {
			// 点击头像进入详情页面
			holder.headImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
					intent.putExtra(UserTable.U_ID, jsonPostItem.getP_userid());
					intent.putExtra(UserTable.U_NICKNAME, jsonPostItem.getP_username());
					intent.putExtra(UserTable.U_SMALL_AVATAR, jsonPostItem.getP_small_avatar());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
				}
			});
		} else {
			holder.headImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), MyPersonDetailActivity.class));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});
		}

		// 设置内容
		if (jsonPostItem.getP_text_content() == null || jsonPostItem.getP_text_content().length() < 1) {
			holder.contentTextView.setVisibility(View.GONE);
		} else {
			holder.contentTextView.setText(jsonPostItem.getP_text_content());
			holder.contentTextView.setVisibility(View.VISIBLE);
		}

		// 设置姓名
		holder.nameTextView.setText(jsonPostItem.getP_username());

		// 设置性别
		if (jsonPostItem.getP_gender().equals(Constants.Gender.MALE)) {
			holder.genderImageView.setImageResource(R.drawable.male);
		} else {
			holder.genderImageView.setImageResource(R.drawable.female);
		}

		// 设置日期
		holder.timeTextView.setText(DateTimeTools.getInterval(jsonPostItem.getP_time()));

		// 设置被赞次数
		if (jsonPostItem.getP_favor_count() > 0) {
			holder.favorCountTextView.setText("" + jsonPostItem.getP_favor_count() + "赞");
			holder.favorCountTextView.setVisibility(View.VISIBLE);
		} else {
			holder.favorCountTextView.setVisibility(View.GONE);
		}
		holder.favorCountTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (jsonPostItem.getP_favor_count() > 0) {
					startActivity(new Intent(getActivity(), AllFavorsActivity.class).putExtra(AllFavorsActivity.PA_ID, jsonPostItem.getP_postid())
							.putExtra(AllFavorsActivity.PA_USERID, jsonPostItem.getP_userid())
							.putExtra(AllFavorsActivity.FAVOR_COUNT, jsonPostItem.getP_favor_count()).putExtra(AllFavorsActivity.TYPE, "post"));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			}
		});

		List<Map<String, String>> comments = jsonPostItem.getCommentList();

		// 设置评论
		if (comments != null) {
			if (comments.size() == 0) {
				holder.comment1Container.setVisibility(View.GONE);
				holder.comment2Container.setVisibility(View.GONE);
			} else if (comments.size() == 1) {
				holder.comment1Container.setVisibility(View.VISIBLE);
				holder.comment2Container.setVisibility(View.GONE);
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
			} else {
				LogTool.e("评论有超过两个");
			}
		} else {
			holder.comment1Container.setVisibility(View.GONE);
			holder.comment2Container.setVisibility(View.GONE);
		}

		// 设置评论次数
		if (jsonPostItem.getP_comment_count() == 0) {
			holder.commentCountTextView.setVisibility(View.GONE);
		} else {
			holder.commentCountTextView.setVisibility(View.VISIBLE);
			holder.commentCountTextView.setText("查看全部" + jsonPostItem.getP_comment_count() + "条评论");
		}

		// 评论
		holder.commentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), PostDetailActivity.class).putExtra(PostDetailActivity.POST_ITEM, jsonPostItem));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		// 设置是否赞过
		holder.favorBtn.setChecked(jsonPostItem.isLike());

		holder.favorBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put(PostTable.P_POSTID, jsonPostItem.getP_postid());
				params.put(PostTable.P_USERID, jsonPostItem.getP_userid());
				params.put(UserTable.U_ID, userPreference.getU_id());

				TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						if (!jsonPostItem.isLike()) {// 喜欢
							holder.favorCountTextView.setText("" + (jsonPostItem.getP_favor_count() + 1) + "赞");
							jsonPostItem.setP_favor_count(jsonPostItem.getP_favor_count() + 1);
							jsonPostItem.setLike(true);
						} else {// 喜欢变成不喜欢
							holder.favorCountTextView.setText("" + (jsonPostItem.getP_favor_count() - 1) + "赞");
							jsonPostItem.setP_favor_count(jsonPostItem.getP_favor_count() - 1);
							jsonPostItem.setLike(false);
						}
						// 设置被赞次数
						if (jsonPostItem.getP_favor_count() > 0) {
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
								LogTool.e("学校帖子赞返回错误" + response);
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
						// TODO Auto-generated method stub
						LogTool.e("学校帖子赞失败");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
					}

				};
				if (!jsonPostItem.isLike()) {
					AsyncHttpClientTool.post(getActivity(), "post/like", params, responseHandler);
				} else {
					AsyncHttpClientTool.post(getActivity(), "post/unlike", params, responseHandler);
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

		String[] smallPhotos = null;
		// 设置缩略图
		if (!jsonPostItem.getP_thumbnail().isEmpty()) {
			smallPhotos = jsonPostItem.getP_thumbnail().split("\\|");
		}

		if (smallPhotos != null && smallPhotos.length > 0) {
			switch (smallPhotos.length) {
			case 1:// 只有一张图片
				holder.imageViewGroup1.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.itemImageView.setVisibility(View.VISIBLE);
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView, ImageLoaderTool.getImageOptions());
				break;
			case 2:// 有两张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.INVISIBLE);
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2, ImageLoaderTool.getImageOptions());
				break;
			case 3:// 有三张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.VISIBLE);
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView3, ImageLoaderTool.getImageOptions());
				break;
			case 4:// 有四张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.VISIBLE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.INVISIBLE);
				holder.itemImageView4.setVisibility(View.VISIBLE);
				holder.itemImageView5.setVisibility(View.VISIBLE);
				holder.itemImageView6.setVisibility(View.INVISIBLE);
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView4, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), holder.itemImageView5, ImageLoaderTool.getImageOptions());
				break;
			case 5:// 有五张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.VISIBLE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.VISIBLE);
				holder.itemImageView4.setVisibility(View.VISIBLE);
				holder.itemImageView5.setVisibility(View.VISIBLE);
				holder.itemImageView6.setVisibility(View.INVISIBLE);
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView3, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), holder.itemImageView4, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[4]), holder.itemImageView5, ImageLoaderTool.getImageOptions());
				break;
			case 6:// 有六张图片
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.VISIBLE);
				holder.imageViewGroup1.setVisibility(View.VISIBLE);
				holder.itemImageView1.setVisibility(View.VISIBLE);
				holder.itemImageView2.setVisibility(View.VISIBLE);
				holder.itemImageView3.setVisibility(View.VISIBLE);
				holder.itemImageView4.setVisibility(View.VISIBLE);
				holder.itemImageView5.setVisibility(View.VISIBLE);
				holder.itemImageView6.setVisibility(View.VISIBLE);
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView3, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), holder.itemImageView4, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[4]), holder.itemImageView5, ImageLoaderTool.getImageOptions());
				imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[5]), holder.itemImageView6, ImageLoaderTool.getImageOptions());
				break;
			default:
				holder.itemImageView.setVisibility(View.GONE);
				holder.imageViewGroup1.setVisibility(View.GONE);
				holder.imageViewGroup2.setVisibility(View.GONE);
				break;
			}
		} else {
			holder.itemImageView.setVisibility(View.GONE);
			holder.imageViewGroup1.setVisibility(View.GONE);
			holder.imageViewGroup2.setVisibility(View.GONE);
		}

		String[] tempBbigPhotoUrls = null;

		if (!jsonPostItem.getP_big_photo().isEmpty()) {
			tempBbigPhotoUrls = jsonPostItem.getP_big_photo().split("\\|");
			for (int i = 0; i < tempBbigPhotoUrls.length; i++) {
				tempBbigPhotoUrls[i] = AsyncHttpClientTool.getAbsoluteUrl(tempBbigPhotoUrls[i]);
			}
		}
		final String[] bigPhotoUrls = tempBbigPhotoUrls;

		holder.itemImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ImageShowerActivity.class);
				intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE, bigPhotoUrls[0]);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
			}
		});

		holder.itemImageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBigPhoto(bigPhotoUrls, 0);
			}
		});
		holder.itemImageView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBigPhoto(bigPhotoUrls, 1);
			}
		});
		holder.itemImageView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBigPhoto(bigPhotoUrls, 2);
			}
		});
		holder.itemImageView4.setOnClickListener(new OnClickListener() {

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
		holder.itemImageView5.setOnClickListener(new OnClickListener() {

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
		holder.itemImageView6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBigPhoto(bigPhotoUrls, 5);
			}
		});

		return view;
	}

	private class ViewHolder {
		public ImageView headImageView;
		public TextView nameTextView;
		public ImageView genderImageView;
		public CheckBox concernBtn;
		public TextView timeTextView;
		public TextView contentTextView;
		public CheckBox favorBtn;
		public TextView favorCountTextView;
		public ImageView commentBtn;
		public TextView commentCountTextView;
		public ImageView moreBtn;
		public ImageView itemImageView;
		public ImageView itemImageView1;
		public ImageView itemImageView2;
		public ImageView itemImageView3;
		public ImageView itemImageView4;
		public ImageView itemImageView5;
		public ImageView itemImageView6;
		public View imageViewGroup2;
		public View imageViewGroup1;
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

}
