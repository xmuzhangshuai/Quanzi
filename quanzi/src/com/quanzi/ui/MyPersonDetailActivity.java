package com.quanzi.ui;

import java.util.Date;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants;
import com.quanzi.jsonobject.JsonPostItem;
import com.quanzi.ui.ActDetailActivity.CommentAdapter;
import com.quanzi.ui.MainHomeFragment.PostAdapter;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�MyPersonDetailActivity  
 * ���������ҵĸ�������ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-5-8 ����3:08:09 
 *
 */
public class MyPersonDetailActivity extends BaseFragmentActivity implements OnClickListener {
	private TextView leftTextView;//�������������
	private View leftButton;//��������ఴť
	private View headView;//��������
	private ListView mListView;

	private LinkedList<JsonPostItem> jsonPostItemList;
	private UserPreference userPreference;
	private PostAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_person_detail);
		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonPostItemList = new LinkedList<JsonPostItem>();
		
		findViewById();
		initView();
		
		getDataTask();
		
		mListView.addHeaderView(headView);
		mAdapter = new PostAdapter();
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		headView = getLayoutInflater().inflate(R.layout.person_detail_headview, null);
		mListView = (ListView) findViewById(R.id.my_person_post_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("׿��");
		leftButton.setOnClickListener(this);
	}

	/**
	 * �����ȡ����
	 */
	private void getDataTask() {
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
		//						//������״λ�ȡ����
		//						if (page == 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//							}
		//							jsonPostItemList = new LinkedList<JsonPostItem>();
		//							jsonPostItemList.addAll(temp);
		//						}
		//						//����ǻ�ȡ����
		//						else if (page > 0) {
		//							if (temp.size() < Config.PAGE_NUM) {
		//								pageNow = -1;
		//								ToastTool.showShort(getActivity(), "û�и����ˣ�");
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
		//				LogTool.e("LoveBridgeSchoolFragment", "��ȡ�б�ʧ��");
		//				postListView.onRefreshComplete();
		//			}
		//		};
		//		AsyncHttpClientTool.post(getActivity(), "getlovebridgelist", params, responseHandler);
		JsonPostItem item1 = new JsonPostItem(1, 1, "��˧", "drawable://" + R.drawable.headimage1, "��", 78,
				"һ�����ģ��ټ���Ȼ����", "drawable://" + R.drawable.content, new Date(), 20, 2);
		JsonPostItem item2 = new JsonPostItem(2, 2, "Ҷ��", "drawable://" + R.drawable.headimage2, "Ů", 8, "������������鲻�Խ�",
				"drawable://" + R.drawable.content2, new Date(), 45, 20);
		JsonPostItem item3 = new JsonPostItem(3, 3, "�ٷ�", "drawable://" + R.drawable.headimage3, "��", 78,
				"����һƬ�ܼ�į����������Щ���ĵ���", "drawable://" + R.drawable.content, new Date(), 76, 32);
		JsonPostItem item4 = new JsonPostItem(4, 4, "ΰǿ", "drawable://" + R.drawable.headimage4, "Ů", 8,
				"����������ӣ�ÿ�춼������", "drawable://" + R.drawable.content2, new Date(), 26, 280);
		JsonPostItem item5 = new JsonPostItem(5, 5, "����", "drawable://" + R.drawable.headimage5, "Ů", 8,
				"���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", "drawable://" + R.drawable.content, new Date(), 256, 46);
		jsonPostItemList.add(item1);
		jsonPostItemList.add(item2);
		jsonPostItemList.add(item3);
		jsonPostItemList.add(item4);
		jsonPostItemList.add(item5);
	}

	/**
	 * ��ʾ��������ĶԻ�����
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		default:
			break;
		}
	}

	/**
	 *
	 * ��Ŀ���ƣ�quanzi  
	 * �����ƣ�PostAdapter  
	 * ��������Ȧ�������ӵ�������
	 * @author zhangshuai
	 * @date ����ʱ�䣺2015-4-19 ����8:55:46 
	 *
	 */
	class PostAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
			public ImageView genderImageView;
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
				view = LayoutInflater.from(MyPersonDetailActivity.this).inflate(R.layout.post_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.genderImageView = (ImageView) view.findViewById(R.id.gender);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.contentTextView = (TextView) view.findViewById(R.id.content);
				holder.contentImageView = (ImageView) view.findViewById(R.id.item_image);
				holder.favorBtn = (CheckBox) view.findViewById(R.id.favor_btn);
				holder.favorCountTextView = (TextView) view.findViewById(R.id.favor_count);
				holder.commentBtn = (ImageView) view.findViewById(R.id.comment_btn);
				holder.commentCountTextView = (TextView) view.findViewById(R.id.comment_count);
				holder.moreBtn = (ImageView) view.findViewById(R.id.more);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			//����ͷ��
			if (!TextUtils.isEmpty(jsonPostItem.getN_small_avatar())) {
				//				imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_small_avatar()),
				//						holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				imageLoader.displayImage(jsonPostItem.getN_small_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != jsonPostItem.getN_userid()) {
					//���ͷ���������ҳ��
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

			//������Ƭ
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

			//��������
			holder.contentTextView.setText(jsonPostItem.getN_content());

			//��������
			holder.nameTextView.setText(jsonPostItem.getN_name());

			//�����Ա�
			if (jsonPostItem.getN_gender().equals(Constants.Gender.MALE)) {
				holder.genderImageView.setImageResource(R.drawable.male);
			} else {
				holder.genderImageView.setImageResource(R.drawable.female);
			}

			//��������
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(jsonPostItem.getN_time()));

			//���ñ��޴���
			holder.favorCountTextView.setText("" + jsonPostItem.getN_flipcount() + "��");

			holder.favorCountTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(MyPersonDetailActivity.this, AllFavorsActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonPostItem));
					MyPersonDetailActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//�������۴���
			holder.commentCountTextView.setText("�鿴ȫ��" + jsonPostItem.getN_commentcount() + "������");

			//����
			holder.commentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(MyPersonDetailActivity.this, PostDetailActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonPostItem));
					MyPersonDetailActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
			//				//�Ķ�
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
			//							ToastTool.showShort(getActivity(), "���ܶ��Լ��Ķ�Ŷ~");
			//						}
			//					}
			//				});
			//			}
			return view;
		}
	}
}
