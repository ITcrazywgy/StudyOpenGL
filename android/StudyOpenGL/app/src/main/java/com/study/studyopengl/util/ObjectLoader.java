package com.study.studyopengl.util;


import android.content.Context;
import android.util.SparseArray;

import com.study.studyopengl.data.VertexArray.PTNVertex;
import com.study.studyopengl.util.Geometry.Point;
import com.study.studyopengl.util.Geometry.TextureCoordinates;
import com.study.studyopengl.util.Geometry.Vector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ObjectLoader {


    public static List<PTNVertex> load(Context context, String path) {
        List<PTNVertex> vertexList = new ArrayList<>();
        List<Point> posList = new ArrayList<>();
        List<TextureCoordinates> texList = new ArrayList<>();
        SparseArray<List<PTNVertex>> samePosIndexVertexMap = new SparseArray<>();
        try {
            InputStream in = context.getResources().getAssets().open(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (split.length < 1) {
                    continue;
                }
                String tag = split[0].trim();
                if ("v".equals(tag)) { // pos
                    posList.add(new Point(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
                } else if ("vt".equals(tag)) { // st
                    texList.add(new TextureCoordinates(Float.parseFloat(split[1]) / 2.0f, Float.parseFloat(split[2]) / 2.0f));
                } else if ("f".equals(tag)) { //三角形面  索引数据
                    PTNVertex vertexA = new PTNVertex();
                    String[] aIndexInfo = split[1].split("/");
                    // 顶点 A 的 位置 和 纹理坐标
                    int posIndexOfA = Integer.parseInt(aIndexInfo[0]) - 1;
                    vertexA.setPosition(posList.get(posIndexOfA));
                    vertexA.setTextureCoordinates(texList.get(Integer.parseInt(aIndexInfo[1]) - 1));

                    PTNVertex vertexB = new PTNVertex();
                    String[] bIndexInfo = split[2].split("/");
                    // 顶点 B 的 位置 和 纹理坐标
                    int posIndexOfB = Integer.parseInt(bIndexInfo[0]) - 1;
                    vertexB.setPosition(posList.get(posIndexOfB));
                    vertexB.setTextureCoordinates(texList.get(Integer.parseInt(bIndexInfo[1]) - 1));

                    PTNVertex vertexC = new PTNVertex();
                    String[] cIndexInfo = split[3].split("/");
                    // 顶点 C 的 位置 和 纹理坐标
                    int posIndexOfC = Integer.parseInt(cIndexInfo[0]) - 1;
                    vertexC.setPosition(posList.get(posIndexOfC));
                    vertexC.setTextureCoordinates(texList.get(Integer.parseInt(cIndexInfo[1]) - 1));

                    Vector normal = Geometry
                            .vectorBetween(vertexA.getPosition(), vertexB.getPosition())
                            .crossProduct(Geometry.vectorBetween(vertexA.getPosition(), vertexC.getPosition()));
                    vertexA.setNormal(normal);
                    vertexB.setNormal(normal);
                    vertexC.setNormal(normal);

                    vertexList.add(vertexA);
                    vertexList.add(vertexB);
                    vertexList.add(vertexC);

                    //建立位置引索对应的顶点集合，用于后面调整顶点法向量
                    List<PTNVertex> ptnVertices = samePosIndexVertexMap.get(posIndexOfA);
                    if (ptnVertices == null) {
                        ptnVertices = new ArrayList<>();
                        samePosIndexVertexMap.put(posIndexOfA, ptnVertices);
                    }
                    ptnVertices.add(vertexA);

                    List<PTNVertex> ptnVertices2 = samePosIndexVertexMap.get(posIndexOfB);
                    if (ptnVertices2 == null) {
                        ptnVertices2 = new ArrayList<>();
                        samePosIndexVertexMap.put(posIndexOfB, ptnVertices2);
                    }
                    ptnVertices2.add(vertexB);

                    List<PTNVertex> ptnVertices3 = samePosIndexVertexMap.get(posIndexOfC);
                    if (ptnVertices3 == null) {
                        ptnVertices3 = new ArrayList<>();
                        samePosIndexVertexMap.put(posIndexOfC, ptnVertices3);
                    }
                    ptnVertices3.add(vertexC);
                }
            }
            //调整顶点法向量(法向量相加后归一化)
            adjustNormal(samePosIndexVertexMap);
            return vertexList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void adjustNormal(SparseArray<List<PTNVertex>> ptnVerticesMap) {
        int size = ptnVerticesMap.size();
        for (int i = 0; i < size; i++) {
            List<PTNVertex> ptnVertices = ptnVerticesMap.valueAt(i);
            Vector normal = getFinalNormal(ptnVertices);
            for (int j = 0; j < ptnVertices.size(); j++) {
                PTNVertex ptnVertex = ptnVertices.get(j);
                ptnVertex.setNormal(normal);
            }
        }
    }

    private static Vector getFinalNormal(List<PTNVertex> ptnVertices) {
        float[] normal = new float[3];
        for (int i = 0; i < ptnVertices.size(); i++) {
            Vector vec = ptnVertices.get(i).getNormal();
            normal[0] += vec.x;
            normal[1] += vec.y;
            normal[2] += vec.z;
        }
        return new Vector(normal[0], normal[1], normal[2]).normalize();
    }


}
