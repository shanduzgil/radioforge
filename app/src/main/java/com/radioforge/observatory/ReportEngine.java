package com.radioforge.observatory;

import android.content.Context;

import java.io.*; import java.nio.charset.StandardCharsets; import java.nio.file.*; import java.util.*; import java.util.zip.*;

final class ReportEngine {
    static File latestSession(ContextLike c){return c.getLatest();}
    static File exportZip(java.io.File session, java.io.File out) throws IOException{
        try(ZipOutputStream z=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(out)))){
            add(z,session,"session.jsonl"); add(z,html(session),"report.html"); add(z,readme(),"README.txt");
        } return out;
    }
    private static void add(ZipOutputStream z, byte[] data,String name)throws IOException{z.putNextEntry(new ZipEntry(name));z.write(data);z.closeEntry();}
    private static void add(ZipOutputStream z,File f,String name)throws IOException{z.putNextEntry(new ZipEntry(name));try(InputStream in=new FileInputStream(f)){in.transferTo(z);}z.closeEntry();}
    private static byte[] html(File f)throws IOException{String s=new String(Files.readAllBytes(f.toPath()),StandardCharsets.UTF_8); long n=s.lines().count(); String h="<!doctype html><meta charset='utf-8'><title>RADIOFORGE Report</title><style>body{font-family:system-ui;background:#080b12;color:#eef}main{max-width:1100px;margin:40px auto;padding:24px}pre{white-space:pre-wrap;background:#111725;padding:20px;border-radius:16px}</style><main><h1>RADIOFORGE</h1><p>Session samples: "+n+"</p><p>Raw telemetry stays in this archive. This report is generated offline.</p><pre>"+Json.esc(s.substring(0,Math.min(s.length(),20000))).replace("&","&amp;").replace("<","&lt;")+"</pre></main>";return h.getBytes(StandardCharsets.UTF_8);}
    private static byte[] readme(){return "RADIOFORGE offline export\nContains session.jsonl and a local HTML summary.\nNo cloud upload is performed by the app.\n".getBytes(StandardCharsets.UTF_8);}
    interface ContextLike{File getLatest();}
}
