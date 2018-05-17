import org.json.JSONArray;
import util.RestorationModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Silver.Vane on 2017/2/22 0022.
 */
//薄弱环节识别
public class WeakLink extends HttpServlet {
    public static int faultSection=0;
    String IdBox[] = new String[]{
            "11758949",
            "11752944",
            "11755904",
            "17234212",
            "17234379",
            "19950952",
            "23919192",
            "23918564",
            "23919150"
    };
    //此函数负责与前端交互
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        response.setContentType("text/json; charset=utf-8");
        String dbMode = URLDecoder.decode(request.getParameter("dbMode"), "utf-8");
        System.out.println("liyanjun!!!前台映射到后台！");
        int moni_num=4;
        moni_num = Integer.parseInt(request.getParameter("moninum"));
        System.out.println("moni图像对应为"+moni_num);

        WeakLink wealinkObject = new WeakLink();
        //调用薄弱环节求解函数之后将数据打包
        try {

            System.out.println("jieguo!!!!!");
            //  System.out.println(WeaklinkBoxArray.toString());
            String[] WeaklinkBoxArray4 = wealinkObject.WeakLinkFunction();


            //算法获得薄弱线路
              String[] WeaklinkBoxArray1 = {"monixianlu3"};

              String[] WeaklinkBoxArray2 = {"monixianlu1"};

              String[] WeaklinkBoxArray3 = {"monixianlu1"};
              String[] WeaklinkBoxArray5 = {"monixianlu6","_x31_7235406_1_"};

              String[] WeaklinkBoxArray6 = {"monixianlu4"};
              String[] WeaklinkBoxArray7 =  {"monixianlu6"};
              //String[] WeaklinkBoxArray7 = wealinkObject.BranchImportance();//1225


            //薄弱节点编号
            String[] WeaklinkNode1 = {"分接开关柜2"};
            int maxPower1 = 50;
            String[] WeaklinkNode2 = {"分接开关柜1"};
            int maxPower2 = 75;
            String[] WeaklinkNode3 = {"分接开关柜1"};
            int maxPower3 = 35;
            int maxPower4 = 400;
            int maxPower5 = 400;
            int maxPower6 = 406;
            String[] WeaklinkNode7 = {"交流母线1"};
            int maxPower7 = 2;


            // String[] WeaklinkBoxArray={"monixianlu3"};
            JSONArray dbJsonArrayWeak1 = new JSONArray();
            dbJsonArrayWeak1.put(WeaklinkBoxArray1);
            dbJsonArrayWeak1.put(WeaklinkNode1);
            JSONArray dbJsonArrayWeak2 = new JSONArray();
            dbJsonArrayWeak2.put(WeaklinkBoxArray2);
            dbJsonArrayWeak2.put(WeaklinkNode2);
            JSONArray dbJsonArrayWeak3 = new JSONArray();
            dbJsonArrayWeak3.put(WeaklinkBoxArray3);
            dbJsonArrayWeak3.put(WeaklinkNode3);
            JSONArray dbJsonArrayWeak4 = new JSONArray();
            dbJsonArrayWeak4.put(WeaklinkBoxArray4);
            dbJsonArrayWeak4.put(maxPower4);
            JSONArray dbJsonArrayWeak5 = new JSONArray();
            dbJsonArrayWeak5.put(WeaklinkBoxArray5);
            dbJsonArrayWeak5.put(maxPower5);
            JSONArray dbJsonArrayWeak6 = new JSONArray();
            dbJsonArrayWeak6.put(WeaklinkBoxArray6);
            dbJsonArrayWeak6.put(maxPower6);
            JSONArray dbJsonArrayWeak7 = new JSONArray();
            dbJsonArrayWeak7.put(WeaklinkBoxArray7);
            dbJsonArrayWeak7.put(WeaklinkNode7);
            //--------数据发送----------------



            PrintWriter out = response.getWriter();
            if (moni_num == 1) {
                out.print(dbJsonArrayWeak1);
            } else if (moni_num == 2) {
                out.print(dbJsonArrayWeak2);
            } else if (moni_num == 3) {
                out.print(dbJsonArrayWeak3);
            } else if (moni_num == 5) {
                out.print(dbJsonArrayWeak5);
            } else if (moni_num == 6) {
                out.print(dbJsonArrayWeak6);
            } else if (moni_num == 7) {
                out.print(dbJsonArrayWeak7);
            }else{
                out.print(dbJsonArrayWeak4);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        doGet(request,response);
    }

    //此函数求解薄弱环节，有较多冗余信息
    public String[] WeakLinkFunction() throws Exception { //0913 unused
            ArrayList WeakBox = new ArrayList();
            JSONArray dbJsonArrayWeak = new JSONArray();
            RestorationModule resWeak;
            String ID = "11758949";
            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> map2 = new HashMap<String, String>();
            Map<String, String> map3 = new HashMap<String, String>();
            Map<String, String> map4 = new HashMap<String, String>();
            Map<String, String> map5 = new HashMap<String, String>();

        //--------故障恢复----------------
        for(int countLine=1;countLine<10;countLine++) {
            //System.out.println("当前考察第" + countLine + "条线路");
            //resWeak = new RestorationModule(2, faultSection, IdBox[countLine-1],"");
            //String outagePower = Double.toString(resWeak.toScheme());
            //String Minivoltage = Double.toString(resWeak.getVoltage());
            //String switchID = resWeak.getSwitchID();
           // String restorablePower = Double.toString(resWeak.getRestorablePower());

            map.put("ID", ID);
           // map2.put("outagePower", outagePower);
           // map3.put("Minivoltage", Minivoltage);
           // map4.put("switchID", switchID);
           // map5.put("restorablePower", restorablePower);
            //------------------------
            dbJsonArrayWeak.put(map);
            dbJsonArrayWeak.put(map2);
            dbJsonArrayWeak.put(map3);
            dbJsonArrayWeak.put(map4);
            dbJsonArrayWeak.put(map5);

            //System.out.println("restorablePower=" + restorablePower);
            //if(Double.parseDouble(restorablePower)<=1.0){
            //    WeakBox.add(IdBox[countLine-1]);
           // }
        }
            int size=WeakBox.size();
            String[] WeakBoxArray=new String[size];
            for(int i=0;i<WeakBox.size();i++){
                WeakBoxArray[i]=(String)WeakBox.get(i);
            }

        System.out.println("fengeceshi");
        return WeakBoxArray;
    }

    public String[] BranchImportance() throws Exception {
        System.out.println("Branch Importance Assessment");
        // get data
        WeakLink wl = new WeakLink();
        String[][] dataTable = wl.GetRealData();
        // show structure of data table
        for(int i = 0; i < dataTable[0].length; i++) {      //length of one row
            System.out.println(dataTable[0][i]);
            System.out.println(dataTable[1][i]);
            System.out.println(dataTable[2][i]);
        }
        // Branch Importance Assessment
        Float QF52_Ia = Float.valueOf(0);Float QF52_P  = Float.valueOf(0);
        Float QF51_Ia = Float.valueOf(0);Float QF51_P  = Float.valueOf(0);
        Float QF31_Ia = Float.valueOf(0);Float QF31_P  = Float.valueOf(0);
        Float QF32_Ia = Float.valueOf(0);Float QF32_P  = Float.valueOf(0);
        Float QF91_Ia = Float.valueOf(0);Float QF91_P  = Float.valueOf(0);
        Float QF92_Ia = Float.valueOf(0);Float QF92_P  = Float.valueOf(0);
        for(int i = 0; i < dataTable[0].length; i++) {
            if(dataTable[0][i].equals("0320000046")){           //line1 line4
                QF52_Ia = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000055")){
                QF52_P = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000087")){
                QF32_P = Float.valueOf(dataTable[2][i]);

            }else if(dataTable[0][i].equals("0320000033")){    // line6
                QF51_Ia = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000042")){
                QF51_P = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000074")){
                QF31_P = Float.valueOf(dataTable[2][i]);

            }else if(dataTable[0][i].equals("0320000065")){     //line7
                QF31_Ia = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000074")){
                QF31_P = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000106")){
                QF91_P = Float.valueOf(dataTable[2][i]);

            }else if(dataTable[0][i].equals("0320000078")){     //line5
                QF32_Ia = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000087")){
                QF32_P = Float.valueOf(dataTable[2][i]);
            }else if(dataTable[0][i].equals("0320000119")){
                QF92_P = Float.valueOf(dataTable[2][i]);
            }
        }
        System.out.println("------ Vital variable of BIA ------");
        System.out.println("    QF52_P : "+QF52_P+"   QF52_Ia : "+QF52_Ia);
        System.out.println("    QF51_P : "+QF51_P+"   QF51_Ia : "+QF51_Ia);
        System.out.println("    QF31_P : "+QF31_P+"   QF31_Ia : "+QF31_Ia);
        System.out.println("    QF32_P : "+QF32_P+"   QF32_Ia : "+QF32_Ia);
        System.out.println("    QF91_P : "+QF91_P+"   QF91_Ia : "+QF91_Ia);
        System.out.println("    QF92_P : "+QF92_P+"   QF92_Ia : "+QF92_Ia);




        System.out.println("RUNNING! of Branch Importance Assessment");
        float[] WeakValues = new float[5];
        WeakValues[1]= (float) Math.sqrt(QF52_P*QF32_P)*QF52_Ia;  System.out.println("  value of BIA_1    :"+WeakValues[1]);
        WeakValues[2]= (float) Math.sqrt(QF51_P*QF31_P)*QF51_Ia;  System.out.println("  value of BIA_2    :"+WeakValues[2]);
        WeakValues[3]= (float) Math.sqrt(QF31_P*QF91_P)*QF31_Ia;  System.out.println("  value of BIA_3    :"+WeakValues[3]);
        WeakValues[4]= (float) Math.sqrt(QF32_P*QF92_P)*QF32_Ia;  System.out.println("  value of BIA_4    :"+WeakValues[4]);

        System.out.println("End of Branch Importance Assessment");
        if(WeakValues[1]>WeakValues[2]&&WeakValues[1]>WeakValues[3]&&WeakValues[1]>WeakValues[4]){
            String[] BIA = {"monixianlu1"};
            return BIA;
        }else if(WeakValues[2]>WeakValues[1]&&WeakValues[2]>WeakValues[3]&&WeakValues[2]>WeakValues[4]){
            String[] BIA = {"monixianlu6"};
            return BIA;
        }else if(WeakValues[3]>WeakValues[1]&&WeakValues[3]>WeakValues[2]&&WeakValues[3]>WeakValues[4]){
            String[] BIA = {"monixianlu7"};
            return BIA;
        }else if(WeakValues[4]>WeakValues[1]&&WeakValues[4]>WeakValues[2]&&WeakValues[4]>WeakValues[3]){
            String[] BIA = {"monixianlu5"};
            return BIA;
        }else{
            String[] BIA = {"monixianlu6"};
            System.out.println("    Error！No INPUT of  Branch Importance Assessment ！");
            return BIA;
        }
        //String[] ErrorMessages_bia = new String[]{"    Error！No INPUT of  Branch Importance Assessment ！"};
        //return ErrorMessages_bia;
    }

    public String[][] GetRealData(){
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
        String[][] table3xN = new String[3][ID.length];
        for(int i = 0; i < ID.length; i++){
            table3xN[0][i] = ID[i];
            table3xN[1][i] = name[i];
            table3xN[2][i] = String.valueOf(value[i]);
        }
        return table3xN;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("----薄弱环节识别模块开始运行----");
        WeakLink ceshi = new WeakLink();
        //ceshi.GetRealData();
        ///ceshi.BranchImportance();
        ///System.out.println(ceshi.BranchImportance()[0]);
        //System.out.println(ceshi.WeakLinkFunction());

        System.out.println("main fuction");
        //System.out.println(WeakBox1);

    }
}
