package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.Activity.Bean.Books;
import com.example.toollibs.Activity.DataBase.DBHelper;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.DialogUtil;

import java.util.List;

public class GridView_BookAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Books> data;
    private DBHelper dbHelper;

    public GridView_BookAdapter(Context context, List<Books> list, DBHelper helper){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.dbHelper = helper;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout_grid_view_book,null);
        TextView tvName = view.findViewById(R.id.tvBookName);
        ImageView imgCover = view.findViewById(R.id.imgCover);
        ImageView imgDel = view.findViewById(R.id.imgDelete);

        tvName.setText(data.get(position).getName());
        final Bitmap bitmap;
        if(data.get(position).getAlbum().equals("null")){
            bitmap = BitmapUtil.getBitmapFromRes(context, R.drawable.img_book_cover);
        }else{
             bitmap = BitmapUtil.getBitmapFromFile(data.get(position).getAlbum());
        }
        imgCover.setImageBitmap(bitmap);

        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showMsgDialog(context, "Notice!", "Remove book from the list?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                Log.d("TAG", "delete book: " + data.get(position).getName());
                                dbHelper.deleteBook(data.get(position));
                                data.remove(position);
                                notifyDataSetChanged();
                                break;
                        }
                    }
                });
            }
        });

        return view;
    }
}
