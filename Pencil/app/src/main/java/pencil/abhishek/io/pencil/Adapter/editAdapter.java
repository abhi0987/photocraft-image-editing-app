package pencil.abhishek.io.pencil.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pencil.abhishek.io.pencil.R;
import pencil.abhishek.io.pencil.utills.GalleryPhotoAlbum;

/**
 * Created by Abhishek on 5/13/2016.
 */
public class editAdapter extends RecyclerView.Adapter<editAdapter.listHolder> {
    Context context;
    List<GalleryPhotoAlbum> albumList;
    int imgwidth;
    ArrayList<Integer> countList;

    public editAdapter(Context baseContext, List<GalleryPhotoAlbum> albumList, ArrayList<Integer> countList, int columnWidth) {
        this.context = baseContext;
        this.albumList = albumList;
        this.imgwidth = columnWidth ;
        this.countList = countList;
    }

    @Override
    public editAdapter.listHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_item_layout,parent,false);
        listHolder holder = new listHolder(view);


        return holder;
    }



    @Override
    public void onBindViewHolder(editAdapter.listHolder holder, int position) {



        holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.thumbnail.setLayoutParams(new RelativeLayout.LayoutParams(imgwidth,imgwidth+90));
        holder.linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(imgwidth,imgwidth+90));
        holder.linearLayout.getBackground().setAlpha(130);


       Glide.with(context).load(albumList.get(position).getData()).
                thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);

        Log.d("adaper_thumb",albumList.get(position).getData());

        holder.name.setText(albumList.get(position).getBucketName());

        if (albumList.get(position).getTotalCount()==1){

           // holder.count.setText(String.valueOf(albumList.get(position).getTotalCount()) + " Photo");
            holder.count.setText(String.valueOf(countList.get(position)) + " Photo");
        }else {

            holder.count.setText(String.valueOf(countList.get(position)) + " Photos");
           // holder.count.setText(String.valueOf(albumList.get(position).getTotalCount()) + " Photos");
        }





    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class listHolder extends RecyclerView.ViewHolder{


        ImageView thumbnail;
        RelativeLayout layout;
        RelativeLayout linearLayout;
        TextView name,count ;

        public listHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            layout = (RelativeLayout) itemView.findViewById(R.id.each);
           linearLayout = (RelativeLayout) itemView.findViewById(R.id.blur_lay);
            name = (TextView) itemView.findViewById(R.id.name);
            count = (TextView) itemView.findViewById(R.id.count);



        }
    }
}
