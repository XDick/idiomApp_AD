package com.example.administrator.havingdate;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class FamousPeopleAdapter extends RecyclerView.Adapter<FamousPeopleAdapter.ViewHolder> {
    private Context mContext;
    private List<FamousPeople> mFamousPeopleList;

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView idiomImage;
        TextView idiomName;
        TextView idiomContent;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            idiomImage = view.findViewById(R.id.information_image);
            idiomName =  view.findViewById(R.id.information_name);
            idiomContent = view.findViewById(R.id.information_content);
        }
    }



    public FamousPeopleAdapter(List<FamousPeople> FamousPeopleList) {
        mFamousPeopleList = FamousPeopleList;
    }

    @Override
    public FamousPeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.information_item, parent, false);

        final FamousPeopleAdapter.ViewHolder holder = new FamousPeopleAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                FamousPeople famousPeople = mFamousPeopleList.get(position);
                Intent intent = new Intent(mContext , InformationActivity.class);
                intent.putExtra(InformationActivity.INFORMATION_NAME,famousPeople.getTitle());
                intent.putExtra("image_url",famousPeople.getImageUrl());
                intent.putExtra("idiom_content",famousPeople.getContent());
                intent.putExtra("idiom_pre",famousPeople.getBody());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }


    public void onBindViewHolder(FamousPeopleAdapter.ViewHolder holder, int position){

        FamousPeople famousPeople= mFamousPeopleList.get(position);
        holder. idiomName.setText(famousPeople.getTitle());
        holder. idiomContent.setText(famousPeople.getBody());

        if(famousPeople.getImageUrl().equals("http://www.gs5000.cn/images/defaultpic.gif")) {
            Glide.with(mContext).load(R.drawable.android).into(holder.idiomImage);
        }
        else{
            Glide.with(mContext).load(famousPeople.getImageUrl()).into(holder.idiomImage);
        }
    }
    @Override
    public int getItemCount(){
        return mFamousPeopleList.size();
    }

}