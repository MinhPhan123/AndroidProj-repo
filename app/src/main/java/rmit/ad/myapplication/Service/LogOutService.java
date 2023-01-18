package rmit.ad.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import rmit.ad.myapplication.LoginActivity;
import rmit.ad.myapplication.Profile;

public class LogOutService extends Service {
    private static final long WAIT_TIME = 10000;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser currUser;
    private Handler handler;
    private Runnable runnable;
    private static LogOutService service;
    private long lastTouchEventTime;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient gsc;

    @Override
    public void onCreate(){
        super.onCreate();
        service = this;
        gsc = GoogleSignIn.getClient(this,gso);
        handler = new Handler();
        currUser = firebaseAuth.getCurrentUser();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastTouchEventTime > WAIT_TIME){
                    if (currUser != null) {
                        Log.d("Sign Out", "Signing out");
                        gsc.signOut();                                          //google sign out
                        FirebaseAuth.getInstance().signOut();//email password sign out
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
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

    public static LogOutService getInstance(){
        return service;
    }

    public void updateTouchEventTime(){
        lastTouchEventTime = System.currentTimeMillis();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}