package com.radioforge.observatory;

import android.os.Build;
import android.telephony.*;
import java.util.*;

final class CellParser {
    private CellParser() {}
    static Map<String,String> parse(CellInfo c) {
        Map<String,String> m = new LinkedHashMap<>();
        m.put("registered", Boolean.toString(c.isRegistered()));
        m.put("timestamp_ns", Long.toString(c.getTimeStamp()));
        if (c instanceof CellInfoLte) parseLte((CellInfoLte)c,m);
        else if (Build.VERSION.SDK_INT >= 29 && c instanceof CellInfoNr) parseNr((CellInfoNr)c,m);
        else if (c instanceof CellInfoWcdma) parseWcdma((CellInfoWcdma)c,m);
        else if (c instanceof CellInfoGsm) parseGsm((CellInfoGsm)c,m);
        else if (c instanceof CellInfoCdma) parseCdma((CellInfoCdma)c,m);
        else m.put("type",c.getClass().getSimpleName());
        return m;
    }
    private static void parseLte(CellInfoLte c, Map<String,String> m) {
        CellIdentityLte id=c.getCellIdentity(); CellSignalStrengthLte s=c.getCellSignalStrength();
        m.put("type","LTE"); m.put("mcc", Integer.toString(id.getMcc())); m.put("mnc",Integer.toString(id.getMnc()));
        m.put("ci",Integer.toString(id.getCi())); m.put("pci",Integer.toString(id.getPci())); m.put("tac",Integer.toString(id.getTac()));
        m.put("earfcn",Integer.toString(id.getEarfcn())); m.put("band",Build.VERSION.SDK_INT>=28?Arrays.toString(id.getBands()):"");
        m.put("rsrp",Integer.toString(s.getRsrp())); m.put("rsrq",Integer.toString(s.getRsrq())); m.put("rssi",Integer.toString(s.getRssi()));
        m.put("rssnr",Integer.toString(s.getRssnr())); m.put("timing_advance",Integer.toString(s.getTimingAdvance()));
    }
    private static void parseNr(CellInfoNr c, Map<String,String> m) {
        CellIdentityNr id=(CellIdentityNr)c.getCellIdentity(); CellSignalStrengthNr s=(CellSignalStrengthNr)c.getCellSignalStrength();
        m.put("type","NR"); m.put("mcc",id.getMccString()); m.put("mnc",id.getMncString()); m.put("nci",Long.toString(id.getNci()));
        m.put("pci",Integer.toString(id.getPci())); m.put("tac",Integer.toString(id.getTac())); m.put("nrarfcn",Integer.toString(id.getNrarfcn()));
        if(Build.VERSION.SDK_INT>=29){m.put("ss_rsrp",Integer.toString(s.getSsRsrp()));m.put("ss_rsrq",Integer.toString(s.getSsRsrq()));m.put("ss_sinr",Integer.toString(s.getSsSinr()));}
    }
    private static void parseWcdma(CellInfoWcdma c, Map<String,String> m){m.put("type","WCDMA"); CellIdentityWcdma id=c.getCellIdentity(); CellSignalStrengthWcdma s=c.getCellSignalStrength(); m.put("cid",Integer.toString(id.getCid())); m.put("lac",Integer.toString(id.getLac())); m.put("psc",Integer.toString(id.getPsc())); m.put("uarfcn",Integer.toString(id.getUarfcn())); m.put("dbm",Integer.toString(s.getDbm()));}
    private static void parseGsm(CellInfoGsm c, Map<String,String> m){m.put("type","GSM"); CellIdentityGsm id=c.getCellIdentity(); CellSignalStrengthGsm s=c.getCellSignalStrength(); m.put("cid",Integer.toString(id.getCid()));m.put("lac",Integer.toString(id.getLac()));m.put("arfcn",Integer.toString(id.getArfcn()));m.put("bsic",Integer.toString(id.getBsic()));m.put("dbm",Integer.toString(s.getDbm()));}
    private static void parseCdma(CellInfoCdma c, Map<String,String> m){m.put("type","CDMA"); CellIdentityCdma id=c.getCellIdentity(); CellSignalStrengthCdma s=c.getCellSignalStrength(); m.put("base_station_id",Integer.toString(id.getBasestationId()));m.put("network_id",Integer.toString(id.getNetworkId()));m.put("system_id",Integer.toString(id.getSystemId()));m.put("dbm",Integer.toString(s.getCdmaDbm()));}
}
