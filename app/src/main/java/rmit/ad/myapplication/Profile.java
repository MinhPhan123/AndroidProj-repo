package rmit.ad.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;

//profile activity
public class Profile extends AppCompatActivity {
    ImageView avatar;
    ImageView back;
    TextView full_name,email,dob,address,phone_number;
    Button resetPassword, updateProfile;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseUser user;

    String userID;

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        gsc = GoogleSignIn.getClient(this,gso);

        //get the current user
        user = firebaseAuth.getCurrentUser();

        //fetch the data from firebase
        userID = firebaseAuth.getUid();          //get the user id that has been created automatically by firebase
        DocumentReference documentReference = firestore.collection("user").document(userID);
        //get the avatar of a specific user
        StorageReference profileReference = FirebaseStorage.getInstance().getReference().child("user_ava/"+userID+"/profile.jpg");


        // to fetch data image data in Storage (FIREBASE)
        try {
            final File localFile = File.createTempFile("avatar","jpg");
            profileReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Utility.showToast(Profile.this,"Your avatar has been retrieved");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        avatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Utility.showToast(Profile.this,"Error occurred");
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

        avatar = findViewById(R.id.avatar);
        resetPassword = findViewById(R.id.resetPassword);
        full_name = findViewById(R.id.User_name);
        email = findViewById(R.id.User_email);
        dob = findViewById(R.id.User_dob);
        address = findViewById(R.id.User_address);
        phone_number = findViewById(R.id.User_phone);
        updateProfile = findViewById(R.id.update);

        //Implementation of back method
        back = (ImageView) findViewById(R.id.backPro);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });

        //fetch and display the data
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                full_name.setText(documentSnapshot != null ? documentSnapshot.getString("full_name") : null);
                email.setText(documentSnapshot != null ? documentSnapshot.getString("email") : null);
                dob.setText(documentSnapshot != null ? documentSnapshot.getString("dob") : null);
                address.setText(documentSnapshot != null ? documentSnapshot.getString("address") : null);
                phone_number.setText(documentSnapshot != null ? documentSnapshot.getString("phone_number") : null);
            }
        });

        //to update the profile
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(),EditProfile.class);
                //pass the data
                i.putExtra("full_name",full_name.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("dob",dob.getText().toString());
                i.putExtra("address",address.getText().toString());
                i.putExtra("phone_number",phone_number.getText().toString());
                startActivity(i);
                finish();
            }
        });

        //to change the password
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetPassword = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your new password");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get the new password
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Utility.showToast(Profile.this,"Your password has been changed");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utility.showToast(Profile.this,"Your new password is not saved");
                            }
                        });


                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }


    //function for logout
    public void logout(View view){
        gsc.signOut();                                          //google sign out
        FirebaseAuth.getInstance().signOut();                   //email password sign out
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}
