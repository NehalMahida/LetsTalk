package com.mahidanehal21.letstalk3;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<msg_class> AddString = new ArrayList<>() ;
    private  FirebaseAuth firebaseAuth;

    public MessageAdapter(ArrayList<msg_class> AddString ) {
        this.AddString = AddString;
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_relativ_message,parent,false);
    return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String me = firebaseAuth.getCurrentUser().getUid();

        if(me.equals(AddString.get(position).getFrom()))
        {

            holder.messagetext.setGravity(Gravity.RIGHT);
          //  holder.messagetext.setBackgroundResource(R.drawable.me);
            holder.messagetext.setBackgroundColor(0xFF5BAAF4);

        }
        else
  {
      holder.messagetext.setGravity(Gravity.LEFT);
      //holder.messagetext.setBackgroundResource(R.drawable.you);
        holder.messagetext.setBackgroundColor(0xFFFFFFFF);

  }
        holder.messagetext.setText((AddString.get(position)).getMessage());
    }

    @Override
    public int getItemCount() {
        return AddString.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
       public TextView messagetext;
        public MessageViewHolder(View itemView) {
            super(itemView);
            messagetext = (TextView)itemView.findViewById(R.id.message_display);

        }
    }
}
