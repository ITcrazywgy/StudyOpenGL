package com.study.studyopengl.parser;

import android.util.Log;

import java.io.BufferedReader;

/**
 * Created by Felix on 2018/1/6 15:01
 */

class MTLParser {
    private static final String TAG = "MTLParser";
    private static final String COMMAND_MATERIAL = "newmtl";
    private static final String COMMAND_AMBIENT_COLOR = "Ka";
    private static final String COMMAND_DIFFUSE_COLOR = "Kd";
    private static final String COMMAND_SPECULAR_COLOR = "Ks";
    private static final String COMMAND_TRANSMISSION_COLOR = "Tf";
    //d alphadefines the transparency of the material to be alpha. The default is 1.0 (not transparent at all)
    // Some formats use Tr instead of d;
    // Tr alphadefines the transparency of the material to be alpha. The default is 1.0 (not transparent at all).
    private static final String COMMAND_DISSOLVE = "d";
    private static final String COMMAND_SPECULAR_EXPONENT = "Ns";
    private static final String COMMAND_AMBIENT_TEXTURE = "map_Ka";
    private static final String COMMAND_DIFFUSE_TEXTURE = "map_Kd";
    private static final String COMMAND_SPECULAR_TEXTURE = "map_Ks";
    private static final String COMMAND_SPECULAR_EXPONENT_TEXTURE = "map_Ns";
    private static final String COMMAND_DISSOLVE_TEXTURE = "map_d";

    private MTLLibrary library;
    private MTLMaterial currentMaterial;

    MTLLibrary parse(BufferedReader reader) {
        currentMaterial = null;
        library = new MTLLibrary();
        Line line = new Line();
        try {
            while (line.parse(reader)) {
                if (line.isComment()) {
                    processComment(line);
                } else if (line.isCommand(COMMAND_MATERIAL)) {
                    processMaterial(line);
                } else if (line.isCommand(COMMAND_AMBIENT_COLOR)) {
                    processAmbientColor(line);
                } else if (line.isCommand(COMMAND_DIFFUSE_COLOR)) {
                    processDiffuseColor(line);
                } else if (line.isCommand(COMMAND_SPECULAR_COLOR)) {
                    processSpecularColor(line);
                } else if (line.isCommand(COMMAND_TRANSMISSION_COLOR)) {
                    processTransmissionColor(line);
                } else if (line.isCommand(COMMAND_DISSOLVE)) {
                    processDissolve(line);
                } else if (line.isCommand(COMMAND_SPECULAR_EXPONENT)) {
                    processSpecularExponent(line);
                } else if (line.isCommand(COMMAND_AMBIENT_TEXTURE)) {
                    processAmbientTexture(line);
                } else if (line.isCommand(COMMAND_DIFFUSE_TEXTURE)) {
                    processDiffuseTexture(line);
                } else if (line.isCommand(COMMAND_SPECULAR_TEXTURE)) {
                    processSpecularTexture(line);
                } else if (line.isCommand(COMMAND_SPECULAR_EXPONENT_TEXTURE)) {
                    processSpecularExponentTexture(line);
                } else if (line.isCommand(COMMAND_DISSOLVE_TEXTURE)) {
                    processDissolveTexture(line);
                }
            }
            return library;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void processComment(Line line) {
        Log.d(TAG, line.get());
    }

    private void processMaterial(Line line) {
        currentMaterial = new MTLMaterial(line.getString(0));
        library.materials.add(currentMaterial);
    }

    private void assureMaterial() {
        if (currentMaterial == null) {
            throw new RuntimeException("Material name has not been defined.");
        }
    }

    private void processAmbientColor(Line line) {
        assureMaterial();
        currentMaterial.setAmbientColor(new float[]{line.getFloat(0), line.getFloat(1), line.getFloat(2)});
    }

    private void processDiffuseColor(Line line) {
        assureMaterial();
        currentMaterial.setDiffuseColor(new float[]{line.getFloat(0), line.getFloat(1), line.getFloat(2)});
    }

    private void processSpecularColor(Line line) {
        assureMaterial();
        currentMaterial.setSpecularColor(new float[]{line.getFloat(0), line.getFloat(1), line.getFloat(2)});
    }

    private void processTransmissionColor(Line line) {
        assureMaterial();
        currentMaterial.setTransmissionColor(new float[]{line.getFloat(0), line.getFloat(1), line.getFloat(2)});
    }

    private void processDissolve(Line line) {
        assureMaterial();
        currentMaterial.setDissolve(line.getFloat(0));
    }

    private void processSpecularExponent(Line line) {
        assureMaterial();
        currentMaterial.setSpecularExponent(line.getFloat(0));
    }

    private void processAmbientTexture(Line line) {
        assureMaterial();
        currentMaterial.setAmbientTexture(line.getString(0));
    }

    private void processDiffuseTexture(Line line) {
        assureMaterial();
        currentMaterial.setDiffuseTexture(line.getString(0));
    }

    private void processSpecularTexture(Line line) {
        assureMaterial();
        currentMaterial.setSpecularTexture(line.getString(0));
    }

    private void processSpecularExponentTexture(Line line) {
        assureMaterial();
        currentMaterial.setSpecularExponentTexture(line.getString(0));
    }

    private void processDissolveTexture(Line line) {
        assureMaterial();
        currentMaterial.setDissolveTexture(line.getString(0));
    }

}
