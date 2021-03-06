package com.mxfit.mentix.menu3.Utils;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Mentix on 2018-02-26.
 */

public class CopyDatabase {
    public CopyDatabase(Context context){
        try{
            InputStream inputStream = context.getAssets()
                    .open(DatabasePremadesHelper.DATABASE_NAME);
            String OutFileName = DatabasePremadesHelper.DBLOCATION
                               + DatabasePremadesHelper.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(OutFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff))>0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
