/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.study.studyopengl.objects;


import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.CylinderProgram;
import com.study.studyopengl.util.Geometry;
import com.study.studyopengl.util.ObjectBuilder;
import com.study.studyopengl.util.ObjectBuilder.DrawCommand;
import com.study.studyopengl.util.ObjectBuilder.GeneratedData;

import java.util.List;

public class Cylinder {
    private static final int POSITION_COMPONENT_COUNT = 3;


    private VertexArray vertexArray;
    private List<DrawCommand> drawList;


    public Geometry.Point center;
    public float radius;
    public float height;

    public Cylinder(Geometry.Point center, float radius, float height, int numPoints) {
        this.center = center;
        this.radius = radius;
        this.height = height;
        GeneratedData generatedData = ObjectBuilder.createCylinder( center, radius,  height, numPoints);
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }


    public void bindData(CylinderProgram colorProgram) {
        vertexArray.setVertexAttributePointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}