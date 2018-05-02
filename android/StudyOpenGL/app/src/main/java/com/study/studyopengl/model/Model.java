package com.study.studyopengl.model;

import android.content.Context;
import android.os.Environment;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.util.TextureHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

/**
 * obj mtl 模型加载
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
    private static final String COMMAND_DIFFUSE_TEXTURE = "map_Kd";
    private static final String COMMAND_SPECULAR_TEXTURE = "map_Ks";

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private String mDirectory;
    private List<Mesh> mMeshes;
    private VertexArray vertexArray;
    private List<DrawCommand> drawCommands = new ArrayList<>();

    public Model(Context context, String path) {
        int i = path.lastIndexOf('/');
        this.mDirectory = i != -1 ? path.substring(0, i) + "/" : "";
        this.mMeshes = new ArrayList<>();
        loadModel(context, path);

        prepareData();

    }

    private int getTotalVertexCount() {
        int count = 0;
        for (Mesh mesh : mMeshes) {
            count += mesh.vertices.size();
        }
        return count;
    }

    private void prepareData() {
        int totalVertexCount = getTotalVertexCount();
        float[] vertexData = new float[totalVertexCount * TOTAL_COMPONENT_COUNT];
        int startVertex = 0;
        Collections.sort(mMeshes, new Comparator<Mesh>() {
            @Override
            public int compare(Mesh o1, Mesh o2) {
                return o1.material.name.compareTo(o2.material.name);
            }
        });
        int offset = 0;
        for (Mesh mesh : mMeshes) {
            for (int i = 0; i < mesh.vertices.size(); i++) {
                Vertex vertex = mesh.vertices.get(i);
                vertexData[offset++] = vertex.position.x;
                vertexData[offset++] = vertex.position.y;
                vertexData[offset++] = vertex.position.z;

                vertexData[offset++] = vertex.texCoord.s;
                vertexData[offset++] = vertex.texCoord.t;

                vertexData[offset++] = vertex.normal.x;
                vertexData[offset++] = vertex.normal.y;
                vertexData[offset++] = vertex.normal.z;

            }
            drawCommands.add(new DrawCommand(startVertex, mesh.vertices.size(), mesh.material));
            startVertex += mesh.vertices.size();
            mesh.vertices.clear();
        }
        vertexArray = new VertexArray(vertexData);
    }

    public void bindData(ModelProgram program) {
        int dataOffset = 0;
        vertexArray.setVertexAttributePointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(
                dataOffset,
                program.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
        dataOffset += TEXTURE_COORDINATES_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(
                dataOffset,
                program.getNormalAttributeLocation(),
                NORMAL_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(ModelProgram program) {
        for (DrawCommand drawCmd : drawCommands) {
            drawCmd.draw(program);
        }
    }

    private class DrawCommand {
        private int startVertex;
        private int numVertices;
        private Material material;

        DrawCommand(int startVertex, int numVertices, Material material) {
            this.startVertex = startVertex;
            this.numVertices = numVertices;
            this.material = material;
        }

        public void draw(ModelProgram program) {
            program.setMaterial(material);
            glDrawArrays(GL_TRIANGLES, startVertex, numVertices);
        }
    }


    static class Material {
        Material(String name) {
            this.name = name;
        }

        String name;
        Color Ka;// 0.588235 0.588235 0.588235
        Color Kd;// 1 1 1
        Color Ks;// 0.90.90.9
        float Ns;// 4
        List<Texture> textures = new ArrayList<>();

        //public String map_Kd;// n_203
        //public String map_kS;//
        //public String map_kA;//
        void addTexture(Texture texture) {
            this.textures.add(texture);
        }

        int getTextureId(String type) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i).type.equals(type)) {
                    return textures.get(i).id;
                }
            }
            return 0;
        }
    }

    static class Mesh {

        List<Vertex> vertices = new ArrayList<>();
        Material material;


        void addVertex(Vertex vertex) {
            this.vertices.add(vertex);
        }

    }

    class Vertex {
        public Vec3 position;
        public Vec3 normal;
        public TexCoord texCoord;

        Vertex(Vec3 position, TexCoord texCoord, Vec3 normal) {
            this.position = position;
            this.texCoord = texCoord;
            this.normal = normal;
        }
    }

    public static class Texture {
        static String TYPE_TEXTURE_DIFFUSE = "texture_diffuse";
        static String TYPE_TEXTURE_SPECULAR = "texture_specular";
        public int id;
        public String type;
        public String path;

        Texture(int id, String type, String path) {
            this.id = id;
            this.type = type;
            this.path = path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Texture texture = (Texture) o;
            return path != null ? path.equals(texture.path) : texture.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private void loadModel(Context context, String path) {
        List<Vec3> positions = new ArrayList<>();
        List<Vec3> normals = new ArrayList<>();
        List<TexCoord> texCoords = new ArrayList<>();
        Map<String, Material> materialMap = new HashMap<>();
        Map<String, Mesh> meshMap = new HashMap<>();
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            Mesh mesh = null;
            boolean hasNormal = false;
            Map<Integer, List<Vec3>> posIndexNormalsMap = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                String[] splits = line.trim().split(WHITE_SPACE_PATTERN);
                if (COMMAND_POSITION.equals(splits[0])) {
                    positions.add(new Vec3(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
                } else if (COMMAND_TEXCOORD.equals(splits[0])) {
                    texCoords.add(new TexCoord(Float.parseFloat(splits[1]), Float.parseFloat(splits[2])));
                } else if (COMMAND_NORMAL.equals(splits[0])) {
                    normals.add(new Vec3(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
                } else if (COMMAND_FACE.equals(splits[0])) {
                    if (mesh == null) {
                        mesh = new Mesh();
                        mMeshes.add(mesh);
                    }
                    String[] ptnIndex = splits[1].split("/");
                    String[] ptnIndex2 = splits[2].split("/");
                    String[] ptnIndex3 = splits[3].split("/");
                    int[] posIndexes = new int[]{Integer.parseInt(ptnIndex[0]) - 1, Integer.parseInt(ptnIndex2[0]) - 1, Integer.parseInt(ptnIndex3[0]) - 1};
                    int[] texIndexes = new int[]{Integer.parseInt(ptnIndex[1]) - 1, Integer.parseInt(ptnIndex2[1]) - 1, Integer.parseInt(ptnIndex3[1]) - 1};

                    Vec3 position = positions.get(posIndexes[0]);
                    Vec3 position2 = positions.get(posIndexes[1]);
                    Vec3 position3 = positions.get(posIndexes[2]);

                    TexCoord texCoord = texCoords.get(texIndexes[0]);
                    TexCoord texCoord2 = texCoords.get(texIndexes[1]);
                    TexCoord texCoord3 = texCoords.get(texIndexes[2]);

                    Vertex vertex = new Vertex(position, texCoord, null);
                    Vertex vertex2 = new Vertex(position2, texCoord2, null);
                    Vertex vertex3 = new Vertex(position3, texCoord3, null);
                    Vertex[] vertexes = new Vertex[]{vertex, vertex2, vertex3};
                    hasNormal = ptnIndex.length > 2;
                    if (hasNormal) {
                        int[] normalIndexes = new int[]{Integer.parseInt(ptnIndex[2]) - 1, Integer.parseInt(ptnIndex2[2]) - 1, Integer.parseInt(ptnIndex3[2]) - 1};
                        for (int i = 0; i < 3; i++) {
                            vertexes[i].normal = normals.get(normalIndexes[i]);
                            addNormalForAverage(posIndexNormalsMap, posIndexes[i], vertexes[i].normal);
                        }
                    } else {
                        Vec3 n = VectorHelper.vectorBetween(position, position2).crossProduct(VectorHelper.vectorBetween(position, position3));
                        for (int i = 0; i < 3; i++) {
                            vertexes[i].normal = new Vec3(n.x, n.y, n.z);
                            addNormalForAverage(posIndexNormalsMap, posIndexes[i], vertexes[i].normal);
                        }
                    }
                    mesh.addVertex(vertex);
                    mesh.addVertex(vertex2);
                    mesh.addVertex(vertex3);
                } else if (COMMAND_MATERIAL_LIB.equals(splits[0])) {
                    loadMaterials(context, materialMap, mDirectory + splits[1]);
                } else if (COMMAND_USE_MATERIAL.equals(splits[0])) {
                    mesh = meshMap.get(splits[1]);
                    if (mesh == null) {
                        mesh = new Mesh();
                        mesh.material = materialMap.get(splits[1]);
                        meshMap.put(splits[1], mesh);
                        mMeshes.add(mesh);
                    }
                }
            }

            averageNormals(posIndexNormalsMap);

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

    private void addNormalForAverage(Map<Integer, List<Vec3>> posIndexNormalsMap, int posIndex, Vec3 normal) {
        List<Vec3> normalsPreAverage = posIndexNormalsMap.get(posIndex);
        if (normalsPreAverage == null) {
            normalsPreAverage = new ArrayList<>();
            //一个顶点可以对应多个法向量
            posIndexNormalsMap.put(posIndex, normalsPreAverage);
        }
        normalsPreAverage.add(normal);
    }

    private void averageNormals(Map<Integer, List<Vec3>> posIndexNormalsMap) {
        for (List<Vec3> normals : posIndexNormalsMap.values()) {
            Vec3 sum = new Vec3();
            //把集合中所有的法向量求和
            for (Vec3 n : normals) {
                sum.x += n.x;
                sum.y += n.y;
                sum.z += n.z;
            }
            Vec3 averageNormal = sum.normalize();
            for (Vec3 n : normals) {
                n.x += averageNormal.x;
                n.y += averageNormal.y;
                n.z += averageNormal.z;
            }
        }
    }

    private Map<String, Integer> loadedTextures = new HashMap<>();

    //加载材质库
    private void loadMaterials(Context context, Map<String, Material> materials, String libraryName) {
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(libraryName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            Material material = null;
            while ((line = reader.readLine()) != null) {
                String[] split = line.trim().split(WHITE_SPACE_PATTERN);
                if (COMMAND_NEW_MATERIAL.equalsIgnoreCase(split[0])) {
                    material = materials.get(split[1]);
                    if (material == null) {
                        material = new Material(split[1]);
                        materials.put(split[1], material);
                    }
                } else if (COMMAND_AMBIENT_COLOR.equalsIgnoreCase(split[0])) {
                    if (material != null) {
                        material.Ka = new Color(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        );
                    }
                } else if (COMMAND_DIFFUSE_COLOR.equalsIgnoreCase(split[0])) {
                    if (material != null) {
                        material.Kd = new Color(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        );
                    }
                } else if (COMMAND_SPECULAR_COLOR.equalsIgnoreCase(split[0])) {
                    if (material != null) {
                        material.Ks = new Color(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        );
                    }
                } else if (COMMAND_SPECULAR_EXPONENT.equals(split[0])) {
                    if (material != null) {
                        material.Ns = Float.parseFloat(split[1]);
                    }
                } else if (COMMAND_DIFFUSE_TEXTURE.equals(split[0])) {
                    if (material != null && split.length > 1) {
                        loadTexture(context, material, split[1], Texture.TYPE_TEXTURE_DIFFUSE);
                    }
                } else if (COMMAND_SPECULAR_TEXTURE.equals(split[0])) {
                    if (material != null && split.length > 1) {
                        loadTexture(context, material, split[1], Texture.TYPE_TEXTURE_SPECULAR);
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

    private void loadTexture(Context context, Material material, String path, String type) {
        int id;
        if (loadedTextures.containsKey(path)) {
            id = loadedTextures.get(path);
        } else {
            id = TextureHelper.loadTexture(context, mDirectory + path);
            loadedTextures.put(path, id);
        }
        material.addTexture(new Texture(id, type, path));
    }


}
