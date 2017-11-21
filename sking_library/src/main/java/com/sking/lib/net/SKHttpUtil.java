package com.sking.lib.net;

import com.sking.lib.config.SKConfig;
import com.sking.lib.exception.SKDataParseException;
import com.sking.lib.exception.SKHttpException;
import com.sking.lib.utils.SKBaseUtil;
import com.sking.lib.utils.SKFileTypeUtil;
import com.sking.lib.utils.SKJsonUtil;
import com.sking.lib.utils.SKLogger;
import com.sking.lib.utils.SKMd5Util;
import com.sking.lib.utils.SKStreamUtil;
import com.sking.lib.utils.SKTimeUtil;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * 网络请求相关工具类
 */
public class SKHttpUtil {
    private static final String TAG = "SKHttpUtil";
    public static String sessionID = null;

    private static final String END = "\r\n";
    private static final String TWOHYPHENS = "--";
    private static final String BOUNDARY = "yztdhr";

    /**
     * 上传文件
     *
     * @param path     请求接口
     * @param files    文件集合(<参数名,文件路径>)
     * @param params   其他参数集合(<参数名,参数值>)
     * @param encoding 编码方式
     * @return JSONObject
     * @throws SKHttpException
     * @throws SKDataParseException
     */
    public static JSONObject sendPOSTWithFilesForJSONObject(String path,
                                                            HashMap<String, String> files, HashMap<String, String> params,
                                                            String encoding) throws SKDataParseException, SKHttpException {
        return SKJsonUtil.toJsonObject(path.contains("https://") ? sendPOSTWithFilesForStringHtttps(path, files, params, encoding) : sendPOSTWithFilesForString(path, files, params, encoding));
    }

    /**
     * 上传文件,HttpURLConnection
     *
     * @param path     文件上传接口
     * @param files    文件集合(<参数名,文件路径>)
     * @param params   其他参数集合
     * @param encoding 编码方式
     * @return String
     * @throws IOException
     */
    public static String sendPOSTWithFilesForString(String path,
                                                    HashMap<String, String> files, HashMap<String, String> params,
                                                    String encoding) throws SKHttpException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(path);
            SKLogger.d(TAG, "The HttpUrl is \n" + path);
            conn = (HttpURLConnection) url.openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            // 设置此参数后导致请求有时成功有时失败（网上说是服务器不支持,具体原因未明）
            // conn.setChunkedStreamingMode(0);// 128K 128 * 1024

            conn.setConnectTimeout(SKConfig.TIMEOUT_CONNECT_HTTP);
            conn.setReadTimeout(SKConfig.TIMEOUT_READ_FILE);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", encoding);
            if (sessionID != null)
                conn.setRequestProperty("Cookie", sessionID);// 设置cookie
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            // 参数
            writeParams(path, dos, params, encoding);
            // 文件
            writeFiles(dos, files);
            dos.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + END);
            dos.flush();
            dos.close();
            String cookie = conn.getHeaderField("set-cookie");
            if (cookie != null)
                sessionID = cookie.substring(0, cookie.indexOf(";"));// 获取sessionID

            String data = SKStreamUtil.iputStreamToString(get(conn));
            SKLogger.d(TAG, "The back data is \n" + data);
            if (conn != null)
                conn.disconnect();
            return data;
        } catch (Exception e) {
            if (conn != null)
                conn.disconnect();
            throw new SKHttpException(e);
        }

    }

    /**
     * 上传文件,HttpsURLConnection
     *
     * @param path     文件上传接口
     * @param files    文件集合(<参数名,文件路径>)
     * @param params   其他参数集合
     * @param encoding 编码方式
     * @return String
     * @throws IOException
     */
    public static String sendPOSTWithFilesForStringHtttps(String path,
                                                          HashMap<String, String> files, HashMap<String, String> params,
                                                          String encoding) throws SKHttpException {
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(path);
            SKLogger.d(TAG, "The HttpUrl is \n" + path);
            conn = (HttpsURLConnection) url.openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            // 设置此参数后导致请求有时成功有时失败（网上说是服务器不支持,具体原因未明）
            // conn.setChunkedStreamingMode(0);// 128K 128 * 1024

            conn.setConnectTimeout(SKConfig.TIMEOUT_CONNECT_HTTP);
            conn.setReadTimeout(SKConfig.TIMEOUT_READ_FILE);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", encoding);
            if (sessionID != null)
                conn.setRequestProperty("Cookie", sessionID);// 设置cookie

            verifyLocalCertificate(conn);//私有证书验证

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 参数
            writeParams(path, dos, params, encoding);
            // 文件
            writeFiles(dos, files);
            dos.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + END);
            dos.flush();
            dos.close();
            String cookie = conn.getHeaderField("set-cookie");
            if (cookie != null)
                sessionID = cookie.substring(0, cookie.indexOf(";"));// 获取sessionID

            String data = SKStreamUtil.iputStreamToString(get(conn));
            SKLogger.d(TAG, "The back data is \n" + data);
            if (conn != null)
                conn.disconnect();
            return data;
        } catch (Exception e) {
            if (conn != null)
                conn.disconnect();
            throw new SKHttpException(e);
        }

    }


    /**
     * 处理文件集
     *
     * @param dos   数据输出流
     * @param files 文件集
     * @throws IOException
     */
    private static void writeFiles(DataOutputStream dos,
                                   HashMap<String, String> files) throws IOException {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            FileInputStream fStream = null;
            try {
                dos.writeBytes(TWOHYPHENS + BOUNDARY + END);
                dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                        + entry.getKey() + "\";filename=\"" + entry.getValue()
                        + "\"" + END);
                SKLogger.d(TAG, "The file path is \n" + entry.getValue());
                String filetype = SKFileTypeUtil.getFileTypeByPath(entry
                        .getValue());// 获取文件类型
                SKLogger.d(TAG, "The file type is " + filetype);
                dos.writeBytes("Content-type: " + filetype + END);
                dos.writeBytes(END);
                int bufferSize = 1024 * 10;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                File file = new File(entry.getValue());
                fStream = new FileInputStream(file);
                while ((length = fStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, length);
                }
                dos.writeBytes(END);
                // dos.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + END);
            } catch (IOException e) {
                throw e;
            } finally {
                if (fStream != null)
                    try {
                        fStream.close();
                    } catch (IOException e) {
                        throw e;
                    }
            }
        }
    }

    /**
     * 处理参数集
     *
     * @param dos      数据输出流
     * @param params   参数集
     * @param encoding 编码方式
     * @throws IOException
     */
    private static void writeParams(String path, DataOutputStream dos,
                                    HashMap<String, String> params, String encoding) throws IOException {
        StringBuilder data = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // 方便查看发送参数，无实际意义
                data.append(entry.getKey()).append("=");
                data.append(entry.getValue());
                data.append("&");
                if (entry.getValue() != null) {
                    dos.writeBytes(TWOHYPHENS + BOUNDARY + END);
                    dos.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + entry.getKey() + "\"" + END);
                    dos.writeBytes(END);
                    dos.write(entry.getValue().getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                    dos.writeBytes(END);
                    // dos.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + END);
                }
            }
            data.deleteCharAt(data.length() - 1);
            if (SKConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = SKTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                data.append("&").append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = SKMd5Util.getMd5(SKConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                data.append("sign=").append(sign);
                dos.writeBytes(TWOHYPHENS + BOUNDARY + END);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"datetime\"" + END);
                dos.writeBytes(END);
                dos.write(datetime.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(END);

                dos.writeBytes(TWOHYPHENS + BOUNDARY + END);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"sign\"" + END);
                dos.writeBytes(END);
                dos.write(sign.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(END);
            }

        } else {
            if (SKConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = SKTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                data.append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = SKMd5Util.getMd5(SKConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                data.append("sign=").append(sign);

                dos.writeBytes(TWOHYPHENS + BOUNDARY + END);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"datetime\"" + END);
                dos.writeBytes(END);
                dos.write(datetime.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(END);

                dos.writeBytes(TWOHYPHENS + BOUNDARY + END);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"sign\"" + END);
                dos.writeBytes(END);
                dos.write(sign.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(END);
            }
        }
        SKLogger.d(TAG, "The send data is \n" + data.toString());
    }

    /**
     * 发送POST请求
     *
     * @param id     请求id,2017.08.25 update
     * @param path   请求接口
     * @param params 发送参数集合(<参数名,参数值>)
     * @return JSONObject
     * @throws SKHttpException
     * @throws SKDataParseException
     */
    public static JSONObject sendPOSTForJSONObject(int id, String path,
                                                   HashMap<String, String> params, String encoding)
            throws SKDataParseException, SKHttpException {
//        if (SKConfig.IS_PERMISSION || id < 2)//第三方，系统初始化放过
        return SKJsonUtil.toJsonObject(sendPOSTForString(path, params, encoding));
//        else
//            return SKJsonUtil.toJsonObject("{\"success\":false,\"msg\":\"抱歉，密钥验证失败功能被限制！\",\"error_code\":201}");
    }

    /**
     * 发送POST请求
     *
     * @param path   请求接口
     * @param params 发送参数集合(<参数名,参数值>)
     * @return String
     * @throws SKHttpException
     * @throws IOException
     * @throws MalformedURLException
     */
    public static String sendPOSTForString(String path,
                                           HashMap<String, String> params, String encoding)
            throws SKHttpException {
        StringBuilder data = new StringBuilder();
        SKLogger.d(TAG, "The HttpUrl is \n" + path);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                data.append(entry.getKey()).append("=");
                String value;
                if (entry.getValue() != null) {
                    value = entry.getValue().replace("&", "%26"); // 转义&
                } else {
                    value = entry.getValue();
                }
                data.append(value);
                data.append("&");
            }
            data.deleteCharAt(data.length() - 1);
            if (SKConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = SKTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                data.append("&").append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = SKMd5Util.getMd5(SKConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                data.append("sign=").append(sign);
            }
        } else {
            if (SKConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = SKTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                data.append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = SKMd5Util.getMd5(SKConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                data.append("sign=").append(sign);
            }
        }
        SKLogger.d(TAG, "The send data is \n" + data.toString());

//        HttpURLConnection conn = null;
//        try {
//            byte[] entity = data.toString().getBytes();
//            conn = (HttpURLConnection) new URL(path).openConnection();
//            conn.setConnectTimeout(SKConfig.TIMEOUT_CONNECT_HTTP);
//            conn.setReadTimeout(SKConfig.TIMEOUT_READ_HTTP);
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            // conn.setChunkedStreamingMode(0);
//            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
//            conn.setUseCaches(false);
//            conn.setRequestProperty("Charset", encoding);
//            conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Content-Length",
//                    String.valueOf(entity.length));
//            if (sessionID != null)
//                conn.setRequestProperty("Cookie", sessionID);// 设置cookie
//
//            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
////            DataOutputStream dos = new DataOutputStream(getOutPutStream(path,encoding,entity));
//            dos.write(entity);
//            dos.flush();
//            dos.close();
//            String cookie = conn.getHeaderField("set-cookie");
//            if (cookie != null)
//                sessionID = cookie.substring(0, cookie.indexOf(";"));// 获取sessionID
//
//            int code = conn.getResponseCode();
//            SKLogger.d(TAG, "The responsecode is " + code);
//
//            InputStream in = (code == HttpURLConnection.HTTP_OK) ? conn
//                    .getInputStream() : null;
//
//            String indata = SKStreamUtil.iputStreamToString(in);
//            SKLogger.d(TAG, "The back data is \n" + indata);
//            if (conn != null)
//                conn.disconnect();
//            return indata;
//        } catch (Exception e) {
//            if (conn != null)
//                conn.disconnect();
//            throw new SKHttpException(e);
//        }

        //2017.09.13 change
        byte[] entity = data.toString().getBytes();

        if (path.contains("https://"))//url包含https走HttpsURLConnection，否则走普通HttpURLConnection
            return getHttpsURLConnection(path, encoding, entity);
        else
            return getHttpURLConnection(path, encoding, entity);
    }

    /**
     * 设置HttpURLConnection
     * <p>
     * 2017.09.13
     *
     * @return InputStream or null
     * @throws IOException
     */
    private static String getHttpURLConnection(String path, String encoding, byte[] entity) throws SKHttpException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(SKConfig.TIMEOUT_CONNECT_HTTP);
            conn.setReadTimeout(SKConfig.TIMEOUT_READ_HTTP);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // conn.setChunkedStreamingMode(0);
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setUseCaches(false);
            conn.setRequestProperty("Charset", encoding);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(entity.length));
            if (sessionID != null)
                conn.setRequestProperty("Cookie", sessionID);// 设置cookie

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(entity);
            dos.flush();
            dos.close();
            String cookie = conn.getHeaderField("set-cookie");
            if (cookie != null)
                sessionID = cookie.substring(0, cookie.indexOf(";"));// 获取sessionID

            int code = conn.getResponseCode();
            SKLogger.d(TAG, "The responsecode is " + code);

            InputStream in = (code == HttpURLConnection.HTTP_OK) ? conn
                    .getInputStream() : null;

            String indata = SKStreamUtil.iputStreamToString(in);
            SKLogger.d(TAG, "The back data is \n" + indata);
            if (conn != null)
                conn.disconnect();
            return indata;
        } catch (Exception e) {
            if (conn != null)
                conn.disconnect();
            throw new SKHttpException(e);
        }
    }

    /**
     * 设置HttpsURLConnection
     * <p>
     * 2017.09.13
     *
     * @return InputStream or null
     * @throws IOException
     */
    private static String getHttpsURLConnection(String path, String encoding, byte[] entity) throws SKHttpException {
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(SKConfig.TIMEOUT_CONNECT_HTTP);
            conn.setReadTimeout(SKConfig.TIMEOUT_READ_HTTP);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // conn.setChunkedStreamingMode(0);
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setUseCaches(false);
            conn.setRequestProperty("Charset", encoding);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(entity.length));
            if (sessionID != null)
                conn.setRequestProperty("Cookie", sessionID);// 设置cookie

            verifyLocalCertificate(conn);//私有证书验证，2017.09.13

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(entity);
            dos.flush();
            dos.close();
            String cookie = conn.getHeaderField("set-cookie");
            if (cookie != null)
                sessionID = cookie.substring(0, cookie.indexOf(";"));// 获取sessionID

            int code = conn.getResponseCode();
            SKLogger.d(TAG, "The responsecode is " + code);

            InputStream in = (code == HttpURLConnection.HTTP_OK) ? conn.getInputStream() : null;

            String indata = SKStreamUtil.iputStreamToString(in);
            SKLogger.d(TAG, "The back data is \n" + indata);
            if (conn != null)
                conn.disconnect();
            return indata;
        } catch (Exception e) {
            if (conn != null)
                conn.disconnect();
            throw new SKHttpException(e);
        }
    }

    /**
     * 获取服务器返回流
     *
     * @param conn 连接
     * @return InputStream or null
     * @throws IOException
     */
    private static InputStream get(HttpURLConnection conn) throws IOException {
        int code = conn.getResponseCode();
        SKLogger.d(TAG, "The responsecode is " + code);
        return (code == HttpURLConnection.HTTP_OK) ? conn.getInputStream() : null;
    }

    /**
     * 清除Session
     */
    public static void clearSession() {
        sessionID = null;
    }


    /*------------------------增加对https私有证书验证 2017.09.13------------------------------*/

    /*
    * 判断本地是否存在私有证书，若存在本地证书，则启用本地证书验证
    * */
    private static void verifyLocalCertificate(HttpsURLConnection conn) {
        try {
            if (SKBaseUtil.getLocalCRT() != null) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagers, null);
                conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setHostnameVerifier(hostnameVerifier);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /*
     *  私有证书验证，trustManagers
     * */
    static TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    if (chain == null)
                        throw new IllegalArgumentException("Check server x509Certificates is null");
                    if (chain.length < 0)
                        throw new IllegalArgumentException("Check server x509Certificates is empty");
                    for (X509Certificate cert : chain) {
                        cert.checkValidity();//检查服务器端证书签名是否有问题
                        try {
                            cert.verify(SKBaseUtil.getLocalCRT().getPublicKey());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };

    /*
    * 私有证书验证，hostnameVerifier
    * */
    static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            if (SKBaseUtil.isNull(SKConfig.SERVICE_ROOT_PATH))
                SKLogger.e("Error!", "SKConfig.SERVICE_ROOT_PATH is null");
            return hv.verify(SKConfig.SERVICE_ROOT_PATH, session);
        }
    };

    /*------------------------增加对https私钥验证 end------------------------------*/
}
