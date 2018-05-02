package com.study.studyopengl.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.study.studyopengl.camera.filter.CameraFilter;
import com.study.studyopengl.util.MatrixHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private Handler mBackgroundHandler;
    private int mCameraId = 0;

    private Integer mSensorOrientation;
    private Size mPreviewSize;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mBackgroundHandler = new Handler(handlerThread.getLooper());
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(true);
    }


    private CameraDevice mCameraDevice;
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        private void createCameraPreviewSession() {
            Surface surface = new Surface(getSurfaceTexture());
            try {
                final CaptureRequest.Builder requestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                requestBuilder.addTarget(surface);
                mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        try {
                            session.setRepeatingRequest(requestBuilder.build(),
                                    new CameraCaptureSession.CaptureCallback() {
                                        @Override
                                        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                                            super.onCaptureProgressed(session, request, partialResult);
                                        }

                                        @Override
                                        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                                            super.onCaptureCompleted(session, request, result);
                                            requestRender();
                                        }
                                    }, mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    }
                }, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }
    };

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private void openCamera() {
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            assert manager != null;
            manager.openCamera(String.valueOf(mCameraId), mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private static final String TAG = "CameraView";

    private void calcPreviewSize(int width, int height) {
        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            assert manager != null;
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(String.valueOf(mCameraId));
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            int displayRotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
            if ((mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)) == null) {
                mSensorOrientation = 90;
            }
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                        swappedDimensions = true;
                    }
                    break;
                default:
                    Log.e(TAG, "Display rotation is invalid: " + displayRotation);
            }

            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
            }

            Size[] choices = map.getOutputSizes(SurfaceHolder.class);
            mPreviewSize = chooseOptimalSize(choices, rotatedPreviewWidth, rotatedPreviewHeight);
            assert mPreviewSize != null;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private static Size chooseOptimalSize(Size[] choices, int viewWidth, int viewHeight) {
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        for (Size option : choices) {
            if (option.getWidth() >= viewWidth && option.getHeight() >= viewHeight) {
                bigEnough.add(option);
            } else {
                notBigEnough.add(option);
            }
        }
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }


    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        closeCamera();
    }

    private CameraFilter mCameraFilter;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mCameraFilter = new CameraFilter();
        mCameraFilter.onSurfaceCreated(getContext());
        openCamera();
    }

    private float[] rotatedMatrix = new float[16];
    float[] model = new float[16];
    float[] view = new float[16];
    float[] projection = new float[16];
    float[] matrix = new float[16];

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        calcPreviewSize(width, height);
        Matrix.setIdentityM(rotatedMatrix, 0);
        Matrix.rotateM(rotatedMatrix, 0, getRotateAngle(), 0f, 1f, 0f);
        Matrix.setLookAtM(view, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(matrix, 0,view , 0, rotatedMatrix, 0);
        mCameraFilter.setMatrix(matrix);
        mCameraFilter.onSurfaceChanged(width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mCameraFilter.onDrawFrame();
    }


    public SurfaceTexture getSurfaceTexture() {
        return mCameraFilter.getSurfaceTexture();
    }


    private int getRotateAngle() {
        Activity activity = (Activity) getContext();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        return rotation * 90 - mSensorOrientation;
    }
}
