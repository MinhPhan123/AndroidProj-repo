package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.myapplication.ModelClass.Item;

public class PaymentActivity extends BackgroundActivity {

    String paymentMethod;

    FirebaseFirestore db;
    ArrayList<Item> cartItems;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cartItems = new ArrayList<Item>();
        db = FirebaseFirestore.getInstance();

        RadioGroup paymentMethodRadioGroup = findViewById(R.id.payment_radio_group);

        paymentMethodRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.cash_option:
                    paymentMethod = "Cash";
                    break;
                case R.id.google_pay_option:
                    paymentMethod = "Google Pay";
                    break;
            }
        });


        Button btnPay = findViewById(R.id.button_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Shopping Cart").document(currUser.getUid()).collection("Items").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                if (!list.isEmpty()) {
                                    double price = 0;
                                    for (DocumentSnapshot d : list) {
                                        Item i = d.toObject(Item.class);
                                        DocumentReference df = firestore.collection("Ongoing Orders").document(currUser.getUid()).collection("Items").document(i.getID());
                                        df.set(i);
                                        DocumentReference documentReference = firestore.collection("Shopping Cart").document(currUser.getUid()).collection("Items").document(i.getID());
                                        documentReference.delete();
                                    }
                                }
                            }
                        });
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        ImageView btnBack = findViewById(R.id.back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this, ShoppingCartActivity.class));
                finish();
            }
        });
    }
}