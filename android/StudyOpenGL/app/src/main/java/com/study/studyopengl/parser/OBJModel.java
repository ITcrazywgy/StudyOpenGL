package com.study.studyopengl.parser;

import android.content.Context;
import android.text.TextUtils;

import com.study.studyopengl.geometry.Position;
import com.study.studyopengl.geometry.TexCoordinates;
import com.study.studyopengl.geometry.Vector;
import com.study.studyopengl.objects.OBJObject;
import com.study.studyopengl.programs.OBJProgram;
import com.study.studyopengl.util.TextureHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Felix on 2018/1/6 00:02
 */
public class OBJModel {
    public static final int POSITION_COMPONENT_COUNT = 3;
    public static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    public static final int NORMAL_COMPONENT_COUNT = 3;

    private final List<Position> positions = new ArrayList<>();
    private final List<TexCoordinates> texCoords = new ArrayList<>();
    private final List<Vector> normals = new ArrayList<>();
    private final List<OBJObject> objects = new ArrayList<>();

    private final List<MTLLibrary> materialLibraries = new ArrayList<>();

    public List<OBJObject> getObjects() {
        return objects;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<TexCoordinates> getTexCoords() {
        return texCoords;
    }

    public List<Vector> getNormals() {
        return normals;
    }

    public List<MTLLibrary> getMaterialLibraries() {
        return materialLibraries;
    }


    private Map<String, Integer> textureIds = new HashMap<>();

    public void loadTexture(Context context) {
        for (MTLLibrary library : materialLibraries) {
            for (MTLMaterial material : library.materials) {
                String ambientTexture = material.getAmbientTexture();
                if (!TextUtils.isEmpty(ambientTexture) && !textureIds.containsKey(ambientTexture)) {
                    int ambientTextureId = TextureHelper.loadTexture(context, ambientTexture);
                    textureIds.put(ambientTexture, ambientTextureId);
                }
                String diffuseTexture = material.getDiffuseTexture();
                if (!TextUtils.isEmpty(diffuseTexture) && !textureIds.containsKey(diffuseTexture)) {
                    int diffuseTextureId = TextureHelper.loadTexture(context, diffuseTexture);
                    textureIds.put(diffuseTexture, diffuseTextureId);
                }
                String specularTexture = material.getSpecularTexture();
                if (!TextUtils.isEmpty(specularTexture) && !textureIds.containsKey(specularTexture)) {
                    int specularTextureId = TextureHelper.loadTexture(context, specularTexture);
                    textureIds.put(specularTexture, specularTextureId);
                }
            }
        }
    }

    public int getTextureId(String textureName) {
        Integer result = textureIds.get(textureName);
        return result != null ? result : 0;
    }

    public void prepare(Context context) {
        loadTexture(context);
        List<OBJObject> objects = getObjects();
        for (OBJObject obj : objects) {
            obj.init();
        }
    }


    public void draw(OBJProgram program) {
        for (OBJObject object : objects) {
            object.bindData(program);
            object.draw(program);
        }
    }

}
