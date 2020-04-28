package kronos.cacaomovil.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import kronos.cacaomovil.R;
import kronos.cacaomovil.models.AppsM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    private static final int COUNT = 100;

    private final Context mContext;
    //private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    List<AppsM> mItems =  new ArrayList<AppsM>();

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final CircleImageView image_favorite;
        public final LinearLayout contApp;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image_favorite = (CircleImageView) view.findViewById(R.id.image_favorite);
            contApp = (LinearLayout) view.findViewById(R.id.contApp);

        }
    }
    public SimpleAdapter(Context context, List<AppsM> arraylist) {
        mContext = context;
        mItems.addAll(arraylist);
        /*mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }*/
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.items_apps, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final AppsM item = mItems.get(position);
        holder.title.setText(item.getNombre());

        Picasso.get()
                .load(item.image)
                .into(holder.image_favorite);


        holder.contApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(item.app_type.toString().equals("app")){
                    Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(item.app_url);
                    if (launchIntent != null) {
                        mContext.startActivity(launchIntent);
                    }else{
                        String url = "http://play.google.com/store/apps/details?id="+item.app_url;
                        openUrl(url);
                    }
                }else{
                    String url = item.app_url;
                    openUrl(url);
                }


            }
        });


    }

    private void openUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application can handle this request." + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        //mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}