/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.generator.parser;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author Mamisoa
 */
public class FileUtility {    

    /**
     * retrieve every files of the parameter's directory
     * @param directory
     * @return array of files
     */
    public static File[] listfiles(String directory){
        File directoryPath = new File(directory);
        return directoryPath.listFiles();
    } 
    
    public static String[] splitLine(String string){
        return string.split("=");
    }

    public static void createDirectory(String directory, String path) throws Exception{
        Path directoryPath = Paths.get(path + File.separator + directory);
        // deleteRecursively(directoryPath);
        Files.createDirectories(directoryPath);
    }

    public static void deleteRecursively(Path path) throws Exception{
        if(Files.isDirectory(path))
            for(File file : Objects.requireNonNull(path.toFile().listFiles()))
                file.delete();
        Files.deleteIfExists(path);
    }
    
    public static List<String[]> readFile(String path) throws Exception{
        File myObj = new File(path);
        Scanner myReader = new Scanner(myObj);
        List<String[]> res = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            res.add(splitLine(data));
        }
        myReader.close();
        return res;
    }
    public static String readOneFile(String path) throws Exception{
        return getString(path);
    }

    public static String getString(String path) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while((line = reader.readLine()) != null){
            builder.append(line).append("\n");
        }
        reader.close();
        return builder.toString();
    }

    public static void createFile(String path, String fileName) throws Exception{
        String separator = File.separator;
        path = path + separator + fileName;
        File file = new File(path);
        System.out.println(file.getPath() + " created");
    }
    
    public static void writeFile(String path, String body) throws Exception{
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(body);
        }
    }
    
    public static void generateFile(String path, String fileName, String body) throws Exception{
        createFile(path, fileName);
        path = path + File.separator + fileName;
        writeFile(path, body);
    }
    
}
