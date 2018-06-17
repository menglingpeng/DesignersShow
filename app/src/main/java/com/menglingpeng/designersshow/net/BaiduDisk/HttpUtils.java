package com.menglingpeng.designersshow.net.BaiduDisk;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static java.net.Proxy.Type.HTTP;

public class HttpUtils {

    //post请求地址
    private final static String API = "https://pcs.baidu.com/rest/2.0/pcs/file";

    //本地文件路径, 例如:"/Users/macuser/Documents/workspace/test.jpg"
    private static String mLocalPath;

    //上传文件路径（含上传的文件名称), 例如:"/apps/yuantest/test.jpg"
    private static String mDestPath;

    //开发者准入标识 access_token, 通过OAuth获得
    private static String mAccessToken;

    public void pcs_mkdir(String path) {
        if (null != mbOauth) {
            BaiduPCSClient api = new BaiduPCSClient();
            api.setAccessToken(mbOauth);
            final BaiduPCSActionInfo.PCSFileInfoResponse ret = api.makeDir(path);
            Toast.makeText(conx,
                    "Mkdir:  " + ret.status.errorCode
                            + "   " + ret.status.message,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void upload(){
        if (args.length != 3) {
            System.out.println("Usage: PCSUploadDemo file_to_upload destination your_access_token");
            return;
        }

        mLocalPath = args[0];
        mDestPath = args[1];
        mAccessToken = args[2];

        File fileToUpload = new File(mLocalPath);
        if (!(fileToUpload).isFile()) {
            System.out.println("Input file_to_upload is invalid!");
            return;
        }

        System.out.println("Uploading...");

        Thread workerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    doUpload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        workerThread.start();

    }

    public static void doUpload() throws IOException{

        File fileToUpload = new File(mLocalPath);

        if(null != fileToUpload && fileToUpload.length() > 0){

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("method", "upload"));
            params.add(new BasicNameValuePair("access_token", mAccessToken));
            params.add(new BasicNameValuePair("path", mDestPath));

            //添加请求参数，通过POST表单进行传递，除上传文件内容之外的其它参数通过query_string进行传递。
            String postUrl = API + "?" + buildParams(params);

            HttpPost post = new HttpPost(postUrl);

            //添加文件内容，必须使用multipart/form-data编码的HTTP entity
            MultipartEntity entity = new MultipartEntity();
            FileBody fo = new FileBody(fileToUpload);
            entity.addPart("uploaded",fo);
            post.setEntity(entity);

            //创建client
            HttpClient client = new DefaultHttpClient();

            //发送请求
            HttpResponse response = client.execute(post);

            System.out.println(response.getStatusLine().toString());
            System.out.println(EntityUtils.toString(response.getEntity()));

        }
    }

    // url encoded query string
    protected static String buildParams(List urlParams){
        String ret = null;
        if(null != urlParams && urlParams.size() > 0){
            try {
                // 指定HTTP.UTF_8为charset参数以保证中文文件路径的正确
                HttpEntity paramsEntity = new UrlEncodedFormEntity(urlParams, HTTP.UTF_8);
                ret = EntityUtils.toString(paramsEntity);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static void download(){

    }

    public static void doDownload() throws IOException{

        File fileToUpload = new File(mLocalPath);

        if(null != fileToUpload && fileToUpload.length() > 0){

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("method", "download"));
            params.add(new BasicNameValuePair("access_token", mAccessToken));
            params.add(new BasicNameValuePair("path", mDestPath));


            String getUrl = API + "?" + buildParams(params);

            HttpGet get = new HttpGet(getUrl);

            //添加文件内容，必须使用multipart/form-data编码的HTTP entity
            MultipartEntity entity = new MultipartEntity();
            FileBody fo = new FileBody(fileToUpload);
            entity.addPart("uploaded",fo);
            post.setEntity(entity);

            //创建client
            HttpClient client = new DefaultHttpClient();

            //发送请求
            HttpResponse response = client.execute(get);

            System.out.println(response.getStatusLine().toString());
            System.out.println(EntityUtils.toString(response.getEntity()));

        }
    }
}
