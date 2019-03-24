package map.android.mr_auspicious.bukarteadmin.Sessions;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Seller_Session {

    private static final String KEY_IS_SELLERID = "seller_id";

    // LogCat tag
    private static String TAG = Seller_Session.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BukarteSeller";



    public Seller_Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setSellerID(String id) {

        editor.putString(KEY_IS_SELLERID, id);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }



    public String getSellerID(){
        return pref.getString(KEY_IS_SELLERID, "0");
    }


}
