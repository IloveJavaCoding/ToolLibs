package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.Activity.Bean.Books;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;

import java.util.List;

public class GridView_BookAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Books> data;

    public GridView_BookAdapter(Context context, List<Books> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
    }
    @Override
    public int getCount() {
        return data==null? 0:data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout_grid_view_book,null);
        TextView tvName = view.findViewById(R.id.tvBookName);
        ImageView imgCover = view.findViewById(R.id.imgCover);
        ImageView imgDel = view.findViewById(R.id.imgDelete);

        tvName.setText(data.get(i).getName());
        Bitmap bitmap;
        if(data.get(i).getAlbum().equals("null")){
            bitmap = BitmapUtil.getBitmapFromRes(context, R.drawable.img_book_cover);
        }else{
             bitmap = BitmapUtil.GetBitmapFromFile(data.get(i).getAlbum());
        }
        imgCover.setImageBitmap(bitmap);

        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
