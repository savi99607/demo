package phonepe.com.childeyeprotection;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import phonepe.com.childeyeprotection.interfaces.CallBack;
public class ServerHandler {
    private Dialog progressdlg;
    private Showtoast showtoast;
    private Context ct1;

    private SaveImpPrefrences imp;
    public ServerHandler() {
        imp=new SaveImpPrefrences();
    }
    public boolean CheckInternetState(Context ct, int sholoader) {
        try {
            showtoast = new Showtoast();
            ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null)
            {
                return true;
            }
            else
                {
                  if(sholoader <= 0) {
                    Toast.makeText(ct1,"Communication error !",Toast.LENGTH_LONG).show();

                }
                return false;
            }
        } catch (Exception e) {
            //Toast.makeText(ct, "Exception CheckNetState", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void sendToServer(final Context ct, final String url, final Map<String, String> data, final int showloader, final CallBack cb) {
        ct1 = ct;
        if (progressdlg != null && progressdlg.isShowing()) {
            dissmissLoader();
        }
        if(CheckInternetState(ct, showloader))
           {
               try {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response)
                                {

                                    try
                                    {
                                        System.out.println("response===="+response);
                                        cb.getRespone(response, null);
                                        dissmissLoader();

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                        cb.getRespone(response, null);

                                        //if (showloader <= 0)
                                           // showtoast.showToast(ct, "Communication", "Communication error.", null);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {

                                        cb.getRespone("error", null);
                                        NetworkResponse networkResponse = error.networkResponse;
                                        if (networkResponse != null && networkResponse.statusCode == 401)
                                        {
                                            showtoast.showToast(ct1, "Unauthorized Access!", "Unauthorized Access!", null);
                                        } else {
                                           // showtoast.showToast(ct1, "Communication Error!", "Communication error!", null);
                                        }
                                        if (progressdlg != null && progressdlg.isShowing()) {
                                            dissmissLoader();
                                        }
                                    } catch (Exception e)
                                    {

                                        e.printStackTrace();
                                    }
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return data;
                        }


                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            try {
                                params.put("X-API-KEY", "Q4GxNgqgKV9XJyoKHNgs");
                                if (imp.reterivePrefrence(ct, "r_token").toString().length() > 0) {
                                    if (data.containsKey("isherder")) {

                                       // params.put("Rtoken", getNewRToken() + "");

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return params;
                        }

                        ;
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            2000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    RequestQueue requestQueue = Volley.newRequestQueue(ct);
                    requestQueue.add(stringRequest);
                    stringRequest.setShouldCache(true);
                    showProgressDialog();
                    progressdlg.show();

                    if (showloader >= 1) {
                        dissmissLoader();
                    }
                } catch (Exception e)
                {

                    if (progressdlg != null)
                        dissmissLoader();

                }

        }
    }






    Handler hnd;
    Runnable runnable;
    int fadeEffect=0;
    private void showProgressDialog() {
        progressdlg = new Dialog(ct1);
        progressdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdlg.setContentView(R.layout.showprogressdialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = progressdlg.getWindow();
        lp.copyFrom(window.getAttributes());
        progressdlg.setCancelable(false);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
//        progressdlg.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        progressdlg.getWindow().setBackgroundDrawable(null);
        progressdlg.getWindow().setDimAmount(0);
        progressdlg.show();
      }

    private void dissmissLoader()
    {
        if(hnd!=null)
        {
            hnd.removeCallbacks(runnable);
        }
       if(progressdlg!=null&&progressdlg.isShowing())
       {
           progressdlg.dismiss();
       }

        }
     }





