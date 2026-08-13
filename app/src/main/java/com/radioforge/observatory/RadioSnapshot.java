package com.radioforge.observatory;

import android.telephony.*;
import java.util.*;

final class RadioSnapshot {
    long timestampMs;
    String operator = "unknown";
    String simOperator = "unknown";
    String networkType = "unknown";
    String isoCountry = "";
    String radioTech = "unknown";
    String publicIp = "";
    String pingMs = "";
    String transport = "";
    String localIp = "";
    String ssid = "";
    final List<Map<String,String>> cells = new ArrayList<>();

    String toJsonLine() {
        StringBuilder b = new StringBuilder(2048);
        b.append('{');
        field(b,"timestamp_ms",Long.toString(timestampMs),false);
        field(b,"operator",operator,true); field(b,"sim_operator",simOperator,true);
        field(b,"network_type",networkType,true); field(b,"iso_country",isoCountry,true);
        field(b,"radio_technology",radioTech,true); field(b,"public_ip",publicIp,true);
        field(b,"ping_ms",pingMs,true); field(b,"transport",transport,true);
        field(b,"local_ip",localIp,true); field(b,"ssid",ssid,true);
        b.append(",\"cells\":[");
        for (int i=0;i<cells.size();i++) { if(i>0)b.append(','); Map<String,String> c=cells.get(i); b.append('{'); int j=0; for(var e:c.entrySet()){if(j++>0)b.append(','); field(b,e.getKey(),e.getValue(),true);} b.append('}'); }
        b.append("]}");
        return b.toString();
    }
    private void field(StringBuilder b,String k,String v,boolean quote){
        if(b.length()>1 && b.charAt(b.length()-1)!='{' && b.charAt(b.length()-1)!='[') b.append(',');
        b.append(Json.q(k)).append(':').append(quote?Json.q(v):v);
    }
}
