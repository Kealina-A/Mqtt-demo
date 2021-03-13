package com.example.mqttdemo.util;


import cn.hutool.core.codec.Base64;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SslUtil {

    public static SocketFactory getSocketFactory() throws Exception {
        //ssl 连接 , 这里的 TrustManager 是自己实现的，没有去校验服务端的证书
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new MyTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        return sc.getSocketFactory();
    }

    //MyTM 是自己实现的认证管理类，里面并有校验服务端的证书就返回true,永久成功！
    static class MyTM implements TrustManager, X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }

    public static SSLSocketFactory getSocketFactory(String caPath, String crtPath, String keyPath, String password) throws Exception {
        // CA certificate is used to authenticate server
        CertificateFactory cAf = CertificateFactory.getInstance("X.509");
        FileInputStream caIn = new FileInputStream(caPath);
        X509Certificate ca = (X509Certificate) cAf.generateCertificate(caIn);
        KeyStore caKs = KeyStore.getInstance("JKS");
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", ca);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(caKs);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream crtIn = new FileInputStream(crtPath);
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(crtIn);

        crtIn.close();
        // client key and certificates are sent to server so it can authenticate
        // us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//      ks.load(caIn,password.toCharArray());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", caCert);
        ks.setKeyEntry("private-key", getPrivateKey(keyPath), password.toCharArray(),
                new java.security.cert.Certificate[]{caCert}  );
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(ks, password.toCharArray());
//      keyIn.close();

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1");

//        context.init(kmf.getKeyManagers(),tmf.getTrustManagers(), new SecureRandom());
        context.init(null,tmf.getTrustManagers(), new SecureRandom());

        return context.getSocketFactory();
    }

    private static PrivateKey getPrivateKey(String path) throws Exception{

        Base64 base64=new Base64();
        byte[] buffer=   base64.decode(getPem(path));

        PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);


    }


    private static String getPem(String path) throws Exception{
        FileInputStream fin=new FileInputStream(path);
        BufferedReader br= new BufferedReader(new InputStreamReader(fin));
        String readLine= null;
        StringBuilder sb= new StringBuilder();
        while((readLine= br.readLine())!=null){
            if(readLine.charAt(0)=='-'){
                continue;
            }else{
                sb.append(readLine);
                sb.append('\r');
            }
        }
        fin.close();
        return sb.toString();
    }

}
