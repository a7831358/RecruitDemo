package com.example.recruitdemo.updateversion;

import java.io.File;
import java.lang.reflect.Method;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.recruitdemo.R;

/**
 * @author wenjie 版本更新的工具类
 */
public class UpdateVersionUtil {

    /**
     * 接口回调
     *
     * @author wenjie
     */
    public interface UpdateListener {
        void onUpdateReturned(int updateStatus, VersionInfo versionInfo);
    }

    public UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    /**
     * 网络测试 检测版本
     *
     * @param context 上下文
     */
    public static void checkVersion(final Context context, final UpdateListener updateListener) {
        HttpRequest.get(ServerReqAddress.UPDATA_VERSION_REQ, new HttpRequest.RequestCallBackListener() {

            @Override
            public void onSuccess(String resultData) {
                try {
                    JSONObject jsonObject = JsonUtil.stringToJson(resultData);
                    JSONObject array = jsonObject.getJSONObject("data");
                    //array.getJSONObject(0).toString()
                    VersionInfo mVersionInfo = JsonUtil.jsonToBean(array.toString(), VersionInfo.class);
                    mVersionInfo.setDownloadUrl("http://223.72.255.100/MOA/static/apk/moa.apk");
                    int clientVersionCode = ApkUtils.getVersionCode(context);
                    //int serverVersionCode = mVersionInfo.getVersionCode();
                    String id = mVersionInfo.getId();
//					String url=mVersionInfo.getDownloadUrl();
                    int id1 = Integer.valueOf(id);
                    //有新版本
                    if (clientVersionCode < id1) {
                        int i = NetworkUtil.checkedNetWorkType(context);
                        if (i == NetworkUtil.NOWIFI) {
                            updateListener.onUpdateReturned(UpdateStatus.NOWIFI, mVersionInfo);
                        } else if (i == NetworkUtil.WIFI) {
                            updateListener.onUpdateReturned(UpdateStatus.YES, mVersionInfo);
                        }
                    } else {
                        //无新本
                        updateListener.onUpdateReturned(UpdateStatus.NO, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("err", "" + e.getLocalizedMessage());
                    updateListener.onUpdateReturned(UpdateStatus.ERROR, null);
                }
            }

            @Override
            public void onFailure(String error) {
                updateListener.onUpdateReturned(UpdateStatus.TIMEOUT, null);
            }
        });
    }


    /**
     * 本地测试
     */
//	public static void localCheckedVersion(final Context context,final UpdateListener updateListener){
//		try {
////			JSONObject jsonObject = JsonUtil.stringToJson(resultData);
////			JSONArray array = jsonObject.getJSONArray("data");
////			VersionInfo mVersionInfo = JsonUtil.jsonToBean(array.getJSONObject(0).toString(), VersionInfo.class);
//			VersionInfo mVersionInfo = new VersionInfo();
//			mVersionInfo.setDownloadUrl("http://gdown.baidu.com/data/wisegame/57a788487345e938/QQ_358.apk");
//			mVersionInfo.setVersionDesc("\n更新内容：\n1、增加xxxxx功能\n2、增加xxxx显示！\n3、用户界面优化！\n4、xxxxxx！");
//			mVersionInfo.setVersionCode(2);
//			mVersionInfo.setVersionName("v2020");
//			mVersionInfo.setVersionSize("20.1M");
//			mVersionInfo.setId("1");
//			int clientVersionCode = ApkUtils.getVersionCode(context);
//			int serverVersionCode = mVersionInfo.getVersionCode();
//			//有新版本
//			if(clientVersionCode < serverVersionCode){
//				int i = NetworkUtil.checkedNetWorkType(context);
//				if(i == NetworkUtil.NOWIFI){
//					updateListener.onUpdateReturned(UpdateStatus.NOWIFI,mVersionInfo);
//				}else if(i == NetworkUtil.WIFI){
//					updateListener.onUpdateReturned(UpdateStatus.YES,mVersionInfo);
//				}
//			}else{
//				//无新本
//				updateListener.onUpdateReturned(UpdateStatus.NO,null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			updateListener.onUpdateReturned(UpdateStatus.ERROR,null);
//		}
//	}
//

    /**
     * 弹出新版本提示
     *
     * @param context     上下文
     * @param versionInfo 更新内容
     */
    public static void showDialog(final Context context, final VersionInfo versionInfo) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        final File file = new File(SDCardUtils.getRootDirectory() + "/updateVersion/" + versionInfo.getName());
        dialog.setCancelable(true);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);//
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        dialog.setContentView(view);

        final Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_update_content);
        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
        final TextView tvUpdateMsgSize = (TextView) view.findViewById(R.id.tv_update_msg_size);
        final TextView tv_update_content = (TextView) view.findViewById(R.id.tv_update_content);
        //tvContent.setText(versionInfo.getVersionDesc());
        tvUpdateTile.setText("最新版本：" + versionInfo.getVersion());
        tv_update_content.setText("\n更新内容：\n " + versionInfo.getNotes());
        if (file.exists() && file.getName().equals(versionInfo.getName())) {
            tvUpdateMsgSize.setVisibility(View.VISIBLE);
            tvUpdateMsgSize.setText("新版本已经下载，是否安装？");

        } else {
            tvUpdateMsgSize.setVisibility(View.GONE);
        }

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.btn_update_id_ok) {
                    //新版本已经下载
                    if (file.exists() && file.getName().equals(versionInfo.getName())) {
                        Intent intent = ApkUtils.getInstallIntent(file);
                        context.startActivity(intent);
                    } else {
                        //没有下载，则开启服务下载新版本
                        Intent intent = new Intent(context, UpdateVersionService.class);
                        intent.putExtra("downloadUrl", versionInfo.getDownloadUrl());
                        intent.putExtra("name", versionInfo.getName());
                        context.startService(intent);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 弹出新版本提示
     *
     * @param context     上下文
     * @param versionInfo 更新内容
     */
    public static void showOnWifiDialog(final Context context, final VersionInfo versionInfo) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        final File file = new File(SDCardUtils.getRootDirectory() + "/updateVersion/" + versionInfo.getName());
        dialog.setCancelable(true);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);//
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        dialog.setContentView(view);

        final Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_update_content);
        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
        final TextView tvUpdateMsgSize = (TextView) view.findViewById(R.id.tv_update_msg_size);
        final TextView tv_update_content = (TextView) view.findViewById(R.id.tv_update_content);
        //tvContent.setText(versionInfo.getVersionDesc());
        tvUpdateTile.setText("最新版本：" + versionInfo.getVersion());
        tv_update_content.setText("\n更新内容：\n " + versionInfo.getNotes());
        if (file.exists() && file.getName().equals(versionInfo.getName())) {
            tvUpdateMsgSize.setVisibility(View.VISIBLE);
            tvUpdateMsgSize.setText("新版本已经下载，是否安装？");

        } else {
            tvUpdateMsgSize.setVisibility(View.GONE);
        }

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_update_id_ok) {
                    //新版本已经下载
                    if (file.exists() && file.getName().equals(versionInfo.getName())) {
                        Intent intent = ApkUtils.getInstallIntent(file);
                        context.startActivity(intent);
                    } else {
                        //没有下载，则开启服务下载新版本
                        dialog.dismiss();
                        new AlertDialog.Builder(context).setIcon(android.R.drawable.btn_star)
                                .setTitle("温馨提示").setMessage("当前非wifi网络,下载会消耗手机流量!")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                            Intent intent = new Intent(context, UpdateVersionService.class);
                                            intent.putExtra("downloadUrl", versionInfo.getDownloadUrl());
                                            intent.putExtra("name", versionInfo.getName());
                                            context.startService(intent);

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();// show很关键
                    }
                }

            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 收起通知栏
     *
     * @param context
     */
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
