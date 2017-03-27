package pencil.abhishek.io.pencil;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import pencil.abhishek.io.pencil.Package_wallpaper.wallsplash;


public class FirstActivity extends AppCompatActivity {

    String[] PERMISSIONS ={ Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA} ;

    public static int PERMISSION_ALL_REQUEST_CODE = 9 ;


    TextView OK ;
    Animation slideLeft ,slideRight ;
    CardView layout01,layout02,Heading ;
    RelativeLayout lay01,lay02, denial_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);




        slideLeft = AnimationUtils.loadAnimation(this,R.anim.left_in_1);
        slideRight =  AnimationUtils.loadAnimation(this,R.anim.right_in_1);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;



        Heading = (CardView) findViewById(R.id.relativeLayout);
        lay01 = (RelativeLayout) findViewById(R.id.lay01);
        lay02 = (RelativeLayout) findViewById(R.id.lay02);
        layout01 = (CardView) findViewById(R.id.layout01);
        layout02 = (CardView) findViewById(R.id.layout02);




       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){

            lay01.setBackground(getDrawable(R.drawable.ripple_effect));
            lay02.setBackground(getDrawable(R.drawable.ripple_effect));
        }else {

           TypedValue outValue = new TypedValue();
           getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
           lay01.setBackgroundResource(outValue.resourceId);
           lay02.setBackgroundResource(outValue.resourceId);
       }

        layout01.startAnimation(slideLeft);
        layout02.startAnimation(slideRight);

         //cool_lay.getBackground().setAlpha(100);



        checkReqPermissions();

        lay01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,EditActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.activity_close_scale);
            }
        });

        lay02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wallintent = new Intent(FirstActivity.this, wallsplash.class);
                startActivity(wallintent);
                overridePendingTransition(R.anim.right_in,R.anim.activity_close_scale);
            }
        });



    }


    public void checkReqPermissions(){
        if (!hasPermissions(getBaseContext(),PERMISSIONS)){

            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL_REQUEST_CODE);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == PERMISSION_ALL_REQUEST_CODE  ) {

            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED)

            {





            }else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                                 || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                       || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))
                {


                    showDialogOK("STORAGE and CAMERA  Permission required for this app",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                           checkReqPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            finish();
                                            // proceed with logic by disabling the related features or quit the app.
                                            break;
                                    }
                                }
                            });
                } else {
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();

                }
            }


        }

    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



    public static boolean hasPermissions(Context context , String... permissions){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context!=null && permissions != null){

            for (String permission : permissions){

                if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){

                    return false ;
                }
            }
        }

        return true ;


    }


}
