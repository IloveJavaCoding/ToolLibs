package com.example.toollibs.Activity.Demo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.toollibs.Activity.Adapters.GridView_BookAdapter;
import com.example.toollibs.Activity.Bean.Books;
import com.example.toollibs.Activity.DataBase.DBHelper;
import com.example.toollibs.R;
import com.example.toollibs.Util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

public class Demo_Simple_Reader extends AppCompatActivity {
    private final String TAG = "SIMPLE_READER";
    private ImageView imgBack, imgImport;
    private GridView gridView;
    private GridView_BookAdapter adapter;

    private DBHelper dbHelper;
    private List<Books> list;

    private int curIndex=0;
    private final int IMPORT_BOOK_CODE = 0x001;
    private final int CHANGE_COVER_CODE = 0x002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_simple_reader);

        init();
        setData();
        setListener();
    }

    private void init() {
        imgBack = findViewById(R.id.imgBack);
        imgImport = findViewById(R.id.imgImport);
        gridView = findViewById(R.id.gridViewBook);

        dbHelper = DBHelper.getInstance(getApplicationContext());
        list = new ArrayList<>();
    }

    private void setData() {
        list = dbHelper.getAllBook();
        adapter = new GridView_BookAdapter(this, list);
        gridView.setAdapter(adapter);
    }

    private void setListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Demo_File_Selector_Activity.class);
                intent.putExtra("flag", 2);
                startActivityForResult(intent, IMPORT_BOOK_CODE);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open book
                Intent intent = new Intent(getApplicationContext(), ReaderActivity.class);
                intent.putExtra("book", list.get(position));
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //change cover page
                curIndex = position;
                IntentUtil.readImageFile(Demo_Simple_Reader.this, CHANGE_COVER_CODE);
                return true;
            }
        });
    }

    private Books extraBook(String path){
        Books books = new Books();
        if(path.endsWith(".txt")){
            String name = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
            Log.d(TAG, "book name: " + name);
            books.setName(name);
            books.setAuthor(name);
            books.setPath(path);
            books.setAlbum("null");
            books.setTag(0);
        }
        return books;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2 && requestCode==IMPORT_BOOK_CODE){
            List<String> paths = data.getExtras().getStringArrayList("files");
            for(String path: paths){
                Books books = extraBook(path);
                if(books!=null){
                    dbHelper.addBook(books);
                }
            }
            list.clear();
            list = dbHelper.getAllBook();
            adapter.notifyDataSetChanged();
        }
        if (requestCode==RESULT_OK && requestCode==CHANGE_COVER_CODE){
            Uri uri = data.getData();
            ContentResolver contentResolver = this.getContentResolver();
            String path = IntentUtil.getRealPath4Uri(this, uri, contentResolver);
            //change album path
            list.get(curIndex).setAlbum(path);
            dbHelper.updateBook(list.get(curIndex).getId(), path);
            adapter.notifyDataSetChanged();
        }
    }
}
