package com.example.exportsensordata_acc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPresenter;

import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements SensorEventListener, OnClickListener {

    //declare and define the variables.
    private Button mWriteButton, mStopButton;
    private boolean doWrite = false;
    private SensorManager sm;
    private TextView AT;
    private String file = "AccData.csv";// or.txt format.
    private String fileContents;
    private boolean ifStart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AT = (TextView)findViewById(R.id.textView);
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                1000000);
        mWriteButton = (Button)findViewById(R.id.Button_Start);
        mWriteButton.setOnClickListener(this);
        mStopButton = (Button)findViewById(R.id.Button_Stop);
        mStopButton.setOnClickListener(this);
    }

    public void onPause(){
        super.onPause();
    }

    public void writeFile(String fileName,String message){
        try{
            FileOutputStream fOut = openFileOutput(fileName, Context.MODE_APPEND);
            byte [] bytes = message.getBytes();
            fOut.write(bytes);
            fOut.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }






    @Override
    public void onSensorChanged(SensorEvent event) {
        String Sensor_message = null;
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float X = event.values[0];
            float Y = event.values[1];
            float Z = event.values[2];
            DecimalFormat df = new DecimalFormat("#,##0.000");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = sdf.format(new Date());
            Sensor_message = str + "\n";
            Sensor_message += df.format(X) + " ";
            Sensor_message += df.format(Y) + " ";
            Sensor_message += df.format(Z) + "\n";
            AT.setText(Sensor_message + "\n");

            if(doWrite){
                writeFile(file,Sensor_message);
                ifStart = true;
            }
            else if(doWrite == false && ifStart == true){
                File filedir = new File(getFilesDir(),file);
                Toast.makeText(getBaseContext(),
                        "Stop record / File saved at" + filedir,
                        Toast.LENGTH_LONG).show();
                ifStart = false;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.Button_Start){
            doWrite = true;
            Toast.makeText(getBaseContext(),"Start record...", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.Button_Stop){
            doWrite = false;
        }
    }
}
