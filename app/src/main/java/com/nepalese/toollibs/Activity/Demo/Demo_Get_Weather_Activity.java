package com.nepalese.toollibs.Activity.Demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalese.toollibs.Activity.Config.Constant;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Bean.WeatherInfo;
import com.nepalese.toollibs.Util.ConvertUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author nepalese on 2020/9/18 17:01
 * @usage
 */
public class Demo_Get_Weather_Activity extends AppCompatActivity {
    private static final String TAG = "Demo_Get_Weather";

    private EditText etLng, etLat;
    private Button bGetWeather;
    private ImageView imgWeather;
    private TextView tvWeather, tvTemp;

    private final String WEATHER_API_URL = "https://free-api.heweather.com/s6/weather/now?key=20ce3187f9664117b3236fdf72ac67cc&location=";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_get_weather);

        init();
        setListener();
    }

    private void init() {
        etLng = findViewById(R.id.etLng);
        etLat = findViewById(R.id.etLat);

        tvWeather = findViewById(R.id.tvWeather);
        tvTemp = findViewById(R.id.tvTemp);

        imgWeather = findViewById(R.id.imgWeather);
        bGetWeather = findViewById(R.id.bGetWeather);
    }

    private void setListener() {
        bGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather();
            }
        });
    }

    private void getWeather() {
        String lng = etLng.getText().toString();
        String lat = etLat.getText().toString();

        if(!lng.equals(null) && !lat.equals(null)){
            String jsonString = getJsonString(WEATHER_API_URL+lng+","+ lat, "utf-8");
            WeatherInfo weatherInfo = (WeatherInfo) ConvertUtil.getObject(jsonString, WeatherInfo.class);
            if (weatherInfo!=null &&weatherInfo.getHeWeather6()!=null &&weatherInfo.getHeWeather6().size()>0
                    &&weatherInfo.getHeWeather6().get(0).getNow()!=null){
                String code = weatherInfo.getHeWeather6().get(0).getNow().getCond_code();
                updateWeather(Integer.parseInt(code),Integer.parseInt(weatherInfo.getHeWeather6().get(0).getNow().getTmp()));
            }else {
                updateWeather(0,0);
            }
        }else{
            Log.e(TAG, "输入为空！！！");
        }
    }

    private void updateWeather(int code, int temp) {
        setType(code);
        updateTemp(temp);
    }

    private void setType(int code) {
        switch (code){
            case Constant.WEATHER_SUNNY_CODE:
                imgWeather.setImageResource(R.mipmap.weather100);
                tvWeather.setText(R.string.weather_string_sunny);
                break;
            case Constant.WEATHER_CLOUDY_CODE:
                imgWeather.setImageResource(R.mipmap.weather101);
                tvWeather.setText(R.string.weather_string_cloudy);
                break;
            case Constant.WEATHER_FEW_CLOUDS_CODE:
                imgWeather.setImageResource(R.mipmap.weather102);
                tvWeather.setText(R.string.weather_string_few_clouds);
                break;
            case Constant.WEATHER_PARTLY_CLOUDY_CODE:
                imgWeather.setImageResource(R.mipmap.weather103);
                tvWeather.setText(R.string.weather_string_partly_cloudy);
                break;
            case Constant.WEATHER_OVERCAST_CODE:
                imgWeather.setImageResource(R.mipmap.weather104);
                tvWeather.setText(R.string.weather_string_overcast);
                break;
            case Constant.WEATHER_WINDY_CODE:
                imgWeather.setImageResource(R.mipmap.weather200);
                tvWeather.setText(R.string.weather_string_windy);
                break;
            case Constant.WEATHER_CALM_CODE:
                imgWeather.setImageResource(R.mipmap.weather201);
                tvWeather.setText(R.string.weather_string_calm);
                break;
            case Constant.WEATHER_LIGHT_BREEZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather200);
                tvWeather.setText(R.string.weather_string_light_breeze);
                break;
            case Constant.WEATHER_MODERATE_BREEZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather200);
                tvWeather.setText(R.string.weather_string_moderate_breeze);
                break;
            case Constant.WEATHER_FRESH_BREEZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather200);
                tvWeather.setText(R.string.weather_string_fresh_breeze);
                break;
            case Constant.WEATHER_STRONG_BREEZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather205);
                tvWeather.setText(R.string.weather_string_strong_breeze);
                break;
            case Constant.WEATHER_HIGH_WIND_CODE:
                imgWeather.setImageResource(R.mipmap.weather205);
                tvWeather.setText(R.string.weather_string_high_wind);
                break;
            case Constant.WEATHER_GALE_CODE:
                imgWeather.setImageResource(R.mipmap.weather205);
                tvWeather.setText(R.string.weather_string_gale);
                break;
            case Constant.WEATHER_STRONG_GALE_CODE:
                imgWeather.setImageResource(R.mipmap.weather208);
                tvWeather.setText(R.string.weather_string_strong_gale);
                break;
            case Constant.WEATHER_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather208);
                tvWeather.setText(R.string.weather_string_storm);
                break;
            case Constant.WEATHER_VIOLENT_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather208);
                tvWeather.setText(R.string.weather_string_violent_storm);
                break;
            case Constant.WEATHER_HURRICANE_CODE:
                imgWeather.setImageResource(R.mipmap.weather101);
                tvWeather.setText(R.string.weather_string_hurricane);
                break;
            case Constant.WEATHER_TORNADO_CODE:
                imgWeather.setImageResource(R.mipmap.weather208);
                tvWeather.setText(R.string.weather_string_tornado);
                break;
            case Constant.WEATHER_TROPICAL_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather208);
                tvWeather.setText(R.string.weather_string_tropical_storm);
                break;
            case Constant.WEATHER_SHOWER_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather300);
                tvWeather.setText(R.string.weather_string_shower_rain);
                break;
            case Constant.WEATHER_HEAVY_SHOWER_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather301);
                tvWeather.setText(R.string.weather_string_heavy_shower_rain);
                break;
            case Constant.WEATHER_THUNDERSHOWER_CODE:
                imgWeather.setImageResource(R.mipmap.weather302);
                tvWeather.setText(R.string.weather_string_thundershower);
                break;
            case Constant.WEATHER_HEAVY_THUNDERSHOWER_CODE:
                imgWeather.setImageResource(R.mipmap.weather303);
                tvWeather.setText(R.string.weather_string_heavy_thunderstorm);
                break;
            case Constant.WEATHER_THUNDERSHOWER_WITH_HAIL_CODE:
                imgWeather.setImageResource(R.mipmap.weather304);
                tvWeather.setText(R.string.weather_string_thundershower_with_hail);
                break;
            case Constant.WEATHER_LIGHT_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather305);
                tvWeather.setText(R.string.weather_string_light_rain);
                break;
            case Constant.WEATHER_MODERATE_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather306);
                tvWeather.setText(R.string.weather_string_moderate_rain);
                break;
            case Constant.WEATHER_HEAVY_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather307);
                tvWeather.setText(R.string.weather_string_heavy_rain);
                break;
            case Constant.WEATHER_EXTREME_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather312);
                tvWeather.setText(R.string.weather_string_extreme_rain);
                break;
            case Constant.WEATHER_DRIZZLE_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather309);
                tvWeather.setText(R.string.weather_string_drizzle_rain);
                break;
            case Constant.WEATHER_STORM_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather310);
                tvWeather.setText(R.string.weather_string_storm_rain);
                break;
            case Constant.WEATHER_HEAVY_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather311);
                tvWeather.setText(R.string.weather_string_heavy_storm);
                break;
            case Constant.WEATHER_SEVERE_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather312);
                tvWeather.setText(R.string.weather_string_severe_storm);
                break;
            case Constant.WEATHER_FREEZING_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather313);
                tvWeather.setText(R.string.weather_string_freezing_rain);
                break;
            case Constant.WEATHER_LIGHT_TO_MODERATE_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather314);
                tvWeather.setText(R.string.weather_string_light_to_moderate_rain);
                break;
            case Constant.WEATHER_MODERATE_TO_HEAVY_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather307);
                tvWeather.setText(R.string.weather_string_moderate_to_heavy_rain);
                break;
            case Constant.WEATHER_HEAVY_RAIN_TO_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather310);
                tvWeather.setText(R.string.weather_string_heavy_rain_to_storm);
                break;
            case Constant.WEATHER_STORM_TO_HEAVY_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather311);
                tvWeather.setText(R.string.weather_string_storm_to_heavy_storm);
                break;
            case Constant.WEATHER_HEAVY_TO_SEVERE_STORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather312);
                tvWeather.setText(R.string.weather_string_heavy_to_severe_storm);
                break;
            case Constant.WEATHER_RAIN_CODE:
                imgWeather.setImageResource(R.mipmap.weather399);
                tvWeather.setText(R.string.weather_string_rain);
                break;
            case Constant.WEATHER_LIGHT_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather400);
                tvWeather.setText(R.string.weather_string_light_snow);
                break;
            case Constant.WEATHER_MODERATE_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather401);
                tvWeather.setText(R.string.weather_string_moderate_snow);
                break;
            case Constant.WEATHER_HEAVY_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather402);
                tvWeather.setText(R.string.weather_string_heavy_snow);
                break;
            case Constant.WEATHER_SNOWSTORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather403);
                tvWeather.setText(R.string.weather_string_snowstorm);
                break;
            case Constant.WEATHER_SLEET_CODE:
                imgWeather.setImageResource(R.mipmap.weather404);
                tvWeather.setText(R.string.weather_string_sleet);
                break;
            case Constant.WEATHER_RAIN_AND_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather405);
                tvWeather.setText(R.string.weather_string_rain_and_snow);
                break;
            case Constant.WEATHER_SHOWER_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather406);
                tvWeather.setText(R.string.weather_string_shower_snow);
                break;
            case Constant.WEATHER_SNOW_FLURRY_CODE:
                imgWeather.setImageResource(R.mipmap.weather407);
                tvWeather.setText(R.string.weather_string_snow_flurry);
                break;
            case Constant.WEATHER_LIGHT_TO_MODERATE_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather401);
                tvWeather.setText(R.string.weather_string_light_to_moderate_snow);
                break;
            case Constant.WEATHER_MODERATE_TO_HEAVY_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather402);
                tvWeather.setText(R.string.weather_string_moderate_to_heavy_snow);
                break;
            case Constant.WEATHER_HEAVY_SNOW_TO_SNOWSTORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather403);
                tvWeather.setText(R.string.weather_string_heavy_snow_to_snowstorm);
                break;
            case Constant.WEATHER_SNOW_CODE:
                imgWeather.setImageResource(R.mipmap.weather499);
                tvWeather.setText(R.string.weather_string_snow);
                break;
            case Constant.WEATHER_MIST_CODE:
                imgWeather.setImageResource(R.mipmap.weather500);
                tvWeather.setText(R.string.weather_string_mist);
                break;
            case Constant.WEATHER_FOGGY_CODE:
                imgWeather.setImageResource(R.mipmap.weather501);
                tvWeather.setText(R.string.weather_string_foggy);
                break;
            case Constant.WEATHER_HAZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather502);
                tvWeather.setText(R.string.weather_string_haze);
                break;
            case Constant.WEATHER_SAND_CODE:
                imgWeather.setImageResource(R.mipmap.weather503);
                tvWeather.setText(R.string.weather_string_sand);
                break;
            case Constant.WEATHER_DUST_CODE:
                imgWeather.setImageResource(R.mipmap.weather504);
                tvWeather.setText(R.string.weather_string_dust);
                break;
            case Constant.WEATHER_DUSTSTORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather507);
                tvWeather.setText(R.string.weather_string_duststorm);
                break;
            case Constant.WEATHER_SANDSTORM_CODE:
                imgWeather.setImageResource(R.mipmap.weather508);
                tvWeather.setText(R.string.weather_string_sandstorm);
                break;
            case Constant.WEATHER_DENSE_FOG_CODE:
                imgWeather.setImageResource(R.mipmap.weather509);
                tvWeather.setText(R.string.weather_string_dense_fog);
                break;
            case Constant.WEATHER_STRONG_FOG_CODE:
                imgWeather.setImageResource(R.mipmap.weather509);
                tvWeather.setText(R.string.weather_string_strong_fog);
                break;
            case Constant.WEATHER_MODERATE_HAZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather511);
                tvWeather.setText(R.string.weather_string_moderate_haze);
                break;
            case Constant.WEATHER_HEAVY_HAZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather512);
                tvWeather.setText(R.string.weather_string_heavy_haze);
                break;
            case Constant.WEATHER_SEVERE_HAZE_CODE:
                imgWeather.setImageResource(R.mipmap.weather513);
                tvWeather.setText(R.string.weather_string_severe_haze);
                break;
            case Constant.WEATHER_HEAVY_FOG_CODE:
                imgWeather.setImageResource(R.mipmap.weather509);
                tvWeather.setText(R.string.weather_string_heavy_fog);
                break;
            case Constant.WEATHER_EXTRA_HEAVY_FOG_CODE:
                imgWeather.setImageResource(R.mipmap.weather509);
                tvWeather.setText(R.string.weather_string_extra_heavy_fog);
                break;
            case Constant.WEATHER_HOT_CODE:
                imgWeather.setImageResource(R.mipmap.weather900);
                tvWeather.setText(R.string.weather_string_hot);
                break;
            case Constant.WEATHER_COLD_CODE:
                imgWeather.setImageResource(R.mipmap.weather901);
                tvWeather.setText(R.string.weather_string_cold);
                break;
            default:
                imgWeather.setImageResource(R.mipmap.weather999);
                tvWeather.setText(R.string.weather_string_unknown);
                break;
        }
    }

    private void updateTemp(int temp) {
        tvTemp.setText(getString(R.string.weather_temp,temp));
    }

    private String getJsonString(String url, String encoding){
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            // 创建网络连接
            URL url1 = new URL(url);
            // 打开网络
            URLConnection uc = url1.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 建立文件输入流
            inputStreamReader = new InputStreamReader(uc.getInputStream(), encoding);
            // 高效率读取
            bufferedReader = new BufferedReader(inputStreamReader);
            // 下载页面源码

            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                buffer.append(temp.trim());
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "网页打开失败，请重新输入网址!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "网页打开失败,请检查网络!");
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return buffer.toString();
    }
}
