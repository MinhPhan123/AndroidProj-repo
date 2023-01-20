package rmit.ad.myapplication.BroadcastReceiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import org.checkerframework.checker.units.qual.A;

import rmit.ad.myapplication.R;


public class NetworkReceiver extends BroadcastReceiver {

    AlertDialog.Builder builder1;
    AlertDialog alertDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        builder1 = new AlertDialog.Builder(context);
        try{
            if (isConnected(context)){
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }else{
                View customLayout = LayoutInflater.from(context).inflate(R.layout.alert_dialog,null);
                builder1.setView(customLayout);
                AppCompatButton retryBtn = customLayout.findViewById(R.id.btn_try_again);
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                alertDialog = builder1.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                Toast.makeText(context,"Network not connected", Toast.LENGTH_SHORT).show();

                retryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        onReceive(context,intent);
                    }
                });
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
