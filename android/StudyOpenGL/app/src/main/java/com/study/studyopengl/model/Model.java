package com.study.studyopengl.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2018/4/7 23:24
 */

public class Model {
    private static final String WHITE_SPACE_PATTERN = "[\\s]+";
    private static final String COMMAND_POSITION = "v";
    private static final String COMMAND_TEXCOORD = "vt";
    private static final String COMMAND_NORMAL = "vn";
    private static final String COMMAND_FACE = "f";
    private static final String COMMAND_USE_MATERIAL = "usemtl";
    private static final String COMMAND_MATERIAL_LIB = "mtllib";

    private static final String COMMAND_NEW_MATERIAL = "newmtl";
    private static final String COMMAND_AMBIENT_COLOR = "Ka";
    private static final String COMMAND_DIFFUSE_COLOR = "Kd";
    private static final String COMMAND_SPECULAR_COLOR = "Ks";
    private static final String COMMAND_SPECULAR_EXPONENT = "Ns";
    private static final String COMMAND_AMBIENT_TEXTURE = "map_Ka";
    private static final String COMMAND_DIFFUSE_TEXTURE = "map_Kd";
    private static final String COMMAND_SPECULAR_TEXTURE = "map_Ks";


    static class Material {
        public Material(String name) {
            this.name = name;
        }

        public String name;
        public Vec3 Ka;// 0.588235 0.588235 0.588235
        public Vec3 Kd;// 1 1 1
        public Vec3 Ks;// 0.90.90.9
        public float Ns;// 4
        public String map_Kd;// n_203
        public String map_kS;//
        public String map_kA;//
    }

    static class Mesh {
        List<Vertex> vertices = new ArrayList<>();
        Material material;

        public void addVertex(Vertex vertex) {
            this.vertices.add(vertex);
        }
    }

    class Vertex {
        Vec3 position;
        Vec3 normal;
        Vec2 texCoord;
    }

    static class Texture {
        public static String TYPE_TEXTURE_DIFFUSE = "texture_diffuse";
        public static String TYPE_TEXTURE_SPECULAR = "texture_specular";
        public static String TYPE_TEXTURE_NORMAL = "texture_normal";
        public int id;
        public String type;
        public String path;
    }


    private String mDirectory;
    private List<Mesh> mMeshes;

    public Model(Context context, String path) {
        this.mDirectory = path.substring(0, path.lastIndexOf('/'));
        this.mMeshes = new ArrayList<>();
        loadModel(context, path);
    }


    private void loadModel(Context context, String path) {
        List<Vec3> positions = new ArrayList<>();
        List<Vec3> normals = new ArrayList<>();
        List<Vec2> texCoords = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            Mesh mesh = null;
            while ((line = reader.readLine()) != null) {
                String[] splits = line.trim().split(WHITE_SPACE_PATTERN);
                if (COMMAND_POSITION.equals(splits[0])) {
                    positions.add(new Vec3(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
                } else if (COMMAND_TEXCOORD.equals(splits[0])) {
                    texCoords.add(new Vec2(Float.parseFloat(splits[1]), Float.parseFloat(splits[2])));
                } else if (COMMAND_NORMAL.equals(splits[0])) {
                    normals.add(new Vec3(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
                } else if (COMMAND_FACE.equals(splits[0])) {
                    if (mesh == null) {
                        mesh = new Mesh();
                        mMeshes.add(mesh);
                    }
                    for (int i = 1; i < splits.length; i++) {
                        String[] indexs = splits[i].split("/");
                        Vertex vertex = new Vertex();
                        for (int j = 0; j < indexs.length; j++) {
                            int index = Integer.parseInt(indexs[j]);
                            if (j == 0) {
                                vertex.position = positions.get(index);
                            } else if (j == 1) {
                                vertex.texCoord = texCoords.get(index);
                            } else if (j == 2) {
                                vertex.normal = normals.get(index);
                            }
                        }
                        // TODO: 2018/4/9  是否生成法向量

                        mesh.addVertex(vertex);
                    }
                } else if (COMMAND_MATERIAL_LIB.equals(splits[0])) {
                    loadMaterials(context, materials, splits[1]);
                    // TODO: 2018/4/9 加载纹理

                } else if (COMMAND_USE_MATERIAL.equals(splits[0])) {
                    mesh = new Mesh();
                    mMeshes.add(mesh);
                    mesh.material = getMaterial(materials, splits[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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


    private Material getMaterial(List<Material> materials, String name) {
        if (materials != null && !materials.isEmpty()) {
            for (Material m : materials) {
                if (m.name.equals(name)) {
                    return m;
                }
            }
        }
        return null;
    }


    private void loadMaterials(Context context, List<Material> materials, String libraryName) {
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(libraryName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            Material material = null;
            while ((line = reader.readLine()) != null) {
                String[] split = line.trim().split(WHITE_SPACE_PATTERN);
                if (COMMAND_NEW_MATERIAL.equalsIgnoreCase(split[0])) {
                    material = new Material(split[1]);
                    materials.add(material);
                } else if (COMMAND_AMBIENT_COLOR.equalsIgnoreCase(split[0])) {
                    if (material != null) {
                        material.Ka = new Vec3(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        );
                    }
                } else if (COMMAND_DIFFUSE_COLOR.equalsIgnoreCase(split[0])) {
                    if (material != null) {
                        material.Kd = new Vec3(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        );
                    }
                } else if (COMMAND_SPECULAR_COLOR.equalsIgnoreCase(split[0])) {
                    if (material != null) {
                        material.Ks = new Vec3(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        );
                    }
                } else if (COMMAND_SPECULAR_EXPONENT.equals(split[0])) {
                    if (material != null) {
                        material.Ns = Float.parseFloat(split[1]);
                    }
                } else if (COMMAND_AMBIENT_TEXTURE.equals(split[0])) {
                    if (material != null && split.length > 1) {
                        material.map_kA = split[1];
                    }
                } else if (COMMAND_DIFFUSE_TEXTURE.equals(split[0])) {
                    if (material != null && split.length > 1) {
                        material.map_Kd = split[1];
                    }
                } else if (COMMAND_SPECULAR_TEXTURE.equals(split[0])) {
                    if (material != null && split.length > 1) {
                        material.map_kS = split[1];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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


}
