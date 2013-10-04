package com.sahlitehu.emailcontacter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahlitehu.emailcontacter.LocationUtil.LocationUpdate;

public class EmailContacter extends Activity {
	
	public static final String DATA_FILE_NAME = "data.csv";
	public static final String IMAGE_FILE_NAME = "image.jpg";
	
	public static final int ACTION_TAKE_PHOTO = 0;
	
	private EditText data_a;
	private EditText data_b;
	private EditText data;
	private Button save_data;
	private ImageView image;
	private TextView latitude;
	private TextView longtitude;
	private Button locate;
	private Button send_email;
	private Context context;
	
	private LocationUtil location_util;
	
	private OnClickListener save_action = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (FileUtil.isExternalStorageWritable()){
				String original_data = "";
				original_data += "data a, "+data_a.getText().toString()+", ";
				original_data += "data b, "+data_b.getText().toString();
				File file = FileUtil.getAlbumStorageDir(context, DATA_FILE_NAME);
				FileUtil.write(file, original_data);
				String back_data = FileUtil.read(file);
				data.setText(back_data);
			}
		}
		
	};
	
	private OnClickListener select_action = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = FileUtil.getAlbumStorageDir(context, IMAGE_FILE_NAME);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		    startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
		}
		
	};
	
	private LocationUpdate location_handle = new LocationUpdate(){

		@Override
		public void update(boolean isnewdata) {
			// TODO Auto-generated method stub
			if (isnewdata){
				Location location = location_util.getLocation();
				double alti = location.getLatitude();
				double longti = location.getLongitude();
				latitude.setText(String.valueOf(alti));
				longtitude.setText(String.valueOf(longti));
			}
		}
		
	};
	
	private OnClickListener locate_action = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			location_util.start(context);
		}
		
	};
	
	private OnClickListener send_email_action = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
			String original_data = "";
			original_data += "data a, "+data_a.getText().toString()+", ";
			original_data += "data b, "+data_b.getText().toString();
			sendIntent.putExtra(Intent.EXTRA_TEXT, original_data);
			
			ArrayList<Uri> imageUris = new ArrayList<Uri>();
			File file = FileUtil.getAlbumStorageDir(context, DATA_FILE_NAME);
			imageUris.add(Uri.fromFile(file));
			file = FileUtil.getAlbumStorageDir(context, IMAGE_FILE_NAME);
			imageUris.add(Uri.fromFile(file));
			sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
			sendIntent.setType("image/jpeg");
			startActivity(sendIntent);
		}
		
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_contacter);
        context = this;
        
        data_a = (EditText)findViewById(R.id.input_data_a);
        data_b = (EditText)findViewById(R.id.input_data_b);
        data = (EditText)findViewById(R.id.output_data);
        save_data = (Button)findViewById(R.id.save);
        save_data.setOnClickListener(save_action);
        
        image = (ImageView)findViewById(R.id.image);
        image.setOnClickListener(select_action);
        
        location_util = new LocationUtil();
        location_util.setCallback(location_handle);
        locate = (Button)findViewById(R.id.locate);
        locate.setOnClickListener(locate_action);
        
        send_email = (Button)findViewById(R.id.send_email);
        send_email.setOnClickListener(send_email_action);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.email_contacter, menu);
        return true;
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode){
			switch(requestCode){
				case ACTION_TAKE_PHOTO:
				{
					File file = FileUtil.getAlbumStorageDir(context, IMAGE_FILE_NAME);
					// Get the dimensions of the View
				    int targetW = image.getWidth();
				    int targetH = image.getHeight();
				  
				    // Get the dimensions of the bitmap
				    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				    bmOptions.inJustDecodeBounds = true;
				    BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
				    int photoW = bmOptions.outWidth;
				    int photoH = bmOptions.outHeight;
				  
				    // Determine how much to scale down the image
				    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
				  
				    // Decode the image file into a Bitmap sized to fill the View
				    bmOptions.inJustDecodeBounds = false;
				    bmOptions.inSampleSize = scaleFactor;
				    bmOptions.inPurgeable = true;
				  
				    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
				    image.setImageBitmap(bitmap);
				}
				break;
			}
		}else{
			
		}
	}
    
}
