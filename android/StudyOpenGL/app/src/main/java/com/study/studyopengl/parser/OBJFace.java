package com.study.studyopengl.parser;

import com.study.studyopengl.geometry.Position;
import com.study.studyopengl.geometry.TexCoordinates;
import com.study.studyopengl.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.study.studyopengl.parser.OBJFace.VertexIndex.UNDEFINED_INDEX;

public class OBJFace {
    private OBJModel model;

    public OBJFace(OBJModel model) {
        this.model = model;
    }

    static class VertexIndex {
        static final int UNDEFINED_INDEX = Integer.MIN_VALUE;
        int positionIndex = UNDEFINED_INDEX;
        int texCoordIndex = UNDEFINED_INDEX;
        int normalIndex = UNDEFINED_INDEX;

        VertexIndex(int positionIndex, int texCoordIndex, int normalIndex) {
            this.positionIndex = positionIndex;
            this.texCoordIndex = texCoordIndex;
            this.normalIndex = normalIndex;
        }

        boolean hasPositionIndex() {
            return (positionIndex != UNDEFINED_INDEX);
        }

        boolean hasNormalIndex() {
            return (normalIndex != UNDEFINED_INDEX);
        }

        boolean hasTexCoordIndex() {
            return (texCoordIndex != UNDEFINED_INDEX);
        }
    }

    public boolean hasPositions() {
        return !indexesOfVertexes.isEmpty() && indexesOfVertexes.get(0).hasPositionIndex();
    }

    public boolean hasNormals() {
        return !indexesOfVertexes.isEmpty() && indexesOfVertexes.get(0).hasNormalIndex();
    }

    public boolean hasTextureCoordinates() {
        return !indexesOfVertexes.isEmpty() && indexesOfVertexes.get(0).hasTexCoordIndex();
    }


    final List<VertexIndex> indexesOfVertexes = new ArrayList<>(3);


    public int getVertexCount() {
        return indexesOfVertexes.size();
    }

    private Position getPosition(int i) {
        int positionIndex = UNDEFINED_INDEX;
        if (i < indexesOfVertexes.size()) {
            positionIndex = indexesOfVertexes.get(i).positionIndex;
        }
        if (positionIndex != UNDEFINED_INDEX & positionIndex < model.getPositions().size()) {
            return model.getPositions().get(positionIndex);
        }
        return null;
    }

    private TexCoordinates getTexCoordinates(int i) {
        int texCoordIndex = UNDEFINED_INDEX;
        if (i < indexesOfVertexes.size()) {
            texCoordIndex = indexesOfVertexes.get(i).texCoordIndex;
        }
        if (texCoordIndex != UNDEFINED_INDEX && texCoordIndex < model.getTexCoords().size()) {
            return model.getTexCoords().get(texCoordIndex);
        }
        return null;
    }

    private Vector getNormal(int i) {
        int normalIndex = UNDEFINED_INDEX;
        if (i < indexesOfVertexes.size()) {
            normalIndex = indexesOfVertexes.get(i).normalIndex;
        }
        if (normalIndex != UNDEFINED_INDEX && normalIndex < model.getNormals().size()) {
            return model.getNormals().get(normalIndex);
        }
        return null;
    }

    public List<Float> getVertex(int i) {
        Position position = getPosition(i);
        TexCoordinates texCoordinates = getTexCoordinates(i);
        Vector normal = getNormal(i);
        List<Float> vertex = new ArrayList<>(8);
        if (position != null) {
            vertex.add(position.x);
            vertex.add(position.y);
            vertex.add(position.z);
        }
        if (texCoordinates != null) {
            vertex.add(texCoordinates.s);
            vertex.add(texCoordinates.t);
        }
        if (normal != null) {
            vertex.add(normal.x);
            vertex.add(normal.y);
            vertex.add(normal.z);
        }
        return vertex;
    }


}
