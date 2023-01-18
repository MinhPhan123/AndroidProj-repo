package rmit.ad.myapplication;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import rmit.ad.myapplication.BroadcastReceiver.NetworkReceiver;
import rmit.ad.myapplication.Service.LogOutService;

public class BackgroundActivity extends AppCompatActivity {
    private BroadcastReceiver mNetworkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetworkReceiver = new NetworkReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        startService(new Intent(getApplicationContext(), LogOutService.class));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogOutService service = LogOutService.getInstance();
        if (service != null){
            service.updateTouchEventTime();
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, LogOutService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
    }
}

