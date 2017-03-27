package pencil.abhishek.io.pencil;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.system.Os;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pencil.abhishek.io.pencil.Adapter.pic_adapter;
import pencil.abhishek.io.pencil.utills.TypefaceSpan;

public class Photo_list extends AppCompatActivity {


    public static int REQUST_SHOW = 654 ;

    RecyclerView recyclerView;

    private int columnWidth;
    private int imageWidth;
    public static final int GRID_PADDING = 2;
    float padding;

    ArrayList<String> picList ;
    String bucket;
    public static pic_adapter picAdapter ;
    ArrayList<Integer> hList ;
    ArrayList<Integer> wList;

    SharedPreferences pref_shp , show_pref;


    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        show_pref = getSharedPreferences("STORE_EACH_PIC",MODE_PRIVATE);
        pref_shp = getSharedPreferences("STORE_ALBUM_NAME",MODE_PRIVATE);
      //  bucket = getIntent().getStringExtra("Bucket_name");
        bucket = pref_shp.getString("BUCKET_NAME",null);

        SpannableString s = new SpannableString(bucket.toUpperCase());
        s.setSpan(new TypefaceSpan(this, "urban.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        InitilizeGridLayout();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_pic);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(2,dpToPx(2),true));

        new load_pic_sync().execute();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                        SharedPreferences.Editor editor = show_pref.edit();

                        Intent show_intent = new Intent(Photo_list.this,Show_Image.class);

                        editor.putString("one_image",picList.get(position));
                        editor.putInt("postion",position);

                        editor.commit();

                    //   show_intent.putExtra("Img_uri_string",picList.get(position));

                     //   show_intent.putExtra("position_",position);


                    startActivityForResult(show_intent,REQUST_SHOW);

                    overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);


            }

            @Override
            public void onLongClick(View view, int position) {




            }
        }));


    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==REQUST_SHOW && resultCode == RESULT_OK){

            recreate();
            picAdapter.notifyDataSetChanged();

        }



    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();


        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);
    }



    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    public class load_pic_sync extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {

            getImagesOfBucket(bucket);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            picAdapter = new pic_adapter(getBaseContext(),picList,hList,columnWidth);

            recyclerView.setAdapter(picAdapter);

        }

        private void getImagesOfBucket(String bucket) {


            picList = new ArrayList<>();
            hList = new ArrayList<>();
            wList = new ArrayList<>();


            final String orderBy = MediaStore.Images.Media.DATE_ADDED;
            final String[] columns = { MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID ,MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH};

            String searchParams = null;

            searchParams = "bucket_display_name = \"" + bucket + "\"";


            Cursor mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    searchParams, null, orderBy + " DESC");

            if (mPhotoCursor.moveToFirst()){

                do {
                    String imgdata = mPhotoCursor.getString(mPhotoCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    int height = mPhotoCursor.getInt(mPhotoCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                    int width = mPhotoCursor.getInt(mPhotoCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));


                    picList.add(imgdata);
                    hList.add(height);
                    wList.add(width);



                }while (mPhotoCursor.moveToNext());

            }
            mPhotoCursor.close();

        }
    }



    private void InitilizeGridLayout() {
        Resources r = getResources();
        padding =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                GRID_PADDING, r.getDisplayMetrics());

        // Column width
        imageWidth = getScreenWidth();

        columnWidth = (int) ((getScreenWidth() - ((
                2 + 1) * padding)) /
                2);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                
                overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);
                return  true ;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpaceItemDecoration(int spanCount, int spacing, boolean includeEdge) {

            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }


        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }




    public int getScreenWidth(){
        int columnWidth;
        WindowManager wm = (WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point point = new Point();
        try{
            display.getSize(point);
        }catch (NoSuchMethodError e){
            point.x = display.getWidth();
            point.y = display.getHeight();

        }

        columnWidth=point.x;

        return columnWidth;
    }

}
