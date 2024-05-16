/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.generator.service;

import java.util.HashMap;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import django.sourcegenerator.database.DbConnection;
import django.sourcegenerator.utils.ObjectUtility;

/**
 *
 * @author Mamisoa
 */
public class DbService {

    public static String getColumnType(String className) {
        String[] splited = className.split("\\.");
        return splited[splited.length - 1];
    }

    public static String formatString(String column) {
        String[] splited = column.split("_");
        if (splited.length < 2)
            return column;
        String res = splited[0];
        for (int i = 1; i < splited.length; i++) {
            res += ObjectUtility.capitalize(splited[i]);
        }
        return res;
    }

    public static List<String> getAllTables(Connection con) throws Exception {
        List<String> lst = new ArrayList<>();
        DatabaseMetaData meta = (DatabaseMetaData) con.getMetaData();
        ResultSet rs = meta.getTables(null, null, null, new String[] {
                "TABLE"
        });
        while (rs.next()) {
            lst.add(rs.getString(3));
        }
        return lst;
    }

    public static String[] getAllTablesArrays(Connection con) throws Exception {
        List<String> lst = getAllTables(con);
        String[] array = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            array[i] = lst.get(i);
        }
        return array;
    }

    public static List<String> getAllTables(DbConnection con) throws Exception {
        List<String> lst = new ArrayList<>();
        DatabaseMetaData meta = (DatabaseMetaData) con.getConnection().getMetaData();
        ResultSet rs = meta.getTables(null, null, null, new String[] {
                "TABLE"
        });
        while (rs.next()) {
            lst.add(rs.getString(3));
        }
        return lst;
    }

    public static String[] getAllTablesArrays(DbConnection con) throws Exception {
        List<String> lst = getAllTables(con);
        String[] array = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            array[i] = lst.get(i);
        }
        return array;
    }

    public static HashMap<String, String> getColumnNameAndType(Connection con, String tableName) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        String query = "SELECT * FROM " + tableName;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = rsmd.getColumnName(i);
            String value = getColumnType(rsmd.getColumnClassName(i));
            map.put(key, value);
        }
        return map;
    }

    public static HashMap<String, String> getDetailsColumn(Connection con, String tableName) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        String query = "SELECT * FROM " + tableName;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = rsmd.getColumnClassName(i);
            String column = getColumnType(rsmd.getColumnName(i));
            map.put(column, key);
        }
        return map;
    }

    public static List<String> getPrimaryKey(DbConnection dbConnection, String tableName) throws Exception {
        String query = dbConnection.getListConnection().get(dbConnection.getInUseConnection()).getDatabaseType()
                .getPrimaryKeyQuery();
        ArrayList<String> listPrimaryKeys = new ArrayList<>();
        PreparedStatement stmt = dbConnection.getConnection().prepareCall(query);
        stmt.setString(1, tableName);
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
            listPrimaryKeys.add(rs.getString(1));
        return listPrimaryKeys;
    }

    public static HashMap<String, String> getForeignKeys(DbConnection dbConnection, String tableName) throws Exception {
        String query = dbConnection.getListConnection().get(dbConnection.getInUseConnection()).getDatabaseType()
                .getForeignKeyQuery();
        HashMap<String, String> listForeignKeys = new HashMap<>();
        PreparedStatement stmt = dbConnection.getConnection().prepareCall(query);
        stmt.setString(1, tableName);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            listForeignKeys.put(rs.getString(1), rs.getString(2));
        }
        return listForeignKeys;
    }

    public static String getType(String str) {
        return str.split("\\.")[str.split("\\.").length - 1];
    }

    public static List<String> getPrimaryKeyType(DbConnection dbConnection, String tableName) throws Exception {
        ArrayList<String> listPrimaryKeysType = new ArrayList<>();
        HashMap<String, String> map = getDetailsColumn(dbConnection.getConnection(), tableName);
        List<String> primaryKeys = getPrimaryKey(dbConnection, tableName);
        for (Map.Entry<String, String> set : map.entrySet()) {
            if (primaryKeys.contains(set.getKey())) {
                listPrimaryKeysType.add(getType(set.getValue()));
            }
        }
        return listPrimaryKeysType;
    }

    public static String getAttribute(DbConnection dbConnection, String table) throws Exception{
        String attribute = "";
        HashMap<String, String> tempColumns = DbService.getDetailsColumn(dbConnection.getConnection(), table);
        for (Map.Entry<String, String> set : tempColumns.entrySet()) {
            if(set.getValue().equals("java.lang.String")){
                attribute = set.getKey();
                break;
            }
        }
        return attribute;
    }
}
