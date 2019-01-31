package com.mahidanehal21.letstalk3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.support.v4.content.ContextCompat.createDeviceProtectedStorageContext;
import static android.support.v4.content.ContextCompat.startActivity;

public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MyOwnHolder> {

    private LayoutInflater layoutInflater;

    ContactsFunction list;
    private ArrayList<ContactsFunction> arraylist;
    boolean checked = false;
    private DatabaseReference   database_ref;
    Context ctx;

    public MyOwnAdapter(LayoutInflater inflater, List<ContactsFunction> items,Context ctx) {
        this.layoutInflater = inflater;
        arraylist = (ArrayList<ContactsFunction>) items;
        this.ctx = ctx;

    }




    @Override
    public MyOwnAdapter.MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater myinflater = LayoutInflater.from(cx1);
        View myownView = layoutInflater.inflate(R.layout.recycler_row, parent, false);
        return new MyOwnHolder(myownView,ctx,arraylist);

    }

    @Override
    public void onBindViewHolder(final MyOwnAdapter.MyOwnHolder holder, final int position) {
        list = arraylist.get(position);
        String name = (list.getName());
        String phone = (list.getPhone());
        holder.t1.setText(name);
        //holder.t2.setText(phone);
        if(!(list.getImage().equals("default_image")))
            Picasso.get().load(list.getImage()).into(holder.i1);
    }

    //@Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    //  @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyOwnHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t1,t2;
        ImageView i1;
        LinearLayout rec1;
        LayoutInflater layoutInflater2;
        Context ctx;
        ArrayList<ContactsFunction> contactsFunctions = new ArrayList<ContactsFunction>();
        public MyOwnHolder(View itemView,Context ctx,ArrayList<ContactsFunction> contactsFunctions) {
            super(itemView);
            this.ctx = ctx;
            this.contactsFunctions = contactsFunctions;
            this.layoutInflater2 = layoutInflater2;
            t1 = (TextView) itemView.findViewById(R.id.contect_name);
            i1 = (ImageView) itemView.findViewById(R.id.profile_image_circleview);
            rec1 = (LinearLayout) itemView.findViewById(R.id.constrain);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(final View view) {
            database_ref = FirebaseDatabase.getInstance().getReference().child("Users");
            int position = getAdapterPosition();
            ContactsFunction cf = this.contactsFunctions.get(position);
            final String phon = cf.getPhone().replaceAll(" ","");
            final String nm = cf.getName();
            Toast.makeText(view.getContext(), "In Onclick "  , Toast.LENGTH_SHORT).show();
            try{
                database_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //  Toast.makeText(view.getContext(), "In OnDataChange "  , Toast.LENGTH_SHORT).show();
                        for(DataSnapshot tp : dataSnapshot.getChildren()) {
                            Map<String,String> ob = (Map<String,String>)tp.getValue();
                            Toast.makeText(view.getContext(), "In forloop " + ob.get("phone_num") +" and " + phon, Toast.LENGTH_SHORT).show();
                            if (ob.get("phone_num").equals(phon)) {
                                String ImG = ob.get("image");
                                String stat = ob.get("status");
                                // Toast.makeText(view.getContext(), "In if "  +tp.getKey(), Toast.LENGTH_SHORT).show();
                                Intent cate = new Intent(view.getContext(),chatwindo.class);
                                cate.putExtra("id",tp.getKey());
                                cate.putExtra("phnum" ,phon );
                                cate.putExtra("name",nm);
                                cate.putExtra("image",ImG);
                                cate.putExtra("status",stat);
                                view.getContext().startActivity(cate);


                            }



                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }



                });
            }
            catch (Exception e){
                Toast.makeText(view.getContext() ,e.getMessage(),Toast.LENGTH_SHORT ).show();
            }


        }



    }
}
