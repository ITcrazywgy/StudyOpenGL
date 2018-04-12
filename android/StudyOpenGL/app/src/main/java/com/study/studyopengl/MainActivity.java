/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.study.studyopengl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.study.studyopengl.renderer.BallRenderer;
import com.study.studyopengl.renderer.ModelRenderer;
import com.study.studyopengl.renderer.ObjectRenderer;

import static android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY;

public class MainActivity extends Activity {
    /**
     * Hold a reference to our GLSurfaceView
     */
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glSurfaceView = new GLSurfaceView(this);
        // Check if the system supports OpenGL ES 2.0.
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")));
        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2);
            //glSurfaceView.setRenderer(new CubeRenderer(this));
            //glSurfaceView.setRenderer(new CylinderRenderer(this));
            //glSurfaceView.setRenderer(new BallRenderer(this));
            //glSurfaceView.setRenderer(new PyramidRenderer(this));
            //glSurfaceView.setRenderer(new EarthRenderer(this));
            //glSurfaceView.setRenderer(new ImageRenderer(this));
            //glSurfaceView.setRenderer(new ImageCubeRenderer(this));
            //glSurfaceView.setRenderer(new TeapotRenderer(this));
            //glSurfaceView.setRenderer(new ObjectRenderer(this));
            glSurfaceView.setRenderer(new ModelRenderer(this));
            glSurfaceView.setRenderMode(RENDERMODE_CONTINUOUSLY);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }
}