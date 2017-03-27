package pencil.abhishek.io.pencil.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import pencil.abhishek.io.pencil.MyApplication;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.Wallpaper;
import pencil.abhishek.io.pencil.Package_wallpaper.Wall_Activity;
import pencil.abhishek.io.pencil.R;

/**
 * Created by Abhishek on 6/5/2016.
 */
public class WallPaper_adapter extends RecyclerView.Adapter<WallPaper_adapter.wall_holder> {
    private Context _activity;
    private LayoutInflater inflater;
    private List<Wallpaper> wallpapersList = new ArrayList<Wallpaper>();

    private int imageWidth;
    ImageLoader imageLoader ;
    private String[] bgColors;

    public WallPaper_adapter(Context activity, List<Wallpaper> photosList , int columnWidth) {

        this._activity=activity;
        this.wallpapersList = photosList;
        this.imageWidth = columnWidth;

        bgColors = _activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }


    @Override
    public WallPaper_adapter.wall_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_activity).inflate(R.layout.new_grid_recycle,parent,false);
        wall_holder holder = new wall_holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WallPaper_adapter.wall_holder holder, int position) {
        Wallpaper p = wallpapersList.get(position);

        String color = bgColors[position % bgColors.length];

        holder.thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.thumbNail.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth,p.getHeight()));

        holder.thumbNail.setBackgroundColor(Color.parseColor(color));

        Glide.with(_activity)
                .load(p.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .thumbnail(0.5f)
                .into(holder.thumbNail);

    }

    @Override
    public int getItemCount() {
        return wallpapersList.size();

    }

  public class wall_holder extends RecyclerView.ViewHolder{

      ImageView thumbNail ;
      RelativeLayout ripp;

      public wall_holder(View itemView) {
          super(itemView);

          thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);
          ripp = (RelativeLayout) itemView.findViewById(R.id.select_ripple);
      }
  }

}
