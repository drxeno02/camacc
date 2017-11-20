package com.camacc.csun.camacc.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.camacc.csun.camacc.R;
import com.camacc.csun.camacc.utils.CameraPreview;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Debug";
    private Camera cam;
    private CameraPreview mPreview;
    private Camera.Parameters camparams;
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                File pictureFile = getOutputMediaFile();
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkCameraHardware(this)) {
            try {
                cam = getCameraInstance();
                mPreview = new CameraPreview(this, cam);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);

                // Add a listener to the Capture button
                Button captureButton = (Button) findViewById(R.id.button_capture);
                captureButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get an image from the camera
                                cam.takePicture(null, null, pictureCallback);
                            }
                        }
                );
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }





    private File getOutputMediaFile() throws IOException {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance() throws Exception{
        Camera c;
        c = Camera.open();
        return c; // returns null if camera is unavailable
    }
}
