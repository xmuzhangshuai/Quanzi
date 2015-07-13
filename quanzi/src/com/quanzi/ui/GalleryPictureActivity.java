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
 * 项目名称：quanzi  
 * 类名称：GalleryPictureActivity  
 * 类描述：多张大图浏览时页面
 * @author zhangshuai
 * @date 创建时间：2015-5-26 上午10:55:59 
 *
 */
public class GalleryPictureActivity extends BaseFragmentActivity {
	public static final String IMAGE_URLS = "image_urls";
	public static final String POSITON = "position";
	// 翻页控件
	private ViewPager mViewPager;

	// 这个是底部显示当前状态点imageView
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
			 * 这里将每一页显示的view存放到ArrayList集合中 可以在ViewPager适配器中顺序调用展示
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
				 * 这里是每一页要显示的布局，根据应用需要和特点自由设计显示的内容 以及需要显示多少页等
				 */
				LayoutInflater mLi = LayoutInflater.from(this);
				View view = mLi.inflate(R.layout.gallery_image_pager, null);
				ImageView image = (ImageView) view.findViewById(R.id.imageview);
				imageLoader.displayImage(imageUrls[i], image, getImageOptions());
				views.add(view);
			}

			mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

			// 填充ViewPager的数据适配器
			GalleryPagerAdapter mPagerAdapter = new GalleryPagerAdapter(views);
			mViewPager.setAdapter(mPagerAdapter);
			mViewPager.setCurrentItem(position);//设置当前显示页面
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
	 * 项目名称：quanzi  
	 * 类名称：MyOnPageChangeListener  
	 * 类描述：滑动时的事件
	 * @author zhangshuai
	 * @date 创建时间：2015-5-29 下午9:24:21 
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int page) {

			// 翻页时当前page,改变当前状态园点图片
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
	 * 项目名称：quanzi  
	 * 类名称：GalleryPagerAdapter  
	 * 类描述：适配器
	 * @author zhangshuai
	 * @date 创建时间：2015-5-29 下午9:24:11 
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

		// 页面view
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}
	}

	/**
	 * 普通图片设置
	 * @return
	 */
	protected DisplayImageOptions getImageOptions() {
		DisplayImageOptions options = null;
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions  
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading)// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.image_error) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中  
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中  
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build(); // 创建配置过得DisplayImageOption对象
		return options;
	}

}
