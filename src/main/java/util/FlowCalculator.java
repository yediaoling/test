package util;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by chenwei on 2016/5/11.
 */
public class FlowCalculator {
    private int nodeNumber;
    private double endValue;
    private double[][] nodeApparentPower;
    private double[][] branch;
    private int[][] matrixC;
    private int[] matrixH;
    private int iterationTimes;
    public double[] nodeKV;
    public double miniVol;

    public FlowCalculator() {
        iterationTimes = 0;
        endValue = 0.001;  //设置迭代结束误差
    }
    public double[] forwardAndSweep(NetParame netParame){

        double[][] branch = netParame.getBranch();
        this.branch = branch;
        int root = netParame.getRoot();
        double[][] loadApparentPower = netParame.getLoadApparentPower();
        double[] nodeKV = netParame.getNodeKV();
        nodeNumber = nodeKV.length;
        //region 生成网形结构矩阵和分层矩阵。
        int[][] matrixD = new int[nodeNumber][nodeNumber];
        int x1, y1;
        for (int i = 0; i < branch.length; i++) {
            x1 = (int) branch[i][0];
            y1 = (int) branch[i][1];
            matrixD[x1 - 1][y1 - 1] = 1;
            matrixD[y1 - 1][x1 - 1] = 1;
        }
        MatrixOperator matrix = new MatrixOperator(matrixD,root);
        matrixC = (int[][])matrix.generateCH().get(0);
        matrixH = (int[])matrix.generateCH().get(1);
        //endregion
        nodeApparentPower = new double[nodeNumber][2];
        for(int i=0;i<nodeNumber;i++)
            for(int j=0;j<2;j++){
                nodeApparentPower[i][j] = loadApparentPower[i][j];
            }
        //region 上一次迭代结点电压矩阵，初始化
        double[] previousNodeKV = new double[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            previousNodeKV[i] = nodeKV[i];
        }
        //endregion
        int depth =0;
        int deepest =0;
        int node;
        double r,x,p,q;
        //region 得到拓扑图树状结构的总层数
        for (int i = 0; i < matrixH.length; i++) {
            if (matrixH[i] > depth) {
                depth = matrixH[i];
                deepest = depth;
            }
        }
        //endregion
        Iterator ite;
        int sonNode;
        boolean isEnded=false;
        double error;
        long start = System.currentTimeMillis();
        while(!isEnded) {
            iterationTimes++;
            //region 回代过程
            while (depth != 0) {
                for (int i = 0; i < matrixH.length; i++) {
                    if (matrixH[i] == depth) {
                        ite = searchSonNodes(i + 1).iterator();
                        while (ite.hasNext()) {
                            sonNode = Integer.parseInt(ite.next().toString()) ;
                            nodeApparentPower[i][0] = nodeApparentPower[i][0] + nodeApparentPower[sonNode - 1][0];
                            nodeApparentPower[i][1] = nodeApparentPower[i][1] + nodeApparentPower[sonNode - 1][1];
                        }
                        node = i + 1;
                        r = searchBranch(node)[0];
                        x = searchBranch(node)[1];
                        p = nodeApparentPower[i][0];
                        q = nodeApparentPower[i][1];
                        nodeApparentPower[i][0] = p + ((p * p + q * q) / (nodeKV[i] * nodeKV[i])) * r;
                        nodeApparentPower[i][1] = q + ((p * p + q * q) / (nodeKV[i] * nodeKV[i])) * x;
                    }
                }
                depth--;
            }
            //endregion
            //region 前推过程
            while (depth < deepest) {
                for (int i = 0; i < matrixH.length; i++) {
                    if (matrixH[i] == depth) {
                        ite = searchSonNodes(i + 1).iterator();
                        while (ite.hasNext()) {
                            sonNode = Integer.parseInt(ite.next().toString()) ;
                            r = searchBranch(sonNode)[0];
                            x = searchBranch(sonNode)[1];
                            p = nodeApparentPower[i][0];
                            q = nodeApparentPower[i][1];
                            nodeKV[sonNode - 1] = nodeKV[i] - (p * r + q * x) / nodeKV[i];
                        }
                    }
                }
                depth++;
            }
            //endregion
            //region 判断是否收敛
            for(int i=0;i<nodeNumber;i++){
                error = nodeKV[i] - previousNodeKV[i];
                if(Math.abs(error)> endValue){
                    isEnded = false;
                }else isEnded = true;
            }
            //endregion
            //region 更新前一次迭代矩阵
            for(int i=0;i<nodeNumber;i++){
                previousNodeKV[i] = nodeKV[i];
            }
            //endregion
            //region 更新节点负荷矩阵
            for(int i=0;i<nodeNumber;i++)
                for(int j=0;j<2;j++){
                    nodeApparentPower[i][j] = loadApparentPower[i][j];
                }
            //endregion
        }
        long end = System.currentTimeMillis();
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String TimeString = time.format(new java.util.Date());
        System.out.println();
        System.out.println("Chen's Power Version 1.0,"+TimeString+" -- AC Power Flow");
        System.out.println();
        System.out.println("Forward and backward method power flow converged in "+iterationTimes+" iterations.");
        System.out.println();
        System.out.println("Converged in "+(end-start)/1000.0+" seconds");
        System.out.println("=====================================");
        System.out.println("| Power Flow Result                 |");
        System.out.println("=====================================");
        System.out.println("| Bus Data                          |");
        System.out.println("=====================================");
        System.out.println("Bus   Voltage");
        System.out.println("---   -------");
        for(int i =0;i<nodeNumber;i++){
            System.out.print(i+1+"    ");
            System.out.println(nodeKV[i]);
        }
        this.nodeKV=nodeKV;
        return nodeKV;
    }
    private double[] searchBranch(int node){
        double[] z = new double[2];
        for(int i=0;i<branch.length;i++){
            if((int)branch[i][1]==node){
                z[0] = branch[i][2];
                z[1] = branch[i][3];
            }
        }
        return z;
    }
    private LinkedList searchSonNodes(int node){
        LinkedList sonNodes = new LinkedList();
        for(int i=0;i<matrixC.length;i++){
            if(matrixC[node-1][i]==1){
                sonNodes.add(i+1);
            }
        }
        return sonNodes;
    }
    public double toFindAverVol(){
        double temp=0;
        for(int i=0;i<this.nodeKV.length;i++){
            temp=this.nodeKV[i]+temp;
        }
        this.miniVol=temp/this.nodeKV.length;
        return this.miniVol;
    }
}