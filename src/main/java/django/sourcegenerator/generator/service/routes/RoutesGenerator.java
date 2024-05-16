package django.sourcegenerator.generator.service.routes;

import java.io.File;

import django.sourcegenerator.generator.parser.FileUtility;
import django.sourcegenerator.utils.Misc;
import django.sourcegenerator.utils.ObjectUtility;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoutesGenerator {
    String routesSyntax = "path('#entity#', #entity#View.index, name='#entity#'),\npath('#entity#/insert-#entity#', #entity#View.insertion),\npath('#entity#/insert', #entity#View.insert#Table#Form, name='insertForm-#entity#'),\npath('#entity#/updateForm/<int:id>', #entity#View.update#Table#Form, name='updateForm-#entity#'),\npath('#entity#/update-#entity#', #entity#View.update#Table#),\npath('#entity#/delete/<int:id>', #entity#View.delete#Table#),\n";
    String importSyntax  = "from #project# import  #entity#View\n";
    String pathSyntax = "<li class=\"nav-item\">\n\t<a href=\"#path#\" class=\"nav-link\">\n\t\t<i class=\"fas fa-circle nav-icon\"></i>\n\t\t<p> #Name# </p>\n\t</a>\n</li>\n";
    
    public String generatePath(String[] tables){
        String res = "";
        String temp = "";
        for (String table : tables) {
            temp = ObjectUtility.formatToCamelCase(table);
            res += this.getRoutesSyntax()
                .replace("#entity#", temp)
                .replace("#Table#", ObjectUtility.capitalize(temp));
            
        }
        return res;
    }

    public String generateImports(String[] tables, String projectName){
        String res = "";
        for (String table : tables) {
            res += this.getImportSyntax()
                .replace("#path#", table)
                .replace("#project#", projectName)
                .replace("#entity#", ObjectUtility.formatToCamelCase(table));
            
        }
        return res;
    }

    public String generateItems(String[] tables){
        String res = "";
        for (String table : tables) {
            res += this.getPathSyntax()
                .replace("#path#", ObjectUtility.formatToCamelCase(table))
                .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToSpacedString(table)));
            
        }
        return res;
    }

    public String generateRoutes(String[] tables, String projectName) throws Exception{
        String template = Misc.getViewTemplateLocation() + File.separator + "html/routes.txt";
        String res = FileUtility.readOneFile(template);
        String imports = this.generateImports(tables, projectName);
        String routes = this.generatePath(tables);
        res = res
            .replace("$-{IMPORTS}", imports)
            .replace("$-{ROUTES}", Misc.tabulate(routes));
        return res;
    }

    public String generateSideBar(String[] tables) throws Exception{
        String template = Misc.getViewTemplateLocation() + File.separator + "html/side-bar.txt";
        String res = FileUtility.readOneFile(template);
        String path = this.generateItems(tables);
        res = res
            .replace("$-{PATH}", Misc.tabulate(path));
        return res;
    }
}
