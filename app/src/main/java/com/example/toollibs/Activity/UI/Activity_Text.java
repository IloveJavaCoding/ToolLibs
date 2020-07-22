package com.example.toollibs.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.toollibs.OverWriteClass.MarqueeHorizontal;
import com.example.toollibs.OverWriteClass.MarqueeVertical;
import com.example.toollibs.OverWriteClass.SelfPasswordTransformationMethod;
import com.example.toollibs.R;
import com.example.toollibs.OverWriteClass.MarqueeHorizontalText;
import com.example.toollibs.Util.SystemUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Activity_Text extends AppCompatActivity {
    private TextView tvLamp, tvTest;
    private TextView tvTest1, tvTest2, tvTest3, tvTest4, tvTest5, tvTest6, tvTest7, tvTest8;
    private EditText etPassword, etEnter, etDel;
    private Button bEnter;
    private ToggleButton tbControl;
    private MarqueeHorizontalText view;
    private MarqueeHorizontal horizontal;
    private MarqueeVertical vertical;
    private Spinner spinner1, spinner2;

    private String data1;
    private int data2;
    private String[] strArray;
    private List<Object> list;

    private SelfPasswordTransformationMethod transformationMethod;
    private static final String DEFAULT_TEXT = "曾经沧海难为水，除却巫山不是云。取次花丛懒回顾，半缘修道半缘君。" +
            "葡萄美酒夜光杯，欲饮琵琶马上催。醉卧沙场君莫笑。古来征战几人回。";
    private static final String DEFAULT_TEXT2 = "What is faith? If it doesn't endure when we are tested the most?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__text);

        init();
        setData();
        setListener();
    }

    private void init() {
        tvLamp = findViewById(R.id.tvLamp);
        etPassword = findViewById(R.id.etPassword);
        etEnter = findViewById(R.id.etEnter);
        etDel = findViewById(R.id.etDel);

        bEnter = findViewById(R.id.bEnter);
        tbControl = findViewById(R.id.tbControl);
        view = findViewById(R.id.view);
        horizontal = findViewById(R.id.marqueeH);
        vertical = findViewById(R.id.marqueeV);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        tvTest = findViewById(R.id.tvTest);

        tvTest1 = findViewById(R.id.tvTest1);
        tvTest2 = findViewById(R.id.tvTest2);
        tvTest3 = findViewById(R.id.tvTest3);
        tvTest4 = findViewById(R.id.tvTest4);
        tvTest5 = findViewById(R.id.tvTest5);
        tvTest6 = findViewById(R.id.tvTest6);
        tvTest7 = findViewById(R.id.tvTest7);
        tvTest8 = findViewById(R.id.tvTest8);

        testTextStyle();
    }

    private void setData() {
        transformationMethod = new SelfPasswordTransformationMethod('@');
        etPassword.setTransformationMethod(transformationMethod);

        tvLamp.setText(DEFAULT_TEXT);
        tvLamp.setTextColor(Color.YELLOW);
        //tvLamp.setTextColor(getResources().getColor(R.color.colorYellow));
        tvLamp.setSelected(true);

        strArray = getResources().getStringArray(R.array.Date);
        int[] intArray = getResources().getIntArray(R.array.google_colors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list = Arrays.stream(intArray).boxed().collect(Collectors.toList());
        }

        //set default value
        data1 = strArray[0];
        data2 = intArray[0];

        //set adapter
        ArrayAdapter<String> strAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strArray);
        strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(strAdapter);

        ArrayAdapter<Object> intAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        intAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(intAdapter);

        setMarquee(view);
        setHorizontal();
        setVertical();
    }

    private void setVertical() {
        vertical.setTextSize(20);
        vertical.setTextColor(Color.YELLOW);
        vertical.setBackgroundColor(Color.BLACK);
        vertical.setContents(DEFAULT_TEXT);
    }

    private void setHorizontal() {
        horizontal.setTextSize(20);
        horizontal.setTextColor(Color.YELLOW);
        horizontal.setBackgroundColor(Color.BLACK);
        horizontal.setSpeed(5);
        horizontal.setContents(DEFAULT_TEXT2);
    }

    private void setMarquee(MarqueeHorizontalText view){
        view.setTextSize(25);
        view.setTextColor(Color.YELLOW);
        view.setBackgroundColor(Color.BLACK);
        view.setTextSpeed(8);
        view.setContent(DEFAULT_TEXT2);
    }

    private void setListener() {
        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etDel.getText().toString();
                if(temp!=null){
                    tvLamp.setText(temp);
                    view.setContent(temp);
                }
            }
        });

        //ues keyboard to control input
        etEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))){
                    //hide keyboard
                    ((InputMethodManager)etEnter.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(etEnter.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //do something
                    SystemUtil.ShowToast(getApplicationContext(), etEnter.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //hide/show password
        tbControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    etPassword.setTransformationMethod(transformationMethod);//no need .getInstance()
                }
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //i --> index of array
                data1 = strArray[i];
                SystemUtil.ShowToast(getApplicationContext(),data1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data2 = (int) list.get(i);
                SystemUtil.ShowToast(getApplicationContext(), data2+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void testTextStyle(){
        //============0,1,23456 7 8 9101112131415
        String text = "我是text,请尽情地调试我吧!";
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/xingkai.ttf");

        //set font
        tvTest1.setText(text);
        tvTest1.setTypeface(typeface, Typeface.BOLD);

        //set text size
//        tvTest1.setTextSize(18.0f);
        tvTest1.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_size_18));

        //delete line
        tvTest2.setText(text);
        tvTest2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //under line
        tvTest3.setText(text);
        tvTest3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //italic and bold
        tvTest4.setText(text);
        //tvTest4.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        //tvTest4.getPaint().setFakeBoldText(true);
        tvTest4.setTypeface(null, Typeface.BOLD_ITALIC);
//        tvTest4.setTypeface(null, Typeface.ITALIC);

        //text color
        tvTest5.setText(text);
        tvTest5.setTextColor(Color.RED);
        //tvTest5.setTextColor(Color.argb(255,255,0,0));
        //tvTest5.setTextColor(Color.parseColor("#ff0000"));
//        tvTest5.setTextColor(getResources().getColor(R.color.colorRed));

        //use html
        String content = "<font color=\"#ffff00\">" + text +"</font>";
        tvTest6.setText(Html.fromHtml(content));

        //set part style
        //control by the index
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new TypefaceSpan("serif"),0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗斜体
        msp.setSpan(new UnderlineSpan(), 8, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new StrikethroughSpan(), 11, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 13, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        msp.setSpan(new URLSpan("http://www.baidu.com"), 13, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //网络
        tvTest7.setText(msp);
        tvTest7.setMovementMethod(LinkMovementMethod.getInstance());//required when add some superLinks
    }
}
