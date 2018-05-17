package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by zjuCHENW on 2016/5/26.
 */
public class RestorationModule {
    private NetParame netParame;
    int schemeNumber=0;
    double[] averVoltage=new double[5];
    double restorablePower=0;
    double[][] voltages=new double[10][10];
    int nodeNumber;
    String[] tie2close= new String[5];
    String svgid = "";


    public RestorationModule() {
    }
    public boolean toScheme(){

            FlowCalculator flowCalculator = new FlowCalculator();
            //flowCalculator.forwardAndSweep(netParame);
            //---根据故障点，找到下游失电区域节点，并计算失电负荷-----
            double outagePower = 0;
            LinkedList outageNodes = netParame.toFindOutage(netParame.getFaultNode());
            System.out.print("失电节点为：");
            Iterator itr = outageNodes.iterator();
            while (itr.hasNext()) {
                Integer integer = (Integer) itr.next();
                System.out.print(integer);
                System.out.print(',');
                outagePower = outagePower + netParame.getLoadApparentPower(integer);
            }
            System.out.println("失电负荷：" + outagePower + " MW");
            this.restorablePower = outagePower;
            //------------------------------------
            //---根据失电区域得到故障恢复方案
            Iterator itr_ = outageNodes.iterator();
            String result = "";
            Integer outageNode = 0;
            while (itr_.hasNext()) {
                outageNode = (Integer) itr_.next();
                result = netParame.try2FindScehme(outageNode);
                if (!result.equals("Restoration scheme does not exist!")) {
                    NetParame netParameTemp = new NetParame(1,svgid);
                    netParameTemp.setNodeNumber(nodeNumber);
                    netParameTemp.setNodeKV(12.66);
                    netParameTemp.setFaultNode(netParame.getFaultNode());
                    netParameTemp.closeTie(outageNode);
                    double[] nodeKV = flowCalculator.forwardAndSweep(netParameTemp);//校验恢复方案是否满足潮流约束
                    double averVol = flowCalculator.toFindAverVol();
                    this.tie2close[schemeNumber] = result;
                    this.averVoltage[schemeNumber] = averVol;
                    for (int i = 0; i < nodeKV.length; i++) {
                        this.voltages[i][schemeNumber] = nodeKV[i];
                    }
                    System.out.println("恢复方案为：联络线" + result);
                    System.out.println("节点平均电压为：  " + averVol + " kV");
                    schemeNumber = schemeNumber + 1;
                }
            }
            if (schemeNumber != 0) return true;
        else{
                System.out.println("Restoration scheme does not exist! ");
                return false;
            }
    }

    public String[] getTie2close() {
        return tie2close;
    }

    public double[][] getVoltages() {
        return voltages;
    }

    public double[] getAverVoltage() {
        return averVoltage;
    }

    public int getSchemeNumber() {
        return schemeNumber;
    }

    public double getRestorablePower() {
        return restorablePower;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void topoTransf(String faultID,String svgid){
        this.svgid=svgid;
        int faultNode=0;
        if(svgid.equals("shiji")) {
            nodeNumber=5;
            if (faultID.equals("monixianlu1") | faultID.equals("monixianlu2")) faultNode = 5;
            if (faultID.equals("monixianlu5")) faultNode = 4;
            if (faultID.equals("monixianlu6")) faultNode = 2;
            if (faultID.equals("monixianlu7")) faultNode = 3;
            netParame = new NetParame(1,svgid);
            netParame.setNodeNumber(nodeNumber);
            netParame.setNodeKV(12.66);
            netParame.setFaultNode(faultNode);
        }
        if(svgid.equals("moni1")) {
            nodeNumber=10;
            if (faultID.equals("monixianlu1")) faultNode = 2;
            if (faultID.equals("monixianlu2")) faultNode = 3;
            if (faultID.equals("monixianlu3")) faultNode = 4;
            if (faultID.equals("monixianlu4")) faultNode = 5;
            if (faultID.equals("monixianlu5")) faultNode = 6;
            if (faultID.equals("monixianlu6")) faultNode = 7;
            if (faultID.equals("monixianlu7")) faultNode = 10;
            if (faultID.equals("monixianlu8")) faultNode = 9;
            netParame = new NetParame(1,svgid);
            netParame.setNodeNumber(nodeNumber);
            netParame.setNodeKV(12.66);
            netParame.setFaultNode(faultNode);
        }
        if(svgid.equals("moni2")) {
            nodeNumber=9;
            if (faultID.equals("monixianlu1")) faultNode = 3;
            if (faultID.equals("monixianlu2")) faultNode = 4;
            if (faultID.equals("monixianlu3")) faultNode = 5;
            if (faultID.equals("monixianlu4")) faultNode = 7;
            if (faultID.equals("monixianlu5")) faultNode = 8;
            if (faultID.equals("monixianlu6")) faultNode = 9;
            netParame = new NetParame(1,svgid);
            netParame.setNodeNumber(nodeNumber);
            netParame.setNodeKV(12.66);
            netParame.setFaultNode(faultNode);
        }
    }

    public static void main(String[] args){

        //--------故障恢复----------------
        Map<String, String> map = new HashMap<String, String>();
        RestorationModule res;
        res = new RestorationModule();
        res.topoTransf("monixianlu1","moni2");
        res.toScheme();
        int schemeNumber=res.getSchemeNumber();
        map.put("schemeNumber",schemeNumber+"");
        map.put("restorablePower",res.getRestorablePower()+"");
        double[][] voltages=res.getVoltages();
        for(int i=0;i<schemeNumber;i++){
            for(int j=0;j<res.getNodeNumber();j++) {
                voltages[j][i] = res.getVoltages()[j][i];
                map.put(i*10+(j + 20) + "", voltages[j][i]+"");
            }
        }
        System.out.println(map);

    }

}
