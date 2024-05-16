package django.sourcegenerator.generator.service.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import django.sourcegenerator.database.DbConnection;
import django.sourcegenerator.generator.parser.FileUtility;
import django.sourcegenerator.generator.service.DbService;
import django.sourcegenerator.utils.Misc;
import django.sourcegenerator.utils.ObjectUtility;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ControllerGenerator{

    public String getImports(String table ,HashMap<String, String> foreignKeys) throws Exception{
        String res = "from ." + ObjectUtility.formatToCamelCase(table) + " import " + ObjectUtility.formatToCamelCase(ObjectUtility.capitalize(table)) + "\n";
        for(Map.Entry<String, String> set : foreignKeys.entrySet()) {
            res += "from ." + ObjectUtility.formatToCamelCase(set.getValue()) + " import " + ObjectUtility.formatToCamelCase(ObjectUtility.capitalize(set.getValue())) + "\n";
        }
        return res;
    }

    public String getValues(String table, HashMap<String, String> columns, HashMap<String, String> foreignKeys){
        String res = "";
        for(Map.Entry<String, String> set : columns.entrySet()) {
            String temp = foreignKeys.get(set.getKey());
            if(temp != null){
                temp = ObjectUtility.formatToCamelCase(temp);
                String Temp = ObjectUtility.capitalize(temp);
                res += "\t\t" + temp + " = " + Temp + "()\n";
                res += "\t\t" + temp + "." +set.getKey() + " = request.POST['" + set.getKey() + "']\n";
                res += "\t\t" + table + "." + set.getKey() + " = " + temp + "\n";
                continue;
            }
            res += "\t\t" + table + "." + set.getKey() + " = request.POST['" + set.getKey() + "']\n";
        }
        return res;
    }
    public String getValues2(String table, HashMap<String, String> columns, HashMap<String, String> foreignKeys, List<String> primaryKey){
        String res = "";
        for(Map.Entry<String, String> set : columns.entrySet()) {
            String temp = foreignKeys.get(set.getKey());
            if(set.getKey().equals(primaryKey.get(0))) continue;
            if(temp != null){
                temp = ObjectUtility.formatToCamelCase(temp);
                String Temp = ObjectUtility.capitalize(temp);
                res += "\t\t\t" + temp + " = " + Temp + "()\n";
                res += "\t\t\t" + temp + "." +set.getKey() + " = request.POST['" + set.getKey() + "']\n";
                res += "\t\t\t" + table + "." + set.getKey() + " = " + temp + "\n";
                continue;
            }
            res += "\t\t\t" + table + "." + set.getKey() + " = request.POST['" + set.getKey() + "']\n";
        }
        return res;
    }

    public String getForeigns(HashMap<String, String> foreignKeys) throws Exception{
        String res = "";
        String temp = "";
        if(foreignKeys.isEmpty()) return res;
        for(Map.Entry<String, String> set : foreignKeys.entrySet()) {
            temp = ObjectUtility.formatToCamelCase(set.getValue());
            res += temp + "s = " + ObjectUtility.capitalize(temp) + ".objects.all()\n";
        }
        res = res.substring(0, res.length() - 3);
        return Misc.tabulate(res); 
    }
    
    public String getInsertContent(HashMap<String, String> foreignKeys) throws Exception{
        String res = "";
        if(foreignKeys.isEmpty()) return res;
        for(Map.Entry<String, String> set : foreignKeys.entrySet()) {
            res += "\t" + "'" + set.getValue() +  "' : " + ObjectUtility.formatToCamelCase(set.getValue()) + "s,\n";
        }
        res = res.substring(0, res.length() - 2);
        return Misc.tabulate(res); 
    }
    public String getUpdateContent(HashMap<String, String> foreignKeys) throws Exception{
        String res = "\t'object' : object,\n";
        if(foreignKeys.isEmpty()) return res;
        for(Map.Entry<String, String> set : foreignKeys.entrySet()) {
            res += "\t" + "'" + set.getValue() +  "' : " + ObjectUtility.formatToCamelCase(set.getValue()) + "s,\n";
        }
        res = res.substring(0, res.length() - 2);
        return Misc.tabulate(Misc.tabulate(res)); 
    }
    public String generateController(String table, DbConnection con)throws Exception{
        String template = Misc.getSourceTemplateLocation().concat(File.separator).concat("view.code");
        String res = FileUtility.readOneFile(template);
        HashMap<String, String> columns = DbService.getDetailsColumn(con.getConnection(), table);
        HashMap<String, String> foreignKeys = DbService.getForeignKeys(con, table);
        List<String> primaryKey = DbService.getPrimaryKey(con, table);
        String values = this.getValues(table, columns, foreignKeys);
        String values2 = this.getValues2(table, columns, foreignKeys, primaryKey);
        String insertContent = this.getInsertContent(foreignKeys);
        String updateContent = this.getUpdateContent(foreignKeys);
        String foreigns = this.getForeigns(foreignKeys);
        String imports = this.getImports(table, foreignKeys);
        res = res
            .replace("#imports#", imports)
            .replace("#name#", table)
            .replace("#nameCamel#", ObjectUtility.formatToCamelCase(table))
            .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)))
            .replace("#id#", primaryKey.get(0))
            .replace("#insert_content#", insertContent)
            .replace("#update_content#", updateContent)
            .replace("#foreigns#", foreigns)
            .replace("#values#", values)
            .replace("#values2#", values2);
        return res;
    }
}
