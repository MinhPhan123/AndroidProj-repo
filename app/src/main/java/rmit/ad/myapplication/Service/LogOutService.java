package rmit.ad.myapplication.Service;

import static java.lang.Math.abs;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rmit.ad.myapplication.LoginActivity;
import rmit.ad.myapplication.SplashActivity;

public class LogOutService extends Service {
    private static final long WAIT_TIME =10000;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private Handler handler;
    private Runnable runnable;
    private static LogOutService service;
    private long lastTouchEventTime;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient gsc;
    public static CountDownTimer timer;

    @Override
    public void onCreate(){
        super.onCreate();
        service = this;
        handler = new Handler();
        lastTouchEventTime = System.currentTimeMillis();
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        runnable = new Runnable() {
            @Override
            public void run() {
                if ((System.currentTimeMillis() -  lastTouchEventTime) > WAIT_TIME){
                    if (currUser != null) {
                        LogOutServiceFunc();
                        stopSelf();
                    }
                }
                else{
                    handler.postDelayed(runnable, WAIT_TIME);
                }
            }
        };
        handler.post(runnable);
    }


    public void LogOutServiceFunc(){
        FirebaseAuth.getInstance().signOut();//email password sign out
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        Toast.makeText(getApplicationContext(), "Signed out due to inactivity", Toast.LENGTH_SHORT).show();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    public static LogOutService getInstance(){
        return service;
    }

    public void updateTouchEventTime(){
        lastTouchEventTime = System.currentTimeMillis();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}