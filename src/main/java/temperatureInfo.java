import org.json.JSONArray;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by yediaoling on 2017/3/10.
 */
public class temperatureInfo extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/json; charset=utf-8");

        String dbMode = null;
        try {
            dbMode = URLDecoder.decode(request.getParameter("dbMode"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String dbJson = URLDecoder.decode(request.getParameter("dbJson"), "utf-8");

        System.out.println("模式为："+dbMode+ "数据");
//        double num = Double.parseDouble(number1);   //解析成double格式
//        System.out.println("000");

        JSONArray dbJsonArray = new JSONArray();
//        System.out.println("111");

//        getFromDatabase doDB = new getFromDatabase();
        interactWithDB doDB = new interactWithDB();
//        System.out.println("222");
        if (dbMode.equals("read")){
//            System.out.println("read");
            try {
                dbJsonArray = doDB.Read("WEATHER");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(dbJsonArray);
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print(dbJsonArray);       //将后台数据传递到页面
            out.flush();
            out.close();
        }else if (dbMode.equals("write")){
//            System.out.println("write");
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print(dbJsonArray);       //将后台数据传递到页面
            out.flush();
            out.close();
        }else if (dbMode.equals("delete")){

        }
    }

    public static void main(String[] args) throws Exception{
        interactWithDB doDB = new interactWithDB();
        JSONArray a = new JSONArray();
        System.out.println(a);
    }
}
