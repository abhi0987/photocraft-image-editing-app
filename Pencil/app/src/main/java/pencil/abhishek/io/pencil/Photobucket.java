package pencil.abhishek.io.pencil;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import pencil.abhishek.io.pencil.utills.GalleryPhotoAlbum;

public class Photobucket extends AppCompatActivity {

    RelativeLayout splah_lay;

    static List<GalleryPhotoAlbum> albumList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photobucket);
        splah_lay = (RelativeLayout) findViewById(R.id.splash_lay);






        goToNext();






    }


    public void goToNext(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                Intent intent = new Intent(Photobucket.this,FirstActivity.class);
                startActivity(intent);


                finish();

            }
        },2000);
    }







}
