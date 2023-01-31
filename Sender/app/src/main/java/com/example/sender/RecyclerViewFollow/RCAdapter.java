package com.example.sender.RecyclerViewFollow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sender.R;
import com.example.sender.Userinfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RCAdapter extends RecyclerView.Adapter<RCVHolders> {

    private List<UsersObject> usersList;
    private Context context;

    public RCAdapter(List<UsersObject>usersList, Context context){
        this.usersList = usersList;
        this.context = context;

    }
    @Override
    public RCVHolders onCreateViewHolder( ViewGroup parent, int viewType) {
       View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_followers_item, null);
    RCVHolders rcv = new RCVHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RCVHolders holder, int position) {
holder.mEmail.setText(usersList.get(position).getEmail());

        if(Userinfo.listFollowing.contains(usersList.get(holder.getLayoutPosition()).getUid())){
            holder.mFollow.setText("following");
        }else{
            holder.mFollow.setText("follow");
        }

        //saving follows to the db
        //do later
        //if it's follow change to following
     /*  holder.mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(holder.mFollow.getText().equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("following").child(String.valueOf(usersList)).get(holder.getLayoutPosition()).getUid().setValue(true);
                }else  {                   FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("following").child(String.valueOf(usersList)).get(holder.getLayoutPosition()).getUid().removeValue(true);
                }
            }
       }); */
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
