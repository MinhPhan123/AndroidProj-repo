package rmit.ad.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends BackgroundActivity implements DatePickerDialog.OnDateSetListener {
    EditText profileName, profileEmail, profilePhone, profileDOB,profileAddress;
    ImageView profileImage;
    ImageView back;
    Button saveUpdate;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseUser user;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Implementation of back method
        back = (ImageView) findViewById(R.id.backEdi);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditProfile.this, Profile.class));
                finish();
            }
        });

        //get the current user
        user = firebaseAuth.getCurrentUser();
        //fetch the data from firebase
        userID = firebaseAuth.getUid();          //get the user id that has been created automatically by firebase


        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profilePhone = findViewById(R.id.profile_phone);
        profileDOB = findViewById(R.id.profile_dob);
        profileAddress = findViewById(R.id.profile_address);
        profileImage = findViewById(R.id.profile_avatar);
        saveUpdate = findViewById(R.id.save_update);

        Intent data = getIntent();
        String full_name = data.getStringExtra("full_name");
        String email = data.getStringExtra("email");
        String address = data.getStringExtra("address");
        String dob = data.getStringExtra("dob");
        String phone_number = data.getStringExtra("phone_number");

        profileName.setText(full_name);
        profileEmail.setText(email);
        profilePhone.setText(phone_number);
        profileDOB.setText(dob);
        profileAddress.setText(address);

        //get the avatar
        StorageReference profileReference = FirebaseStorage.getInstance().getReference().child("user_ava/"+userID+"/profile.jpg");


        //get the photo from user gallery
        try {
            final File localFile = File.createTempFile("avatar","jpg");
            profileReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Utility.showToast(EditProfile.this,"Your avatar has been retrieved");
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Utility.showToast(EditProfile.this,"Error occurred");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileImage.setOnClickListener((v)->{
                //open the gallery to pic the image
                Intent openGalleryIntent  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,100);
        });




        //updating function
        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profileName.getText().toString().isEmpty() ||profileEmail.getText().toString().isEmpty() ||profilePhone.getText().toString().isEmpty()
                        ||profileDOB.getText().toString().isEmpty()||profileAddress.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this,"Can not leave empty flied",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    profileEmail.setError("Email is invalid");
                }
                String email = profileEmail.getText().toString();
                String full_name = profileName.getText().toString();
                String dob = profileDOB.getText().toString();
                String phone_number = profilePhone.getText().toString();
                String address = profileAddress.getText().toString();

                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = firestore.collection("user").document(userID);
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email", email);
                        edited.put("full_name", full_name);
                        edited.put("dob", dob);
                        edited.put("phone_number", phone_number);
                        edited.put("address", address);

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Utility.showToast(EditProfile.this,"Information is updated");
                                startActivity(new Intent(getApplicationContext(),Profile.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utility.showToast(EditProfile.this,e.getLocalizedMessage());
                            }
                        });

                        Toast.makeText(EditProfile.this,"Email is changed",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                //get the Uri of the image
                Uri imageUri = data.getData();
                //avatar.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    //function to upload the photo on Firebase
    private void uploadImageToFirebase(Uri imageUri){
        //upload the avatar to Firebase
        StorageReference fileRef = storageReference.child("user_ava/"+userID+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Utility.showToast(Profile.this,"Image has been uploaded");
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utility.showToast(EditProfile.this,"Image has not been uploaded" + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        profileDOB.setText(dateFormat.format(calendar.getTime()));
    }


}