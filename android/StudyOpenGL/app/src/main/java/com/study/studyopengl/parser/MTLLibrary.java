package com.study.studyopengl.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2018/1/6 14:53
 */

public class MTLLibrary {

    final List<MTLMaterial> materials = new ArrayList<>();

    public MTLMaterial getMaterial(String name) {
        for (MTLMaterial material : materials) {
            if (name.equals(material.getName())) {
                return material;
            }
        }
        return null;
    }

}
