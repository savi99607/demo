package phonepe.com.childeyeprotection;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SaveImpPrefrences
{
    private String prefName = "childprotection";
    public void savePrefrencesData(Context ct, Object data, String sharekey)
    {
        SharedPreferences sharedpreferences = ct.getSharedPreferences(prefName, ct.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (sharekey.equals("volume"))
        {
            try {
                editor.putString("volume", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }

       else if (sharekey.equals("brightness"))
        {
            try {
                editor.putString("brightness", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }
        }
        else if (sharekey.equals("sierantime"))
        {
            try {
                editor.putString("sierantime", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }
        }

        else if (sharekey.equals("your_key"))
        {
            try {
                editor.putString("your_key", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }
        }

        else if (sharekey.equals("max_audio"))
        {
            try {
                editor.putString("max_audio", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }
        }




        editor.commit();
    }

    public Object reterivePrefrence(Context ct, String shareKey) {
        if (ct != null)
        {
            SharedPreferences prefs = ct.getSharedPreferences(prefName, ct.MODE_PRIVATE);
            if (shareKey.equals("volume")) {
                if (prefs.contains("volume"))//sarpanch pass
                {
                    return prefs.getString("volume", "");
                }
            }

           else if (shareKey.equals("brightness")) {
                if (prefs.contains("brightness"))//sarpanch pass
                {
                    return prefs.getString("brightness", "");
                }
            }
            else if (shareKey.equals("sierantime")) {
                if (prefs.contains("sierantime"))//sarpanch pass
                {
                    return prefs.getString("sierantime", "");
                }
            }
            else if (shareKey.equals("your_key")) {
                if (prefs.contains("your_key"))//sarpanch pass
                {
                    return prefs.getString("your_key", "");
                }
            }

            else if (shareKey.equals("max_audio")) {
                if (prefs.contains("max_audio"))//sarpanch pass
                {
                    return prefs.getString("max_audio", "");
                }
            }




        }

        return "0";
    }


}
