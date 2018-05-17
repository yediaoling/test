/**
 * Created by yediaoling on 2016/3/18.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class interactWithDB {
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@10.15.194.25:1521:idpora";
    String username = "rds-pw";
    String password = "123456789";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public JSONArray Read(String table) throws Exception {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        JSONArray array = new JSONArray();

        if(table.equals("PW_ACLINESEGMENT")){
            String sqlString="select ACLINE_ID,CONNECTIVITYNODE_L,CONNECTIVITYNODE_R from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("ACLINE_ID",rs.getString("ACLINE_ID"));
                map.put("L",rs.getString("CONNECTIVITYNODE_L"));
                map.put("R",rs.getString("CONNECTIVITYNODE_R"));
                array.put(map);
            }
        }else if(table.equals("PW_SWITCH")){
            String sqlString="select SWITCH_ID,CONNECTIVITY_L,CONNECTIVITY_R from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("SWITCH_ID",rs.getString("SWITCH_ID"));
                map.put("L",rs.getString("CONNECTIVITY_L"));
                map.put("R",rs.getString("CONNECTIVITY_R"));
                array.put(map);
            }
        }else if(table.equals("PW_BUSBARSECTION")){
            String sqlString="select BUS_ID,CONNECTIVITY from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("BUS_ID",rs.getString("BUS_ID"));
                map.put("L",rs.getString("CONNECTIVITY"));
                array.put(map);
            }
        }else if(table.equals("WEATHER")){
            String sqlString="select DAY ,WEEK, MAXTEMP, MINTEMP, WEATHER, WINDATT, WINDAPM from " + table + " ORDER BY ID";
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("day",rs.getString("DAY"));
                map.put("week",rs.getString("WEEK"));
                map.put("maxtemp",rs.getString("MAXTEMP"));
                map.put("mintemp",rs.getString("MINTEMP"));
                map.put("weather",rs.getString("WEATHER"));
                map.put("windatt",rs.getString("WINDATT"));
                map.put("windapm",rs.getString("WINDAPM"));
                array.put(map);
            }
        }

        rs.close();
        stmt.close();
        conn.close();
        return array;
    }

    public void Write(JSONObject jsonObj,String tableName) throws Exception{
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        if(tableName.equals("PW_SUBSTATION")){
            String sqlString="select Subs_Id,Subs_Name,Subs_Type,Subs_Circuits,Subs_Zones,Subs_BaseVoltage from " + tableName;
            rs = stmt.executeQuery(sqlString);
            rs.afterLast();
            rs.moveToInsertRow();
            rs.updateString("Subs_Id", jsonObj.getString("Subs_Id"));
            rs.updateString("Subs_Name",jsonObj.getString("Subs_Name"));
            rs.updateString("Subs_Type", jsonObj.getString("Subs_Type"));
            rs.updateString("Subs_Circuits", jsonObj.getString("Subs_Circuits"));
            rs.updateString("Subs_Zones", jsonObj.getString("Subs_Zones"));
            rs.updateString("Subs_BaseVoltage", jsonObj.getString("Subs_BaseVoltage"));
        }else if(tableName.equals("PW_ACLINESEGMENT")){
            String sqlString="select ACLINE_ID,ACLINE_NAME,ACLINE_TYPE,ACLINE_LENGTH,ACLINE_CIRCUITS,ACLINE_ZONES,CONNECTIVITYNODE_L,CONNECTIVITYNODE_R from " + tableName;
            rs = stmt.executeQuery(sqlString);
            rs.afterLast();
            rs.moveToInsertRow();
            rs.updateString("ACLINE_ID", jsonObj.getString("ACLINE_ID"));
            rs.updateString("ACLINE_NAME",jsonObj.getString("ACLINE_NAME"));
            rs.updateString("ACLINE_TYPE", jsonObj.getString("ACLINE_TYPE"));
            rs.updateString("ACLINE_LENGTH", jsonObj.getString("ACLINE_LENGTH"));
            rs.updateString("ACLINE_CIRCUITS", jsonObj.getString("ACLINE_CIRCUITS"));
            rs.updateString("ACLINE_ZONES", jsonObj.getString("ACLINE_ZONES"));
            rs.updateString("CONNECTIVITYNODE_L", jsonObj.getString("CONNECTIVITYNODE_L"));
            rs.updateString("CONNECTIVITYNODE_R", jsonObj.getString("CONNECTIVITYNODE_R"));
        }else if(tableName.equals("PW_BUSBARSECTION")){
            String sqlString="select BUS_ID,BUS_NAME,CONNECTIVITY from " + tableName;
            rs = stmt.executeQuery(sqlString);
            rs.afterLast();
            rs.moveToInsertRow();
            rs.updateString("BUS_ID", jsonObj.getString("BUS_ID"));
            rs.updateString("BUS_NAME",jsonObj.getString("BUS_NAME"));
            rs.updateString("CONNECTIVITY", jsonObj.getString("CONNECTIVITY"));
        }else if(tableName.equals("PW_SWITCH")){
            String sqlString="select SWITCH_ID,SWITCH_NAME,CONNECTIVITY_L,CONNECTIVITY_R from " + tableName;
            rs = stmt.executeQuery(sqlString);
            rs.afterLast();
            rs.moveToInsertRow();
            rs.updateString("SWITCH_ID", jsonObj.getString("SWITCH_ID"));
            rs.updateString("SWITCH_NAME",jsonObj.getString("SWITCH_NAME"));
            rs.updateString("CONNECTIVITY_L", jsonObj.getString("CONNECTIVITY_L"));
            rs.updateString("CONNECTIVITY_R", jsonObj.getString("CONNECTIVITY_R"));
        }
        rs.insertRow();
        rs.close();
        stmt.close();
        conn.close();
    }

    public void Delete(String tableName)throws Exception{
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        System.out.println(tableName);
        String sqlString="DELETE * from PW_SUBSTATION";
        stmt.execute(sqlString);
        stmt.close();
        conn.close();
    }

    public static void main(String[] args)throws Exception{
        interactWithDB doDB = new interactWithDB();
        JSONArray result = doDB.Read("WEATHER");
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("FIID","1");
//        jsonObj.put("FICURRENT","12");
//        jsonObj.put("FIVOLTAGE","12");
//        jsonObj.put("OWNERID","1");
//        jsonObj.put("FAULTINFO","0");
//        doDB.Write(jsonObj);
        System.out.println(result);
//        doDB.Delete("PW_SUBSTATION");
    }
}
