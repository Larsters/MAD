package com.example.sender.RecyclerViewFollow;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sender.R;

public class RCVHolders extends RecyclerView.ViewHolder {
    public TextView mEmail;
    public Button mFollow;

    public RCVHolders(View itemView){
super(itemView);
    mEmail = itemView.findViewById(R.id.email);
    mFollow = itemView.findViewById(R.id.follow);
    }


}
