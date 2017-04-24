package bogdan_mihalchenko.words.model.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import bogdan_mihalchenko.words.R;

/**
 * Created by Shipohvost on 28.02.2017.
 */

public class Utils {
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public boolean isShow() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean isShow;
        /* get showingParam
        * 0 - default - show
        * 1 - show later
        * 2 - never show
        * */
        int showParam = sharedPref.getInt("reclamaMode",0);
        int lastTimeShow = sharedPref.getInt("lastTimeShow", 0);

        if(showParam == 1){
            if (System.currentTimeMillis()/1000 > Constants.REFERENCE.OVER_TIME + lastTimeShow){
                isShow = true;
            }else{
                isShow = false;
            }

        }

        else if(showParam == 2){
            isShow = false;
        }else {
            isShow = true;
        }
        return isShow;

    }

    public void setPrefs(int i) {

        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("reclamaMode", i);
        editor.putInt("lastTimeShow", (int) (System.currentTimeMillis()/1000));
        editor.commit();
    }
}
