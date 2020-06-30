package com.example.toollibs.Activity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.toollibs.R;

import java.io.File;
import java.util.List;

public class ListView_FileSelector_Adapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    private LayoutInflater inflater;
    private List<File> data;
    private FileInterListener interListener;

    public ListView_FileSelector_Adapter(Context context, List<File> data, FileInterListener interListener){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.interListener = interListener;
    }
    @Override
    public int getCount() {
        return data==null ? 0:data.size();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        interListener.itemClick(buttonView, isChecked);
    }

    static class ViewHolder {
        public LinearLayout layout;
        public TextView tvData;
        public ImageView imageView;
        public CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_file_selector, null);
            holder = new ViewHolder();

            holder.layout = convertView.findViewById(R.id.layout_all);
            holder.tvData = convertView.findViewById(R.id.tvFilePath);
            holder.imageView = convertView.findViewById(R.id.imgFileDir);
            holder.checkBox = convertView.findViewById(R.id.cbChoose);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        holder.tvData.setText(data.get(position).getPath().substring(data.get(position).getPath().lastIndexOf("/")+1));//show the last layer
        if(data.get(position).isDirectory()){
            holder.imageView.setImageResource(R.drawable.icon_dir);
        }else{
            //file
            holder.imageView.setImageResource(R.drawable.icon_file);
        }

        //make component be able click from outside
        holder.checkBox.setOnCheckedChangeListener(this);
        holder.checkBox.setTag(position);
        return convertView;
    }

    public interface FileInterListener {
        void itemClick(View v, boolean isChecked);
    }
}
