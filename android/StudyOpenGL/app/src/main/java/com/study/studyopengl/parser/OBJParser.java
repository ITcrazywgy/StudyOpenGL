package com.study.studyopengl.parser;

import android.content.Context;

import com.study.studyopengl.geometry.Position;
import com.study.studyopengl.geometry.TexCoordinates;
import com.study.studyopengl.geometry.Vector;
import com.study.studyopengl.objects.OBJObject;
import com.study.studyopengl.parser.OBJFace.VertexIndex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Felix on 2018/1/6 00:05
 */

public class OBJParser {
    private static final String COMMAND_POSITION = "v";
    private static final String COMMAND_TEXCOORD = "vt";
    private static final String COMMAND_NORMAL = "vn";
    private static final String COMMAND_OBJECT = "o";
    private static final String COMMAND_FACE = "f";
    private static final String COMMAND_MATERIAL = "usemtl";
    private static final String COMMAND_MATERIAL_LIB = "mtllib";

    private OBJModel model;
    private OBJObject currentObject;

    public OBJModel parse(Context context, BufferedReader reader) {
        model = new OBJModel();
        Line line = new Line();
        try {
            while (line.parse(reader)) {
                if (line.isComment()) {
                    processComment(line);
                } else if (line.isCommand(COMMAND_POSITION)) {
                    processPosition(line);
                } else if (line.isCommand(COMMAND_TEXCOORD)) {
                    processTexCoord(line);
                } else if (line.isCommand(COMMAND_NORMAL)) {
                    processNormal(line);
                } else if (line.isCommand(COMMAND_OBJECT)) {
                    processObject(line);
                } else if (line.isCommand(COMMAND_FACE)) {
                    processFace(line);
                } else if (line.isCommand(COMMAND_MATERIAL_LIB)) {
                    processMaterialLibrary(context, line);
                } else if (line.isCommand(COMMAND_MATERIAL)) {
                    processMaterial(line);
                }
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private OBJMesh currentMesh;

    private void assureMesh() {
        if (currentMesh != null) {
            return;
        }
        assureObject();
        currentMesh = new OBJMesh(model);
        currentObject.getMeshes().put(currentMesh.getMaterialName(), currentMesh);
    }

    private void processMaterial(Line line) {
        assureObject();
        String materialName = line.getString(0);
        OBJMesh mesh = currentObject.getMeshes().get(materialName);
        if (mesh == null) {
            mesh = new OBJMesh(model, materialName);
            currentObject.getMeshes().put(materialName, mesh);
        }
        currentMesh = mesh;
    }

    private void processMaterialLibrary(Context context, Line line) {
        for (int i = 0; i < line.getParamCount(); i++) {
            String libraryName = line.getString(i);
            MTLParser mtlParser = new MTLParser();
            MTLLibrary mtlLibrary = loadMtlLibrary(context, libraryName, mtlParser);
            model.getMaterialLibraries().add(mtlLibrary);
        }
    }

    private MTLLibrary loadMtlLibrary(Context context, String libraryName, MTLParser mtlParser) {
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(libraryName);
            reader = new BufferedReader(new InputStreamReader(in));
            return mtlParser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void assureObject() {
        if (currentObject != null) {
            return;
        }
        currentObject = new OBJObject(model);
        model.getObjects().add(currentObject);
    }

    private void processFace(Line line) {
        assureMesh();
        OBJFace face = new OBJFace(model);
        for (int i = 0; i < line.getParamCount(); ++i) {
            int[] indexes = parseVertexIndexes(line.getString(i));
            face.indexesOfVertexes.add(new VertexIndex(indexes[0] - 1, indexes[1] - 1, indexes[2] - 1));
        }
        currentMesh.getFaces().add(face);
    }

    private int[] parseVertexIndexes(String segment) {
        final String[] indexes = segment.split("/");
        int positionIndex = VertexIndex.UNDEFINED_INDEX;
        int texIndex = VertexIndex.UNDEFINED_INDEX;
        int normalIndex = VertexIndex.UNDEFINED_INDEX;
        if (indexes.length > 0) {
            positionIndex = parseInt(indexes[0], VertexIndex.UNDEFINED_INDEX);
        }
        if (indexes.length > 1) {
            texIndex = parseInt(indexes[1], VertexIndex.UNDEFINED_INDEX);
        }
        if (indexes.length > 2) {
            normalIndex = parseInt(indexes[2], VertexIndex.UNDEFINED_INDEX);
        }
        return new int[]{positionIndex, texIndex, normalIndex};
    }

    private static int parseInt(String text, int def) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    private void processObject(Line line) {
        if (line.getParamCount() > 1) {
            final String objName = line.getString(0);
            currentObject = new OBJObject(objName, model);
            model.getObjects().add(currentObject);
        }
    }

    private void processNormal(Line line) {
        float x = line.getFloat(0);
        float y = line.getFloat(1);
        float z = line.getFloat(2);
        model.getNormals().add(new Vector(x, y, z));
    }

    private void processTexCoord(Line line) {
        float s = line.getFloat(0);
        float t = line.getFloat(1);
        model.getTexCoords().add(new TexCoordinates(s, t));
    }

    private void processPosition(Line line) {
        float x = line.getFloat(0);
        float y = line.getFloat(1);
        float z = line.getFloat(2);
        model.getPositions().add(new Position(x, y, z));
    }

    private void processComment(Line line) {
    }
}
