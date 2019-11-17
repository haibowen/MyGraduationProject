package com.example.administrator.filemanagementassistant.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.administrator.filemanagementassistant.bean.FileTransfer;
import com.example.administrator.filemanagementassistant.util.Md5Util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * 服务端接收文件
 */

public class WifiServerService extends IntentService {
    private ServerSocket serverSocket;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private FileOutputStream fileOutputStream;
    private OnprogressChangListener onprogressChangListener;
    private static final int PORT=4786;

    public class  MyBinder extends Binder{

        public WifiServerService getService(){
            return WifiServerService.this;
        }
    }

    public interface  OnprogressChangListener{

        void  onProgressChanged(FileTransfer fileTransfer,int progress);
        void  onTransferFinshed(File file);

    }

    public WifiServerService() {
        super("WifiServerService");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    @Override
    protected void onHandleIntent( Intent intent) {
        clean();
        File file=null;
        try {
            serverSocket =new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(PORT));
            Socket client=serverSocket.accept();
            inputStream=client.getInputStream();
            objectInputStream=new ObjectInputStream(inputStream);
            FileTransfer fileTransfer= (FileTransfer) objectInputStream.readObject();
            String name=new File(fileTransfer.getFilePath()).getName();
            file=new File(Environment.getExternalStorageDirectory()+"/"+name);
            fileOutputStream=new FileOutputStream(file);
            byte buf[]=new byte[512];
            int len;
            long total = 0;
            int progress;
            while ((len=inputStream.read(buf))!=-1){
                fileOutputStream.write(buf,0,len);
                total+=len;
                progress=(int) ((total*100)/fileTransfer.getFileLength());

                if (onprogressChangListener!=null){
                    onprogressChangListener.onProgressChanged(fileTransfer,progress);
                }
            }
            serverSocket.close();
            inputStream.close();
            objectInputStream.close();
            fileOutputStream.close();
            serverSocket=null;
            inputStream=null;
            objectInputStream=null;
            fileOutputStream=null;
            Log.e("22222", "文件接收成功，文件的MD5码是：" + Md5Util.getMd5(file));
        }catch (Exception e){
        }finally {
            clean();
            if (onprogressChangListener!=null){
                onprogressChangListener.onTransferFinshed(file);
            }
            startService(new Intent(this,WifiServerService.class));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        clean();
    }
    public void setOnprogressChangListener(OnprogressChangListener onprogressChangListener) {
        this.onprogressChangListener = onprogressChangListener;
    }
    private void clean() {
        if (serverSocket!=null){
            try {
                serverSocket.close();
                serverSocket=null;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (inputStream!=null){
            try {
                inputStream.close();
                inputStream=null;

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (fileOutputStream!=null){
            try {
                fileOutputStream.close();
                fileOutputStream=null;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (objectInputStream!=null){
            try {
                objectInputStream.close();
                objectInputStream=null;

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
