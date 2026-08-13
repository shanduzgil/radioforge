package com.radioforge.observatory;

import android.app.*; import android.content.*; import android.os.*; import java.io.*; import java.util.concurrent.*;

public class CaptureService extends Service {
    private static final String CHANNEL="radioforge_capture"; private ScheduledExecutorService exec; private Collector collector; private File logFile; private long count=0;
    @Override public void onCreate(){super.onCreate(); collector=new Collector(this); createChannel(); startForeground(1201, notification("RADIOFORGE capture is running"));}
    private void createChannel(){NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); if(Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(new NotificationChannel(CHANNEL,"RADIOFORGE Capture",NotificationManager.IMPORTANCE_LOW));}
    private Notification notification(String text){Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,CHANNEL):new Notification.Builder(this); return b.setSmallIcon(com.radioforge.observatory.R.drawable.ic_launcher).setContentTitle("RADIOFORGE").setContentText(text).setOngoing(true).build();}
    @Override public int onStartCommand(Intent i,int flags,int startId){ if(exec==null){exec=Executors.newSingleThreadScheduledExecutor(); File dir=new File(getFilesDir(),"sessions");dir.mkdirs();logFile=new File(dir,"session_"+System.currentTimeMillis()+".jsonl"); exec.scheduleAtFixedRate(this::sample,0,5,TimeUnit.SECONDS);} return START_STICKY;}
    private void sample(){try{RadioSnapshot s=collector.collect(); try(FileWriter w=new FileWriter(logFile,true)){w.write(s.toJsonLine());w.write('\n');} count++; Intent x=new Intent("com.radioforge.observatory.SAMPLE");x.setPackage(getPackageName());x.putExtra("count",count);x.putExtra("network",s.networkType);x.putExtra("operator",s.operator);x.putExtra("cells",s.cells.size());sendBroadcast(x);}catch(Exception ignored){}}
    @Override public void onDestroy(){if(exec!=null)exec.shutdownNow(); super.onDestroy();}
    @Override public android.os.IBinder onBind(Intent intent){return null;}
    public File getLogFile(){return logFile;}
}
