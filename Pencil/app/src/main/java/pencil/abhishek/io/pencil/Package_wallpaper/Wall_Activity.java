package pencil.abhishek.io.pencil.Package_wallpaper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pencil.abhishek.io.pencil.Adapter.Nav_adapter;
import pencil.abhishek.io.pencil.MyApplication;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.Category;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.navDraweItems;
import pencil.abhishek.io.pencil.R;
import pencil.abhishek.io.pencil.utills.TypefaceSpan;

public class Wall_Activity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final String TAG = Wall_Activity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
  public static   boolean stat ;
    int pos;
    private List<Category> albumsList;

    public static final int REQUEST_DEAFULT_GALLERY = 7890 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_list_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        albumsList = MyApplication.getInstance().getPrefManger().getCategories();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
















        Random r = new Random();
        int num = r.nextInt(albumsList.size());

        displayView(num);


    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);

    }



    @Override
    public void onBackPressed() {

        if (stat == true) {
            drawerFragment.closeDrawer();
            stat = false;
        } else {

            super.onBackPressed();
            overridePendingTransition(R.anim.activity_open_scale,R.anim.right_out);


        }


    }

    private void displayView(int position) {

        Fragment fragment = null;
        String albumId = albumsList.get(position).getId();
        fragment = Wall_grid_fragment.newInstance(albumId);

        Log.e(TAG, "GridFragment is creating");

        if (fragment != null){

        try {


            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            SpannableString s = new SpannableString(albumsList.get(position).getTitle());
            s.setSpan(new TypefaceSpan(this, "AvenirLTStd-Book.otf"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            getSupportActionBar().setTitle(s);



        }catch (Exception e){

            e.printStackTrace();

        }
        }else {


            Log.e(TAG, "Error in creating fragment");

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wall, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case R.id.action_about :

                AlertDialog.Builder builder = new AlertDialog.Builder(Wall_Activity.this);
                LayoutInflater inflater = LayoutInflater.from(Wall_Activity.this);
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



            case R.id.action_rating:

                String ur = "https://play.google.com/store/apps/details?id=pencil.abhishek.io.pencil";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ur));
                startActivity(browserIntent);


                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }




}
