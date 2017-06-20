package com.example.recruitdemo.FinishAll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 张金瑞 on 2017/3/28.
 */
public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CollectorActivity.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
