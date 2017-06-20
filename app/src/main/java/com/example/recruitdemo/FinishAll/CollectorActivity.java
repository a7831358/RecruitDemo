package com.example.recruitdemo.FinishAll;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张金瑞 on 2017/3/28.
 */
public class CollectorActivity {
    private static List<AppCompatActivity> list=new ArrayList<AppCompatActivity>();

    public static void addActivity(AppCompatActivity a){
        list.add(a);
    }

    public static void removeActivity(AppCompatActivity a){
        list.remove(a);
    }

    public static void finishActivity(){
        for(AppCompatActivity a:list){
            if(!a.isFinishing()){
                a.finish();
            }
        }
    }
}
