package com.example.dina.someapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    MyDb dbhelper;
    Intent Service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelper = new MyDb(getApplicationContext());
        dbhelper.open();
        Service=new Intent(this, MediaService.class);
        startService(Service);

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        dbhelper.close();
    }
    private static long back_pressed;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Intent i = new Intent(this, MediaService.class);
            stopService(Service);
        }
        back_pressed = System.currentTimeMillis();
        return super.onTouchEvent(event);
    }


    public void NewKind(View view) {
        Intent intent = new Intent(this, AddKindActivity.class);
        startActivity(intent);
    }
}
