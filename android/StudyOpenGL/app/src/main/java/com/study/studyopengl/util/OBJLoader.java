package com.study.studyopengl.util;


import android.content.Context;

import com.study.studyopengl.parser.OBJModel;
import com.study.studyopengl.parser.OBJParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OBJLoader {

    public static OBJModel load(Context context, String objPath) {
        BufferedReader reader = null;
        try {
            OBJParser objParser = new OBJParser();
            InputStream in = context.getResources().getAssets().open(objPath);
            reader = new BufferedReader(new InputStreamReader(in));
            return objParser.parse(context, reader);
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
        return new OBJModel();
    }


}
