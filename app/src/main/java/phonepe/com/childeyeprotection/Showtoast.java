package phonepe.com.childeyeprotection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



public class Showtoast {
    private Dialog toastDialog;

    public Showtoast() {

    }

    public void showToast(final Context act, final String title, String msg, View view) {

       System.out.println("invalid==="+title+"==="+msg+"==="+view);

        try
        {
         if (toastDialog != null && toastDialog.isShowing()) {
            toastDialog.dismiss();
        }
        toastDialog = new Dialog(act);
        toastDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(title.equalsIgnoreCase("Internet"))
        {
            Toast.makeText(act,title,Toast.LENGTH_LONG).show();
        }


        else if(title.equalsIgnoreCase("Communication Error!"))
        {

            Toast.makeText(act,title,Toast.LENGTH_LONG).show();
        }
        else if(title.equalsIgnoreCase("400"))
        {
            Toast.makeText(act,title,Toast.LENGTH_LONG).show();
        }
        else if(title.equalsIgnoreCase("410")) {
            Toast.makeText(act,title,Toast.LENGTH_LONG).show();
        }
        } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
