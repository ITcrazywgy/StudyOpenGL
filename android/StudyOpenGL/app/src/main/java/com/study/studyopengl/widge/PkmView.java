package com.study.studyopengl.widge;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.util.TextureHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Felix on 2018/4/18 00:07
 */

public class PkmView extends GLSurfaceView {

    private boolean isPlaying;

    public PkmView(Context context) {
        this(context, null);
    }

    public PkmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setEGLContextClientVersion(2);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        setRenderer(new FGLRender());
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    private String mPath;
    private ZipInputStream mZipStream;
    private int mFrameRate = 25;


    public void setFrameRate(int frameRate) {
        this.mFrameRate = frameRate;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public void start() {
        if (!isPlaying) {
            if (!open()) return;
            changeState(StateChangeListener.STOP, StateChangeListener.START);
            isPlaying = true;
            requestRender();
        }
    }

    public boolean open() {
        if (mPath == null) return false;
        try {
            if (mPath.startsWith("assets/")) {
                InputStream s = getResources().getAssets().open(mPath.substring(7));
                mZipStream = new ZipInputStream(s);
            } else {
                mZipStream = new ZipInputStream(new FileInputStream(mPath));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private StateChangeListener mStateChangeListener;

    public void setStateChangeListener(StateChangeListener listener) {
        this.mStateChangeListener = listener;
    }

    private void changeState(int lastState, int nowState) {
        if (this.mStateChangeListener != null) {
            this.mStateChangeListener.onStateChanged(lastState, nowState);
        }
    }

    private boolean hasElements() {
        try {
            if (mZipStream != null) {
                if (mZipStream.getNextEntry() != null) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public class FGLRender implements Renderer {
        private final float[] VERTEX_DATA = {
                -1.0f, 1.0f, 0.0f, 0.0f,
                -1.0f, -1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 1.0f, 1.0f
        };

        private PkmProgram mProgram;

        private VertexArray vertexArray;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            mProgram = new PkmProgram(getContext());
            vertexArray = new VertexArray(VERTEX_DATA);
        }

        public void bindData() {
            vertexArray.setVertexAttributePointer(
                    0,
                    mProgram.getPositionAttributeLocation(),
                    2,
                    16);

            vertexArray.setVertexAttributePointer(
                    2,
                    mProgram.getTextureCoordinatesAttributeLocation(),
                    2,
                    16);
        }

        public void draw() {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        }


        private int mWidth;
        private int mHeight;

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        private long mLastTimeMillis = 0;


        @Override
        public void onDrawFrame(GL10 gl) {
            long interval = System.currentTimeMillis() - mLastTimeMillis;
            float stepTime = 1000f / mFrameRate;
            if (interval < stepTime) {
                try {
                    Thread.sleep((long) (stepTime - interval));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.mLastTimeMillis = System.currentTimeMillis();
            render();
            if (!isPlaying) {
                changeState(StateChangeListener.PLAYING, StateChangeListener.STOP);
            } else {
                requestRender();
            }
        }

        private void render() {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mProgram.useProgram();
            int textureId = 0, textureId2 = 0;
            int[] texWH = new int[2];
            if (hasElements()) {
                textureId = TextureHelper.loadETC1Texture(mZipStream, texWH);
            }
            if (hasElements()) {
                textureId2 = TextureHelper.loadETC1Texture(mZipStream, null);
            }
            if (textureId != 0 && textureId2 != 0) {
                mProgram.setUniforms(getMatrix(texWH[0], texWH[1]), textureId, textureId2);
                bindData();
                draw();
            } else {
                isPlaying = false;
            }
        }


        private float[] getMatrix(int texWidth, int texHeight) {
            float[] matrix = new float[16];
            float[] view = new float[16];
            float[] projection = new float[16];

            float widgetRatio = (float) mWidth / mHeight;
            float texRatio = (float) texWidth / texHeight;

            if (texRatio > widgetRatio) {
                Matrix.orthoM(projection, 0, -1, 1, -texRatio / widgetRatio, texRatio / widgetRatio, 1, 3);
            } else {
                Matrix.orthoM(projection, 0, -widgetRatio / texRatio, widgetRatio / texRatio, -1, 1, 1, 3);
            }
            Matrix.setLookAtM(view, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0);
            Matrix.multiplyMM(matrix, 0, projection, 0, view, 0);
            return matrix;
        }

    }
}
