package pencil.abhishek.io.pencil.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.navDraweItems;
import pencil.abhishek.io.pencil.R;

/**
 * Created by Abhishek on 6/5/2016.
 */
public class Nav_adapter extends RecyclerView.Adapter<Nav_adapter.nav_holder> {

    Context context;
    List<navDraweItems> data = Collections.emptyList();

    int position;

    public Nav_adapter(Context baseContext, ArrayList<navDraweItems> navDrawerItems) {
        this.context = baseContext;
        data = navDrawerItems;
    }



    @Override
    public Nav_adapter.nav_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_each, parent, false);
        nav_holder holder = new nav_holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Nav_adapter.nav_holder holder, int position) {
        navDraweItems current = data.get(position);

        holder.card.getBackground().setAlpha(50);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){

            holder.ripp.setBackground(context.getDrawable(R.drawable.ripple_effect));
        }else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.ripp.setBackgroundResource(outValue.resourceId);
        }

        holder.a_name.setText(current.getAlbumTitle());
        holder.a_count.setText( "("+current.getAlbumPhotCount()+")" );
    }

    @Override
    public int getItemCount() {
        return  data.size();
    }


    public class nav_holder extends RecyclerView.ViewHolder{

        TextView a_name , a_count ;
        RelativeLayout ripp ;
        CardView card;

        public nav_holder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card);
            a_name = (TextView) itemView.findViewById(R.id.titletext);
            a_count = (TextView) itemView.findViewById(R.id.photocountText);
            ripp = (RelativeLayout) itemView.findViewById(R.id.ripp_);
        }
    }
}
