package com.teame.boostcamp.myapplication.util.databinding;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;

public class TextViewUtil {

    @BindingConversion
    public static String convertDoubleToDisplayedString(Double num) {
        if(num==null)
            return null;
        return num.toString();
    }

    @BindingConversion
    public static String convertintToDisplayedString(int number) {
        return String.valueOf(number);
    }

    @BindingAdapter({"startDate","endDate"})
    public static void setPreviewDate(TextView textView, Date start, Date end){
        if(start==null||end==null)
            return;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String text=""+sdf.format(start)+"~"+sdf.format(end);
        textView.setText(text);
    }
    @BindingAdapter({"hashtags"})
    public static void setPreviewHashTag(TextView textView, Map<String,Boolean>hashtags){
        String text="";
        for(String str:hashtags.keySet()){
            text+="#"+str+" ";
        }
        textView.setText(text);
    }
}
