package com.study.studyopengl.camera.filter;

import android.content.Context;

import com.study.studyopengl.data.VertexArray;

import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

/**
 * Created by Felix on 2018/5/1 17:24
 */

public abstract class AbsFilter {
    protected static final int POSITION_COMPONENT_COUNT = 2;
    protected static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    protected static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
    protected static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            -1.0f, 1.0f, 0.0f, 0.0f,
            -1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 1.0f, 1.0f
    };
    protected final VertexArray mVertexArray;

    protected AbsFilter() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public abstract void onSurfaceCreated(Context context);

    public abstract void onSurfaceChanged(int width, int height);

    public abstract void onDrawFrame(int inputTextureId);

}
