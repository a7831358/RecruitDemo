package com.example.recruitdemo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recruitdemo.CustomWidget.MyApplication;
import com.example.recruitdemo.Utils.HttpMode;
import com.example.recruitdemo.Utils.OkUtils;
import com.example.recruitdemo.Utils.SharedUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 录入学生信息界面    实现主要功能：将学生信息录入完成以后上传到服务器。
 */
public class EnterActivity extends AppCompatActivity implements View.OnClickListener, Callback{

    private TextView tvs1, tvs2, tvs3,tvs4, tvs5, tvs6, tvs7, tvs8, tvs9,
             tvs12;
    private ImageView eFinish;
    private Button btn_bj;
    private SharedUtils sharedUtils;
    private int area_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        initView();
    }

    private void initView(){
        eFinish=(ImageView)findViewById(R.id.eFinish);
        btn_bj=(Button)findViewById(R.id.btn_bj);
        sharedUtils = MyApplication.sharedUtils;

        LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
        LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
        LinearLayout l4 = (LinearLayout) findViewById(R.id.l4);
        LinearLayout l5 = (LinearLayout) findViewById(R.id.l5);
        LinearLayout l6 = (LinearLayout) findViewById(R.id.l6);
        LinearLayout l7 = (LinearLayout) findViewById(R.id.l7);
        LinearLayout l8 = (LinearLayout) findViewById(R.id.l8);
        LinearLayout l9 = (LinearLayout) findViewById(R.id.l9);
        LinearLayout l12 = (LinearLayout) findViewById(R.id.l12);

        tvs1 = (TextView) findViewById(R.id.tvs1);
        tvs2 = (TextView) findViewById(R.id.tvs2);
        tvs3 = (TextView) findViewById(R.id.tvs3);
        tvs4=(TextView)findViewById(R.id.tvs4);
        tvs5 = (TextView) findViewById(R.id.tvs5);

        tvs6 = (TextView) findViewById(R.id.tvs6);
        tvs7 = (TextView) findViewById(R.id.tvs7);
        tvs8 = (TextView) findViewById(R.id.tvs8);
        tvs9 = (TextView) findViewById(R.id.tvs9);
        tvs12 = (TextView) findViewById(R.id.tvs12);
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l4.setOnClickListener(this);
        l5.setOnClickListener(this);

        l6.setOnClickListener(this);
        l7.setOnClickListener(this);
        l8.setOnClickListener(this);
        l9.setOnClickListener(this);
        l12.setOnClickListener(this);
        btn_bj.setOnClickListener(this);

        eFinish.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bj:

                String name=tvs1.getText().toString();
                String sex=tvs2.getText().toString();
                String id=tvs3.getText().toString();
                String relation=tvs5.getText().toString();
                String contact=tvs6.getText().toString();
                String address=tvs7.getText().toString();
                String relative=tvs8.getText().toString();
                String relative_contact=tvs9.getText().toString();
                String notes = tvs12.getText().toString();

                Map<String, String> map = new HashMap<String ,String>();
                String areaId=String.valueOf(area_id);

                map.put("token", HttpMode.getTOKEN(this));
                map.put("_method","POST");
                map.put("id",id);
                map.put("name",name);
                map.put("sex",sex);
                map.put("zone_id",areaId);
                map.put("address",address);
                map.put("contact",contact);
                map.put("relative",relative);
                map.put("relation",relation);
                map.put("relative_contact",relative_contact);
                map.put("notes",notes);
                if(name.equals("未填写")&&id.equals("未填写")&&sex.equals("未填写")&&address.equals("未填写")&&contact.equals("未填写")&&
                        relative.equals("未填写")&&relative_contact.equals("未填写")&&relation.equals("未填写")&&areaId.equals("未填写")&&notes.equals("未填写")){
                    Toast.makeText(this, "请填写信息", Toast.LENGTH_SHORT).show();
                }else if(name.equals("未填写")||id.equals("未填写")||sex.equals("未填写")||address.equals("未填写")||contact.equals("未填写")||
                        relative.equals("未填写")||relative_contact.equals("未填写")||relation.equals("未填写")||areaId.equals("未填写")||notes.equals("未填写")){
                    Toast.makeText(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "上传成功请返回", Toast.LENGTH_LONG).show();
                    OkUtils.UploadSJ(HttpMode.HTTPURL+HttpMode.CUSTOMER,map,this);
                    Log.e("上传的信息------------", name+sex+id+contact+address+relative+relative_contact+notes+areaId+relation);
                    Log.e("relation----------",relation);
                Log.e("token----------",HttpMode.getTOKEN(this));
                    finish();
                }
                break;
            case R.id.eFinish:
                finish();
                break;
            case R.id.l1:
                CustomDialog dialog1 = new CustomDialog(this,"姓名",null,false);
                dialog1.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs1.setText(inputString);
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog1.show();

                break;
            case R.id.l2:
                initDog();
                break;
            case R.id.l3:
                CustomDialog dialog2 = new CustomDialog(this,"身份证",null,true);

                dialog2.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs3.setText(inputString);
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog2.show();

                break;
            case R.id.l4:
                Intent intent=new Intent(EnterActivity.this,RegionActivity.class);
                startActivityForResult(intent,0);

                break;
            case R.id.l5:
                CustomDialog dialog5 = new CustomDialog(this,"与学生关系",null,false);
                dialog5.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs5.setText(inputString);
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog5.show();

                break;
            case R.id.l6:
                CustomDialog dialog6 = new CustomDialog(this,"联系方式",null,true);
                dialog6.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs6.setText(inputString);
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog6.show();

                break;
            case R.id.l7:
                CustomDialog dialog7 = new CustomDialog(this,"家庭住址",null,false);
                dialog7.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs7.setText(inputString);
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog7.show();

                break;
            case R.id.l8:
                CustomDialog dialog8 = new CustomDialog(this,"家长姓名",null,false);
                dialog8.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs8.setText(inputString );
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog8.show();

                break;
            case R.id.l9:
                CustomDialog dialog9 = new CustomDialog(this,"联系方式",null,true);
                dialog9.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs9.setText(inputString);
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog9.show();

                break;
            case R.id.l12:
                CustomDialog dialog12 = new CustomDialog(this,"详细概况",null,false);
                dialog12.setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onInputLegitimacy(final String inputString) {
                        tvs12.setText(inputString );
                    }

                    @Override
                    public void onInputIllegal() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                        alertDialog.setTitle("提示:");
                        alertDialog.setMessage(" 内容不能为空");
                        alertDialog.setPositiveButton("ok", null);
                        alertDialog.show();
                    }
                });
                dialog12.show();

                break;

        }
    }
    private void initDog() {

        CustomDog dialog1 = new CustomDog(this);
        dialog1.setOnDialogClickListeners(new CustomDog.OnDialogClickListeners() {
            @Override
            public void onInputLegitimacy(final String inputString) {
                tvs2.setText(inputString );
            }

            @Override
            public void onInputIllegal() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterActivity.this);
                alertDialog.setTitle("提示:");
                alertDialog.setMessage(" 内容不能为空");
                alertDialog.setPositiveButton("ok", null);
                alertDialog.show();
            }
        });
        dialog1.show();

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
       String s = response.body().string();
        Log.e("请求成功------------------",s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==99){
            String s=data.getStringExtra("area");
            area_id=data.getIntExtra("area_id",1);
            tvs4.setText(s);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
