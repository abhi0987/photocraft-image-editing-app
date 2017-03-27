package pencil.abhishek.io.pencil.Package_wallpaper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pencil.abhishek.io.pencil.MyApplication;
import pencil.abhishek.io.pencil.Package_wallpaper.App.AppConstant;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.Wallpaper;
import pencil.abhishek.io.pencil.R;
import pencil.abhishek.io.pencil.utills.Fabmanager;

public class Full_Resolution_Image extends AppCompatActivity {
    private static final String TAG = Full_Resolution_Image.class.getSimpleName();
    ImageView imageView , ic_fab;
    public static final String TAG_SEL_IMAGE = "selectedImage";

    private static final String TAG_ENTRY = "entry",
            TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url";



    List<Wallpaper> photos = new ArrayList<Wallpaper>();
    String fullResolutionUrl;

    public static final int progress_bar_type = 0;

    public File file;

    private ProgressDialog pDialog;

    RelativeLayout set_wall ;
    RelativeLayout layout1  ;



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full__resolution__image);






        layout1 = (RelativeLayout) findViewById(R.id.lienear_);



        set_wall = (RelativeLayout) findViewById(R.id.set_wall);
        set_wall.getBackground().setAlpha(50);


        imageView = (ImageView) findViewById(R.id.full_img_);



        layout1.setVisibility(View.GONE);





      String  stringGson = getIntent().getStringExtra(TAG_SEL_IMAGE);
      int   position_ = getIntent().getIntExtra("postion",0);

        Gson gson = new Gson();

        Wallpaper[] photoarry = gson.fromJson(stringGson,Wallpaper[].class);
        photos = Arrays.asList(photoarry);
        photos = new ArrayList<Wallpaper>(photos);



        Glide.with(getBaseContext()).load(photos.get(position_).getUrl()).thumbnail(0.5f).animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        Cache cache = MyApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(photos.get(position_).getPhotoJson());

        if(entry!=null){

            try{

                String data = new String(entry.data,"UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }catch (Exception e){

            }
        }else {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, photos.get(position_).getPhotoJson(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Image full resolution json: " + response.toString());


                    parseJsonFeed(response);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    error.printStackTrace();

                }
            });


            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        }




        set_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_wallpaper(position_);

            }
        });



    }

    private void parseJsonFeed(JSONObject response) {
        JSONObject entry = null;

        try {


            entry = response.getJSONObject(TAG_ENTRY);


            JSONArray mediacontentArry = entry.getJSONObject(
                    TAG_MEDIA_GROUP).getJSONArray(
                    TAG_MEDIA_CONTENT);

            JSONObject mediaObj = (JSONObject) mediacontentArry.get(0);

            fullResolutionUrl = mediaObj.getString(TAG_IMG_URL);

            /*Glide.with(getBaseContext()).load(fullResolutionUrl).thumbnail(0.1f).animate(android.R.anim.fade_in)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);*/

            layout1.setVisibility(View.VISIBLE);





        }catch (Exception e){


            e.printStackTrace();
            Toast.makeText(getBaseContext(),"Unknown erroe occured",Toast.LENGTH_LONG).show();

        }
    }


    public void set_wallpaper(int position){

        Wallpaper image = photos.get(position);
        String image_name = image.getTitle();


        new downloadFile_url(image_name).execute(fullResolutionUrl);



    }


    class downloadFile_url extends AsyncTask<String,String,String> {

        String img_name;

        public downloadFile_url(String image_name) {
            img_name = image_name;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dismissDialog(progress_bar_type);
            pDialog.dismiss();

            Uri uri = getImageContentUri(getBaseContext(),file);


                Intent intent = null;
                intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Intent chooser_intent = Intent.createChooser(intent,"Set As");
                chooser_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooser_intent);
                overridePendingTransition(R.anim.right_in,R.anim.activity_close_scale);



        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected String doInBackground(String... params) {


            File myDir  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConstant.SDCARD_DIR_NAME);
            myDir.mkdirs();
            String fname = img_name ;
            file = new File(myDir,fname);

            if(file.exists()){

                Log.d("existing path :" , file.getAbsolutePath());


            }else {


                try {
                    URL nUrl = new URL(params[0]);
                    URLConnection conection = nUrl.openConnection();
                    conection.connect();
                    int lenghtOfFile = conection.getContentLength();
                    InputStream input = new BufferedInputStream(nUrl.openStream(), 8192);

                    FileOutputStream out = new FileOutputStream(file);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;

                    while ((count = input.read(data)) != -1) {

                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        out.write(data, 0, count);
                    }
                    out.flush();

                    out.close();
                    input.close();


                    Log.e("img_stored_path", file.getAbsolutePath());


                }catch (Exception e){
                    e.printStackTrace();


                }


            }



            return null;

        }
    }



   /* private void edit_photo(Uri uri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFilename ="photocraft_edit_" +  timeStamp;

        File myDir  = new File(Environment.getExternalStorageDirectory(),FolderName);

        if(!myDir.exists()){
            myDir.mkdir();
        }

        File image = null;
        try {
            image = File.createTempFile(
                    imgFilename,
                    ".jpg",
                    myDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }




        Intent editIntent = new AdobeImageIntent.Builder(Full_Resolution_Image.this)
                .setData(uri)
                .withOutput(Uri.fromFile(image))
                .withOutputFormat(Bitmap.CompressFormat.JPEG)
                .withOutputSize(MegaPixels.Mp5)
                .withOutputQuality(100)
                .saveWithNoChanges(false)
                .withToolList(mTools)
                .withVibrationEnabled(true)
                .build();

        startActivityForResult(editIntent,REQUEST_EDIT_PHOTO);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);

        Log.d(FolderName, image.getAbsolutePath());
    }*/

    public static Uri getImageContentUri(Context context , File imageFile){
        String filePath = imageFile.getAbsolutePath();
        String[] star ={ MediaStore.Images.Media._ID };
        String p = MediaStore.Images.Media.DATA + "=?";
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, star, p, new String[]{filePath}, null);
        if(cursor!=null && cursor.moveToFirst()){
            int ID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + ID);
        }else {
            if(imageFile.exists()){
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }else {
                return null;
            }
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){

            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading Wallpaper");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;


        }




    }

}
