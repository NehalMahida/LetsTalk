package com.mahidanehal21.letstalk3;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AllContacts extends AppCompatActivity {
    ArrayList<ContactsFunction> selectUsers;
    LayoutInflater inflater;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    MyOwnAdapter adapter;
    Cursor phones;
    Toolbar mtoolbar;
    private ProgressDialog lodingbar;
    private DatabaseReference databaseReference;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        recyclerView = (RecyclerView) findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        mtoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        selectUsers = new ArrayList<ContactsFunction>();
        lodingbar = new ProgressDialog(this);

        Toast.makeText(AllContacts.this, "oncreate " , Toast.LENGTH_SHORT).show();
        lodingbar.setTitle("Fetching Contact From Your Contact List ");
        lodingbar.setMessage("Please Wait !!");
        lodingbar.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            final ContactsFunction selectUser = new ContactsFunction();

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phonCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

            while (phonCursor.moveToNext()) {
                final String phonenumber = phonCursor.getString(phonCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ", "");
                ;

                selectUser.setPhone(phonenumber);
                //Toast.makeText(AllContacts.this, "In firstWhile " , Toast.LENGTH_SHORT).show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FOR:
                        for (DataSnapshot tp : dataSnapshot.getChildren()) {

                            Map<String, String> ob = (Map<String, String>) tp.getValue();
                            try {
                                //              Toast.makeText(AllContacts.this, "In forloop " + ob.get("phone_num") + " and " + phonenumber, Toast.LENGTH_SHORT).show();
                                if (ob.get("phone_num").equals(phonenumber)) {
                                    selectUser.setName(name);
                                    selectUser.setImage(ob.get("image").toString().trim());
                                    selectUser.setPhone(phonenumber);
                                    selectUsers.add(selectUser);
                                    break FOR;

                                }
                            } catch (Exception e) {
                                //            Toast.makeText(AllContacts.this, e.getMessage() + ob.get("phone_num") + " and " + phonenumber, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


        }
        lodingbar.dismiss();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        adapter = new MyOwnAdapter(inflater, selectUsers, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);


    }


    public void shocontact(View view) {
        adapter = new MyOwnAdapter(inflater, selectUsers, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.app_bar_search:
            {
                adapter = new MyOwnAdapter(inflater, selectUsers, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
                break;
            }
        }
        return true;
    }
}


