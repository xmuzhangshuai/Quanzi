package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.quanzi.R;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.utils.DensityUtil;
import com.quanzi.utils.LogTool;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�GalleryPictureActivity  
 * �����������Ŵ�ͼ���ʱҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-5-26 ����10:55:59 
 *
 */
public class GalleryPictureActivity extends BaseFragmentActivity {
	public static final String IMAGE_URLS = "image_urls";
	public static final String POSITON = "position";
	// ��ҳ�ؼ�
	private ViewPager mViewPager;

	// ����ǵײ���ʾ��ǰ״̬��imageView
	private List<ImageView> dotImageViews;
	private LinearLayout dotContainer;
	private String[] imageUrls;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gallery_picture);
		dotImageViews = new ArrayList<ImageView>();

		imageUrls = getIntent().getStringArrayExtra(IMAGE_URLS);
		position = getIntent().getIntExtra(POSITON, 0);

		findViewById();
		initView();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		dotContainer = (LinearLayout) findViewById(R.id.dot_container);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		if (imageUrls != null && imageUrls.length > 0) {

			/*
			 * ���ｫÿһҳ��ʾ��view��ŵ�ArrayList������ ������ViewPager��������˳�����չʾ
			 */
			final ArrayList<View> views = new ArrayList<View>();

			for (int i = 0; i < imageUrls.length; i++) {
				ImageView dotImageView = new ImageView(GalleryPictureActivity.this);
				dotImageView.setImageResource(R.drawable.page);
				dotImageView.setScaleType(ScaleType.MATRIX);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
				layoutParams.leftMargin = DensityUtil.dip2px(GalleryPictureActivity.this, 10);
				dotImageView.setLayoutParams(layoutParams);
				dotContainer.addView(dotImageView, i);
				dotImageViews.add(dotImageView);

				/*
				 * ������ÿһҳҪ��ʾ�Ĳ��֣�����Ӧ����Ҫ���ص����������ʾ������ �Լ���Ҫ��ʾ����ҳ��
				 */
				LayoutInflater mLi = LayoutInflater.from(this);
				View view = mLi.inflate(R.layout.gallery_image_pager, null);
				ImageView image = (ImageView) view.findViewById(R.id.imageview);
				imageLoader.displayImage(imageUrls[i], image, getImageOptions());
				views.add(view);
			}

			mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

			// ���ViewPager������������
			GalleryPagerAdapter mPagerAdapter = new GalleryPagerAdapter(views);
			mViewPager.setAdapter(mPagerAdapter);
			mViewPager.setCurrentItem(position);//���õ�ǰ��ʾҳ��
			if (position == 0) {
				for (int i = 0; i < dotImageViews.size(); i++) {
					if (i == 0) {
						dotImageViews.get(i).setImageDrawable(getResources().getDrawable(R.drawable.page_now));
					} else {
						dotImageViews.get(i).setImageDrawable(getResources().getDrawable(R.drawable.page));
					}
				}
			}
		}
	}

	/**
	 *
	 * ��Ŀ���ƣ�quanzi  
	 * �����ƣ�MyOnPageChangeListener  
	 * ������������ʱ���¼�
	 * @author zhangshuai
	 * @date ����ʱ�䣺2015-5-29 ����9:24:21 
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int page) {

			// ��ҳʱ��ǰpage,�ı䵱ǰ״̬԰��ͼƬ
			for (int i = 0; i < dotImageViews.size(); i++) {
				if (page == i) {
					dotImageViews.get(i).setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				} else {
					dotImageViews.get(i).setImageDrawable(getResources().getDrawable(R.drawable.page));
				}
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 *
	 * ��Ŀ���ƣ�quanzi  
	 * �����ƣ�GalleryPagerAdapter  
	 * ��������������
	 * @author zhangshuai
	 * @date ����ʱ�䣺2015-5-29 ����9:24:11 
	 *
	 */
	public class GalleryPagerAdapter extends PagerAdapter {
		private ArrayList<View> views;

		public GalleryPagerAdapter(ArrayList<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return this.views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		// ҳ��view
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}
	}

	/**
	 * ��ͨͼƬ����
	 * @return
	 */
	protected DisplayImageOptions getImageOptions() {
		DisplayImageOptions options = null;
		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions  
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading)// ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.image_error) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.image_error) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.cacheInMemory(false) // �������ص�ͼƬ�Ƿ񻺴����ڴ���  
				.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����  
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build(); // �������ù���DisplayImageOption����
		return options;
	}

}
