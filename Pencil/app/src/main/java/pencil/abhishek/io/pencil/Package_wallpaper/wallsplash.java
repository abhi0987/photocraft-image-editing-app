package pencil.abhishek.io.pencil.Package_wallpaper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pencil.abhishek.io.pencil.MyApplication;
import pencil.abhishek.io.pencil.Package_wallpaper.App.AppConstant;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.Category;
import pencil.abhishek.io.pencil.R;

public class wallsplash extends AppCompatActivity {

    private final String TAG = wallsplash.class.getSimpleName();
    DilatingDotsProgressBar progressBar;
    RelativeLayout toast_err;
    TextView retry ;

    private final String TAG_FEED="feed",TAG_ENTRY="entry",TAG_GPHOTOID = "gphoto$id",TAG_T = "$t"
            ,TAG_ALBUM_TITLE = "title" ,TAG_NO_PHOTOS = "gphoto$numphotos" ;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wallsplash);

        progressBar = (DilatingDotsProgressBar) findViewById(R.id.dot_progress);
        toast_err = (RelativeLayout) findViewById(R.id.error_lay);
        retry = (TextView) findViewById(R.id.retry);

        toast_err.setVisibility(View.GONE);


        String url = AppConstant.URL_PICASA_ALBUMS.replace("PICASA_USER_", MyApplication.getInstance().getPrefManger().getGoogleUserName());
        Log.d(TAG, "Albums request url: " + url);
        JSONObject jsonObject = null;

        progressBar.showNow();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "Albums Response: " + response.toString());
                        List<Category> albums = new ArrayList<Category>() ;
                        try {
                            JSONArray entry = response.getJSONObject(TAG_FEED).getJSONArray(TAG_ENTRY);
                            for(int i =0 ; i<entry.length();i++){

                                JSONObject albumObj = (JSONObject) entry.get(i);
                                String album_id = albumObj.getJSONObject(TAG_GPHOTOID).getString(TAG_T);
                                String album_titel = albumObj.getJSONObject(TAG_ALBUM_TITLE).getString(TAG_T);
                                String albumNoOFpics = albumObj.getJSONObject(TAG_NO_PHOTOS).getString(TAG_T);

                                Category album = new Category();

                                album.setId(album_id);
                                album.setTitle(album_titel);
                                album.setPhotoNo(albumNoOFpics);

                                albums.add(album);

                                Log.d(TAG, "Album Id: " + album_id + ", Album Title: " + album_titel + " No Of Photos it has " + albumNoOFpics);

                            }

                            MyApplication.getInstance().getPrefManger().storeCategories(albums);

                            Intent intent = new Intent(wallsplash.this,Wall_Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.activity_close_scale);
                            finish();





                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

             @Override
              public void onErrorResponse(VolleyError error) {


                 progressBar.setVisibility(View.GONE);
                 toast_err.setVisibility(View.VISIBLE);


                 retry.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         recreate();
                     }
                 });


            }
        });




        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);


    }

}
