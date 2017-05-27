package red.man10.man10mysqlapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * Created by sho-pc on 2017/05/21.
 */
public class MySQLAPI {

    public  Boolean debugMode = false;
    private JavaPlugin plugin;
    private String HOST = null;
    private String DB = null;
    private String USER = null;
    private String PASS = null;
    private String PORT = null;
    private boolean connected = false;
    private Statement st = null;
    private Connection con = null;
    private String conName;
    private MySQLFunc MySQL;

    ////////////////////////////////
    //      コンストラクタ
    ////////////////////////////////
    public MySQLAPI(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.conName = name;
        this.connected = false;
        loadConfig();

        this.connected = Connect(HOST, DB, USER, PASS,PORT);

        if(!this.connected) {
            plugin.getLogger().info("Unable to establish a MySQL connection.");
        }
    }

    /////////////////////////////////
    //       設定ファイル読み込み
    /////////////////////////////////
    public void loadConfig(){
        plugin.getLogger().info("MYSQL Config loading");
        plugin.reloadConfig();
        HOST = plugin.getConfig().getString("mysql.host");
        USER = plugin.getConfig().getString("mysql.user");
        PASS = plugin.getConfig().getString("mysql.pass");
        PORT = plugin.getConfig().getString("mysql.port");
        DB = plugin.getConfig().getString("mysql.db");
        plugin.getLogger().info("Config loaded");
    }

    ////////////////////////////////
    //       接続
    ////////////////////////////////
    public Boolean Connect(String host, String db, String user, String pass,String port) {
        this.HOST = host;
        this.DB = db;
        this.USER = user;
        this.PASS = pass;
        this.MySQL = new MySQLFunc(host, db, user, pass,port);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return false;
        }

        try {
            this.st = this.con.createStatement();
            this.connected = true;
            this.plugin.getLogger().info("[" + this.conName + "] Connected to the database.");
        } catch (SQLException var6) {
            this.connected = false;
            this.plugin.getLogger().info("[" + this.conName + "] Could not connect to the database.");
        }

        this.MySQL = new MySQLFunc(this.HOST, this.DB, this.USER, this.PASS,this.PORT);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return false;
        }
        return Boolean.valueOf(this.connected);
    }

    ////////////////////////////////
    //     行数を数える
    ////////////////////////////////
    public int countRows(String table) {
        int count = 0;
        ResultSet set = this.query(String.format("SELECT * FROM %s", new Object[]{table}));

        try {
            while(set.next()) {
                ++count;
            }
        } catch (SQLException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not select all rows from table: " + table + ", error: " + var5.getErrorCode());
        }

        return count;
    }
    ////////////////////////////////
    //     レコード数
    ////////////////////////////////
    public int count(String table) {
        int count = 0;
        ResultSet set = this.query(String.format("SELECT count(*) from %s", table));

        try {
            count = set.getInt("count(*)");

        } catch (SQLException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not select all rows from table: " + table + ", error: " + var5.getErrorCode());
            return -1;
        }

        return count;
    }
    ////////////////////////////////
    //      実行
    ////////////////////////////////
    public boolean execute(String query) {
        final boolean[] ret = {true};
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (debugMode){
                    plugin.getLogger().info("query:" + query);
                }

                try {
                    st = con.createStatement();
                    st.execute(query);
                } catch (SQLException var3) {
                    plugin.getLogger().info("[" + conName + "] Error executing statement: " +var3.getErrorCode() +":"+ var3.getLocalizedMessage());
                    plugin.getLogger().info(query);
                    ret[0] = false;

                }
            }
        }).start();
        return ret[0];
    }

    ////////////////////////////////
    //      クエリ
    ////////////////////////////////
    public ResultSet query(String query) {
        final ResultSet[] rs = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (debugMode){
                    plugin.getLogger().info("query:" + query);
                }
                try {
                    st = con.createStatement();
                    rs[0] = st.executeQuery(query);
                } catch (SQLException var4) {
                    plugin.getLogger().info("[" + conName + "] Error executing query: " + var4.getErrorCode());
                }
            }
        }).start();
        return rs[0];
    }
}