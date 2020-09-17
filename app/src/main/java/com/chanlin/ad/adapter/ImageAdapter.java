package com.chanlin.ad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chanlin.ad.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
	private Context context;
    private List<String> imageUrls;

	public ImageAdapter(Context context, List<String> urls) {
		this.context = context;
        this.imageUrls = urls;
	}

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
        	convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url = (String) imageUrls.get(position);

        if (url != null && !url.equals("")) {
            ImageLoader.getInstance().displayImage(url, holder.image);
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

	@Override
	public int getCount() {
		return imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
