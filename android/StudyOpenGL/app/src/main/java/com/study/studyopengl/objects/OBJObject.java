package com.study.studyopengl.objects;

import com.study.studyopengl.Constants;
import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.model.Model;
import com.study.studyopengl.parser.MTLMaterial;
import com.study.studyopengl.parser.OBJFace;
import com.study.studyopengl.parser.OBJMesh;
import com.study.studyopengl.parser.OBJModel;
import com.study.studyopengl.programs.OBJProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Felix on 2018/1/6 00:05
 */

public class OBJObject {
    private OBJModel model;

    private Map<String,OBJMesh> meshes = new HashMap<>();

    private String name = "Default";

    public OBJObject(OBJModel model) {
        this.model = model;
    }


    public OBJObject(String name, OBJModel model) {
        this.name = name;
        this.model = model;
    }

    public Map<String, OBJMesh> getMeshes() {
        return meshes;
    }

    interface DrawCommand {
        void draw(OBJProgram program);
    }

    private class DrawCommandImp implements DrawCommand {
        private int startVertex;
        private int numVertices;
        private MTLMaterial material;

        public DrawCommandImp(int startVertex, int numVertices, MTLMaterial material) {
            this.startVertex = startVertex;
            this.numVertices = numVertices;
            this.material = material;
        }

        @Override
        public void draw(OBJProgram program) {
            program.setMaterial(model, material);
            glDrawArrays(GL_TRIANGLES, startVertex, numVertices);
        }
    }

    private VertexArray vertexArray;
    private List<DrawCommand> drawList = new ArrayList<>();

    private int countPerVertex;
    private boolean hasPosition, hasNormal, hasTexCoord;
    private boolean isKnown;

    public void init() {
        List<Float> vertexData = new ArrayList<>();
        int startVertex = 0;
        List<OBJMesh> values =new ArrayList<>();
        values.addAll(meshes.values());
        Collections.sort(values, new Comparator<OBJMesh>() {
            @Override
            public int compare(OBJMesh o1, OBJMesh o2) {
                return o1.getMaterial().getName().compareTo(o2.getMaterial().getName());
            }
        });
        for (OBJMesh mesh : values) {
            int numVertices = 0;
            for (OBJFace face : mesh.getFaces()) {
                for (int i = 0; i < face.getVertexCount(); i++) {
                    List<Float> vertex = face.getVertex(i);
                    if (!isKnown) {
                        countPerVertex = vertex.size();
                        hasPosition = face.hasPositions();
                        hasTexCoord = face.hasTextureCoordinates();
                        hasNormal = face.hasNormals();
                        isKnown = true;
                    }
                    vertexData.addAll(vertex);
                    numVertices++;
                }
            }
            drawList.add(new DrawCommandImp(startVertex, numVertices, mesh.getMaterial()));
            startVertex += numVertices;
        }
        vertexArray = new VertexArray(vertexData);
    }

    public void bindData(OBJProgram program) {
        int dataOffset = 0;
        if (hasPosition) {
            vertexArray.setVertexAttributePointer(
                    0,
                    program.getPositionAttributeLocation(),
                    OBJModel.POSITION_COMPONENT_COUNT,
                    countPerVertex * Constants.BYTES_PER_FLOAT);
            dataOffset += OBJModel.POSITION_COMPONENT_COUNT;
        }

        if (hasTexCoord) {
            vertexArray.setVertexAttributePointer(
                    dataOffset,
                    program.getTextureCoordinatesAttributeLocation(),
                    OBJModel.TEXTURE_COORDINATES_COMPONENT_COUNT,
                    countPerVertex * Constants.BYTES_PER_FLOAT);
            dataOffset += OBJModel.TEXTURE_COORDINATES_COMPONENT_COUNT;
        }

        if (hasNormal) {
            vertexArray.setVertexAttributePointer(
                    dataOffset,
                    program.getNormalAttributeLocation(),
                    OBJModel.NORMAL_COMPONENT_COUNT,
                    countPerVertex * Constants.BYTES_PER_FLOAT);
        }
    }

    public void draw(OBJProgram program) {
        for (DrawCommand drawCommand : drawList) {
            drawCommand.draw(program);
        }
    }

}
