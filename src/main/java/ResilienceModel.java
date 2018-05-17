import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yediaoling on 2016/11/7.
 */
public class ResilienceModel extends HttpServlet {

    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@10.15.194.25:1521:idpora";
    String username = "rds-pw";
    String password = "123456789";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;



    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/json; charset=utf-8");
        String dbMode = URLDecoder.decode(request.getParameter("dbMode"), "utf-8");
        String table;
        System.out.println("10kV出线相关能力分析");
        System.out.println(dbMode);
        if(dbMode.equals("faultLocation")){
            table = "PW_ACLINESEGMENT";
        }else if(dbMode.equals("kaiguanshuju")){
            table = "PW_KAIGUAN";
        }else if(dbMode.equals("kaiguanshuju1")){
            table = "PW_KAIGUAN1";
         }
        else{
            table = "PW_YUNXING";
        }

        ResilienceModel rm = new ResilienceModel();
        if(dbMode.equals("yunxingshuju")){
            JSONArray result = rm.Shishi();
            PrintWriter out = response.getWriter();
            out.print(result);
            out.flush();
            out.close();
        }else {
            try {
                JSONArray result = rm.Read(table);
                PrintWriter out = response.getWriter();
                out.print(result);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public JSONArray Shishi(){
        JSONArray array = new JSONArray();
        iceclient ic = new iceclient();
        String[] ID = ic.getIceClientInstance().getTablemRID(32);
        String[] fields = new String[ID.length];
        for(int i=0; i<ID.length; i++){
            fields[i] = "value";
        }
        float[] value;
        value = ic.getIceClientInstance().getFloatValues (ID,fields);
        String[] name;
        for(int i=0; i<ID.length; i++){
            fields[i] = "name";
        }
        name = ic.getIceClientInstance().getStrValues (ID,fields);

        for(int i = 0; i < ID.length; i++){
            Map<String, String> map = new HashMap<String, String>();
            map.put("ACLINE_ID",ID[i]);
            map.put("NAME", name[i]);
            map.put("VOLTAGE", String.valueOf(value[i]));
            array.put(map);
        }
        return array;
    }

    public JSONArray Read(String table) throws Exception {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        JSONArray array = new JSONArray();

        if(table.equals("PW_ACLINESEGMENT")){
            String sqlString="select ACLINE_NAME,ACLINE_ID,ACLINE_LENGTH from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("NAME",rs.getString("ACLINE_NAME"));
                map.put("ACLINE_ID",rs.getString("ACLINE_ID"));
                map.put("LENGTH",rs.getString("ACLINE_LENGTH"));
                array.put(map);
            }
        }
        if(table.equals("PW_YUNXING")){
            String sqlString="select ACLINE_NAME,ACLINE_ID,ZHONGLEI,XINXI from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("NAME",rs.getString("ACLINE_NAME"));
                map.put("ACLINE_ID",rs.getString("ACLINE_ID"));
                map.put("ZHONGLEI",rs.getString("ZHONGLEI"));
                map.put("XINXI",rs.getString("XINXI"));
                array.put(map);
            }
        }
        if(table.equals("PW_KAIGUAN")){
            String sqlString="select KAIGUAN_NAME,KAIGUAN_ID,KAIGUAN_IMPORTANCE from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("NAME",rs.getString("KAIGUAN_NAME"));
                map.put("KAIGUAN_ID",rs.getString("KAIGUAN_ID"));
                map.put("KAIGUAN_IMPORTANCE",rs.getString("KAIGUAN_IMPORTANCE"));
                array.put(map);
            }
        }
        if(table.equals("PW_KAIGUAN1")){
            String sqlString="select KAIGUAN_NAME,KAIGUAN_ID,KAIGUAN_IMPORTANCE from " + table;
            rs = stmt.executeQuery(sqlString);
            while (rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("NAME",rs.getString("KAIGUAN_NAME"));
                map.put("KAIGUAN_ID",rs.getString("KAIGUAN_ID"));
                map.put("KAIGUAN_IMPORTANCE",rs.getString("KAIGUAN_IMPORTANCE"));
                array.put(map);
            }
        }


        return array;
    }

    public static void main(String[] args) throws Exception{

        ResilienceModel rm = new ResilienceModel();
        JSONArray result = rm.Shishi();
        System.out.println(result);
    }
}
