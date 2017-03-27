package pencil.abhishek.io.pencil;

import android.app.Application;
import android.text.TextUtils;

import com.adobe.creativesdk.aviary.IAviaryClientCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import pencil.abhishek.io.pencil.utills.LruBitmapCache;
import pencil.abhishek.io.pencil.utills.PrefManager;

/**
 * Created by Abhishek on 5/7/2016.
 */
public class MyApplication extends Application implements IAviaryClientCredentials {

    public  static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    private static MyApplication mInstance;
    private PrefManager pref;


    private static final String CREATIVE_SDK_CLIENT_ID = "82894ea0aa73464589a051b547749cb7";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "8d934998-a161-433d-834d-11543d3e9b81";





    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());

        mInstance = this;
        pref = new PrefManager(this);


    }


    public static  MyApplication getInstance(){

        return mInstance;
    }


    public PrefManager getPrefManger() {
        if (pref == null) {
            pref = new PrefManager(this);
        }

        return pref;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){

        getRequestQueue();
        if(mImageLoader==null){

            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue,mLruBitmapCache);
        }

        return this.mImageLoader;

    }


    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }





    @Override
    public String getBillingKey() {
        return "";
    }




}
