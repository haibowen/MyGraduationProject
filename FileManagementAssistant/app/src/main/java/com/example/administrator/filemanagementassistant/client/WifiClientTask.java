package com.example.administrator.filemanagementassistant.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.administrator.filemanagementassistant.bean.FileTransfer;
import com.example.administrator.filemanagementassistant.util.Md5Util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * 客户端发送文件
 */

public class WifiClientTask extends AsyncTask<String ,Integer,Boolean> {
    private ProgressDialog progressDialog;
    private FileTransfer fileTransfer;
    private static final int PORT=4786;

    public WifiClientTask(Context  context,FileTransfer fileTransfer){
        this.fileTransfer=fileTransfer;
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("正在发送文件");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        fileTransfer.setMd5(Md5Util.getMd5(new File(fileTransfer.getFilePath())));

        Log.e("22222", "文件的MD5码值是：" + fileTransfer.getMd5());
        Socket socket=null;
        OutputStream outputStream=null;
        ObjectOutputStream  objectOutputStream=null;
        InputStream inputStream=null;
        try {
            socket=new Socket();
            socket.bind(null);
            socket.connect((new InetSocketAddress(strings[0],PORT)),10000);
            outputStream=socket.getOutputStream();
            objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(fileTransfer);
            inputStream =new FileInputStream(new File(fileTransfer.getFilePath()));

            long filesize=fileTransfer.getFileLength();
            long total=0;
            //wenhaibo modify the byte[] buf
            byte[] buf=new byte[1024];
           // byte [] buf=new  byte[512];
            int len;
            while ((len=inputStream.read(buf))!=-1){
                outputStream.write(buf,0,len);
                total+=len;
                int progress= (int) ((total*100)/filesize);
                publishProgress(progress);
            }
            outputStream.close();
            objectOutputStream.close();
            inputStream.close();
            socket.close();
            outputStream=null;
            objectOutputStream=null;
            inputStream=null;
            socket=null;
            return true;
        }catch (Exception e){
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (inputStream!=null){
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (socket!=null){
                try {
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
       progressDialog.cancel();
    }
}
