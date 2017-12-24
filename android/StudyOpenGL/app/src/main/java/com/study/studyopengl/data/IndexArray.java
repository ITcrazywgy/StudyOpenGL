/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.study.studyopengl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IndexArray {
    private final ByteBuffer indexBuffer;

    public IndexArray(byte[] indexData) {
        indexBuffer = ByteBuffer
                .allocateDirect(indexData.length)
                .order(ByteOrder.nativeOrder())
                .put(indexData);
        indexBuffer.position(0);
    }

    public ByteBuffer getIndexBuffer() {
        return indexBuffer;
    }
}
