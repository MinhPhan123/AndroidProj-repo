package rmit.ad.myapplication.BroadcastReceiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    AlertDialog.Builder builder1;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if (isConnected(context)){

            }else{
                Toast.makeText(context,"Network not connected", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private boolean isConnected(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnectedOrConnecting());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
