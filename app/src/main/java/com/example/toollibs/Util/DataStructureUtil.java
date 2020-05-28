package com.example.toollibs.Util;

import java.util.Arrays;
import java.util.List;

public class DataStructureUtil {
    //array <--> list
    public static List<String> strArray2List(String[] strArray){
        return Arrays.asList(strArray);
    }

    public static List<Object> intArray2List(int[] intArray){
        return null;
        //return Arrays.stream(intArray).boxed().collect(Collectors.toList());
    }
}
