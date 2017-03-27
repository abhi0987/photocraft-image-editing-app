package pencil.abhishek.io.pencil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pencil.abhishek.io.pencil.Adapter.editAdapter;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.Category;
import pencil.abhishek.io.pencil.utills.GalleryPhotoAlbum;
import pencil.abhishek.io.pencil.utills.TypefaceSpan;

public class EditActivity extends AppCompatActivity {

    ToolLoaderFactory.Tools[] mTools = {
            ToolLoaderFactory.Tools.CROP,
            ToolLoaderFactory.Tools.EFFECTS,
            ToolLoaderFactory.Tools.ADJUST,
            ToolLoaderFactory.Tools.FRAMES,
            ToolLoaderFactory.Tools.COLOR,
            ToolLoaderFactory.Tools.SPLASH,
            ToolLoaderFactory.Tools.ORIENTATION,
            ToolLoaderFactory.Tools.ENHANCE,
            ToolLoaderFactory.Tools.REDEYE

    };


    SharedPreferences pref_shp ;
    private static final String PREF_NAME = "ph_bucket";

    ProgressWheel eWheel;


    public String mTakingPhotoPath;
    public String mEditingPhotoPath;

    public static final int REQUEST_DEAFULT_GALLERY = 7890 ;

    public static final int REQUEST_EDIT_PHOTO = 4567 ;

    public static final int REQ_CAMERA_CODE = 4321 ;

    public  String FolderName = "PhotoCraft";

    private int columnWidth;
    private int imageWidth;
    public static final int GRID_PADDING = 2;
    float padding;


    File  image  = null;

    public static RecyclerView recyclerView;
    static ArrayList<GalleryPhotoAlbum> albumList;
   public static editAdapter eadapter ;
    String NAME_ACTIVITY = "EDIT YOUR IMAGES";
    static ArrayList<Integer> countList;


    FloatingActionButton fab;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == RESULT_OK){


            recreate();
        }else if (requestCode==REQ_CAMERA_CODE){

            if(resultCode==RESULT_OK){




              Uri uri = Uri.fromFile(image);


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imgFilename ="photocraft_edit_" +  timeStamp;

                File myDir  = new File(Environment.getExternalStorageDirectory(),FolderName);

                if(!myDir.exists()){
                    myDir.mkdir();
                }

                File nimage = null;
                try {
                    nimage = File.createTempFile(
                            imgFilename,  /* prefix */
                            ".jpg",         /* suffix */
                            myDir      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mEditingPhotoPath = "file:" + nimage.getAbsolutePath();

                Intent editIntent = new AdobeImageIntent.Builder(EditActivity.this)
                        .setData(uri)
                        .withOutput(Uri.fromFile(nimage))
                        .withOutputFormat(Bitmap.CompressFormat.JPEG)
                        .withOutputSize(MegaPixels.Mp5)
                        .withOutputQuality(100)
                        .withAutoColorEnabled(true)
                        .saveWithNoChanges(false)
                        .withToolList(mTools)
                        .withVibrationEnabled(true)
                        .build();

                startActivityForResult(editIntent,6);
                overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);

            }
        }


        else if (requestCode == REQUEST_DEAFULT_GALLERY){

            if (resultCode == RESULT_OK){


                Uri selectGalleryImage = data.getData();


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String img_Filename ="photocraft_edit_" +  timeStamp;

                File my_Dir  = new File(Environment.getExternalStorageDirectory(),FolderName);

                if(!my_Dir.exists()){
                    my_Dir.mkdir();
                }

                File gimage = null;
                try {
                    gimage = File.createTempFile(
                            img_Filename,  /* prefix */
                            ".jpg",         /* suffix */
                            my_Dir      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }



                mEditingPhotoPath = "file:" + gimage.getAbsolutePath();

                Intent editIntent = new AdobeImageIntent.Builder(EditActivity.this)
                        .setData(selectGalleryImage)
                        .withOutput(Uri.fromFile(gimage))
                        .withOutputFormat(Bitmap.CompressFormat.JPEG)
                        .withOutputSize(MegaPixels.Mp3)
                        .withOutputQuality(90)
                        .withToolList(mTools)
                        .withVibrationEnabled(true)
                        .build();

               startActivityForResult(editIntent,6);
                overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);

            }
        }

        else if (requestCode==6){

            if(resultCode==RESULT_OK){

                recreate();
            }
        }




    }

////////////////////////////all methods are under this margin/////////////////




    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null,
                null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }








    public static Uri getImageContentUri(Context context , String imageFile){
        // String filePath = imageFile.getAbsolutePath();
        String[] star ={ MediaStore.Images.Media._ID };
        String p = MediaStore.Images.Media.DATA + "=?";
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, star, p, new String[]{imageFile}, null);
        if(cursor!=null && cursor.moveToFirst()){
            int ID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + ID);
        }

        else {

            return null;
        }


    }


    public void open_default_gallery(){

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        galleryIntent.setType("image/*");

        startActivityForResult(Intent.createChooser(galleryIntent,"Select a Image"),REQUEST_DEAFULT_GALLERY);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);


    }

    ////////////////////////////all methods are above this margin////////////////////////

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.right_out);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SpannableString s = new SpannableString(NAME_ACTIVITY);
        s.setSpan(new TypefaceSpan(this, "urban.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eWheel = (ProgressWheel) findViewById(R.id.eWheel);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        pref_shp = getSharedPreferences("STORE_ALBUM_NAME",MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.recycleview_edit);
        albumList = new ArrayList<>();
        countList = new ArrayList<>();

        InitilizeGridLayout();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(2,dpToPx(2),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        eWheel.setVisibility(View.VISIBLE);




         new GalleryAlbumAsync().execute();





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imgFilename = "photocraft_cam" + timeStamp;

                File nfile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);


                try {
                     image = File.createTempFile(
                            imgFilename,  /* prefix */
                            ".jpg",         /* suffix */
                            nfile      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mTakingPhotoPath = "file:" + image.getAbsolutePath();
                Intent takePicintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(takePicintent,REQ_CAMERA_CODE);


            }
        });






        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                SharedPreferences.Editor editor = pref_shp.edit();

                Intent intent = new Intent(EditActivity.this,Photo_list.class);
               // intent.putExtra("Bucket_name",albumList.get(position).getBucketName());
                editor.putString("BUCKET_NAME" ,albumList.get(position).getBucketName());
                editor.commit();
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));





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







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.activity_open_scale,R.anim.right_out);
                return  true ;

            case R.id.action_settings :
                open_default_gallery();
                return true;


            case R.id.action_rating :


                        String ur = "https://play.google.com/store/apps/details?id=pencil.abhishek.io.pencil";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ur));
                        startActivity(browserIntent);




                return true;


            case R.id.action_about :

                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                LayoutInflater inflater = LayoutInflater.from(EditActivity.this);
                View view = inflater.inflate(R.layout.about_dialog,null);
                Button ok = (Button) view.findViewById(R.id.buttonOk);

                builder.setView(view);

                AlertDialog alert = builder.create();

                alert.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });



                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }





    public class GalleryAlbumAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            getAlbumWise();


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            eadapter = new editAdapter(getBaseContext(),albumList,countList,columnWidth);
            recyclerView.setAdapter(eadapter);
            eWheel.setVisibility(View.GONE);

        }

        private void getAlbumWise() {

            String[] projection_album = {MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_ADDED,
                    MediaStore.Images.ImageColumns.DATA};

            String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_ADDED + " DESC";
          //  String BUCKET_ORDER_BY = "MAX(datetaken) DESC";


            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;



            Cursor cursor = getContentResolver().query(images, projection_album, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

            Log.v("ListingImages", " query count=" + cursor.getCount());

            GalleryPhotoAlbum album;


            if (cursor.moveToFirst()) {

                String bucket;
                String date;
                String data;
                long bucketId;



                do {


                    bucket = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    bucketId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));



                    if (bucket != null && bucket.length() > 0) {

                        album = new GalleryPhotoAlbum();
                        album.setBucketId(bucketId);
                        album.setBucketName(bucket);
                        album.setDateTaken(date);
                        album.setData(data);
                       // album.setTotalCount(photoCountByAlbum(bucket));


                        albumList.add(album);

                       Log.v("ListingImages", " bucket=" + bucket
                                + "  date_taken=" + date + "  _data=" + data
                                + " bucket_id=" + bucketId);




                    }


                } while (cursor.moveToNext());

                for(int i = 0 ; i< albumList.size();i++){

                    countList.add(photoCountByAlbum(albumList.get(i).getBucketName()));
                }
            }

            cursor.close();
        }


    }


    private int photoCountByAlbum(String bucketName) {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            Cursor mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");

            if (mPhotoCursor.getCount() > 0) {
                return mPhotoCursor.getCount();
            }
            mPhotoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

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






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }



}