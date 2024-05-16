/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.database;

import java.sql.SQLException;

import django.sourcegenerator.generator.parser.FileUtility;
import django.sourcegenerator.properties.DatabaseType;
import django.sourcegenerator.utils.Misc;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author Mamisoa
 */
public class DbProperties {
    String database;
    String datasource;
    String username;
    String password;
    DatabaseType databaseType;

    //SETTERS and GETTERS

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    //CONSTRUCTOR
    public DbProperties(){}
    
    //FUNCTION

    public Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName(this.getDatabaseType().getDriver());
        Connection con = DriverManager.getConnection(this.getDatasource(),this.getUsername(),this.getPassword());
        return con;
    }

    public void addConnection(String connectionName) throws Exception{
        String separator = File.separator;
        String path = Misc.getConnectionConfLocation() + separator + "database.json";
        String con = FileUtility.readOneFile(path);
        String end = con.substring(con.length() - 9, con.length() -1);
        con = con.substring(0, con.length() - 9);
        con += ",\n\t\t";
        con += "\""+connectionName+"\": {\n";
        con += "\t\t\t\"datasource\": \""+ this.getDatasource() +"\",\n";
        con += "\t\t\t\"username\": \""+ this.getUsername() +"\",\n";
        con += "\t\t\t\"password\": \""+ this.getPassword() +"\",\n";
        con += "\t\t\t\"databaseType\": \"" + this.getDatabaseType() + "\"\n\t\t}";
        con += end;
        FileUtility.createFile(Misc.getConnectionConfLocation() , "database.json");
        FileUtility.writeFile(path, con);
    }
}
