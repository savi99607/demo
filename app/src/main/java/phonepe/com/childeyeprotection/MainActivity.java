package phonepe.com.childeyeprotection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import phonepe.com.childeyeprotection.interfaces.CallBack;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean isPermissionGranted = false;
    private TextView volumeTextFiled, brightnesstextView, screentimeouttextview;
    private SeekBar seekBarforvolume, seekBarforbrightness;
    private TextView minbrightness, minvolume, maxbrihtness, maxvolume, setSieranTime;
    private String sieranStr = "Sieran will start at :";
    private SaveImpPrefrences imp = new SaveImpPrefrences();
    private TextView your_key,accesschildcontrol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        volumeTextFiled = findViewById(R.id.volumeTextFiled);
        brightnesstextView = findViewById(R.id.brightnesstextView);
        screentimeouttextview = findViewById(R.id.screentimeouttextview);

        seekBarforvolume = findViewById(R.id.seekBarforvolume);
        seekBarforbrightness = findViewById(R.id.seekBarforbrightness);
        your_key=findViewById(R.id.your_key);


        minbrightness = findViewById(R.id.minbrightness);
        minvolume = findViewById(R.id.minvolume);


        maxbrihtness = findViewById(R.id.maxbrihtness);
        maxvolume = findViewById(R.id.maxvolume);
        setSieranTime = findViewById(R.id.setSieranTime);
        accesschildcontrol=findViewById(R.id.accesschildcontrol);
        setSeekBaraction();
        setTimeOut();
        showAdDispacher();
        generateRandomNumber();


        // addCateItems();

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            } else {
                youDesirePermissionCode(MainActivity.this);
            }
        } else {

            checkBackgroundSettings(1);
            saveParams();

        }


        setSieranTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time time = new Time(selectedHour, selectedMinute, 0);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                        String s = simpleDateFormat.format(time);
                        screentimeouttextview.setText(sieranStr + s);

                        imp.savePrefrencesData(MainActivity.this, selectedHour + ":" + selectedMinute + "", "sierantime");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        accesschildcontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i=new Intent(MainActivity.this,AccessChildControl.class);
               startActivity(i);
            }
        });
    }

    int serverCallTimeCount=0;
    private void checkBackgroundSettings(int settingcall)
    {
        final Handler hnd = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                setAudioVoulme(Integer.parseInt(imp.reterivePrefrence(MainActivity.this, "volume").toString()));
                setBrigtness(Integer.parseInt(imp.reterivePrefrence(MainActivity.this, "brightness").toString()));

                getAudioaudioMangerVolume();
                getBrightness();
                sieranTime();
                if(serverCallTimeCount==4)
                {
                    serverCallTimeCount=0;
                    reteriveServerData();
                }
                serverCallTimeCount++;
                hnd.postDelayed(this, 3000);


            }
        };
        hnd.postDelayed(runnable, 10);
       }

    WindowManager manager;
    boolean isAlphaChange = false;

    private void showSystemAlert() {
        if (manager == null) {
            manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            if (Build.VERSION.SDK_INT >= 23) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.alpha = 1.0f;
            layoutParams.packageName = getPackageName();
//            layoutParams.buttonBrightness = 1f;
            layoutParams.windowAnimations = android.R.style.Animation_Dialog;

            final View view = View.inflate(getApplicationContext(), R.layout.brightnesslayout, null);
            final ImageView sirenimage = view.findViewById(R.id.sirenimage);

            final Handler hndImageAlpha = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (isAlphaChange) {
                        isAlphaChange = false;
                        sirenimage.setAlpha(.3f);
                    } else {
                        sirenimage.setAlpha(1f);
                        isAlphaChange = true;
                    }
                    hndImageAlpha.postDelayed(this, 100);
                }

            };

            hndImageAlpha.postDelayed(runnable, 1000);
            view.findViewById(R.id.LLbrigtness).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    manager.removeView(view);
                    hndImageAlpha.removeCallbacks(runnable);
                    manager = null;
                    if (mPlayer != null) {
                        mPlayer.pause();
                        ;
                        mPlayer.stop();
                        mPlayer = null;
                    }
                }
            });

            playAudio();
            manager.addView(view, layoutParams);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("result ok===" + requestCode + "==" + resultCode);
        if (requestCode == 1234) {
            youDesirePermissionCode(MainActivity.this);
        } else if (requestCode == 111)//setting permission
        {
            youDesirePermissionCode(MainActivity.this);

        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void youDesirePermissionCode(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            permission = Settings.System.canWrite(context);
        } else {

            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            System.out.println("permissioon granted==");
            checkBackgroundSettings(3);
            saveParams();
            //do your code
        } else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, 111);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, 111);
            }

        }
    }


    private void getAudioaudioMangerVolume() {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int music_volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        ArrayList<Integer> indexAr = new ArrayList<>();
        indexAr.add(music_volume_level);
        indexAr.add(max);

        seekBarforvolume.setMax(max);
        seekBarforvolume.setProgress(music_volume_level);
        minvolume.setText("0");
        maxvolume.setText(max + "");

        volumeTextFiled.setText("Current volume Level : " + music_volume_level);
        imp.savePrefrencesData(MainActivity.this,max,"max_audio");

    }


    public void getBrightness() {
        try {
            int curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            seekBarforbrightness.setMax(255);
            seekBarforbrightness.setProgress(curBrightnessValue);
            minbrightness.setText("0");
            maxbrihtness.setText("255");

            brightnesstextView.setText("Current Brightness Level: " + curBrightnessValue);

        } catch (Settings.SettingNotFoundException e) {

            e.printStackTrace();
        }

    }


    private void setTimeOut() {

        String sieranTime=imp.reterivePrefrence(MainActivity.this,"sierantime").toString();
        if(sieranTime.equalsIgnoreCase("0")) {
            screentimeouttextview.setText("Set Sieran");
        }
        else
        {
            screentimeouttextview.setText("Set will play at "+sieranTime);
        }
    }


    private void setAudioVoulme(int setVolume) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int music_volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (setVolume != music_volume_level) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, setVolume, 1);
        }
    }

    private void setBrigtness(int cb) {
        try {
            int curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);

            if (cb != curBrightnessValue) {
                ContentResolver cResolver = this.getApplicationContext().getContentResolver();
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    private void setScreenTimeOut(int screenTimeOut) {
//        System.out.println("screen time out===" + screenTimeOut);
//        android.provider.Settings.System.putInt(getContentResolver(),
//                Settings.System.SCREEN_OFF_TIMEOUT, screenTimeOut);
//    }


    private void setSeekBaraction() {
        seekBarforvolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeTextFiled.setText("Current volume Level : " + progress);
                imp.savePrefrencesData(MainActivity.this, progress + "", "volume");
                setAudioVoulme(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarforbrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnesstextView.setText("Current Brightness Level: " + progress);
                imp.savePrefrencesData(MainActivity.this, progress + "", "brightness");
                setBrigtness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    MediaPlayer mPlayer;

    private void playAudio() {
        if (mPlayer == null) {
            try {
                mPlayer = new MediaPlayer();
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.horror);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(getApplicationContext(), uri);
                mPlayer.prepare();
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (!mPlayer.isPlaying()) {
                            mPlayer.start();
                        }
                    }
                });

            } catch (Exception e) {
                System.out.println("mplayer==err===" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void sieranTime() {
        String time = imp.reterivePrefrence(MainActivity.this, "sierantime").toString();
        screentimeouttextview.setText("Set will play at "+time);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        if (time.equalsIgnoreCase(currentTime)) {
            showSystemAlert();
        }


    }


    WindowManager managerForAds;

    private void displayAdDialog()
        {
        if (managerForAds == null) {
            managerForAds = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            //   WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.BOTTOM;
            // layoutParams.alpha=.0f;


            if (Build.VERSION.SDK_INT >= 23) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            ;
//                layoutParams.alpha = 1.0f;
            layoutParams.packageName = getPackageName();
            layoutParams.windowAnimations = android.R.style.Animation_Dialog;
            final View view = View.inflate(getApplicationContext(), R.layout.displayad_layout, null);
            view.setBackground(null);
            view.findViewById(R.id.RRdisplayads).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    managerForAds.removeView(view);
                    managerForAds = null;
                }
            });



            AdView  mAdView = (AdView)view. findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    //.addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                    .build();

            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                                      @Override
                                      public void onAdLoaded() {
                                      }

                                      @Override
                                      public void onAdClosed() {
                                          Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                                      }
                                  });

            managerForAds.addView(view, layoutParams);
        }
    }


    Handler adsHandler = new Handler();
    Runnable adsRunnable;

    private void showAdDispacher() {

        adsRunnable = new Runnable() {
            @Override
            public void run() {
                displayAdDialog();
                adsHandler.postDelayed(adsRunnable, 60000);
            }
        };
        adsHandler.postDelayed(adsRunnable, 60000);

    }

    private void generateRandomNumber()
    {

      if(imp.reterivePrefrence(MainActivity.this,"your_key").toString().equalsIgnoreCase("0")) {
          Random rnd = new Random();
          imp.savePrefrencesData(MainActivity.this,String.format("%06d", rnd.nextInt(999999)),"your_key");
          your_key.setText("Remote Key : "+imp.reterivePrefrence(MainActivity.this,"your_key"));
      }
      else
      {
          your_key.setText("Remote Key : "+imp.reterivePrefrence(MainActivity.this,"your_key"));
      }
    }


      private  void saveParams()
      {

          String audio=imp.reterivePrefrence(MainActivity.this,"volume").toString();
          String brightness=imp.reterivePrefrence(MainActivity.this,"brightness").toString();
          String sieran=imp.reterivePrefrence(MainActivity.this,"sierantime").toString();
          String child_key=imp.reterivePrefrence(MainActivity.this,"your_key").toString();
          String max_audio=imp.reterivePrefrence(MainActivity.this,"max_audio").toString();

          Map<String,String> m=new HashMap<>();
          m.put("child_key",child_key);
          m.put("audio",audio);
          m.put("brightness",brightness);
          m.put("sieran",sieran);
          m.put("max_audio",max_audio);
          m.put("formSubmit","Submit");
          new ServerHandler().sendToServer(MainActivity.this, "http://voiceofambala.com/fileupload/SaveChildData.php", m, 0, new CallBack() {
              @Override
              public void getRespone(String dta, ArrayList<Object> respons)
              {

                  try {

                      JSONObject obj=new JSONObject(dta);
                  }
                  catch (Exception e)
                  {
                      e.printStackTrace();
                  }
              }
          });

      }



private void reteriveServerData()
{
    Map<String, String> m = new HashMap<>();
    m.put("child_key", imp.reterivePrefrence(MainActivity.this,"your_key").toString());
    new ServerHandler().sendToServer(MainActivity.this, "http://voiceofambala.com/fileupload/ReteriveChildData.php", m, 1, new CallBack() {
        @Override
        public void getRespone(String dta, ArrayList<Object> respons) {

            try {
                JSONObject obj = new JSONObject(dta);
                if (obj.getBoolean("status")) {

                    JSONArray dataAr = obj.getJSONArray("data");
                    imp.savePrefrencesData(MainActivity.this,dataAr.getJSONObject(0).getString("audio"),"volume");
                    imp.savePrefrencesData(MainActivity.this,dataAr.getJSONObject(0).getString("max_audio"),"max_audio");
                    imp.savePrefrencesData(MainActivity.this,dataAr.getJSONObject(0).getString("brightness"),"brightness");
                    imp.savePrefrencesData(MainActivity.this,dataAr.getJSONObject(0).getString("sieran"),"sierantime");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    });


}



}









