package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.toollibs.R;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerView_Adapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.layout_recycler_view, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if(i%2==0){
            myViewHolder.image.setImageResource(R.drawable.img_bg);
        }else{
            myViewHolder.image.setImageResource(R.drawable.img_bg2);
        }
    }

    //self define holder
    class MyViewHolder extends RecyclerView.ViewHolder{
        //according to the layout layout_recycler_view
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            //init
            image = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}