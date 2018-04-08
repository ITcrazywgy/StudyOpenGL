
package com.study.studyopengl.parser;

import java.util.ArrayList;
import java.util.List;

public class OBJMesh {

    private final List<OBJFace> faces = new ArrayList<>();
    private String materialName = "Default";
    private OBJModel model;

    public OBJMesh(OBJModel model) {
        this.model = model;
    }

    public OBJMesh(OBJModel model, String materialName) {
        this.model = model;
        this.materialName = materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public List<OBJFace> getFaces() {
        return faces;
    }


    public MTLMaterial getMaterial() {
        MTLMaterial material = null;
        if (this.materialName != null && this.materialName.length() > 0) {
            for (MTLLibrary library : model.getMaterialLibraries()) {
                material = library.getMaterial(this.materialName);
                if (material != null) {
                    break;
                }
            }
        }
        return material;
    }
}
