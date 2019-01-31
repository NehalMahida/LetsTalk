package com.mahidanehal21.letstalk3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private LayoutInflater layoutInflater;
    private List<visitmember> visitlist ;
    private DatabaseReference getuserdatareference;

    public MainAdapter(Set<visitmember> ob) {
        visitlist = new ArrayList<>(ob);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mainrecycle, parent, false);
        return new MainAdapter.MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        //FirebaseAuth  mAuth = FirebaseAuth.getInstance();
        /*String user_id = mAuth.getCurrentUser().getUid();
        getuserdatareference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        getuserdatareference.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        visitmember list = visitlist.get(position);
        holder.t2.setText(list.getStatus());
        String name = (list.getName());
        holder.t1.setText(name);



        if(!(list.getImage().equals("default_image")))
        Picasso.get().load(list.getImage()).into(holder.i1);
    }

    @Override
    public int getItemCount() {
        return visitlist.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t1, t2;
        ImageView i1;

        public MainViewHolder(final View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.Maincontect_name);
            t2 = (TextView) itemView.findViewById(R.id.MainStatus);
            i1 = (ImageView) itemView.findViewById(R.id.profile_image_circleview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Toast.makeText(itemView.getContext(), " In onclick= > ", Toast.LENGTH_SHORT).show();

            Intent cate = new Intent(view.getContext(),chatwindo.class);
            cate.putExtra("id", String.valueOf(visitlist.get(getAdapterPosition()).getId()));
            cate.putExtra("phnum" , String.valueOf(visitlist.get(getAdapterPosition()).getNumber()));
            cate.putExtra("name", String.valueOf(visitlist.get(getAdapterPosition()).getName()));
            view.getContext().startActivity(cate);
        }
    }
}
