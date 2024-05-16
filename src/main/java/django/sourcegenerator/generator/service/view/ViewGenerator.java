package django.sourcegenerator.generator.service.view;

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
public class ViewGenerator {
    String input = "<div class=\"form-group\">\n\t<label class=\"form-label\" >#label#</label>\n\t<input type=\"#type#\" name=\"#name#\" class=\"form-control\">\n</div>\n";
    String inputUpdate = "<div class=\"form-group\">\n\t<label class=\"form-label\" >#label#</label>\n\t<input type=\"#type#\" name=\"#name#\" class=\"form-control\"  value = \"{{ object.#name# }}\">\n</div>\n";
    String select = "<div class=\"form-group\">\n\t<label class=\"form-label\">#label#</label>\n\t<select class=\"form-control\" name=\"#name#\">\n#option#\n\t</select>\n</div>\n";
    String option = "{%for elt in #name# %}\n\t<option value = \"{{elt.#id#}}\">{{elt.#attribute#}}</option>\n{%endfor%}";
    String optionUpdate = "{%for elt in #name# %}\n\t{%if elt.#id# == object.#id# %}\n\t\t<option value = \"{{elt.#id#}}\" selected>{{elt.#attribute#}}</option>\n\t{%else%}\n\t\t<option value = \"{{elt.#id#}}\" >{{elt.#attribute#}}</option>\n\t{%endif%}\n{%endfor%}";
    String row = "<td>{{elt.#name#}}</td>\n";
    String header = "<th>#label#</th>";
    DbConnection dbConnection;
    TypeProperties typeProperties = new TypeProperties();

    
    public String generateInput(
        String table, 
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
        ) throws Exception{
        String res = "";
        String temp = "";
        for (Map.Entry<String, String> set : columns.entrySet()) {
            temp = foreignKeys.get(set.getKey());
            if(set.getKey().equals(primaryKeys.get(0))){ 
                continue;
            }
            if(temp != null){
                res += this.getSelect()
                .replace("#label#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(set.getKey())))
                .replace("#name#", set.getKey())
                .replace("#option#", this.generateOption(temp));
                continue;
            }
            res += this.getInput()
            .replace("#label#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(set.getKey())))
            .replace("#type#", this.getTypeProperties().getMapping().get(set.getValue().split("\\.")[set.getValue().split("\\.").length -1]))
            .replace("#name#", set.getKey());
        }
        return Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(res)))))));
    }

    public String generateInputUpdate(
        String table, 
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
        ) throws Exception{
        String res = "";
        String temp = "";
        for (Map.Entry<String, String> set : columns.entrySet()) {
            temp = foreignKeys.get(set.getKey());
            if(set.getKey().equals(primaryKeys.get(0))){ 
                res += this.getInputUpdate()
                .replace("#label#", "")
                .replace("#type#", "hidden")
                .replace("#name#", set.getKey());
                continue;
            }
            if(temp != null){
                res += this.getSelect()
                .replace("#label#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(set.getKey())))
                .replace("#name#", set.getKey())
                .replace("#option#", this.generateOptionUpdate(temp));
                continue;
            }
            res += this.getInputUpdate()
            .replace("#label#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(set.getKey())))
            .replace("#type#", this.getTypeProperties().getMapping().get(set.getValue().split("\\.")[set.getValue().split("\\.").length -1]))
            .replace("#name#", set.getKey());
        }
        return Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate((res)))))));
    }

    public String generateOption(String table) throws Exception{
        String primaryKey = DbService.getPrimaryKey(this.getDbConnection(), table).get(0); 
        String attribute = DbService.getAttribute(this.getDbConnection(), table);
        String res = this.getOption()
            .replace("#name#", table)
            .replace("#id#", primaryKey)
            .replace("#attribute#", attribute);
        return (Misc.tabulate(Misc.tabulate(res)));
    }

    public String generateOptionUpdate(String table) throws Exception{
        String primaryKey = DbService.getPrimaryKey(this.getDbConnection(), table).get(0); 
        String attribute = DbService.getAttribute(this.getDbConnection(), table);
        String res = this.getOptionUpdate()
            .replace("#name#", table)
            .replace("#id#", primaryKey)
            .replace("#attribute#", attribute);
        return (Misc.tabulate(Misc.tabulate(res)));
    }

    public String generateRows(
        String table, 
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
        ) throws Exception{
        String res = "";
        String temp = "";
        String attribute = "";
        for (Map.Entry<String, String> set : columns.entrySet()) {
            temp = foreignKeys.get(set.getKey());
            if(temp != null){
                attribute = DbService.getAttribute(dbConnection, temp);
                res += this.getRow().replace("#name#", set.getKey() + "." + attribute);
                continue;
            }
            res += this.getRow().replace("#name#", set.getKey());
        }
        return Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate((Misc.tabulate(res))))))))))));
    }

    public String generateHeader(String table, HashMap<String, String> columns) throws Exception{
        String res = "";
        for (Map.Entry<String, String> set : columns.entrySet()) {
            res += this.getHeader()
            .replace("#label#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(set.getKey())));
            res += "\n";
        }
        return Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(Misc.tabulate(res)))))))));
    }

    public String generateRD(
        String table, 
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
        ) throws Exception{
        String template = Misc.getViewTemplateLocation() + File.separator + "html/read-delete.txt";
        String res = FileUtility.readOneFile(template);
        res = res
            .replace("#id#", primaryKeys.get(0))
            .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(table)))
            .replace("#name#", ObjectUtility.formatToCamelCase(table))
            .replace("#rows#", this.generateRows(table, foreignKeys, columns, primaryKeys))
            .replace("#header#", this.generateHeader(table, columns));
        return res;
    }

    public String generateCREATE(
        String table, 
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
    ) throws Exception{        
        String template = Misc.getViewTemplateLocation() + File.separator + "html/create.txt";
        String res = FileUtility.readOneFile(template);
        res = res
            .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(table)))            
            .replace("#name#", ObjectUtility.formatToCamelCase(table))
            .replace("#input#", this.generateInput(table, foreignKeys, columns, primaryKeys));
        return res;
    }

    public String generateUPDATE(
        String table, 
        HashMap<String, String> foreignKeys,  
        HashMap<String, String> columns, 
        List<String> primaryKeys
        ) throws Exception{
        String template = Misc.getViewTemplateLocation() + File.separator + "html/update.txt";
        String res = FileUtility.readOneFile(template);
        res = res
            .replace("#name#", ObjectUtility.formatToCamelCase(table))
            .replace("#input#", this.generateInputUpdate(table, foreignKeys, columns, primaryKeys))
            .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(table)));
        return res;
    }
}
