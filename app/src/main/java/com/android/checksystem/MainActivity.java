package com.android.checksystem;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Button mStart;
    private Button mend;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStart = (Button)findViewById(R.id.start);
        mend = (Button)findViewById(R.id.end);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                String cmd ="echo [$(date +%H:%M:%S)"
                        +"], CPU: $(cat /sys/class/thermal/thermal_zone7/temp)"
                        + ", PA: $(cat /sys/class/thermal/thermal_zone0/temp)"
                        + ", BATT: $(cat /sys/class/power_supply/battery/temp) >> /sdcard/temp.log";
                execShell(cmd);
                Toast.makeText(MainActivity.this, "start", Toast.LENGTH_LONG).show();
                handler.postDelayed(this, 30000);
            }
        };

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handler.postDelayed(runnable,0);
            }
        });
        mend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handler.removeCallbacks(runnable);
                Toast.makeText(MainActivity.this, "end", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void execShell(String cmd){
        try{
            Process p = Runtime.getRuntime().exec("sh");
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }


}
