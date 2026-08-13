package com.radioforge.observatory;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.*;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

final class Collector {
    private final Context ctx;
    private final TelephonyManager tm;
    private final ConnectivityManager cm;
    private final WifiManager wm;
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    Collector(Context c){ctx=c.getApplicationContext(); tm=(TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE); cm=(ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE); wm=(WifiManager)ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);}
    RadioSnapshot collect(){
        RadioSnapshot s=new RadioSnapshot(); s.timestampMs=System.currentTimeMillis();
        if(tm!=null){ try{s.operator=String.valueOf(tm.getNetworkOperatorName());}catch(Exception ignored){} try{s.networkType=typeName(tm.getDataNetworkType());}catch(Exception ignored){} try{s.isoCountry=String.valueOf(tm.getNetworkCountryIso());}catch(Exception ignored){} try{s.radioTech=typeName(tm.getDataNetworkType());}catch(Exception ignored){} }
        if(cm!=null){try{Network n=cm.getActiveNetwork(); if(n!=null){NetworkCapabilities nc=cm.getNetworkCapabilities(n); s.transport=transport(nc); LinkProperties lp=cm.getLinkProperties(n); if(lp!=null){ for(LinkAddress la:lp.getLinkAddresses()){String ip=la.getAddress().getHostAddress(); if(ip!=null&&!ip.contains(":")){s.localIp=ip;break;}} }}}catch(Exception ignored){}}
        if(wm!=null){try{WifiInfo wi=wm.getConnectionInfo(); if(wi!=null){s.ssid=String.valueOf(wi.getSSID());}}catch(Exception ignored){}}
        if(hasFineLocation()){try{List<CellInfo> list=tm.getAllCellInfo(); if(list!=null)for(CellInfo c:list)s.cells.add(CellParser.parse(c));}catch(Exception ignored){}}
        return s;
    }
    private boolean hasFineLocation(){return Build.VERSION.SDK_INT<23 || ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;}
    private static String transport(NetworkCapabilities n){if(n==null)return "unknown"; if(n.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))return "CELLULAR"; if(n.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))return "WIFI"; if(n.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))return "ETHERNET"; return "OTHER";}
    static String typeName(int t){switch(t){case TelephonyManager.NETWORK_TYPE_LTE:return "LTE"; case TelephonyManager.NETWORK_TYPE_NR:return "5G_NR"; case TelephonyManager.NETWORK_TYPE_HSPA:return "HSPA"; case TelephonyManager.NETWORK_TYPE_HSPAP:return "HSPA+"; case TelephonyManager.NETWORK_TYPE_EDGE:return "EDGE"; case TelephonyManager.NETWORK_TYPE_GPRS:return "GPRS"; case TelephonyManager.NETWORK_TYPE_UMTS:return "UMTS"; case TelephonyManager.NETWORK_TYPE_GSM:return "GSM"; default:return "TYPE_"+t;}}
}
