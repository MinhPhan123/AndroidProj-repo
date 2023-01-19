package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChatBox extends BackgroundActivity {
    EditText message = (EditText) findViewById(R.id.messageEditText);
    ImageButton sendBtn = (ImageButton) findViewById(R.id.sendBtn);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
    }

    private void sendMessage(String sender,String receiver, String message){

    }

}