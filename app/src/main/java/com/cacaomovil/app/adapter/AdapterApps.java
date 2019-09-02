package com.cacaomovil.app.adapter;

/**
 * Created by Francisco on 6/7/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cacaomovil.app.R;
import com.cacaomovil.app.models.AppsM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AdapterApps extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater=null;
    List<AppsM> listfeed= new ArrayList<>();
    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 200;
    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));


    public AdapterApps(Activity a, List<AppsM> list_feed) {
        activity = a;
        listfeed=list_feed;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return listfeed.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder {
        public ImageView image_favorite;
        public TextView tx_name;
    }


    public View getView(int position, View convertView, ViewGroup container) {
        View vi = convertView;
        ViewHolder holder;

        if (vi == null) {
            vi = inflater.inflate(R.layout.items_apps, container, false);
            holder = new ViewHolder();
            holder.image_favorite = (ImageView) vi.findViewById(R.id.image_favorite);
            holder.tx_name= (TextView) vi.findViewById(R.id.tx_name);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }



        if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        System.out.println("aqui "+position);

        Picasso.get()
                .load(listfeed.get(position).getImage())
                .placeholder(R.drawable.web_hi)
                .error(R.drawable.web_hi)
                .resize(200, 200)
                .into(holder.image_favorite);


        holder.tx_name.setText(listfeed.get(position).getNombre());

        return vi;
    }


    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }




}