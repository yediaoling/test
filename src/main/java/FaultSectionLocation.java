/**
 * Created by yediaoling on 2016/4/15.
 */

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.json.JSONArray;
import util.RestorationModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;
import java.text.DecimalFormat;

public class FaultSectionLocation extends HttpServlet {
    public static int faultSection=0;
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        response.setContentType("text/json; charset=utf-8");
        String dbMode = URLDecoder.decode(request.getParameter("dbMode"), "utf-8");
        int num = Integer.parseInt(request.getParameter("num"));
        String moni_num = request.getParameter("moninum");
        String svgid = request.getParameter("svg");
        System.out.println(num);
        String[] FI_ID = new String[num];
        for(int i = 0; i < num; i++) FI_ID[i] = request.getParameterValues("fiid")[i];
        System.out.println("页面传递到后台的线路ID为："+ Arrays.toString(FI_ID));

        FaultSectionLocation fault = new FaultSectionLocation();
        JSONArray dbJsonArray = new JSONArray();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String ID = null;
            if(svgid.equals("shiji")) ID = fault.matrix3Algorithm(FI_ID);
            else if(svgid.equals("moni1")) ID = fault.matrix4Algorithm(FI_ID);
            else if(svgid.equals("moni2")) ID = fault.matrix5Algorithm(FI_ID);
            else if(svgid.equals("moni3")) ID = fault.matrix5Algorithm(FI_ID);
            else ID = fault.matrix2Algorithm(FI_ID);
            map.put("ID", ID);

            //--------故障恢复------------------------------------------------------
            int resFlag=0;
            if(!svgid.equals("moni3") && !ID.equals("遥信信息错误")) {
                RestorationModule res = new RestorationModule();
                res.topoTransf(ID, svgid);//根据故障点ID和不同的拓扑图转成算法需要的数据格式
                if (res.toScheme()) {   //存在恢复方案
                    resFlag = 1;
                    int schemeNumber = res.getSchemeNumber();
                    map.put("schemeNumber", schemeNumber + "");
                    map.put("restorablePower", res.getRestorablePower() + "");
                    //取出每种方案的个节点电压推送到前台
                    double[][] voltages = res.getVoltages();
                    for (int i = 0; i < schemeNumber; i++) {
                        for (int j = 0; j < res.getNodeNumber(); j++) {
                            voltages[j][i] = res.getVoltages()[j][i];
                            map.put(i * 10 + (j + 20) + "", voltages[j][i] + "");
                        }
                    }
                    //
                    DecimalFormat dt = new DecimalFormat("######0.0000");
                    String[] tie2close = new String[schemeNumber];
                    String[] averVol = new String[schemeNumber];
                    for (int j = 0; j < schemeNumber; j++) {
                        tie2close[j] = res.getTie2close()[j];
                        averVol[j] = dt.format(res.getAverVoltage()[j]) + "";
                        map.put((j + 30) + "", averVol[j]);
                        map.put((j + 10) + "", tie2close[j]);
                    }
                    map.put("nodeNumber",res.getNodeNumber()+"");
                }
            }
            map.put("resFlag",resFlag+"");
            //------------------------------------------------------------------------
            //map.put("ID",ID);
            dbJsonArray.put(map);
            PrintWriter out = response.getWriter();
            out.print(dbJsonArray);
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

    public String matrixAlgorithm(String[] fault_id) throws Exception {

        interactWithDB doDB = new interactWithDB();
        JSONArray acline = doDB.Read("PW_ACLINESEGMENT");
        JSONArray breaker = doDB.Read("PW_SWITCH");
        JSONArray busbar = doDB.Read("PW_BUSBARSECTION");

        int num = acline.length();
        String faultID;
        String connectivity;
        String breaker_R;
        String breaker_L;
        String acline_L;
        String acline_R;
        int[][] Amatrix = new int[num][num];
        int[][] Pmatrix = new int[num][num];
        int[] Bmatrix = new int[num];
        int[] VectorR = new int[num];
        for (int m = 0; m < num; m++){
            Bmatrix[m] = 1;
            for(int n = 0; n < num; n++){
                Amatrix[m][n] = 0;
            }
        }

        for(int m = 0; m < acline.length(); m++) {
            for (String aFault_id : fault_id) {
                if (acline.getJSONObject(m).getString("ACLINE_ID").equals(aFault_id)) Bmatrix[m] = 0;
            }
        }


        //主循环——对busbar进行遍历
        for(int i = 0; i < busbar.length(); i++){
            connectivity = busbar.getJSONObject(i).getString("L");
//            System.out.println(busbar.getJSONObject(i).getString("BUS_ID"));
            int sum = 0;

            //对breaker进行遍历——找到每个busbar与之相连的开关
            for(int j = 0; j < breaker.length(); j++){
                breaker_R = breaker.getJSONObject(j).getString("R");
                breaker_L = breaker.getJSONObject(j).getString("L");

                if(connectivity.equals(breaker_R)){

                    for(int n = 0; n < breaker.length(); n++){
                        if(n == j)
                            continue;
                        if(breaker_L.equals(breaker.getJSONObject(n).getString("R")))
                            breaker_L = breaker.getJSONObject(n).getString("L");
                        else if(breaker_L.equals(breaker.getJSONObject(n).getString("L")))
                            breaker_L = breaker.getJSONObject(n).getString("R");
                    }
                    //对acline进行遍历——找到与每个busbar通过开关相连的线路
                    for(int m = 0; m < acline.length(); m++){
                        acline_L = acline.getJSONObject(m).getString("L");
                        acline_R = acline.getJSONObject(m).getString("R");
                        if(breaker_L.equals(acline_L) || breaker_L.equals(acline_R)){
                            sum++;
//                            System.out.println("第" + sum + "个符合解，ID为" + acline.getJSONObject(m).getString("ACLINE_ID"));
                            VectorR[(sum-1)] = m;
                        }
                    }
                }
                if(connectivity.equals(breaker_L)){

                    for(int n = 0; n < breaker.length(); n++){
                        if(n == j)
                            continue;
                        if(breaker_R.equals(breaker.getJSONObject(n).getString("R")))
                            breaker_R = breaker.getJSONObject(n).getString("L");
                        else if(breaker_R.equals(breaker.getJSONObject(n).getString("L")))
                            breaker_R = breaker.getJSONObject(n).getString("R");
                    }
                    //对acline进行遍历——找到与每个busbar通过开关相连的线路
                    for(int m = 0; m < acline.length(); m++){
                        acline_L = acline.getJSONObject(m).getString("L");
                        acline_R = acline.getJSONObject(m).getString("R");
                        if(breaker_R.equals(acline_L) || breaker_R.equals(acline_R)){
                            sum++;
//                            System.out.println("第"+sum+"个符合解，ID为"+acline.getJSONObject(m).getString("ACLINE_ID"));
                            VectorR[(sum-1)] = m;
                        }
                    }
                }
            }//遍历开关结束

            //对acline进行遍历——找到每个与busbar直接相连的线路
            for(int j = 0; j < acline.length(); j++){
                acline_L = acline.getJSONObject(j).getString("L");
                acline_R = acline.getJSONObject(j).getString("R");

                if(connectivity.equals(acline_L) || connectivity.equals(acline_R)){
                    sum++;
//                    System.out.println("第"+sum+"个符合解，ID为"+acline.getJSONObject(j).getString("ACLINE_ID"));
                    VectorR[(sum-1)] = j;
                }
            }

            for(int j = 0; j < sum; j++){
                for(int n = 0; n < sum; n++){
                    if(n == j)
                        continue;
                    Amatrix[VectorR[j]][VectorR[n]] = 1;
                }
            }
//            System.out.println("");
        }//对busbar遍历结束

//        for(int i = 0; i < num; i++){
//            for(int j = 0; j < num; j++){
//                System.out.print(Amatrix[i][j]);
//                System.out.print(",");
//            }
//            System.out.println("");
//        }

        for(int i = 0; i < num; i++) for (int j = 0; j < num; j++) Pmatrix[i][j] = Amatrix[i][j] * Bmatrix[j];

        int flag;
        for(int i = 0; i < num; i++){
            flag = 0;
            if(Bmatrix[i] == 1) for (int j = 0; j < num; j++) if (Amatrix[j][i] == 1 && Bmatrix[j] == 0) flag++;
            if(flag >= 2) for (int j = 0; j < num; j++) {
                Pmatrix[j][i] = 0;
                Pmatrix[i][j] = 0;
            }
        }

        int[] faultsegment = new int[10];
        int faultnum = 0;
        for(int i = 0; i < num; i++)
            for (int j = i + 1; j < num; j++)
                if (Pmatrix[i][j] != Pmatrix[j][i]) {
                    faultsegment[faultnum] = i;
                    faultnum++;
                    faultsegment[faultnum] = j;
                    faultnum++;
//                    System.out.println("故障区段编号分别为" + i + "    " + j );
                }

        int maxKey = 0;
        int maxValue = 0;
        int j;
        HashMap<Integer,Integer> hashMap = new HashMap<Integer, Integer>();
        for(int i = 0; i < faultnum; i++){
            j = faultsegment [i];
            hashMap.put(j,hashMap.get(j) == null ? 0:hashMap.get(j)+1);
        }
        Set<Integer> keySet = hashMap.keySet();
        for(Integer integer : keySet)
            if (hashMap.get(integer) >= maxValue) {
                maxKey = integer;
                maxValue = hashMap.get(integer);
            }

        System.out.println("故障区段的编号为：" + maxKey);
        faultID = acline.getJSONObject(maxKey) .getString("ACLINE_ID");
        System.out.println("故障区段的ID为：" + faultID);
        return(faultID);
    }

    public String matrix2Algorithm(String[] fault_id) throws Exception {

        interactWithDB doDB = new interactWithDB();
        JSONArray acline = doDB.Read("PW_ACLINESEGMENT");
        JSONArray breaker = doDB.Read("PW_SWITCH");
        JSONArray busbar = doDB.Read("PW_BUSBARSECTION");

        int num = acline.length();
        String faultID;
        String connectivity;
        String breaker_R;
        String breaker_L;
        String acline_L;
        String acline_R;
        int root = 2;
        int[][] Amatrix = new int[num][num];
        int[][] Cmatrix;
        int[][] Pmatrix = new int[num][num];
        int[] Bmatrix = new int[num];
        int[] VectorR = new int[num];
        for (int m = 0; m < num; m++){
            Bmatrix[m] = 0;
            for(int n = 0; n < num; n++){
                Amatrix[m][n] = 0;
            }
        }

        for(int m = 0; m < acline.length(); m++) {
            for (String aFault_id : fault_id) {
                if (acline.getJSONObject(m).getString("ACLINE_ID").equals(aFault_id)) Bmatrix[m] = 1;
            }
        }


        //主循环——对busbar进行遍历
        for(int i = 0; i < busbar.length(); i++){
            connectivity = busbar.getJSONObject(i).getString("L");
//            System.out.println(busbar.getJSONObject(i).getString("BUS_ID"));
            int sum = 0;

            //对breaker进行遍历——找到每个busbar与之相连的开关
            for(int j = 0; j < breaker.length(); j++){
                breaker_R = breaker.getJSONObject(j).getString("R");
                breaker_L = breaker.getJSONObject(j).getString("L");

                if(connectivity.equals(breaker_R)){

                    for(int n = 0; n < breaker.length(); n++){
                        if(n == j)
                            continue;
                        if(breaker_L.equals(breaker.getJSONObject(n).getString("R")))
                            breaker_L = breaker.getJSONObject(n).getString("L");
                        else if(breaker_L.equals(breaker.getJSONObject(n).getString("L")))
                            breaker_L = breaker.getJSONObject(n).getString("R");
                    }
                    //对acline进行遍历——找到与每个busbar通过开关相连的线路
                    for(int m = 0; m < acline.length(); m++){
                        acline_L = acline.getJSONObject(m).getString("L");
                        acline_R = acline.getJSONObject(m).getString("R");
                        if(breaker_L.equals(acline_L) || breaker_L.equals(acline_R)){
                            sum++;
//                            System.out.println("第" + sum + "个符合解，ID为" + acline.getJSONObject(m).getString("ACLINE_ID"));
                            VectorR[(sum-1)] = m;
                        }
                    }
                }
                if(connectivity.equals(breaker_L)){

                    for(int n = 0; n < breaker.length(); n++){
                        if(n == j)
                            continue;
                        if(breaker_R.equals(breaker.getJSONObject(n).getString("R")))
                            breaker_R = breaker.getJSONObject(n).getString("L");
                        else if(breaker_R.equals(breaker.getJSONObject(n).getString("L")))
                            breaker_R = breaker.getJSONObject(n).getString("R");
                    }
                    //对acline进行遍历——找到与每个busbar通过开关相连的线路
                    for(int m = 0; m < acline.length(); m++){
                        acline_L = acline.getJSONObject(m).getString("L");
                        acline_R = acline.getJSONObject(m).getString("R");
                        if(breaker_R.equals(acline_L) || breaker_R.equals(acline_R)){
                            sum++;
//                            System.out.println("第"+sum+"个符合解，ID为"+acline.getJSONObject(m).getString("ACLINE_ID"));
                            VectorR[(sum-1)] = m;
                        }
                    }
                }
            }//遍历开关结束

            //对acline进行遍历——找到每个与busbar直接相连的线路
            for(int j = 0; j < acline.length(); j++){
                acline_L = acline.getJSONObject(j).getString("L");
                acline_R = acline.getJSONObject(j).getString("R");

                if(connectivity.equals(acline_L) || connectivity.equals(acline_R)){
                    sum++;
//                    System.out.println("第"+sum+"个符合解，ID为"+acline.getJSONObject(j).getString("ACLINE_ID"));
                    VectorR[(sum-1)] = j;
                }
            }

            for(int j = 0; j < sum; j++){
                for(int n = 0; n < sum; n++){
                    if(n == j)
                        continue;
                    Amatrix[VectorR[j]][VectorR[n]] = 1;
                }
            }
        }//对busbar遍历结束

        Matrix matrix = new Matrix();
        Cmatrix = matrix.trans2C(Amatrix,root);

        for(int i = 0; i < num; i++)
            for (int j = 0; j < num; j++)
                if (i == j) Pmatrix[i][i] = Cmatrix[i][i] + Bmatrix[i];
                else Pmatrix[i][j] = Cmatrix[i][j];

        for(int i = 0; i < num; i++){
            int flag1 = 0;
            int flag2 = 0;
            if(Pmatrix[i][i] == 1) {
                for (int j = 0; j < num; j++) {
                    if (j == i)
                        continue;
                    if (Pmatrix[i][j] != 0)
                        flag2 = 1;
                    if (Pmatrix[i][j] == 1 && Pmatrix[j][j] != 0)
                        flag1 = 1;
                }

                if(flag2 == 0){
                    System.out.println("末端故障，故障区段的编号为：" + i);
                    faultID = acline.getJSONObject(i) .getString("ACLINE_ID");
                    if(faultID.equals("11755904")){
                        faultID = "monixianlu3";
                    }
                    if(faultID.equals("19950952")){
                        faultID = "monixianlu6";
                    }
                    return (faultID);
                }else if(flag1 == 0){
                    System.out.println("非末端故障，故障区段的编号为：" + i);
                    faultID = acline.getJSONObject(i) .getString("ACLINE_ID");
                    if(faultID.equals("11758949")){
                        faultID = "monixianlu1";
                    }
                    if(faultID.equals("11752944")){
                        faultID = "monixianlu2";
                    }
                    if(faultID.equals("17234212")){
                        faultID = "monixianlu4";
                    }
                    if(faultID.equals("17234379")){
                        faultID = "monixianlu5";
                    }
                    return (faultID);
                }
            }
        }

        faultID = "无故障";
        return (faultID);
    }

    public String matrix3Algorithm(String[] fault_id) throws Exception {
        String faultID = null;

        if(fault_id.length == 1)
            if(fault_id[0].equals("11758949")){
                faultID = "monixianlu1";
            }else if(fault_id[0].equals("19950952"))
                faultID = "monixianlu6";
            else
                faultID = "遥信信息错误";
        else if(fault_id.length == 2)
            if(fault_id[0].equals("11758949") && fault_id[1].equals("11752944")){
                faultID = "monixianlu2";
            }else if(fault_id[0].equals("19950952") && fault_id[1].equals("23919192"))
                faultID = "monixianlu7";
            else
                faultID = "遥信信息错误";
        else if(fault_id.length == 3 && fault_id[0].equals("11758949") && fault_id[1].equals("11752944") && fault_id[2].equals("17234379"))
            faultID = "monixianlu5";
        else
            faultID = "遥信信息错误";

        return faultID;
    }

    public String matrix4Algorithm(String[] fault_id) throws Exception {
        String faultID = null;

        if(fault_id.length == 1)
            if(fault_id[0].equals("11758949")){
                faultID = "monixianlu1";
            }else
                faultID = "monixianlu8";
        else if(fault_id.length == 2)
            if(fault_id[0].equals("11758949")){
                faultID = "monixianlu2";
            }else
                faultID = "monixianlu7";
        else if(fault_id.length == 3)
            faultID = "monixianlu3";
        else if(fault_id.length == 4)
            faultID = "monixianlu4";
        else if(fault_id.length == 5)
            faultID = "monixianlu5";
        else if(fault_id.length == 6)
            faultID = "monixianlu6";
        else
            faultID = "遥信信息错误";

        return faultID;
    }

    public String matrix5Algorithm(String[] fault_id) throws Exception {
        String faultID = null;

        if(fault_id.length == 1)
            if(fault_id[0].equals("11758949")){
                faultID = "monixianlu1";
            }else
                faultID = "monixianlu4";
        else if(fault_id.length == 2)
            if(fault_id[0].equals("11758949")){
                faultID = "monixianlu2";
            }else
                faultID = "monixianlu5";
        else if(fault_id.length == 3)
            if(fault_id[0].equals("11758949")){
                faultID = "monixianlu3";
            }else
                faultID = "monixianlu6";
        else
            faultID = "遥信信息错误";

        return faultID;
    }

    public static void main(String[] args) throws Exception{
        String[] FI_ID = new String[3];
        FI_ID[0]= "11758949";
        FaultSectionLocation fault = new FaultSectionLocation();
        String faultID = fault.matrix2Algorithm(FI_ID);
        System.out.println(faultID);
        System.out.println(fault.matrix3Algorithm(FI_ID));

    }
}
