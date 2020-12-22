package com.nepalese.toollibs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nepalese.toollibs.Bean.Station;
import com.nepalese.toollibs.Bean.Student;

import java.util.ArrayList;
import java.util.List;

public class Demo_Two_Recycle_Activity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerAdapter1 adapter1;
    private List<Station> stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo__two__recycle_);

        init();
        setData();
        initAdapter();
    }

    private void init() {
        recyclerView1 = findViewById(R.id.recycleView1);
    }

    private void setData() {
        stationList = new ArrayList<>();

        Student student1 = new Student(1, "杨幂");
        Student student2 = new Student(2, "唐嫣");
        Student student3 = new Student(3, "林允");
        Student student4 = new Student(4, "汤唯");
        Student student5 = new Student(5, "杨颖");
        Student student6 = new Student(6, "王菲");
        Student student7 = new Student(7, "杨紫");

        List<Student> list1 = new ArrayList<>();
        list1.add(student1);
        list1.add(student3);

        List<Student> list2 = new ArrayList<>();
        list2.add(student2);
        list2.add(student4);
        list2.add(student7);

        List<Student> list3 = new ArrayList<>();
        list3.add(student5);
        list3.add(student6);;

        Station station1 = new Station("MMP", list1);
        Station station2 = new Station("TTY", list2);
        Station station3 = new Station("NNQ", list3);

        stationList.add(station1);
        stationList.add(station2);
        stationList.add(station3);
    }

    private void initAdapter(){
        LinearLayoutManager leftLinearLayoutManager = new LinearLayoutManager(this);
        leftLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(leftLinearLayoutManager);

        adapter1 = new RecyclerAdapter1(this, stationList);
        recyclerView1.setAdapter(adapter1);
        //获取到服务器数据后更新adapter
    }
}

class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Station> list;

    public RecyclerAdapter1(Context context, List<Station> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvStationName;
        private RecyclerView recyclerView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStationName = itemView.findViewById(R.id.tvStationName);
            recyclerView2 = itemView.findViewById(R.id.recycleView2);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycle_1, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Station station = list.get(i);
        if(station!=null){
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvStationName.setText(station.getName());

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            holder.recyclerView2.setLayoutManager(gridLayoutManager);

            RecyclerAdapter2 adapter2 = new RecyclerAdapter2(context);
            holder.recyclerView2.setAdapter(adapter2);
            adapter2.setList(station.getList());
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
}

class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Student> list;

    public RecyclerAdapter2(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<Student> list){
        if(this.list!=null){
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudent = itemView.findViewById(R.id.tvStudent);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycle_2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Student student = list.get(i);
        if(student!=null){
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvStudent.setText(student.getId() + ": " + student.getName());
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
}

