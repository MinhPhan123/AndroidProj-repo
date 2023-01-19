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

public class PaymentActivity extends AppCompatActivity {

    String paymentMethod;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        RadioGroup paymentMethodRadioGroup = findViewById(R.id.payment_radio_group);

        paymentMethodRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.cash_option:
                    paymentMethod = "Cash";
                    break;
                case R.id.card_option:
                    paymentMethod = "Credit/debit card";
                    Intent intent = new Intent(PaymentActivity.this, CreditCardFormActivity.class);
                    startActivity(intent);
                    break;
                case R.id.google_pay_option:
                    paymentMethod = "Google Pay";
                    break;
            }
        });

        Button btnConfirm = findViewById(R.id.button_confirm_selection);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentActivity.this, "You select " + paymentMethod + " payment method", Toast.LENGTH_SHORT).show();

            }
        });

        Button btnPay = findViewById(R.id.button_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentActivity.this, "Pay successfully, thank you for your order !", Toast.LENGTH_LONG).show();
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