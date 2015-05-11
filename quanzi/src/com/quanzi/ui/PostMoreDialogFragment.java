package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;

/**
 *
 * 项目名称：quanzi  
 * 类名称：PostMoreDialogFragment  
 * 类描述：点击帖子的更多时弹出的转发和举报的对话框
 * @author zhangshuai
 * @date 创建时间：2015-4-28 下午6:40:16 
 *
 */
public class PostMoreDialogFragment extends DialogFragment implements OnItemClickListener {
	private View rootView;
	private ListView menuitemListView;
	private List<String> menuitemList;

	/**
	 * 创建实例
	 * @return
	 */
	static PostMoreDialogFragment newInstance() {
		PostMoreDialogFragment f = new PostMoreDialogFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		menuitemListView = (ListView) rootView.findViewById(R.id.dialog_listview);
		menuitemList = new ArrayList<String>();

		menuitemList.add("转发");
		menuitemList.add("举报");

		menuitemListView.setAdapter(new PublishDialogAdapter());
		menuitemListView.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(getActivity(), TranspondActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			PostMoreDialogFragment.this.dismiss();
			break;
		case 1:
			//			intent = new Intent(getActivity(), QrCodeActivity.class);
			//			getActivity().startActivity(intent);
			//			getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			PostMoreDialogFragment.this.dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：HomeDialogAdapter  
	 * 类描述：
	 * @author zhangshuai
	 * @date 创建时间：2015-4-27 上午11:52:43 
	 *
	 */
	private class PublishDialogAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView itemName;
			public TextView righttext;
			public ImageView imageView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuitemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
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
			final ViewHolder holder;

			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_listview_item, null);
				holder = new ViewHolder();
				holder.itemName = (TextView) view.findViewById(R.id.item_name);
				holder.righttext = (TextView) view.findViewById(R.id.righttext);
				holder.imageView = (ImageView) view.findViewById(R.id.leftimage);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.itemName.setText(menuitemList.get(position));
			if (menuitemList.get(position).equals("转发")) {
				holder.imageView.setImageResource(R.drawable.transpond);
			} else {
				holder.imageView.setImageResource(R.drawable.report);
			}

			holder.imageView.setVisibility(View.VISIBLE);

			return view;
		}
	}
}
