package com.mark.games.fallingblocks.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

import com.mark.games.fallingblocks.framework.FileIO;

public class AndroidFileIO implements FileIO {
    AssetManager assets;
    String externalStoragePath;
    //create an instance of the path of where the files are going to be saved
    File data;
    
    public AndroidFileIO(AssetManager assets) {
        this.assets = assets;
        data = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "Android" + File.separator + "data" + 
        		File.separator, "com.mark.games.fallingblocks" + File.separator);
        //check to see if the directory exists
        //if not create it and any parent directories that are missing
        if(!data.exists())
        	data.mkdirs();
        this.externalStoragePath = data.getAbsolutePath() + File.separator;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
    public OutputStream appendToFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName, true);
    }
}
