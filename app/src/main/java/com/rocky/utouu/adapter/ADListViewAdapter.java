package com.rocky.utouu.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.rocky.utouu.R;
import com.rocky.utouu.javabean.ADdetials;
import com.rocky.utouu.utils.BitmapSinglton;
import com.rocky.utouu.utils.Constants;

import java.util.List;

/**
 * Created by xiabaikui on 2015/12/14.
 */
public class ADListViewAdapter extends BaseAdapter {
    private final BitmapSinglton singlton;
    private final BitmapUtils bitmapUtils;
    private Context context;
    private List<ADdetials> list;
    private Handler handler;

    public ADListViewAdapter(Context context, List<ADdetials> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
        singlton = BitmapSinglton.getSinglton();
        bitmapUtils = singlton.getBitmapUtils();
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null)
            ret = list.size();
        return ret;
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = ((ViewHolder) convertView.getTag());
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.aditems, null);
            holder.iv = ((ImageView) convertView.findViewById(R.id.items_iv));
            holder.name = ((TextView) convertView.findViewById(R.id.items_title));
            holder.count = ((TextView) convertView.findViewById(R.id.items_tv_count));
            holder.gold = ((TextView) convertView.findViewById(R.id.items_gold));
            convertView.setTag(holder);
        }
        ADdetials aDdetials = list.get(position);
        holder.name.setText(aDdetials.getName());
        holder.count.setText(aDdetials.getView_count());
        holder.gold.setText(aDdetials.getAward_gold());
        String iv_path = Constants.URL_image + aDdetials.getPicture();
        Log.i("tag","---->imagesPath"+iv_path);
        bitmapUtils.display(holder.iv,iv_path);
        return convertView;
    }
    static class ViewHolder {
        ImageView iv;
        TextView name, count, gold;
    }
}
