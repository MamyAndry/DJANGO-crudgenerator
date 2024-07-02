package django.sourcegenerator.generator.service.models;

import django.sourcegenerator.utils.ObjectUtility;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ModelGenerator {
    String script = "python manage.py inspectdb #table# > #path#/#entity#/#entityCamel#.py";

    public String generateModelGenerator(String path, String[] tables) throws Exception{
        String res = ""; 
        for (String table : tables) {
            res += this.getScript()
                .replace("#entity#",table)
                .replace("#entityCamel#", ObjectUtility.formatToCamelCase(table))
                .replace("#table#", table)
                .replace("#path#", path)
            + "\n";
        }
        return res;
    }

}
