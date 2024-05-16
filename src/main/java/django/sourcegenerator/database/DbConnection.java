/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.database;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

import django.sourcegenerator.generator.parser.JsonUtility;
import django.sourcegenerator.utils.Misc;

/**
 *
 * @author Mamisoa
 */
public class DbConnection {
    @Getter
    final static String confPath = "database.json";
    @Getter @Setter
    String defaultConnection;
    @Getter
    String inUseConnection;
    @Getter @Setter
    public boolean init = false;
    @Setter
    public Connection connection = null;
    @Getter
    private HashMap<String, DbProperties> listConnection;
    //SETTERS & GETTERS

    public void setListConnection(HashMap<String, DbProperties> listConnection) {
        this.listConnection = listConnection;
    }

    //METHODS
    public void read()throws Exception{
        String separator = File.separator;
        String confFile = Misc.getConnectionConfLocation() + separator + getConfPath();
        DbConnection temp = JsonUtility.parseJson(confFile, this.getClass());
        this.setListConnection(temp.getListConnection());
        this.setDefaultConnection(temp.getDefaultConnection());
        this.setInUseConnection(temp.getDefaultConnection());
    }

    public void setInUseConnection(String inUseConnection){
        if(getListConnection().get(inUseConnection) != null)
            this.inUseConnection = inUseConnection;
        else throw new IllegalArgumentException("There is no such connection : "+inUseConnection);
    }

    public Connection createConnection(String connection)throws Exception{
        if(!isInit()) init();
        DbProperties prop = this.getListConnection().get(connection);
        return prop.connect();
    }

    /**
     * connect to the database by changing
     * the connection property
     * @author rakharrs
     * @return
     * @throws Exception
     */
    public Connection connect()throws Exception{
        if(!isInit()) init();
        setConnection(createConnection(getInUseConnection()));
        return getConnection();
    }

    public void init() throws Exception{
        read();
        setInit(true);
    }

    public Connection connect(String connection)throws Exception{
        if(!isInit()) init();
        setConnection(createConnection(connection));
        return getConnection();
    }

    /**
     * Check if the connection property is null or not
     * @return
     * @throws Exception
     */
    public boolean isConnected() throws Exception {
        return getConnection() != null;
    }

    /**
     * get the connection property
     * if it is undefined -> create the property
     * by connecting to the database
     * @author rakharrs
     * @return - connection property
     * @throws Exception
     */
    public Connection getConnection() throws Exception{
        if(this.connection == null) this.connect();
        return this.connection;
    }

    /**
     * @author rakharrs
     */
    public void close() throws Exception {
        getConnection().close();
    }

    /**
     * @author rakharrs
     */
    public void commit() throws Exception {
        getConnection().commit();
    }

}
