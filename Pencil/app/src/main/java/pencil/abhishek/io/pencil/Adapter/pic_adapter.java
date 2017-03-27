package pencil.abhishek.io.pencil.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pencil.abhishek.io.pencil.R;

/**
 * Created by Abhishek on 5/13/2016.
 */
public class pic_adapter extends RecyclerView.Adapter<pic_adapter.pic_holder> {

    Context context ;
    ArrayList<String> picList;
    ArrayList<Integer> hList;
    int imgWidth ;

    public pic_adapter(Context baseContext, ArrayList<String> picList, ArrayList<Integer> hList, int columnWidth) {

        this.context = baseContext;
        this.picList =  picList;
        this.hList = hList;
        this.imgWidth = columnWidth;
    }

    @Override
    public pic_adapter.pic_holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.ic_layout,parent,false);
        pic_holder holder = new pic_holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(pic_adapter.pic_holder holder, int position) {



        holder.pic.setLayoutParams(new RelativeLayout.LayoutParams(imgWidth,imgWidth+90));

        Glide.with(context).load(picList.get(position)).
                 thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);


    }






    @Override
    public int getItemCount() {
        return picList.size();
    }

    public class pic_holder extends RecyclerView.ViewHolder{

        ImageView pic ;

        public pic_holder(View itemView) {
            super(itemView);

            pic= (ImageView) itemView.findViewById(R.id.pic);

        }
    }






}
