/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.generator.parser;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 *
 * @author Mamisoa
 */
public class JsonUtility {

    @SuppressWarnings("unchecked")
    public static <T> T parseJson(String path, Class<?> objectClass) throws Exception{
        // System.out.println(new FileReader(path));
        JsonReader reader = new JsonReader(new BufferedReader(new FileReader(path)));
        Object temp = new Gson().fromJson(reader, objectClass);
        return (T)temp;
    }

    public static String encodeJson(Object object) throws Exception{
        return new Gson().toJson(object);
    }
}
