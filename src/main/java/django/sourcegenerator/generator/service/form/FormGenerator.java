package django.sourcegenerator.generator.service.form;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import django.sourcegenerator.database.DbConnection;
import django.sourcegenerator.generator.parser.FileUtility;
import django.sourcegenerator.generator.service.DbService;
import django.sourcegenerator.utils.Misc;
import django.sourcegenerator.utils.ObjectUtility;

public class FormGenerator {

    public String generateFields(HashMap<String, String> columns){
        String res = "";

        for(Map.Entry<String, String>  set: columns.entrySet()){
            res += "\"" + set.getKey() + "\", ";
        }
        res = res.substring(0, res.length() - 2);
        return res;
    }

    public String generateForm(String table, DbConnection con) throws Exception{
        String template = Misc.getSourceTemplateLocation().concat(File.separator).concat("form.code");
        String res = FileUtility.readOneFile(template);
        HashMap<String, String> columns = DbService.getDetailsColumn(con.getConnection(), table);
        String fields = this.generateFields(columns);
        String name = ObjectUtility.formatToCamelCase(table);
        String entity = ObjectUtility.capitalize(name);
        res = res
            .replace("#nameCamel#", name)
            .replace("#entity#", entity)
            .replace("#fields#", fields);
        return res;
    }
}
