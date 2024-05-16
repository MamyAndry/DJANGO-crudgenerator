package django.sourcegenerator;

import django.sourcegenerator.database.DbConnection;
import django.sourcegenerator.generator.CodeGenerator;
import django.sourcegenerator.generator.service.DbService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DbConnection con = null;
        try {
            con = new DbConnection();
            con.init();
            // String[] tables = {"region", "district"};
            String[] tables = DbService.getAllTablesArrays(con);
            String path = "./back";
            String viewPath = "./front";
            String projectName = "btp";
            CodeGenerator generator = new CodeGenerator();
            generator.setDbConnection(con);

            generator.generate(path, viewPath, projectName, tables);
        } catch (Exception e) {

        }finally{
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
