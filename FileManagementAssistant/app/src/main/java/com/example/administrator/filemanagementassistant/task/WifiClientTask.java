package com.example.administrator.filemanagementassistant.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.example.administrator.filemanagementassistant.bean.FileTransfer;
import com.example.administrator.filemanagementassistant.util.Md5Util;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

        Socket socket=null;

        OutputStream outputStream=null;
        ObjectOutputStream  objectOutputStream=null;
        InputStream inputStream=null;

        socket=new Socket();
        socket.bind(null);
        socket.connect((new InetSocketAddress(strings[0],PORT)),10000);

        outputStream=socket.getOutputStream();
        objectOutputStream=new ObjectOutputStream(outputStream);





        return null;
    }
}
