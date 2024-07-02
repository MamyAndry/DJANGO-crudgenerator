/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.generator;


import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import django.sourcegenerator.database.DbConnection;
import django.sourcegenerator.generator.parser.FileUtility;
import django.sourcegenerator.generator.service.DbService;
import django.sourcegenerator.generator.service.controller.ControllerGenerator;
import django.sourcegenerator.generator.service.form.FormGenerator;
import django.sourcegenerator.generator.service.models.ModelGenerator;
import django.sourcegenerator.generator.service.routes.RoutesGenerator;
import django.sourcegenerator.generator.service.view.ViewGenerator;
import django.sourcegenerator.utils.ObjectUtility;


/**
 * @author Mamisoa
 */
@Getter @Setter
public class CodeGenerator {
    DbConnection dbConnection;


    public CodeGenerator() throws Exception {
        this.dbConnection = new DbConnection();
        this.dbConnection.init();
    }

    public String buildModel(String path, String[] tables) throws Exception{
        ModelGenerator model = new ModelGenerator();
        return model.generateModelGenerator(path, tables);
    }

    public void generateModelGeneratorFile(String path, String projectName, String[] tables) throws Exception{
        FileUtility.createDirectory("", path);
        FileUtility.generateFile(path, "model_generator.txt", buildModel(projectName, tables));
    }

    public String buildController(String table) throws Exception{
        ControllerGenerator controller = new ControllerGenerator();
        return controller.generateController(table, this.getDbConnection());
    }

    public void generateControllerFile(String table, String path) throws Exception{
        FileUtility.generateFile(path, ObjectUtility.formatToCamelCase(table) + "View.py", buildController(table));
    }

    public String buildForm(String table) throws Exception{
        FormGenerator Form = new FormGenerator();
        return Form.generateForm(table, this.getDbConnection());
    }

    public void generateFormFile(String table, String path) throws Exception{
        FileUtility.generateFile(path, ObjectUtility.formatToCamelCase(table) + "Form.py", buildForm(table));
    }

    public String buildRoutes(String[] tables, String projectName) throws Exception{
        RoutesGenerator routes = new RoutesGenerator();
        return routes.generateRoutes(tables, projectName); 
    }

    public void generateRoutesFile(String[] tables, String path, String projectName) throws Exception{
        FileUtility.generateFile(path, "urls.py", buildRoutes(tables, projectName));
    }

    public String buildSideBar(String[] tables) throws Exception{
        RoutesGenerator routes = new RoutesGenerator();
        return routes.generateSideBar(tables); 
    }

    public void generateSideBarFile(String[] tables, String path) throws Exception{
        FileUtility.generateFile(path , "side-bar.html", this.buildSideBar(tables));
    }

    public String buildRD(
        String table,
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
    ) throws Exception{
        ViewGenerator view = new ViewGenerator();        
        view.setDbConnection(this.getDbConnection());
        return view.generateRD(table, foreignKeys, columns, primaryKeys);
    }

    public void generateRD(String table,String path) throws Exception{
        HashMap<String, String> foreignKeys = DbService.getForeignKeys(this.getDbConnection(), table); 
        HashMap<String, String> columns = DbService.getDetailsColumn(this.getDbConnection().getConnection(), table); 
        List<String> primaryKeys = DbService.getPrimaryKey(dbConnection, table);
        String content = buildRD(table, foreignKeys, columns, primaryKeys);
        FileUtility.createDirectory("", path);
        FileUtility.createDirectory("", path + File.separator + table);
        FileUtility.generateFile(path + File.separator + table, ObjectUtility.formatToCamelCase(table) + ".html", content);
    }

    public String buildUpdate(
        String table,
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
    ) throws Exception{
        ViewGenerator view = new ViewGenerator();        
        view.setDbConnection(this.getDbConnection());
        return view.generateUPDATE(table, foreignKeys, columns, primaryKeys);
    }

    public void generateUpdate(String table,String path) throws Exception{
        HashMap<String, String> foreignKeys = DbService.getForeignKeys(this.getDbConnection(), table); 
        HashMap<String, String> columns = DbService.getDetailsColumn(this.getDbConnection().getConnection(), table); 
        List<String> primaryKeys = DbService.getPrimaryKey(dbConnection, table);
        String content = buildUpdate(table, foreignKeys, columns, primaryKeys);
        FileUtility.generateFile(path + File.separator + table, "update-" + ObjectUtility.formatToCamelCase(table) + ".html", content);
    }

    public String buildCreate(
        String table,
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
    ) throws Exception{
        ViewGenerator view = new ViewGenerator();        
        view.setDbConnection(this.getDbConnection());
        return view.generateCREATE(table, foreignKeys, columns, primaryKeys);
    }

    public void generateCreate(String table,String path) throws Exception{
        HashMap<String, String> foreignKeys = DbService.getForeignKeys(this.getDbConnection(), table); 
        HashMap<String, String> columns = DbService.getDetailsColumn(this.getDbConnection().getConnection(), table); 
        List<String> primaryKeys = DbService.getPrimaryKey(dbConnection, table);
        String content = buildCreate(table, foreignKeys, columns, primaryKeys);
        FileUtility.generateFile(path + File.separator + table, "create-" + ObjectUtility.formatToCamelCase(table) + ".html", content);
    }

    public void generate(String path, String viewPath, String projectName, String[] tables) throws Exception{
        this.generateModelGeneratorFile(path, projectName, tables);
        this.generateSideBarFile(tables, viewPath);
        this.generateRoutesFile(tables, path, projectName);
        String backPath= "";
        for (String table : tables) {
            backPath = path + File.separator + table;
            FileUtility.createDirectory("", backPath);
            FileUtility.generateFile(backPath, "__init__.py", "");

            this.generateFormFile(table, backPath);
            this.generateControllerFile(table, backPath);
            this.generateRD(table, viewPath);
            this.generateCreate(table, viewPath);
            this.generateUpdate(table, viewPath);
        }
    }
}
