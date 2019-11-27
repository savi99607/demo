package phonepe.com.childeyeprotection;

import androidx.appcompat.app.AppCompatActivity;
import phonepe.com.childeyeprotection.interfaces.CallBack;

import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AccessChildControl extends AppCompatActivity {

    SaveImpPrefrences imp=new SaveImpPrefrences();
    private LinearLayout LlchildData;
    private TextView volumeTextFiled, brightnesstextView, screentimeouttextview;
    private SeekBar seekBarforvolume, seekBarforbrightness;
    private TextView minbrightness, minvolume, maxbrihtness, maxvolume, setSieranTime;
    private String childKey="",max_audio="0";
    private String sieranStr = "Sieran will start at :";


    @Override
    protected void onCreate(Bundle savedInstanceState)
       {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_child_control);
        getSupportActionBar().hide();

        LlchildData=findViewById(R.id.LlchildData);
           volumeTextFiled = findViewById(R.id.volumeTextFiled);
           brightnesstextView = findViewById(R.id.brightnesstextView);
           screentimeouttextview = findViewById(R.id.screentimeouttextview);

           seekBarforvolume = findViewById(R.id.seekBarforvolume);
           seekBarforbrightness = findViewById(R.id.seekBarforbrightness);

           minbrightness = findViewById(R.id.minbrightness);
           minvolume = findViewById(R.id.minvolume);


           maxbrihtness = findViewById(R.id.maxbrihtness);
           maxvolume = findViewById(R.id.maxvolume);
           setSieranTime = findViewById(R.id.setSieranTime);
           setSeekBaraction();
           saveChildSettings();


        findViewById(R.id.getChildData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reteriveChildData();
            }
        });




       }




    private  void reteriveChildData()
    {

        EditText your_key =findViewById(R.id.your_key);
       if(your_key.getText().toString().length()<=5)
       {
           Toast.makeText(AccessChildControl.this, "Please enter child key to retrieve control keys", Toast.LENGTH_SHORT).show();
       }
        else {

           Map<String, String> m = new HashMap<>();
           m.put("child_key", your_key.getText().toString());
           new ServerHandler().sendToServer(AccessChildControl.this, "http://voiceofambala.com/fileupload/ReteriveChildData.php", m, 0, new CallBack() {
               @Override
               public void getRespone(String dta, ArrayList<Object> respons) {

                   try {

                       JSONObject obj = new JSONObject(dta);
                       if (obj.getBoolean("status")) {
                           LlchildData.setVisibility(View.VISIBLE);
                           JSONArray dataAr = obj.getJSONArray("data");
                           childKey = dataAr.getJSONObject(0).getString("child_key");
                           max_audio = dataAr.getJSONObject(0).getString("max_audio");
                           setAudioaudioMangerVolume(dataAr.getJSONObject(0).getString("audio"), max_audio);
                           setBrightness(dataAr.getJSONObject(0).getString("brightness"));
                           setTimeOut(dataAr.getJSONObject(0).getString("sieran"));

                         }
                       else {
                           Toast.makeText(AccessChildControl.this, "Sorry no data found", Toast.LENGTH_SHORT).show();
                       }

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

           });

       }
    }


    private void setAudioaudioMangerVolume(String volumeLevel,String max)
    {
        seekBarforvolume.setMax(Integer.parseInt(max));
        seekBarforvolume.setProgress(Integer.parseInt(volumeLevel));
        minvolume.setText("0");
        maxvolume.setText(max + "");
        volumeTextFiled.setText("Current volume Level : " + Integer.parseInt(volumeLevel));

    }


    public void setBrightness(String curBrightnessValue)
    {
            seekBarforbrightness.setMax(255);
            seekBarforbrightness.setProgress(Integer.parseInt(curBrightnessValue));
            minbrightness.setText("0");
            maxbrihtness.setText("255");
            brightnesstextView.setText("Current Brightness Level: " + curBrightnessValue);


    }


    private void setTimeOut(String currentSieranTime)
      {
          screentimeouttextview.setText("Set will play at "+currentSieranTime);
      }


    private void setSeekBaraction()
    {
        seekBarforvolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeTextFiled.setText("Current volume Level : " + progress);

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

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    String selectedTime="";
    private void saveChildSettings()
    {

        setSieranTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AccessChildControl.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time time = new Time(selectedHour, selectedMinute, 0);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                        screentimeouttextview.setText(sieranStr + simpleDateFormat.format(time));
                        selectedTime=selectedHour + ":" + selectedMinute;
                        if(selectedMinute<=9)
                        {
                            selectedTime=selectedHour + ":0"+selectedMinute;
                        }



                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });




        findViewById(R.id.accesschildcontrol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,String> m=new HashMap<>();
                m.put("child_key",childKey);
                m.put("audio",seekBarforvolume.getProgress()+"");
                m.put("brightness",seekBarforbrightness.getProgress()+"");
                m.put("sieran",selectedTime);
                m.put("max_audio",max_audio);
                m.put("formSubmit","Submit");

                System.out.println("before to save customization==="+m);

                new ServerHandler().sendToServer(AccessChildControl.this, "http://voiceofambala.com/fileupload/SaveChildData.php", m, 0, new CallBack() {
                    @Override
                    public void getRespone(String dta, ArrayList<Object> respons)
                    {

                        try {

                            JSONObject obj=new JSONObject(dta);
                            if(obj.getBoolean("status"))
                            {
                                Toast.makeText(AccessChildControl.this,"Child Control keys saved sucessfully",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(AccessChildControl.this,"Child Control keys are not saved",Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                    }
                });





            }
        });

    }

}
