package com.study.studyopengl.parser;

/**
 * Created by Felix on 2018/1/6 14:54
 */

public class MTLMaterial {
    private String name;
    private float[] ambientColor = new float[3];
    private float[] diffuseColor = new float[3];
    private float[] specularColor = new float[3];
    private float[] transmissionColor = new float[3];
    private float specularExponent = 0.0f;
    private float dissolve = 1.0f;
    private String ambientTexture;
    private String diffuseTexture;
    private String specularTexture;
    private String specularExponentTexture;
    private String dissolveTexture;

    MTLMaterial(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(float[] ambientColor) {
        this.ambientColor = ambientColor;
    }

    public float[] getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(float[] diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public float[] getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(float[] specularColor) {
        this.specularColor = specularColor;
    }

    public float[] getTransmissionColor() {
        return transmissionColor;
    }

    public void setTransmissionColor(float[] transmissionColor) {
        this.transmissionColor = transmissionColor;
    }

    public float getSpecularExponent() {
        return specularExponent;
    }

    public void setSpecularExponent(float specularExponent) {
        this.specularExponent = specularExponent;
    }

    public float getDissolve() {
        return dissolve;
    }

    public void setDissolve(float dissolve) {
        this.dissolve = dissolve;
    }

    public String getAmbientTexture() {
        return ambientTexture;
    }

    public void setAmbientTexture(String ambientTexture) {
        this.ambientTexture = ambientTexture;
    }

    public String getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(String diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    public String getSpecularTexture() {
        return specularTexture;
    }

    public void setSpecularTexture(String specularTexture) {
        this.specularTexture = specularTexture;
    }

    public String getSpecularExponentTexture() {
        return specularExponentTexture;
    }

    public void setSpecularExponentTexture(String specularExponentTexture) {
        this.specularExponentTexture = specularExponentTexture;
    }

    public String getDissolveTexture() {
        return dissolveTexture;
    }

    public void setDissolveTexture(String dissolveTexture) {
        this.dissolveTexture = dissolveTexture;
    }

}
