package com.mahidanehal21.letstalk3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements   NavigationView.OnNavigationItemSelectedListener  {
    private FirebaseAuth mAuth;
    RecyclerView r1;
    String item_str[];
    private final int gallery_pick=1;
    MainAdapter ad;
    private StorageReference profileImageStoreRef;
    private DatabaseReference getuserdatareference;
    ArrayList<ContactsFunction> chekArray = new ArrayList<ContactsFunction>();
    private DatabaseReference databaseReference;
    Cursor phones;
    private CircleImageView my_profileImage;
    private TextView username;
    public TextView status;
    public  String name,status_string , image;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        r1 = (RecyclerView) findViewById(R.id.rec);
        item_str = getResources().getStringArray(R.array.user_name);

        navigationView.setNavigationItemSelectedListener(this);
        FirebaseUser c_user = mAuth.getCurrentUser();

        if(c_user == null)
        {
            LogOutUser();

        }
        else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            my_profileImage = (CircleImageView) headerView.findViewById(R.id.profile_image_circleview);
          //  my_status = (TextView) headerView.findViewById(R.id.nav_status);

            username = (TextView) headerView.findViewById(R.id.nav_name);
            status = (TextView) headerView.findViewById(R.id.nav_status);

            item_str = getResources().getStringArray(R.array.user_name);


            String user_id = mAuth.getCurrentUser().getUid();
            profileImageStoreRef = FirebaseStorage.getInstance().getReference().child("profile_images");
            getuserdatareference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            getuserdatareference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("user_name").getValue().toString();
                    status_string = dataSnapshot.child("status").getValue().toString();
                    image = dataSnapshot.child("image").getValue().toString();
                    String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                    Toast.makeText(MainActivity.this, " in data change name => " + name, Toast.LENGTH_SHORT).show();
                    username.setText(name);
                    status.setText(status_string);
                    //if(image_stability==1) {
                    Picasso.get().load(image).into(my_profileImage);
                    Toast.makeText(MainActivity.this," after data change name => " + name, Toast.LENGTH_SHORT).show();

                    // }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


    protected void onResume() {

        super.onResume();



    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void LogOutUser() {

        Intent i1 = new Intent(MainActivity.this,email_login.class);
        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();
    }



    @Override
    public void onBackPressed() {

        Toast.makeText(MainActivity.this, "campare  In Onbackpress " , Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_logout:
            {
                mAuth.signOut();
                LogOutUser();
                break;
            }
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            String user_id = mAuth.getCurrentUser().getUid();
            profileImageStoreRef = FirebaseStorage.getInstance().getReference().child("profile_images");
            getuserdatareference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            Intent i1 = new Intent();
            i1.setAction(Intent.ACTION_GET_CONTENT);
            i1.setType("image/*");
            startActivityForResult(i1,gallery_pick);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i1 = new Intent(MainActivity.this,Status_change_activity.class);
            startActivity(i1);



        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
                  refresh();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refresh() {
        ad = new MainAdapter( visitmemberlist.mem);
        r1.setAdapter(ad);
        r1.setLayoutManager(new LinearLayoutManager(this));

    }

    public void find(View view) {
        Intent i1 = new Intent(MainActivity.this,AllContacts.class);
        startActivity(i1);

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
                //  StorageReference filepath = profileImageStoreRef.child(user_id+".jpg");
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
                                            Toast.makeText(MainActivity.this,"Profile image is changed",Toast.LENGTH_SHORT).show();

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
}
