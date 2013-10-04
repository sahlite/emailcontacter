package com.sahlitehu.emailcontacter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	public static final String LOG_TAG = "FileUtil";
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/* Get a file created in public directory */
	public static File getAlbumStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory. 
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            ""), albumName);
//	    if (!file.mkdirs()) {
//	        Log.e(LOG_TAG, "Directory not created");
//	    }
	    if (!file.exists()){
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return file;
	}
	
	/* Get a file created in application directory */
	public static File getAlbumStorageDir(Context context, String albumName) {
	    // Get the directory for the app's private pictures directory. 
	    File file = new File(context.getExternalFilesDir(
	            ""), albumName);
//	    if (!file.mkdirs()) {
//	        Log.e(LOG_TAG, "Directory not created");
//	    }
	    if (!file.exists()){
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return file;
	}
	
	public static void write(File file, String data){
		FileOutputStream outputStream;

		try {
		  outputStream = new FileOutputStream(file);
		  outputStream.write(data.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	public static String read(File file){
		FileInputStream inputStream;
		String data = "";

		try {
		    byte[] input_byte = new byte[1024];
		    inputStream = new FileInputStream(file);
		    int read_byte = inputStream.read(input_byte);
		    data = new String(input_byte);
		    inputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}	
		
		return data;
	}
}
