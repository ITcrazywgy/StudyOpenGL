package com.study.studyopengl.camera;

import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.study.studyopengl.camera.filter.CameraFilter;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraView extends GLSurfaceView {


    private CameraManager mCameraManager;
    private Handler mCameraHandler;
    private int mCameraId = 0;
    private Size mPreviewSize;
    private CameraRenderer mCameraRenderer;


    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mCameraHandler = new Handler(handlerThread.getLooper());
        setEGLContextClientVersion(2);
        mCameraRenderer = new CameraRenderer(getContext());
        setRenderer(mCameraRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(true);
    }


    private CameraDevice mCameraDevice;

    private void openCamera() throws CameraAccessException {
        closeCamera();
        CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(String.valueOf(mCameraId));
        StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size[] outputSizes = map.getOutputSizes(SurfaceHolder.class);
        mPreviewSize = outputSizes[0];
        mCameraRenderer.setPreviewSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

        mCameraManager.openCamera(String.valueOf(mCameraId), new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                mCameraDevice = camera;
                Surface surface = new Surface(mCameraRenderer.getCameraTexture());
                //surfaceTexture.release();
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
                                                //刷新渲染
                                                requestRender();
                                            }
                                        }, mCameraHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        }
                    }, mCameraHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

            }
        }, mCameraHandler);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        closeCamera();
        mCameraRenderer.onSurfaceDestroyed();
    }

    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }


    class CameraRenderer implements Renderer {
        private int mPreviewWidth;
        private int mPreviewHeight;
        private CameraFilter mCameraFilter;
        private int[] mCameraTextures = new int[1];
        private SurfaceTexture mSurfaceTexture;
        private Context mContext;

        CameraRenderer(Context context) {
            this.mContext = context;
            mCameraFilter = new CameraFilter();
        }

        void setPreviewSize(int previewWidth, int previewHeight) {
            this.mPreviewWidth = previewWidth;
            this.mPreviewHeight = previewHeight;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glGenTextures(1, mCameraTextures, 0);
            mSurfaceTexture = new SurfaceTexture(mCameraTextures[0]);

            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            mCameraFilter.onSurfaceCreated(mContext);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mCameraFilter.onSurfaceChanged(width, height, mPreviewHeight, mPreviewWidth);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            if (mSurfaceTexture != null) {
                mSurfaceTexture.updateTexImage();
            }
            mCameraFilter.onDrawFrame(mCameraTextures[0]);
        }


        public SurfaceTexture getCameraTexture() {
            return mSurfaceTexture;
        }

        public void onSurfaceDestroyed() {
            mSurfaceTexture.release();
        }
    }
}
