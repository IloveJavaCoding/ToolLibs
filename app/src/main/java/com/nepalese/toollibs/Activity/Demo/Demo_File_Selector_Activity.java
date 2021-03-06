package com.nepalese.toollibs.Activity.Demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nepalese.toollibs.Activity.Adapters.ListView_FileSelector_Adapter;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.FileUtil;
import com.nepalese.toollibs.Util.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo_File_Selector_Activity  extends AppCompatActivity implements ListView_FileSelector_Adapter.FileInterListener {
    private TextView tvCurPath, tvConfirm, tvResult;
    private LinearLayout layoutRoot, layoutLast;
    private ListView listView;
    private ListView_FileSelector_Adapter adapter;

    private String rootPath;
    private String curPath;
    private List<File> files;
    private List<Integer> index;

    private int flag;//0:test; 1:dir; 2:file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_file_selector);

        getDate();
        init();
        setData();
        setListener();
    }

    private void getDate() {
        flag = getIntent().getExtras().getInt("flag");
        Log.d("tag", "flag: "+ flag);
    }

    private void init() {
        tvCurPath = findViewById(R.id.tvCurPath);
        tvConfirm = findViewById(R.id.tvConfirmChoose);
        tvResult = findViewById(R.id.tvResult);

        layoutRoot = findViewById(R.id.layoutToRoot);
        layoutLast = findViewById(R.id.layoutToLast);

        listView = findViewById(R.id.listViewFile);
        index = new ArrayList<>();
    }

    private void setData() {
        ///storage/emulated/0
        rootPath = FileUtil.getRootPath();
        curPath = rootPath;
        tvCurPath.setText(curPath);
        List<File> temp = Arrays.asList(new File(curPath).listFiles());
        files =  new ArrayList(temp);

        adapter = new ListView_FileSelector_Adapter(this, files, this);//指向的是最开始的list
        listView.setAdapter(adapter);
    }

    private void resetData(String path){
        curPath = path;
        tvCurPath.setText(curPath);
        files.clear();
        index.clear();
        File[] fs = new File(curPath).listFiles();
        Arrays.sort(fs);
        List<File> temp = Arrays.asList(fs);
        for(File f : temp){
            files.add(f);
        }
        adapter.notifyDataSetChanged();
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("click", String.valueOf(position+1));
                //judge file/dir
                if(files.get(position).isFile()){
                    //
                }else{
                    resetData(files.get(position).getPath());
                }
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0://test
                        if(index.size()>0){
                            StringBuilder builder = new StringBuilder();
                            for(int i=0; i<index.size(); i++){
                                builder.append(files.get(index.get(i)).getPath()+"\n");
                            }
                            tvResult.setText(builder.toString());
                        }else{
                            tvResult.setText("未选择任何目标！");
                        }
                        break;
                    case 1://return dirs
                        List<String> temp = new ArrayList<>();
                        if(index.size()>0){
                            for(int i=0; i<index.size(); i++){
                                if(files.get(index.get(i)).isDirectory()){
                                    temp.add(files.get(index.get(i)).getPath());
                                }
                            }
                        }else{
                            //
                        }
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("dirs", (ArrayList<String>) temp);
                        setResult(1, intent);
                        finish();
                        break;
                    case 2://return files
                        List<String> temp2 = new ArrayList<>();
                        if(index.size()>0){
                            for(int i=0; i<index.size(); i++){
                                if(files.get(index.get(i)).isFile()){
                                    temp2.add(files.get(index.get(i)).getPath());
                                }
                            }
                        }else{
                            //
                        }
                        Intent intent2 = new Intent();
                        intent2.putStringArrayListExtra("files", (ArrayList<String>) temp2);
                        setResult(2, intent2);
                        finish();
                        break;
                }
            }
        });

        layoutLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //judge curPath is root or not
                if (curPath.equals(rootPath)) {
                    //do nothing
                    SystemUtil.showToast(getApplicationContext(), "已是根目录");
                }else{
                    //back to last layer
                    resetData(curPath.substring(0, curPath.lastIndexOf("/")));
                }
            }
        });

        layoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curPath.equals(rootPath)){
                    //do nothing
                    SystemUtil.showToast(getApplicationContext(), "已是根目录");
                }else{
                    //back to root
                    resetData(rootPath);
                }
            }
        });
    }

    @Override
    public void itemClick(View v, boolean isChecked) {
        Integer position = (Integer) v.getTag();
        switch (v.getId()){
            case R.id.cbChoose:
                if(isChecked){
                    SystemUtil.showToast(getApplicationContext(), "choose " + (position+1));
                    index.add(position);
                }else{
                    index.remove(position);
                }
                break;
        }
    }
}
