package com.radioforge.observatory;

import android.Manifest; import android.app.*; import android.content.*; import android.content.pm.PackageManager; import android.graphics.*; import android.net.Uri; import android.os.*; import android.provider.Settings; import android.view.*; import android.widget.*; import java.io.*; import java.util.*;

public class MainActivity extends Activity {
    static final int REQ=41,CREATE=42; DashboardView dash; TextView status; BroadcastReceiver receiver; File lastSession;
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(8,11,18));getWindow().setNavigationBarColor(Color.rgb(8,11,18)); build();}
    private void build(){LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,26,28,18);root.setBackgroundColor(Color.rgb(8,11,18));
        TextView title=t("RADIOFORGE",30,Color.WHITE); root.addView(title,new LinearLayout.LayoutParams(-1,60));
        TextView sub=t("PRIVATE NETWORK OBSERVATORY",12,Color.rgb(139,150,174));root.addView(sub,new LinearLayout.LayoutParams(-1,35));
        dash=new DashboardView(this); root.addView(dash,new LinearLayout.LayoutParams(-1,0,1));
        status=t("Ready — grant location + phone permissions to collect CellInfo.",13,Color.rgb(180,190,210)); root.addView(status,new LinearLayout.LayoutParams(-1,55));
        LinearLayout buttons=new LinearLayout(this); buttons.setGravity(Gravity.CENTER); Button perm=btn("PERMISSIONS"), start=btn("START"), stop=btn("STOP"), exp=btn("EXPORT"); buttons.addView(perm);buttons.addView(start);buttons.addView(stop);buttons.addView(exp);root.addView(buttons,new LinearLayout.LayoutParams(-1,60)); setContentView(root);
        perm.setOnClickListener(v->requestNeeded()); start.setOnClickListener(v->startCapture()); stop.setOnClickListener(v->stopCapture()); exp.setOnClickListener(v->export());
        receiver=new BroadcastReceiver(){public void onReceive(Context c,Intent i){status.setText("Samples: "+i.getLongExtra("count",0)+" • "+i.getStringExtra("operator")+" • "+i.getStringExtra("network")+" • cells="+i.getIntExtra("cells",0)); dash.set(i.getStringExtra("network"),i.getStringExtra("operator"),i.getIntExtra("cells",0));}}; registerReceiver(receiver,new IntentFilter("com.radioforge.observatory.SAMPLE"),Context.RECEIVER_NOT_EXPORTED);
        requestNeeded();
    }
    private TextView t(String s,int sp,int color){TextView v=new TextView(this);v.setText(s);v.setTextSize(sp);v.setTextColor(color);v.setGravity(Gravity.CENTER_VERTICAL);return v;}
    private Button btn(String s){Button b=new Button(this);b.setText(s);return b;}
    private void requestNeeded(){List<String> p=new ArrayList<>(); if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)p.add(Manifest.permission.ACCESS_FINE_LOCATION); if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)p.add(Manifest.permission.POST_NOTIFICATIONS); if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)p.add(Manifest.permission.READ_PHONE_STATE); if(!p.isEmpty())requestPermissions(p.toArray(new String[0]),REQ);}
    private boolean ready(){return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED;}
    private void startCapture(){if(!ready()){status.setText("Permissions required.");return;} Intent i=new Intent(this,CaptureService.class); if(Build.VERSION.SDK_INT>=26)startForegroundService(i); else startService(i); status.setText("Capture started. Keep the session running while you map.");}
    private void stopCapture(){stopService(new Intent(this,CaptureService.class)); status.setText("Capture stopped. Session remains local.");}
    private void export(){File dir=new File(getFilesDir(),"sessions");File[] a=dir.listFiles((d,n)->n.endsWith(".jsonl")); if(a==null||a.length==0){status.setText("No session to export.");return;}Arrays.sort(a,Comparator.comparingLong(File::lastModified).reversed());lastSession=a[0];Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.setType("application/zip");i.putExtra(Intent.EXTRA_TITLE,"radioforge-session.zip");startActivityForResult(i,CREATE);}
    @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==CREATE&&c==RESULT_OK&&d!=null){try{java.io.File tmp=new File(getCacheDir(),"export.zip");ReportEngine.exportZip(lastSession,tmp);try(java.io.InputStream in=new FileInputStream(tmp);java.io.OutputStream out=getContentResolver().openOutputStream(d.getData())){in.transferTo(out);}status.setText("Export complete.");}catch(Exception e){status.setText("Export failed: "+e.getMessage());}}}
    @Override protected void onDestroy(){unregisterReceiver(receiver);super.onDestroy();}

    static class DashboardView extends View {Paint p=new Paint(3);String net="—",op="—";int cells=0;DashboardView(android.content.Context c){super(c);p.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));}
        void set(String n,String o,int c){net=n;op=o;cells=c;invalidate();}
        protected void onDraw(Canvas c){super.onDraw(c);p.setColor(Color.rgb(17,23,37));c.drawRoundRect(0,0,getWidth(),getHeight()-15,28,28,p);p.setColor(Color.rgb(53,182,255));p.setTextSize(54);c.drawText("●",38,90,p);p.setColor(Color.WHITE);p.setTextSize(32);c.drawText(net,100,92,p);p.setTextSize(18);p.setColor(Color.rgb(139,150,174));c.drawText(op,100,122,p);p.setColor(Color.rgb(73,227,138));p.setTextSize(22);c.drawText("CELLS OBSERVED: "+cells,38,190,p);p.setColor(Color.rgb(155,108,255));c.drawRoundRect(38,220,getWidth()-38,250,15,15,p);p.setColor(Color.rgb(53,182,255));c.drawRoundRect(38,220,Math.max(38,getWidth()-150),250,15,15,p);p.setColor(Color.rgb(242,246,255));p.setTextSize(16);c.drawText("Offline-first • privacy-first • exportable",38,300,p);}
    }
}
