package pencil.abhishek.io.pencil.utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abhis on 7/17/2016.
 */
public class Fabmanager {

    String pref_name =  "FAVOURITES";
    String KEY_FAB = "FABLIST";
    String KEY_JSON = "FAV_JSON";
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    Context context;


    public Fabmanager(Context context){

        pref = context.getSharedPreferences(pref_name,Context.MODE_PRIVATE);
        this.context = context ;

    }


    public  void storeFavJSONurl(List<String> fablist){

        editor = pref.edit();
        Gson gson = new Gson();

        editor.putString(KEY_JSON, gson.toJson(fablist));

        editor.commit();
    }

    public List<String> getFavJson(){

        List<String> fablist = new ArrayList<String>();
        if(pref.contains(KEY_JSON)){
            String json = pref.getString(KEY_JSON,null);
            Gson gson = new Gson();
            String[] albumArry = gson.fromJson(json,String[].class);
            fablist = Arrays.asList(albumArry);
            fablist = new ArrayList<String>(fablist);
            return fablist;


        }else
            return null;

    }


    public void storeFabList(List<String> fablist){

        editor = pref.edit();
        Gson gson = new Gson();

        editor.putString(KEY_FAB, gson.toJson(fablist));

        editor.commit();



    }


    public List<String> getFabList(){

        List<String> fablist = new ArrayList<String>();
        if(pref.contains(KEY_FAB)){
            String json = pref.getString(KEY_FAB,null);
            Gson gson = new Gson();
            String[] albumArry = gson.fromJson(json,String[].class);
            fablist = Arrays.asList(albumArry);
            fablist = new ArrayList<String>(fablist);
            return fablist;


        }else
            return null;

    }


}
