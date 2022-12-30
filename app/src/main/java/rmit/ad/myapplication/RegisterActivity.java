package rmit.ad.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity<email> extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText emailEditText, passwordEditText, confirmPasswordEditText, full_nameEditText, dobEditText, phone_numberEditText, addressEditText;
    Button create_accountBtn;
    ProgressBar processBar;
    TextView loginTextView;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        confirmPasswordEditText = (EditText) findViewById(R.id.password_confirm);
        full_nameEditText = (EditText) findViewById(R.id.full_name);
        dobEditText = (EditText) findViewById(R.id.dob);
        phone_numberEditText = (EditText) findViewById(R.id.phone_number);
        addressEditText = (EditText) findViewById(R.id.address);
        create_accountBtn = (Button) findViewById(R.id.create_accountBtn);
        loginTextView = (TextView) findViewById(R.id.login);
        processBar = (ProgressBar) findViewById(R.id.progress_bar);


        create_accountBtn.setOnClickListener(view -> Create_account());
        loginTextView.setOnClickListener((v) -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }


    void Create_account() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String full_name = full_nameEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String phone_number = phone_numberEditText.getText().toString();
        String address = addressEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword, phone_number);
        if (!isValidated) {
            return;
        }
        createAccountInFireBase(email, password, full_name, dob, phone_number, address);
    }

    boolean validateData(String email, String password, String confirmPassword, String phone_number) {
        //validate the data that were input by the user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password should has more than 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Confirmed password is not matched");
            return false;
        }
        if (!Patterns.PHONE.matcher(phone_number).matches()) {
            phone_numberEditText.setError("Phone is invalid");
            return false;
        }
        return true;
    }

    void createAccountInFireBase(String email, String password, String full_name, String dob, String phone_number, String address) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            //successfully create an account
                            Utility.showToast(RegisterActivity.this, "Your account has been successfully created. Please check your email for validation");
                            userID = firebaseAuth.getUid();
                            DocumentReference documentReference = firestore.collection("user").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("full_name", full_name);
                            user.put("dob", dob);
                            user.put("phone_number", phone_number);
                            user.put("address", address);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "onSuccess: user profile is created for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Utility.showToast(RegisterActivity.this, task.getException().getLocalizedMessage() + "Please try again");
                        }
                    }
                }
        );
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            processBar.setVisibility(View.VISIBLE);
            create_accountBtn.setVisibility(View.GONE);
        } else {
            processBar.setVisibility(View.GONE);
            create_accountBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);

    }


    public void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dobEditText.setText(dateFormat.format(calendar.getTime()));
    }
}
