package com.study.studyopengl.objects;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.geometry.Circle;
import com.study.studyopengl.geometry.Position;
import com.study.studyopengl.programs.CylinderProgram;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

public class Cylinder {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private VertexArray vertexArray;
    private List<ObjectBuilder.DrawCommand> drawList;

    public Cylinder(Position center, float radius, float height, int numPoints) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createCylinder(center, radius, height, numPoints);
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }


    public void bindData(CylinderProgram colorProgram) {
        vertexArray.setVertexAttributePointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }


    private static class ObjectBuilder {
        private static final int FLOATS_PER_VERTEX = 3;

        static class GeneratedData {
            float[] vertexData;
            List<DrawCommand> drawList;

            GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
                this.vertexData = vertexData;
                this.drawList = drawList;
            }
        }

        private final float[] vertexData;
        private final List<DrawCommand> drawList = new ArrayList<>();
        private int offset = 0;

        private ObjectBuilder(int sizeInVertices) {
            vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
        }

        private GeneratedData build() {
            return new GeneratedData(vertexData, drawList);
        }


        static GeneratedData createCylinder(Position center, float radius, float height, int numPoints) {
            int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints);
            ObjectBuilder builder = new ObjectBuilder(size);
            Circle cylinderTop = new Circle(center.translateY(height / 2f), radius);
            builder.appendCircle(cylinderTop, numPoints);
            builder.appendOpenCylinder(center, radius, height, numPoints);
            Circle cylinderBottom = new Circle(center.translateY(-height / 2f), radius);
            builder.appendCircle(cylinderBottom, numPoints);
            return builder.build();
        }

        private static int sizeOfCircleInVertices(int numPoints) {
            return 1 + (numPoints + 1);
        }

        private static int sizeOfOpenCylinderInVertices(int numPoints) {
            return (numPoints + 1) * 2;
        }

        private void appendCircle(Circle circle, int numPoints) {
            final int startVertex = offset / FLOATS_PER_VERTEX;
            final int numVertices = sizeOfCircleInVertices(numPoints);

            // Center point of fan
            vertexData[offset++] = circle.center.x;
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z;

            // Fan around center point. <= is used because we want to generate
            // the point at the starting angle twice to complete the fan.
            for (int i = 0; i <= numPoints; i++) {
                float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);
                vertexData[offset++] = (float) (circle.center.x + circle.radius * Math.cos(angleInRadians));
                vertexData[offset++] = circle.center.y;
                vertexData[offset++] = (float) (circle.center.z + circle.radius * Math.sin(angleInRadians));
            }
            drawList.add(new DrawCommand() {
                @Override
                public void draw() {
                    glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
                }
            });
        }

        private void appendOpenCylinder(Position center, float radius, float height, int numPoints) {
            final int startVertex = offset / FLOATS_PER_VERTEX;
            final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
            final float yStart = center.y - (height / 2f);
            final float yEnd = center.y + (height / 2f);
            // Generate strip around center point. <= is used because we want to
            // generate the points at the starting angle twice, to complete the
            // strip.
            for (int i = 0; i <= numPoints; i++) {
                float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);
                float xPosition = (float) (center.x + radius * Math.cos(angleInRadians));
                float zPosition = (float) (center.z + radius * Math.sin(angleInRadians));

                vertexData[offset++] = xPosition;
                vertexData[offset++] = yStart;
                vertexData[offset++] = zPosition;

                vertexData[offset++] = xPosition;
                vertexData[offset++] = yEnd;
                vertexData[offset++] = zPosition;
            }
            drawList.add(new DrawCommand() {
                @Override
                public void draw() {
                    glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
                }
            });
        }


        public interface DrawCommand {
            void draw();
        }
    }
}