package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.R;

import java.util.List;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> data;

    public RecyclerView_Adapter(Context context, List<String> data){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.layout_recycler_view, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.tvData.setText(data.get(i));
        if(i%2==0){
            myViewHolder.image.setImageResource(R.drawable.img_ad);
        }else{
            myViewHolder.image.setImageResource(R.drawable.img_ad1);
        }
    }

    //self define holder
    class MyViewHolder extends RecyclerView.ViewHolder{
        //according to the layout layout_recycler_view
        private TextView tvData;
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            //init
            tvData = itemView.findViewById(R.id.tvText);
            image = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public int getItemCount() {
        return data==null? 0:data.size();
    }
}