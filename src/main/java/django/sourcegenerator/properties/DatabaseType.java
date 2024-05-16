/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package django.sourcegenerator.properties;

/**
 *
 * @author DINA
 */
public enum DatabaseType {
    SQLSERVER("sqlserver",
            "com.microsoft.sqlserver.jdbc.SQLServerDrifk.name AS column_name ,ver",
            "",
            "CREATE TABLE IF NOT EXISTS GenCrudConfig ( id INT PRIMARY KEY,table_name VARCHAR,column_name VARCHAR,method VARCHAR,jsonConf TEXT)",
            "SELECT table_name FROM information_sch ema.tables where table_catalog=? and table_schema='public'",
            "SELECT table_name FROM information_schema.tables where table_catalog=? and table_schema='public' and table_name like concat('v_%',?,'%_gc')",
            "SELECT column_name, data_type, character_maximum_length, is_nullable, column_default,udt_name FROM information_schema.columns WHERE table_schema ='public' and table_name=?",
            "SELECT tc.constraint_name,tc.table_name,kcu.column_name,ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name FROM  information_schema.table_constraints AS tc  JOIN information_schema.key_column_usage AS kcu   ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu   ON ccu.constraint_name = tc.constraint_name   AND ccu.table_schema = tc.table_schema WHERE tc.constraint_type like '%KEY%' AND tc.table_name=?",
            new String[]{},
            "SELECT c.name AS column_name,ref.name AS table_name,refc.name AS referenced_column FROM sys.foreign_keys AS fk JOIN sys.foreign_key_columns AS fkc ON fk.object_id = fkc.constraint_object_id JOIN sys.columns AS c ON fkc.parent_column_id = c.column_id AND fkc.parent_object_id = c.object_id JOIN sys.columns AS refc ON fkc.referenced_column_id = refc.column_id AND fkc.referenced_object_id = refc.object_id JOIN sys.tables AS ref ON fkc.referenced_object_id = ref.object_id WHERE fk.parent_object_id = OBJECT_ID(?)",
            new String[]{},
            "SELECT kcu.column_name AS column_name FROM  information_schema.table_constraints AS tc  JOIN information_schema.key_column_usage AS kcu   ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu   ON ccu.constraint_name = tc.constraint_name   AND ccu.table_schema = tc.table_schema WHERE tc.constraint_type='PRIMARY KEY' AND tc.table_name=?"),
    MYSQL("mysql",
            "com.mysql.cj.jdbc.Driver",
            "",
            "CREATE TABLE IF NOT EXISTS GenCrudConfig ( id INT PRIMARY KEY,table_name VARCHAR,column_name VARCHAR,method VARCHAR,jsonConf TEXT )",
            "SELECT table_name FROM information_schema.tables WHERE table_schema=?",
            "SELECT table_name FROM information_schema.tables WHERE table_schema=? and table_name like concat('v_%',?,'_gc%')",
            "SELECT  table_name ,column_name,column_type as data_type ,character_maximum_length, is_nullable,column_default  ,DATA_TYPE AS udt_name ,character_maximum_length from INFORMATION_SCHEMA. COLUMNS where table_name = ?",
            "SELECT CONSTRAINT_NAME, TABLE_NAME, COLUMN_NAME, coalesce(REFERENCED_TABLE_NAME,table_name) AS foreign_table_name, coalesce(REFERENCED_COLUMN_NAME,COLUMN_NAME) as foreign_column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE  table_name=? AND (CONSTRAINT_NAME LIKE  concat('%',?,'%') or CONSTRAINT_NAME=?)",
            new String[]{"fk", "PRIMARY KEY"},
            "SELECT kcu.column_name AS column_name, kcu.referenced_table_name AS table_name FROM information_schema.key_column_usage AS kcu JOIN information_schema.referential_constraints AS rc ON kcu.constraint_name = rc.constraint_name WHERE kcu.table_name = ?",
            new String[]{"fk"},
            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE CONSTRAINT_NAME= 'PRIMARY' AND table_name=?"),
    POSTGRESQL("postgresql",
            "org.postgresql.Driver",
            "SELCT nextval(?)",
            "CREATE TABLE IF NOT EXISTS GenCrudConfig ( id SERIAL PRIMARY KEY,table_name VARCHAR,column_name VARCHAR,method VARCHAR,jsonConf TEXT)",
            "SELECT table_name FROM information_schema.tables WHERE table_catalog=? AND table_schema='public'",
            "SELECT table_name FROM information_schema.tables WHERE table_catalog=? AND table_schema='public' and table_name like concat('v_%',?,'%_gc%')",
            "SELECT table_name, column_name, data_type, character_maximum_length, is_nullable, column_default,udt_name FROM information_schema.columns WHERE tacodeGenerator.getDbConnection().getble_schema ='public' AND table_name=?",
            "SELECT kcu.column_name FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name AND ccu.table_schema = tc.table_schema WHERE tc.table_name = 'district' AND tc.constraint_type = 'FOREIGN KEY'",
            new String[]{"KEY"},
            "SELECT kcu.column_name AS column_name, ccu.table_name AS table_name FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name AND ccu.table_schema = tc.table_schema WHERE tc.table_name = ?  AND tc.constraint_type = 'FOREIGN KEY';",
            new String[]{"KEY"},
            "SELECT kcu.column_name AS column_name FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name AND ccu.table_schema = tc.table_schema WHERE tc.constraint_type='PRIMARY KEY' AND tc.table_name=?"),
    ORACLE("oracle",
            "oracle.jdbc.driver.OracleDriver",
            "SELECT ? FROM DUAL",
            "CREATE TABLE IF NOT EXISTS GenCrudConfig ( id NUMBER PRIMARY KEY,table_name VARCHAR2,column_name VARCHAR2,method VARCHAR2,jsonConf CLOB)",
            "SELECT table_name FROM all_tables where owner=?",
            "SELECT table_name FROM all_tables where owner=? and table_name like concat('v_%',?,'%_gc%')",
            "SELECT column_name, data_type, data_length, nullable, data_default FROM all_tab_columns WHERE owner=? AND table_name=?",
            "SELECT ac.constraint_name, ac.table_name, acc.column_name, ac.owner, r_cons.table_name AS foreign_table_name, r_col.column_name AS foreign_column_name FROM all_constraints ac JOIN all_cons_columns acc ON ac.constraint_name = acc.constraint_name AND ac.owner = acc.owner LEFT JOIN all_constraints r_cons ON ac.r_constraint_name = r_cons.constraint_name AND ac.r_owner = r_cons.owner LEFT JOIN all_cons_columns r_col ON r_cons.constraint_name = r_col.constraint_name AND r_cons.owner = r_col.owner WHERE ac.constraint_type = 'R' AND ac.owner = ? AND ac.table_name = ?",
            new String[]{},
            "SELECT c.column_name AS column_name, ccu.table_name AS table_name FROM all_cons_columns c JOIN all_constraints cons ON c.constraint_name = cons.constraint_name JOIN all_cons_columns ccu ON cons.r_constraint_name = ccu.constraint_nameWHERE cons.constraint_type = 'R' AND cons.table_name = ?;",
            new String[]{},
            "SELECT acc.column_name FROM all_constraints ac JOIN all_cons_columns acc ON ac.constraint_name = acc.constraint_name AND ac.owner = acc.owner WHERE ac.constraint_type = 'P' AND ac.owner = ? AND ac.table_name = ?");

    private String name;
    private String driver;
    private String sequenceQuery;
    private String createConfigQuery;
    private String queryTable;
    private String queryCheckViews;
    private String queryColumn;
    private String queryConstraint;
    private String[] valuequeryConstraint;
    private String foreignKeyQuery;
    private String[] valueforeignKeyQuery;
    private String primaryKeyQuery;

    public String getSequenceQuery() {
        return sequenceQuery;
    }

    public void setSequenceQuery(String sequenceQuery) {
        this.sequenceQuery = sequenceQuery;
    }

    
    public String[] getValuequeryConstraint() {
        return valuequeryConstraint;
    }

    public void setValuequeryConstraint(String[] valuequeryConstraint) {
        this.valuequeryConstraint = valuequeryConstraint;
    }

    public String[] getValueforeignKeyQuery() {
        return valueforeignKeyQuery;
    }

    public void setValueforeignKeyQuery(String[] valueforeignKeyQuery) {
        this.valueforeignKeyQuery = valueforeignKeyQuery;
    }

    private DatabaseType(String name, String driver, String sequence, String createConfigQuery, String queryTable, String queryCheckViews, String queryColumn, String queryConstraint, String[] valuequeryConstraint, String foreignKeyQuery, String[] valueforeignKeyQuery, String primaryKeyQuery) {
        this.setName(name);
        this.setDriver(driver);
        this.setSequenceQuery(sequence);
        this.setCreateConfigQuery(createConfigQuery);
        this.setQueryTable(queryTable);
        this.setQueryCheckViews(queryCheckViews);
        this.setQueryColumn(queryColumn);
        this.setQueryConstraint(queryConstraint);
        this.setValuequeryConstraint(valuequeryConstraint);
        this.setForeignKeyQuery(foreignKeyQuery);
        this.setValueforeignKeyQuery(valueforeignKeyQuery);
        this.setPrimaryKeyQuery(primaryKeyQuery);
    }

    public String getQueryCheckViews() {
        return queryCheckViews;
    }

    public void setQueryCheckViews(String queryCheckViews) {
        this.queryCheckViews = queryCheckViews;
    }


    // ... getters pour les propriétés ...
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCreateConfigQuery() {
        return createConfigQuery;
    }

    public void setCreateConfigQuery(String createConfigQuery) {
        this.createConfigQuery = createConfigQuery;
    }

    public String getQueryTable() {
        return queryTable;
    }

    public void setQueryTable(String queryTable) {
        this.queryTable = queryTable;
    }

    public String getQueryColumn() {
        return queryColumn;
    }

    public void setQueryColumn(String queryColumn) {
        this.queryColumn = queryColumn;
    }

    public String getQueryConstraint() {
        return queryConstraint;
    }

    public void setQueryConstraint(String queryConstraint) {
        this.queryConstraint = queryConstraint;
    }

    public String getForeignKeyQuery() {
        return foreignKeyQuery;
    }

    public void setForeignKeyQuery(String foreignKeyQuery) {
        this.foreignKeyQuery = foreignKeyQuery;
    }

    public String getPrimaryKeyQuery() {
        return primaryKeyQuery;
    }

    public void setPrimaryKeyQuery(String primaryKeyQuery) {
        this.primaryKeyQuery = primaryKeyQuery;
    }
}
