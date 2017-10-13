package com.lizheblogs.launchapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";
    private android.widget.TextView dateTV;
    private android.widget.Button dateSelectBut;
    private android.widget.TextView timeTV;
    private android.widget.Button timeSelectBut;
    private android.widget.Spinner appSpinner;
    private android.widget.ListView dataLV;
    private android.widget.Button AddBut;
    private Calendar selected;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat formatTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dataLV = (ListView) findViewById(R.id.dataLV);
        this.appSpinner = (Spinner) findViewById(R.id.appSpinner);
        this.timeSelectBut = (Button) findViewById(R.id.timeSelectBut);
        this.timeSelectBut.setOnClickListener(this);
        this.timeTV = (TextView) findViewById(R.id.timeTV);
        this.dateSelectBut = (Button) findViewById(R.id.dateSelectBut);
        this.dateSelectBut.setOnClickListener(this);
        this.dateTV = (TextView) findViewById(R.id.dateTV);
        this.AddBut = (Button) findViewById(R.id.AddBut);
        this.AddBut.setOnClickListener(this);

        formatDate = new SimpleDateFormat(YYYY_MM_DD);
        formatTime = new SimpleDateFormat(HH_MM_SS);
        selected = Calendar.getInstance();
        Date date = selected.getTime();
        timeTV.setText(formatTime.format(date));
        dateTV.setText(formatDate.format(date));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddBut:
                getPackages();
                break;
            case R.id.dateSelectBut:
                showDatePicker();
                break;
            case R.id.timeSelectBut:
                showTimePicker();
                break;
            default:
                break;
        }
    }


    public void showDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selected.set(year, monthOfYear, dayOfMonth);
                dateTV.setText(formatDate.format(selected.getTime()));
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void showTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selected.set(Calendar.MINUTE, minute);
                timeTV.setText(formatTime.format(selected.getTime()));
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
    }

    private void getPackages() {
        PackageManager pm = getPackageManager();
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
            if (apps == null || apps.size() == 0) {
                continue;
            }
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String startappName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Log.e("===","id=="+i);
                Log.e("===","packageInfo.packageName=="+packageInfo.packageName);
                Log.e("===","packageInfo.icon=="+packageInfo.applicationInfo.loadIcon(pm));
                System.out.println("启动的activity是: " + startappName + ":" + className);


            }
        }
    }


    public static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 指定时间后进行更新赛事信息(有如闹钟的设置)
     * 注意: Receiver记得在manifest.xml中注册
     *
     * @param ctx
     */
    public static void sendUpdateBroadcast(Context ctx, long time) {
        AlarmManager am = getAlarmManager(ctx);
        // 60秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
        Intent i = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.set(AlarmManager.RTC, time, pendingIntent);
    }

    /**
     * 取消定时执行(有如闹钟的取消)
     *
     * @param ctx
     */
    public static void cancelUpdateBroadcast(Context ctx) {
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.cancel(pendingIntent);
    }


}
