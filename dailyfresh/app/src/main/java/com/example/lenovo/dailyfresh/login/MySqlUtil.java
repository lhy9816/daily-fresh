package com.example.lenovo.dailyfresh.login;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlUtil {

    public static Connection openConnection(String url, String user,
                                            String password) {
        Connection conn = null;
        try {
            final String DRIVER_NAME = "com.mysql.jdbc.Driver";
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            conn = null;
        } catch (SQLException e) {
            conn = null;
            Log.e("SQLException", String.valueOf(e) );
        }

        return conn;
    }

    public static int userLogin(Connection conn, String username, String pwd){
        Statement statement = null;
        ResultSet result = null;
        int NoneConn = -1;
        int NoUser = 1;
        int DoubleName = 2;
        int WrongPwd = 3;
        if(conn == null){
            return NoneConn;
        }
        String sql = new String("select password from userlist where username = "+'"'+username+'"');

        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            if(result != null && result.first()){
                result.last();
                int count = result.getRow();
                if(count!=1){
                    return DoubleName;
                }
                result.beforeFirst();
                result.first();
                int pwdidx = result.findColumn("password");
                if(!pwd.equals(result.getString(pwdidx))){
                    Log.e("PAWD", "passaord:" + result.getString(pwdidx) );
                    System.out
                            .println("passaord:" + result.getString(pwdidx));
                    return WrongPwd;
                }
                else{
                    return 0;
                }
            }
            else{
                return NoUser;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String[] query(Connection conn, String sql) {

        if (conn == null) {
            return null;
        }

        Statement statement = null;
        ResultSet result = null;
        String[] strings = new String[3];
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            result = statement.executeQuery(sql);
            if (result != null && result.first()) {
                ResultSet r1 =  result;
                /*int nameColumnIndex = result.findColumn("username");

                while (!result.isAfterLast()) {
                    System.out.println("------------------");
                    System.out
                            .println("name " + result.getString(nameColumnIndex));
                    Log.e("QueryRes","name" +result.getString(nameColumnIndex));
                    result.next();
                }*/
                strings[0] = r1.getString(1);
                strings[1]=r1.getString(3);
                strings[2]= r1.getString(4);
                return strings;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {

            }
        }
        return null;
    }

    public static boolean execSQL(Connection conn, String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }

        Statement statement = null;

        try {
            statement = conn.createStatement();
            if (statement != null) {
                execResult = statement.execute(sql);
            }
        } catch (SQLException e) {
            return false;
        }

        return true;
    }
}
