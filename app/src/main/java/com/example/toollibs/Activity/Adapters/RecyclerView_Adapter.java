package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.toollibs.R;

import java.util.List;

public class RecyclerView_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> data;

    public RecyclerView_Adapter(Context context, List<String> data){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = layoutInflater.inflate(R.layout.layout_recycler_view, null);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
//        if(i%2==0){
//            myViewHolder.image.setImageResource(R.drawable.img_bg);
//        }else{
//            myViewHolder.image.setImageResource(R.drawable.img_bg2);
//        }
//    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

//    //self define holder
//    class MyViewHolder extends RecyclerView.ViewHolder{
//        //according to the layout layout_recycler_view
//        private ImageView image;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            //init
//            image = itemView.findViewById(R.id.image);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return data==null? 0:data.size();
//    }
}