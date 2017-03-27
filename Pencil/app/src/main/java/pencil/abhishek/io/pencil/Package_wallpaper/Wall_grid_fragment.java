package pencil.abhishek.io.pencil.Package_wallpaper;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pencil.abhishek.io.pencil.Adapter.WallPaper_adapter;
import pencil.abhishek.io.pencil.MyApplication;
import pencil.abhishek.io.pencil.Package_wallpaper.App.AppConstant;
import pencil.abhishek.io.pencil.Package_wallpaper.Picasa.Wallpaper;
import pencil.abhishek.io.pencil.R;
import pencil.abhishek.io.pencil.utills.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wall_grid_fragment extends Fragment {

    private static final String TAG = Wall_grid_fragment.class.getSimpleName();
    private static final String bundleAlbumId = "albumId";



    ProgressWheel pWheel;


    WallPaper_adapter wallPaperAdapter ;
    private List<Wallpaper> photosList;
    private PrefManager pref;
    ArrayList<String> idList;
    private String selectedAlbumId;
    private RecyclerView recyclerView;
    float padding;
    private int  imageWidth , columnWidth  ;
    public static final int GRID_PADDING = 2;
    String url = null;


    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content",TAG_CONTENT = "content", TAG_IMG_URL = "url",TAG_SRC = "src",
            TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height", TAG_ID = "id",
            TAG_T = "$t", TAG_TITLE = "title",TAG_THUMBNAIL = "media$thumbnail";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_wall_grid_fragment, container, false);






        idList = new ArrayList<String>();
        photosList = new ArrayList<Wallpaper>();

        pref = new PrefManager(getActivity());

        selectedAlbumId = getArguments().getString(bundleAlbumId);

        Log.d(TAG,
                "Selected album id: "
                        + getArguments().getString(bundleAlbumId));



        url = AppConstant.URL_ALBUM_PHOTOS.replace("_PICASA_USER_",
                pref.getGoogleUserName())
                .replace("_ALBUM_ID_", selectedAlbumId);


        Log.d(TAG, "Final request url: " + url);



        pWheel = (ProgressWheel) view.findViewById(R.id.pWheel);
        pWheel.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.grid_recycle_view);

        recyclerView.setVisibility(View.GONE);

        InitilizeGridLayout();




        wallPaperAdapter = new WallPaper_adapter(getActivity().getBaseContext(), photosList, imageWidth);
        recyclerView.setEnabled(true);
        recyclerView.setAdapter(wallPaperAdapter);



        recyclerView.setLayoutManager(new GridLayoutManager( getActivity(),2));
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(2,dpToPx(2),false));
{

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                         @Override
                         public void onResponse(JSONObject response) {


                           try{
                               Log.d(TAG, "List of photos json reponse: " + response.toString());
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                             if (response != null) {
                                 parseJsonFeed(response);
                             }




                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {

                             Log.e(TAG, "Error: " + error.getMessage());


                         }
                     });


            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);





            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getBaseContext(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {


                       Intent intent = new Intent(getActivity(),Full_Resolution_Image.class);

                    Gson gson = new Gson();
                    String gsonString = gson.toJson(photosList);


                       intent.putExtra("postion",position);
                       intent.putExtra(Full_Resolution_Image.TAG_SEL_IMAGE,gsonString);


                       startActivity(intent);
                       getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_scale);

                }

                @Override
                public void onLongClick(View view, int position) {



                }
            }));


        }


        return view;
    }



    public void parseJsonFeed(JSONObject response) {

        try {
            JSONArray entry = response.getJSONObject(TAG_FEED).getJSONArray(TAG_ENTRY);
            for (int i = 0; i < entry.length(); i++) {

                JSONObject photoObj = (JSONObject) entry.get(i);
                JSONArray mediacontentArry = photoObj.getJSONObject(TAG_MEDIA_GROUP).getJSONArray(TAG_MEDIA_CONTENT);



                if (mediacontentArry.length() > 0) {

                    JSONObject mediaObj = (JSONObject) mediacontentArry.get(0);


                    String url = mediaObj.getString(TAG_IMG_URL);

                    String photoJson = photoObj.getJSONObject(TAG_ID).getString(TAG_T) + "&imgmax=d";



                    String title = photoObj.getJSONObject(TAG_TITLE).getString(TAG_T);
                    Log.d("title:" , title);



                    int width = mediaObj.getInt(TAG_IMG_WIDTH);
                    int height = mediaObj.getInt(TAG_IMG_HEIGHT);

                    Wallpaper p = new Wallpaper(photoJson, url,title,width, height);

                    photosList.add(p);
                    Log.d(TAG, "Photo: " + url + ", w: " + width + ", h: " + height);

                }
            }



            recyclerView.setVisibility(View.VISIBLE);
            pWheel.setVisibility(View.GONE);

        } catch (Exception e) {

            e.printStackTrace();


        }

    }




    public static Wall_grid_fragment newInstance(String albumId) {
        Wall_grid_fragment f = new Wall_grid_fragment();
        Bundle args = new Bundle();
        args.putString(bundleAlbumId, albumId);
        f.setArguments(args);
        return f;
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
        WindowManager wm = (WindowManager)getActivity().getBaseContext().getSystemService(Context.WINDOW_SERVICE);
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
