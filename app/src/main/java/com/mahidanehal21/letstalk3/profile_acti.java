package com.mahidanehal21.letstalk3;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.*;


import de.hdodenhof.circleimageview.CircleImageView;

public class profile_acti extends AppCompatActivity {


private Button change_userphoto;
    private Button change_status;
    private TextView username;
    private TextView status;
    //private int image_stability=0; // for using default image in first time when user log in.
    private CircleImageView my_profileImage;
    private DatabaseReference getuserdatareference;
    private StorageReference profileImageStoreRef;
    private TextView status_text;
    private FirebaseAuth mAuth;
    private final int gallery_pick=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acti);
        my_profileImage = (CircleImageView)findViewById(R.id.profile_image_circleview);
        username = (TextView)findViewById(R.id.username_textview);
        status = (TextView)findViewById(R.id.status_textview);
        change_status = (Button)findViewById(R.id.change_status_button);
        change_userphoto = (Button) findViewById(R.id.change_photo_button);
        status_text = (TextView)findViewById(R.id.status_textview);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        getuserdatareference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        profileImageStoreRef = FirebaseStorage.getInstance().getReference().child("profile_images");

        getuserdatareference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status_string = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                username.setText(name);
                status.setText(status_string);
                // if(image_stability==1) {
//                Picasso.get().load(image).into(my_profileImage);
                Picasso.get().load(image).into(my_profileImage);


                // }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void ChangePhoto(View view) {
        Intent i1 = new Intent();
        i1.setAction(Intent.ACTION_GET_CONTENT);
        i1.setType("image/*");
        startActivityForResult(i1,gallery_pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallery_pick&&resultCode==RESULT_OK&&data!=null)
        {
            Uri imageuri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                String user_id = mAuth.getCurrentUser().getUid();
                final StorageReference filepath = profileImageStoreRef.child(user_id+".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String path_image=uri.toString();
                                getuserdatareference.child("image").setValue(path_image).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(profile_acti.this,"Profile image is changed",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        });
                    }
                });

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void Ocnext(View view) {
        Intent i1 = new  Intent(profile_acti.this,MainActivity.class);
        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i1);
        finish();
    }

    public void mystatus_change(View view) {
        final String status_s = status_text.getText().toString();
        getuserdatareference.child("status").setValue(status_s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(profile_acti.this,"Status has been is changed",Toast.LENGTH_SHORT).show();
                    status_text.setText(status_s);
                }
            }
        });
    }
}
