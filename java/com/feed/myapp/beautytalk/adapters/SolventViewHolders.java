package com.beautytalk.lannet.beautytalk.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beautytalk.lannet.beautytalk.R;


public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView countryName;
    public ImageView countryPhoto,deletedBtn;
    RelativeLayout laycontent;

    public SolventViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
        deletedBtn = (ImageView) itemView.findViewById(R.id.deletedBtn);
        laycontent = (RelativeLayout) itemView.findViewById(R.id.laycontent);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}