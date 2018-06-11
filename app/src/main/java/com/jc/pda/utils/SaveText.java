package com.jc.pda.utils;

import android.content.Context;
import android.os.Environment;

import com.jc.pda.activity.OutActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by z on 2018/1/9.
 */

public class SaveText {

    public static void saveText(String fileName,String fileContent){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String dirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "OUT";
            File file = new File(dirPath,fileName);

            try {
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }else{
                    file.delete();
                }
                file.createNewFile();

                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(file.length());
                raf.write(fileContent.getBytes());
                raf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
