package com.study.studyopengl.camera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Size;
import android.view.SurfaceHolder;

import static android.content.Context.CAMERA_SERVICE;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraView extends GLSurfaceView {
    private CameraManager mCameraManager;
    private int mCameraId = 0;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new CameraRenderer());
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mCameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        /*HandlerThread mCameraThread = new HandlerThread("camera2 ");
        mThread.start();
        mHandler = new Handler(mThread.getLooper());*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        openCamera();
    }

    private void openCamera() {
        try {
            CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(String.valueOf(mCameraId));
            StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] outputSizes = streamConfigurationMap.getOutputSizes(SurfaceHolder.class);


        } catch (CameraAccessException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        releaseCamera();
    }

    private void releaseCamera() {

    }
}
