package com.example.petruz.cameraapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.petruz.cameraapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    private Size previewsize;
    private Size jpegSizes[] = null;
    private TextureView textureView;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private Button getpictureBtn;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private File file;


    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private final String LOGTAG = "Camera Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        this.textureView = (TextureView) findViewById(R.id.tv_camera);
        this.textureView.setSurfaceTextureListener(surfaceTextureListener);

        this.getpictureBtn = (Button) findViewById(R.id.takePictureBtn);
        this.getpictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

    }

    protected void takePicture() {
        if(null == cameraDevice) {
            Log.e(LOGTAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;

            if (characteristics != null)
            {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length)
            {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
           // int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, Surface.ROTATION_0);
            final File file = new File(Environment.getExternalStorageDirectory()+"/pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener()
            {
                @Override
                public void onImageAvailable(ImageReader reader)
                {
                    Image image = null;
                    try
                    {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (image != null)
                        {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException
                {
                    OutputStream output = null;
                    try
                    {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    }
                    finally
                    {
                        if (null != output)
                        {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback()
            {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result)
                {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    startCamera();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback()
            {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            Toast.makeText(this, "TRUE", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // no camera on this device
            Toast.makeText(this, "FALSE", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void openCamera()
    {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String camerId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camerId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewsize = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                // https://developer.android.com/training/permissions/requesting.html
                //checks if the user have allowed the app to use the camera if not, ask them
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA))
                {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                }
                else
                {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }

            }

            manager.openCamera(camerId, stateCallback, null);

        }
        catch (Exception e)
        {
        }
    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback()
    {
        @Override
        public void onOpened(CameraDevice camera)
        {
            cameraDevice = camera;
            startCamera();
        }

        @Override
        public void onDisconnected(CameraDevice camera)
        {

        }

        @Override
        public void onError(CameraDevice camera, int error)
        {

        }
    };

    void  startCamera()
    {
        if(cameraDevice == null||!textureView.isAvailable()|| previewsize == null)
        {
            return;
        }

        SurfaceTexture texture = textureView.getSurfaceTexture();

        if(texture == null)
        {

            return;
        }
        texture.setDefaultBufferSize(previewsize.getWidth(),previewsize.getHeight());
        Surface surface = new Surface(texture);

        try
        {
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        }
        catch (Exception e)
        {
            Log.d("cameraActivity", "catch");

        }
        previewBuilder.addTarget(surface);
        try
        {
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session)
                {
                    previewSession = session;
                    getChangedPreview();
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session)
                {
                }
            },null);
        }
        catch (Exception e)
        {
        }
    }

    void getChangedPreview()
    {
        if(cameraDevice == null)
        {
            return;
        }

        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("changed Preview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());

        try
        {
            previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
        }
        catch (Exception e)
        {

        }
    }

}
