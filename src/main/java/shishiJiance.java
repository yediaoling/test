import org.json.JSONArray;
import util.RestorationModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/14.
 */
public class shishiJiance extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/json; charset=utf-8");
        String dbMode = URLDecoder.decode(request.getParameter("dbMode"), "utf-8");
        JSONArray dbJsonArray = new JSONArray();
        Map<String, String> map = new HashMap<String, String>();
        String ID2 = "无报警信息";
        String[] faultType =  new String[2];
        faultType[0] = "NULL";
        faultType[1] = "NULL";
        String faultPhase = null;
        int Yuzhi = 10;
        int Yuzhi2 = 600;

        JSONArray array = new JSONArray();
        iceclient ic = new iceclient();
        String[] ID = ic.getIceClientInstance().getTablemRID(32);
        String[] fields = new String[ID.length];
        for(int i=0; i<ID.length; i++){
            fields[i] = "value";
        }
        float[] value;

        value = ic.getIceClientInstance().getFloatValues (ID,fields);
       if(value[32] > Yuzhi || value[33] > Yuzhi || value[34] > Yuzhi){
           ID2 = "monixianlu6";
           faultType = TypeJudge(value[32],value[33],value[34]);
           System.out.println("51QF A相"+ value[32]);
           System.out.println("51QF B相"+ value[33]);
           System.out.println("51QF C相"+ value[34]);
           System.out.println("51QF A相电压"+ value[35]);
       }

        if(value[45] > Yuzhi || value[46] > Yuzhi || value[47] > Yuzhi){
            ID2 = "monixianlu1";
            faultType = TypeJudge(value[45],value[46],value[47]);
        }

        if(value[77] > Yuzhi || value[78] > Yuzhi || value[79] > Yuzhi){
            ID2 = "monixianlu5";
            faultType = TypeJudge(value[77],value[78],value[79]);
        }

        if(value[64] > Yuzhi || value[65] > Yuzhi || value[66] > Yuzhi){
            ID2 = "monixianlu7";
            faultType = TypeJudge(value[64],value[65],value[66]);
        }

        if(value[48] > Yuzhi2 && value[49] > Yuzhi2 && value[50] < Yuzhi2){
            ID2 = "monixianlu1";
            faultType[0] = "单相接地故障";
            faultType[1] = "CN";
        }
        if(value[48] < Yuzhi2 && value[49] > Yuzhi2 && value[50] > Yuzhi2){
            ID2 = "monixianlu1";
            faultType[0] = "单相接地故障";
            faultType[1] = "AN";
        }
        if(value[48] > Yuzhi2 && value[49] < Yuzhi2 && value[50] > Yuzhi2){
            ID2 = "monixianlu1";
            faultType[0] = "单相接地故障";
            faultType[1] = "BN";
        }

        if(value[80] > Yuzhi2 && value[81] > Yuzhi2 && value[82] < Yuzhi2){
            ID2 = "monixianlu5";
            faultType[0] = "单相接地故障";
            faultType[1] = "CN";
        }
        if(value[80] < Yuzhi2 && value[81] > Yuzhi2 && value[82] > Yuzhi2){
            ID2 = "monixianlu5";
            faultType[0] = "单相接地故障";
            faultType[1] = "AN";
        }
        if(value[80] > Yuzhi2 && value[81] < Yuzhi2 && value[82] > Yuzhi2){
            ID2 = "monixianlu1";
            faultType[0] = "单相接地故障";
            faultType[1] = "BN";
        }

        if(value[67] > Yuzhi2 && value[68] > Yuzhi2 && value[69] < Yuzhi2){
            ID2 = "monixianlu7";
            faultType[0] = "单相接地故障";
            faultType[1] = "CN";
        }
        if(value[67] < Yuzhi2 && value[68] > Yuzhi2 && value[69] > Yuzhi2){
            ID2 = "monixianlu7";
            faultType[0] = "单相接地故障";
            faultType[1] = "AN";
        }
        if(value[67] > Yuzhi2 && value[68] < Yuzhi2 && value[69] > Yuzhi2){
            ID2 = "monixianlu7";
            faultType[0] = "单相接地故障";
            faultType[1] = "BN";
        }

        if(value[35] > Yuzhi2 && value[36] > Yuzhi2 && value[37] < Yuzhi2){
            ID2 = "monixianlu6";
            faultType[0] = "单相接地故障";
            faultType[1] = "CN";
        }
        if(value[35] < Yuzhi2 && value[36] > Yuzhi2 && value[37] > Yuzhi2){
            ID2 = "monixianlu6";
            faultType[0] = "单相接地故障";
            faultType[1] = "AN";
        }
        if(value[35] > Yuzhi2 && value[36] < Yuzhi2 && value[37] > Yuzhi2){
            ID2 = "monixianlu6";
            faultType[0] = "单相接地故障";
            faultType[1] = "BN";
        }

        //--------故障恢复------------------------------------------------------
        int resFlag=0;
        String svgid="shiji";
        if(!ID2.equals("无报警信息")) {
            RestorationModule res = new RestorationModule();
            res.topoTransf(ID2, svgid);//根据故障点ID和不同的拓扑图转成算法需要的数据格式
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
        map.put("ID",ID2);
        map.put("fType",faultType[0]);
        map.put("fPhase",faultType[1]);
        dbJsonArray.put(map);
        PrintWriter out = response.getWriter();
        out.print(dbJsonArray);
        out.flush();
        out.close();

    }

    public String[] TypeJudge(float A, float B, float C){
        String[] faultType = new String[2];
        int YZ = 10;
        if(A > YZ && B > YZ && C  > YZ){
            faultType[0] = "三相短路故障";
            faultType[1] = "ABC";
        }
        if(A > YZ && B < YZ && C  < YZ){
            faultType[0] = "单相短路故障";
            faultType[1] = "A";
        }
        if(A < YZ && B > YZ && C  < YZ){
            faultType[0] = "单相短路故障";
            faultType[1] = "B";
        }
        if(A < YZ && B < YZ && C  > YZ){
            faultType[0] = "单相短路故障";
            faultType[1] = "C";
        }
        if(A > YZ && B < YZ && C  > YZ){
            faultType[0] = "两相短路故障";
            faultType[1] = "AC";
        }
        if(A < YZ && B > YZ && C  > YZ){
            faultType[0] = "两相短路故障";
            faultType[1] = "BC";
        }
        if(A > YZ && B > YZ && C  < YZ){
            faultType[0] = "两相短路故障";
            faultType[1] = "AB";
        }

        return faultType;
    }

    public static void main(String[] args) throws InterruptedException {
        float A = 15;
        float B = 15;
        float C = 15;
        shishiJiance ss = new shishiJiance();
        String[] faultType = null;
        faultType = ss.TypeJudge(A,B,C);
        System.out.println(faultType[0]+"   "+faultType[1]);

    }

}
