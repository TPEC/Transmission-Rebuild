package com.dc.transimissionr;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.dc.transimissionr.gameData.Constant;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static MainActivity ma;
    int id;
    private MySurfaceView mySurfaceView;
    private TSurfaceView tSurfaceView;

    public MediaPlayer mp;
    public AudioManager am;
    public int[] vol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.ma=this;
//        mp.reset();
        mp=MediaPlayer.create(this,R.raw.bgm);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        vol=new int[4];
        vol[3]=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        vol[2]=vol[3]/2;
        vol[1]=vol[2]/2;
        vol[0]=0;
        am.setStreamVolume(AudioManager.STREAM_MUSIC,vol[1],AudioManager.FLAG_PLAY_SOUND);
        mp.start();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        mySurfaceView=new MySurfaceView(this);
        tSurfaceView=new TSurfaceView(this);
        changeSV(1);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ratio_width=(float) 1280/dm.widthPixels;
        Constant.ratio_height=(float) 720/dm.heightPixels;
    }

    public void changeSV(int id){
        this.id=id;
        if(id==0){
            mySurfaceView.onResume();
            setContentView(mySurfaceView);
            mySurfaceView.requestFocus();
            mySurfaceView.setFocusableInTouchMode(true);
            tSurfaceView.pause=true;
            mySurfaceView.pause=false;
        }else if(id==1){
            mySurfaceView.onPause();
            setContentView(tSurfaceView);
            tSurfaceView.requestFocus();
            tSurfaceView.setFocusableInTouchMode(true);
            tSurfaceView.pause=false;
            mySurfaceView.pause=true;
        }
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        super.onResume();
        if(id==0)
            mySurfaceView.onResume();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(id==0)
            mySurfaceView.onPause();
        mp.pause();
    }
}
