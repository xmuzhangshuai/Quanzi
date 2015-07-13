package com.quanzi.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.DensityUtil;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�MainHomeFragment  
 * ��������Ȧ�Ӷ�̬��ҳ�棬Ҳ����ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-12 ����8:53:06 
 *
 */
public class MainHomeFragment extends BaseV4Fragment implements OnClickListener {
	private View rootView;// ��View
	private TextView leftTextView;//�������������
	private View searchBtn;//���Ұ�ť
	private View publishBtn;//������ť
	private View emptyView;

	private PullToRefreshListView postListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private UserPreference userPreference;
	private LinkedList<JsonPostItem> jsonPostItemList;
	private int pageNow = 0;//����ҳ��
	private PostAdapter mAdapter;
	private ProgressDialog progressDialog;

	private List<String> imageList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonPostItemList = new LinkedList<JsonPostItem>();
		imageList = new ArrayList<String>();
		imageList.add("drawable://" + R.drawable.content);
		imageList.add("drawable://" + R.drawable.content2);
		imageList.add("drawable://" + R.drawable.content3);
		imageList.add("drawable://" + R.drawable.content4);
		imageList.add("drawable://" + R.drawable.content5);
		imageList.add("drawable://" + R.drawable.content6);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_mainhome, container, false);

		findViewById();// ��ʼ��views
		initView();

		//��ȡ����
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
		if (jsonPostItemList.size() > 0) {
			emptyView.setVisibility(View.GONE);
			postListView.setVisibility(View.VISIBLE);
		}else {
			emptyView.setVisibility(View.VISIBLE);
			postListView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) rootView.findViewById(R.id.left_text1);
		searchBtn = rootView.findViewById(R.id.nav_right_btn2);
		publishBtn = rootView.findViewById(R.id.nav_right_btn1);
		postListView = (PullToRefreshListView) rootView.findViewById(R.id.post_list);
		emptyView = rootView.findViewById(R.id.empty);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("Ȧ��");
		searchBtn.setOnClickListener(this);
		publishBtn.setOnClickListener(this);

		//������������ˢ���¼�
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
		case R.id.nav_right_btn1://����
			showPublishDialog();
			break;
		case R.id.nav_right_btn2://����
			getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
			getActivity().overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
			break;
		default:
			break;
		}
	}

	/**
	 * �˵���ʾ
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
	 * ��ʾ��������ĶԻ�����
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
	 * �����ȡ����
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
		//		JsonPostItem item1 = new JsonPostItem(1, 1, "��˧", "drawable://" + R.drawable.headimage1, "drawable://"
		//				+ R.drawable.headimage1, "��", "һ�����ģ��ټ���Ȼ����", "drawable://" + R.drawable.content, "drawable://"
		//				+ R.drawable.content, new Date(), 20, 2);
		//
		//		JsonPostItem item2 = new JsonPostItem(2, 2, "Ҷ��", "drawable://" + R.drawable.headimage2, "drawable://"
		//				+ R.drawable.headimage2, "Ů", "������������鲻�Խ�", "drawable://" + R.drawable.content2, "drawable://"
		//				+ R.drawable.content2, new Date(), 45, 20);
		//
		//		JsonPostItem item3 = new JsonPostItem(3, 3, "�ٷ�", "drawable://" + R.drawable.headimage3, "drawable://"
		//				+ R.drawable.headimage3, "��", "����һƬ�ܼ�į����������Щ���ĵ���", "drawable://" + R.drawable.content + "|"
		//				+ "drawable://" + R.drawable.content2, "drawable://" + R.drawable.content + "|" + "drawable://"
		//				+ R.drawable.content2, new Date(), 76, 32);
		//
		//		JsonPostItem item4 = new JsonPostItem(4, 4, "ΰǿ", "drawable://" + R.drawable.headimage4, "drawable://"
		//				+ R.drawable.headimage4, "Ů", "����������ӣ�ÿ�춼������", "drawable://" + R.drawable.content2, "drawable://"
		//				+ R.drawable.content2, new Date(), 26, 280);
		//
		//		JsonPostItem item5 = new JsonPostItem(5, 5, "����", "drawable://" + R.drawable.headimage5, "drawable://"
		//				+ R.drawable.headimage5, "Ů", "���ֶ��ˣ������㿪�ĵ��³��ϲ�ɣ�", "drawable://" + R.drawable.content, "drawable://"
		//				+ R.drawable.content, new Date(), 256, 46);
		//		jsonPostItemList.add(item1);
		//		jsonPostItemList.add(item2);
		//		jsonPostItemList.add(item3);
		//		jsonPostItemList.add(item4);
		//		jsonPostItemList.add(item5);
		//		jsonPostItemList.add(item2);
		//		jsonPostItemList.add(item1);
		//		jsonPostItemList.add(item2);
		//		jsonPostItemList.add(item3);
		//		jsonPostItemList.add(item4);
		//		jsonPostItemList.add(item5);
		//		jsonPostItemList.add(item2);
		postListView.onRefreshComplete();
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
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
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

			//����ͷ��
			if (!TextUtils.isEmpty(jsonPostItem.getP_small_avatar())) {
				//	imageLoader.displayImage(AsyncHttpClientImageSound.getAbsoluteUrl(jsonPostItem.getN_small_avatar()),
				//	holder.headImageView, ImageLoaderTool.getHeadImageOptions(10));
				imageLoader.displayImage(jsonPostItem.getP_small_avatar(), holder.headImageView,
						ImageLoaderTool.getHeadImageOptions(10));
				if (userPreference.getU_id() != jsonPostItem.getP_userid()) {
					//���ͷ���������ҳ��
					holder.headImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
							//	intent.putExtra(PersonDetailActivity.PERSON_TYPE, Constants.PersonDetailType.SINGLE);
							//	intent.putExtra(UserTable.U_ID, jsonPostItem.getN_userid());
							startActivity(intent);
							getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
						}
					});
				}
			}

			//��������
			holder.contentTextView.setText(jsonPostItem.getP_text_content());

			//��������
			holder.nameTextView.setText(jsonPostItem.getP_username());

			//�����Ա�
			if (jsonPostItem.getP_gender().equals(Constants.Gender.MALE)) {
				holder.genderImageView.setImageResource(R.drawable.male);
			} else {
				holder.genderImageView.setImageResource(R.drawable.female);
			}

			//��������
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(jsonPostItem.getP_time()));

			//���ñ��޴���
			holder.favorCountTextView.setText("" + jsonPostItem.getP_favor_count() + "��");

			holder.favorCountTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), AllFavorsActivity.class).putExtra(
							PostDetailActivity.POST_ITEM, jsonPostItem));
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//�������۴���
			holder.commentCountTextView.setText("�鿴ȫ��" + jsonPostItem.getP_comment_count() + "������");

			//����
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

			String[] smallPhotos = null;
			//��������ͼ
			if (!jsonPostItem.getP_thumbnail().isEmpty()) {
				smallPhotos = jsonPostItem.getP_thumbnail().split("\\|");
			}
			LogTool.e("����ͼ����" + smallPhotos.length + "��������������������" + jsonPostItem.getP_thumbnail());

			if (smallPhotos != null && smallPhotos.length > 0) {
				switch (smallPhotos.length) {
				case 1://ֻ��һ��ͼƬ
					holder.imageViewGroup1.setVisibility(View.GONE);
					holder.imageViewGroup2.setVisibility(View.GONE);
					holder.itemImageView.setVisibility(View.VISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView,
							ImageLoaderTool.getImageOptions());
					break;
				case 2://������ͼƬ
					holder.itemImageView.setVisibility(View.GONE);
					holder.imageViewGroup2.setVisibility(View.GONE);
					holder.imageViewGroup1.setVisibility(View.VISIBLE);
					holder.itemImageView1.setVisibility(View.VISIBLE);
					holder.itemImageView2.setVisibility(View.VISIBLE);
					holder.itemImageView3.setVisibility(View.INVISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2,
							ImageLoaderTool.getImageOptions());
					break;
				case 3://������ͼƬ
					holder.itemImageView.setVisibility(View.GONE);
					holder.imageViewGroup2.setVisibility(View.GONE);
					holder.imageViewGroup1.setVisibility(View.VISIBLE);
					holder.itemImageView1.setVisibility(View.VISIBLE);
					holder.itemImageView2.setVisibility(View.VISIBLE);
					holder.itemImageView3.setVisibility(View.VISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView3,
							ImageLoaderTool.getImageOptions());
					break;
				case 4://������ͼƬ
					holder.itemImageView.setVisibility(View.GONE);
					holder.imageViewGroup2.setVisibility(View.VISIBLE);
					holder.imageViewGroup1.setVisibility(View.VISIBLE);
					holder.itemImageView1.setVisibility(View.VISIBLE);
					holder.itemImageView2.setVisibility(View.VISIBLE);
					holder.itemImageView3.setVisibility(View.INVISIBLE);
					holder.itemImageView4.setVisibility(View.VISIBLE);
					holder.itemImageView5.setVisibility(View.VISIBLE);
					holder.itemImageView6.setVisibility(View.INVISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView4,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), holder.itemImageView5,
							ImageLoaderTool.getImageOptions());
					break;
				case 5://������ͼƬ
					holder.itemImageView.setVisibility(View.GONE);
					holder.imageViewGroup2.setVisibility(View.VISIBLE);
					holder.imageViewGroup1.setVisibility(View.VISIBLE);
					holder.itemImageView1.setVisibility(View.VISIBLE);
					holder.itemImageView2.setVisibility(View.VISIBLE);
					holder.itemImageView3.setVisibility(View.VISIBLE);
					holder.itemImageView4.setVisibility(View.VISIBLE);
					holder.itemImageView5.setVisibility(View.VISIBLE);
					holder.itemImageView6.setVisibility(View.INVISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView3,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), holder.itemImageView4,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[4]), holder.itemImageView5,
							ImageLoaderTool.getImageOptions());
					break;
				case 6://������ͼƬ
					holder.itemImageView.setVisibility(View.GONE);
					holder.imageViewGroup2.setVisibility(View.VISIBLE);
					holder.imageViewGroup1.setVisibility(View.VISIBLE);
					holder.itemImageView1.setVisibility(View.VISIBLE);
					holder.itemImageView2.setVisibility(View.VISIBLE);
					holder.itemImageView3.setVisibility(View.VISIBLE);
					holder.itemImageView4.setVisibility(View.VISIBLE);
					holder.itemImageView5.setVisibility(View.VISIBLE);
					holder.itemImageView6.setVisibility(View.VISIBLE);
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[0]), holder.itemImageView1,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[1]), holder.itemImageView2,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[2]), holder.itemImageView3,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[3]), holder.itemImageView4,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[4]), holder.itemImageView5,
							ImageLoaderTool.getImageOptions());
					imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallPhotos[5]), holder.itemImageView6,
							ImageLoaderTool.getImageOptions());
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
					}else {
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
					}else {
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

		//�鿴��ͼ
		public void goBigPhoto(String[] urls, int postion) {
			Intent intent = new Intent(getActivity(), GalleryPictureActivity.class);
			intent.putExtra(GalleryPictureActivity.IMAGE_URLS, urls);
			intent.putExtra(GalleryPictureActivity.POSITON, postion);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.zoomin2, R.anim.zoomout);
		}
	}
}
