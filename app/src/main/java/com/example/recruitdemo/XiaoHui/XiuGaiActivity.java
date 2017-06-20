package com.example.recruitdemo.XiaoHui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recruitdemo.CustomWidget.MyApplication;
import com.example.recruitdemo.MainActivity;
import com.example.recruitdemo.R;
import com.example.recruitdemo.Utils.HttpMode;
import com.example.recruitdemo.Utils.OkUtils;
import com.example.recruitdemo.Utils.SharedUtils;
import com.example.recruitdemo.updateversion.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class XiuGaiActivity extends AppCompatActivity implements View.OnClickListener, Callback {

    private Button btn_queding;
    private ImageView back;
    private TextView tv_zhanghao;
    private EditText et_yuanmima;
    private EditText et_xinmima;
    private EditText et_xinnmima;

    private SharedUtils sharedUtils;
    private String LoginNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiu_gai);

        initView();

    }

    public void initView(){
        btn_queding=(Button)findViewById(R.id.btn_queding);
        back=(ImageView)findViewById(R.id.back);
        tv_zhanghao=(TextView) findViewById(R.id.tv_zhanghao);
        et_yuanmima=(EditText)findViewById(R.id.et_yuanmima);
        et_xinmima=(EditText)findViewById(R.id.et_xinmima);
        et_xinnmima=(EditText)findViewById(R.id.et_xinnmima);

        back.setOnClickListener(this);
        btn_queding.setOnClickListener(this);



        sharedUtils= MyApplication.sharedUtils;
        LoginNumber=sharedUtils.getShared("LoginNumber",this);
        tv_zhanghao.setText(LoginNumber);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.back:

                finish();
                break;


            case R.id.btn_queding:
                String yuan=et_yuanmima.getText().toString();
                String xinn=et_xinnmima.getText().toString();
                String xin=et_xinmima.getText().toString();
                if(xinn.equals(" ")||xin.equals(" ")){
                    Toast.makeText(XiuGaiActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!xinn.equals(xin)){
                    Toast.makeText(XiuGaiActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();

                }else {
                    Map<String,String> map=new HashMap<>();
                    map.put("token", HttpMode.getTOKEN(this));
                    map.put("_method","PUT");
                    map.put("pwd1",yuan);
                    map.put("pwd2",xin);
                    OkUtils.UploadSJ(HttpMode.HTTPURL+HttpMode.PASSWD,map,this);
                }



                break;
        }

    }


    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        String data=response.body().string();
        Message msg=hd.obtainMessage();
        msg.what=0;
        msg.obj=data;
        hd.sendMessage(msg);

    }
    Handler hd=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                String data= (String) msg.obj;

                String passwd_status= JsonUtil.JsonPasswd(data);
                if(passwd_status.equals("200")){
                    Toast.makeText(XiuGaiActivity.this,"密码修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                    sharedUtils.saveShared_int("state",0,XiuGaiActivity.this);
                     Intent in=new Intent(XiuGaiActivity.this,XLoginActivity.class);
                    startActivity(in);
                    finish();
                }else {
                    Toast.makeText(XiuGaiActivity.this,"密码修改失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
