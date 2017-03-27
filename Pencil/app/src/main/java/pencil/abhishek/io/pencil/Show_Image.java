package pencil.abhishek.io.pencil;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;




public class Show_Image extends AppCompatActivity {

    ImageView show_img;
    protected static final int REQUEST_EDIT_PHOTO = 1234;

    FloatingActionButton fab1,fab2,fab3,fab4 ;
    TextView qs;
    RelativeLayout black;

    SharedPreferences show_pref;

    int postion_;
    String img_uri ;
    int width,height;
    public  String FolderName = "PhotoCraft";

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



    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK);
        super.finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_EDIT_PHOTO){

            if (resultCode==RESULT_OK){

                Uri editedImageUri = data.getData();



                show_img.setImageURI(editedImageUri);

                String uri_ = getRealPathFromURI(editedImageUri);

                fab1.setVisibility(View.GONE);
                fab4.setVisibility(View.GONE);

                fab2.setVisibility(View.VISIBLE);
                qs.setVisibility(View.VISIBLE);
                black.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);



                  fab2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          share_edit_image(uri_);
                      }
                  });


                fab3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //share_edit_image(editedImageUri);
                        Uri uri = getImageContentUri(getBaseContext(),uri_);

                        Intent intent = null;
                        intent = new Intent(Intent.ACTION_ATTACH_DATA);
                        intent.setDataAndType(uri, "image/*");
                        intent.putExtra("mimeType", "image/*");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Intent chooser_intent = Intent.createChooser(intent,"Set As");
                        chooser_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(chooser_intent);
                    }
                });

            }else {



            }
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);
    }


    ////////////All methods///////////////////that are used// in the activity/////

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

    private void share_edit_image(String editedImageUri) {
        Uri uri = getImageContentUri(getBaseContext(),editedImageUri);

        Intent share_intent = new Intent(Intent.ACTION_SEND);
        share_intent.setDataAndType(uri,"image/*");
        share_intent.putExtra("mimeType", "image/*");
        share_intent.putExtra(Intent.EXTRA_STREAM,uri);
        Intent chooser_intent = Intent.createChooser(share_intent,"Share Using ");
        chooser_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);


        try {
            startActivity(chooser_intent);
        }catch (Exception e){
            e.printStackTrace();
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



    private void edit_photo(String uri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFilename ="photocraft_edit_" +  timeStamp;

        File myDir  = new File(Environment.getExternalStorageDirectory(),FolderName);

        if(!myDir.exists()){
            myDir.mkdir();
        }

        File image = null;
        try {
            image = File.createTempFile(
                    imgFilename,  /* prefix */
                    ".jpg",         /* suffix */
                    myDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }




        Intent editIntent = new AdobeImageIntent.Builder(Show_Image.this)
                .setData(Uri.parse(uri))
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
    }


    public void share_pic(String img_url){



        Uri uri = getImageContentUri(getBaseContext(),img_url);
        Intent share_intent = new Intent(Intent.ACTION_SEND);
        share_intent.setDataAndType(uri,"image/*");
        share_intent.putExtra(Intent.EXTRA_STREAM,uri);
        Intent chooser_intent = Intent.createChooser(share_intent,"Share Using ");
        chooser_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);


        try {
            startActivity(chooser_intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


////////////All methods///////////////////that are used// in the activity/////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show__image);

        show_pref = getSharedPreferences("STORE_EACH_PIC",MODE_PRIVATE);



        fab1= (FloatingActionButton) findViewById(R.id.fab1);
        fab2= (FloatingActionButton) findViewById(R.id.fab2);
        fab3= (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);


        qs = (TextView) findViewById(R.id.qs);
        show_img = (ImageView) findViewById(R.id.show_img);
        black = (RelativeLayout) findViewById(R.id.black);

      //  postion_ = getIntent().getIntExtra("position_",0);
       // img_uri= getIntent().getStringExtra("Img_uri_string");

        img_uri = show_pref.getString("one_image",null);
        postion_ = show_pref.getInt("postion",0);


        Glide.with(getBaseContext()).load(img_uri).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(show_img);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_photo(img_uri);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_pic(img_uri);
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = getImageContentUri(getBaseContext(),img_uri);

                Intent intent = null;
                intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Intent chooser_intent = Intent.createChooser(intent,"Set As");
                chooser_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooser_intent);
            }
        });




        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);

    }












}
