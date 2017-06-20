package com.example.recruitdemo.BaoGe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.recruitdemo.Adapter.Customer1Adapter;
import com.example.recruitdemo.CustomWidget.MyApplication;
import com.example.recruitdemo.FinishAll.BaseActivity;
import com.example.recruitdemo.FinishAll.CollectorActivity;
import com.example.recruitdemo.R;
import com.example.recruitdemo.SiNong.SApproveActivity;
import com.example.recruitdemo.SiNong.SStudentEditActivity;
import com.example.recruitdemo.UserBean.Customer;
import com.example.recruitdemo.UserBean.Customer1;
import com.example.recruitdemo.Utils.HttpMode;
import com.example.recruitdemo.Utils.JsonUtils;
import com.example.recruitdemo.Utils.OkUtils;
import com.example.recruitdemo.Utils.SharedUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BStudent_ShowActivity extends BaseActivity implements View.OnClickListener
, Callback{
    private Button btn_bj,bBtn_finish;
    private ImageView mFinish;
    private Customer c;
    private Map<String,String>map;

    private List<Customer1>list;
    private Customer1Adapter adapter;
    private ListView lv;
    private Customer1 customer1;
    private SharedUtils sharedUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bjxq);
        initView();
        int status=sharedUtils.getShared_int("status",this);
        if (status==0){
            bBtn_finish.setVisibility(View.GONE);
        }else {
            btn_bj.setVisibility(View.GONE);
        }
        btn_bj.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    private void initView(){

        c= (Customer) getIntent().getSerializableExtra("Customer");
        btn_bj=(Button) findViewById(R.id.btn_bj);
        bBtn_finish= (Button) findViewById(R.id.bBtn_finish);
        mFinish=(ImageView)findViewById(R.id.bFinish);
        lv=(ListView)findViewById(R.id.lv);

        sharedUtils= MyApplication.sharedUtils;
        map=new HashMap<String, String>();
        String token= HttpMode.getTOKEN(this);
        map.put("token",token);
        map.put("_method","GET");
//        map.put("indent_id",c.getIdd());
        Log.e("id-------------------",c.getIdd());
        OkUtils.UploadSJ(HttpMode.HTTPURL+HttpMode.CUSTOMER+"/"+c.getIdd(),map,this);
        bBtn_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBtn_finish:
                EventBus.getDefault().post(customer1);
                    CollectorActivity.finishActivity();
                break;

            case R.id.btn_bj:

                Intent intent=new Intent(BStudent_ShowActivity.this, SStudentEditActivity.class);
                intent.putExtra("bianji",customer1);
                startActivity(intent);
                break;
            case R.id.bFinish:
                finish();
                break;
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==1){
                String data= (String) msg.obj;
                customer1= JsonUtils.JsonStudent(data);
                list=new ArrayList<Customer1>();
                list.add(customer1);
                adapter=new Customer1Adapter(BStudent_ShowActivity.this,list);
                lv.setAdapter(adapter);
            }

            super.handleMessage(msg);
        }
    };


    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String ss=response.body().string();
        Log.e("ssss-------------------",ss);
        Message msg=handler.obtainMessage();
        msg.what=1;
        msg.obj=ss;
        handler.sendMessage(msg);
    }

}
