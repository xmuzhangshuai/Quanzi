package com.quanzi.customewidget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�MyMenuDialog  
 * ��������
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-30 ����4:53:33 
 *
 */
public class MyMenuDialog {
	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	private ListView menuitemListView;
	private List<String> menuitemList;
	private int[] drawables = new int[] {};

	public MyMenuDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		//�ؼ������������,ʹ��window.setContentView,�滻�����Ի��򴰿ڵĲ���
		Window window = ad.getWindow();
		window.setContentView(R.layout.menu_dialog);
		titleView = (TextView) window.findViewById(R.id.title);
		menuitemListView = (ListView) window.findViewById(R.id.dialog_listview);
	}

	public void setListItemClickListener(OnItemClickListener listener) {
		menuitemListView.setOnItemClickListener(listener);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMenuList(List<String> list) {
		this.menuitemList = list;
	}

	public void setDrawable(int[] drawable) {
		this.drawables = drawable;
	}

	/**
	 * �رնԻ���
	 */
	public void dismiss() {
		ad.dismiss();
	}

	public void show() {
		if (menuitemList != null) {
			menuitemListView.setAdapter(new MenuDialogAdapter());
		}

		ad.setCancelable(true);
		ad.show();
	}

	/**
	 *
	 * ��Ŀ���ƣ�quanzi  
	 * �����ƣ�HomeDialogAdapter  
	 * ��������
	 * @author zhangshuai
	 * @date ����ʱ�䣺2015-4-27 ����11:52:43 
	 *
	 */
	private class MenuDialogAdapter extends BaseAdapter {
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
				view = LayoutInflater.from(context).inflate(R.layout.menu_dialog_list_item, null);
				holder = new ViewHolder();
				holder.itemName = (TextView) view.findViewById(R.id.item_name);
				holder.righttext = (TextView) view.findViewById(R.id.righttext);
				holder.imageView = (ImageView) view.findViewById(R.id.leftimage);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.itemName.setText(menuitemList.get(position));

			if (drawables.length > 0) {
				holder.imageView.setVisibility(View.VISIBLE);
				holder.imageView.setImageResource(drawables[position]);
			} else {
				holder.imageView.setVisibility(View.GONE);
			}

			return view;
		}
	}
}
