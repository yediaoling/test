package util;

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by chenwei on 2016/5/11.
 * 实际运行拓扑网络参数（离线）
 */
public class NetParame {
    private double[][] loadApparentPower;
    private double[][] branch;
    private double[][] tieBranch={
            {1,4,0.5000,0.5000},
            {3,5,0.5000,0.5000}
    };
    private int[][] matrixC;
    private int[] matrixH;
    private int[][] matrixD;
    private double[] nodeKV;
    private int root;
    private int nodeNumber;
    private int faultNode;
    private boolean[] isVisited;
    private LinkedList<Integer> outage;

    public NetParame(int root,String svgid) {
        this.root = root;
        //region shiji图拓扑和负荷参数
        if(svgid.equals("shiji")){
            this.nodeNumber=5;
            double[][] loadApparentPower = {
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.025,0.002},
                    {0.025,0.002},
                    {0.000,0.000},
            };
            this.loadApparentPower=new double[5][2];
            for(int j=0;j<2;j++) {
                for (int i = 0; i < 5; i++) {
                    this.loadApparentPower[i][j] = loadApparentPower[i][j];
                }
            }
            double[][] branch = {
                    {1,2,0.3660,0.1864},
                    {2,3,0.3811,0.1941},
                    {1,5,0.0922,0.0470},
                    {5,4,0.8190,0.2707},
            };
            this.branch=new double[4][4];
            for(int j=0;j<4;j++) {
                for (int i = 0; i < 4; i++) {
                    this.branch[i][j] = branch[i][j];
                }
            }
            double[][] tieBranch={
                    {1,4,0.5000,0.5000},
                    {3,5,0.5000,0.5000}
            };
            this.tieBranch=new double[2][4];
            for(int j=0;j<4;j++) {
                for (int i = 0; i < 2; i++) {
                    this.tieBranch[i][j] = tieBranch[i][j];
                }
            }
        }
        //endregion
        //region moni1图拓扑和负荷参数
        if(svgid.equals("moni1")){
            this.nodeNumber=10;
            double[][] loadApparentPower = {
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.025,0.002},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.025,0.002},
            };
            this.loadApparentPower=new double[10][2];
            for(int j=0;j<2;j++) {
                for (int i = 0; i < 10; i++) {
                    this.loadApparentPower[i][j] = loadApparentPower[i][j];
                }
            }
            double[][] branch = {
                    {1,2,0.3660,0.1864},
                    {2,3,0.3811,0.1941},
                    {3,4,0.0922,0.0470},
                    {4,5,0.8190,0.2707},
                    {5,6,0.3660,0.1864},
                    {6,7,0.3811,0.1941},
                    {1,8,0.0922,0.0470},
                    {8,9,0.8190,0.2707},
                    {9,10,0.8190,0.2707},
            };
            this.branch=new double[9][4];
            for(int j=0;j<4;j++) {
                for (int i = 0; i < 9; i++) {
                    this.branch[i][j] = branch[i][j];
                }
            }
            double[][] tieBranch={
                    {5,9,0.5000,0.5000}
            };
            this.tieBranch=new double[1][4];
            for(int j=0;j<4;j++) {
                for (int i = 0; i < 1; i++) {
                    this.tieBranch[i][j] = tieBranch[i][j];
                }
            }
        }
        //endregion
        //region moni2图拓扑和负荷参数
        if(svgid.equals("moni2")){
            this.nodeNumber=9;
            double[][] loadApparentPower = {
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.025,0.002},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.000,0.000},
                    {0.025,0.002},
            };
            this.loadApparentPower=new double[9][2];
            for(int j=0;j<2;j++) {
                for (int i = 0; i < 9; i++) {
                    this.loadApparentPower[i][j] = loadApparentPower[i][j];
                }
            }
            double[][] branch = {
                    {1,2,0.3660,0.1864},
                    {2,3,0.3811,0.1941},
                    {3,4,0.0922,0.0470},
                    {4,5,0.8190,0.2707},
                    {1,6,0.3660,0.1864},
                    {6,7,0.3811,0.1941},
                    {7,8,0.0922,0.0470},
                    {8,9,0.8190,0.2707},
            };
            this.branch=new double[8][4];
            for(int j=0;j<4;j++) {
                for (int i = 0; i < 8; i++) {
                    this.branch[i][j] = branch[i][j];
                }
            }
            double[][] tieBranch={
                    {2,6,0.5000,0.5000},
                    {4,8,0.5000,0.5000}
            };
            this.tieBranch=new double[2][4];
            for(int j=0;j<4;j++) {
                for (int i = 0; i < 2; i++) {
                    this.tieBranch[i][j] = tieBranch[i][j];
                }
            }
        }
        //endregion
    }

    public double[][] getBranch() {
        return branch;
    }

    public void setBranch(String url) {
        int rows;
        int columns;
        File file = new File(url);
        try {
            InputStream in = new FileInputStream(file);
            Workbook workbook = Workbook.getWorkbook(in);
            Sheet sheet = workbook.getSheet(0);
            rows = sheet.getRows();
            columns = sheet.getColumns();

            branch = new double[rows][columns];
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    //System.out.println(sheet.getCell(j, i).getContents());
                    branch[i][j] = Double.parseDouble(sheet.getCell(j, i).getContents());
                }
            //region 生成网形结构矩阵和分层矩阵。
            matrixD = new int[nodeNumber][nodeNumber];
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBranch(double[][] branch){
        this.branch = branch;
        //region 生成网形结构矩阵和分层矩阵。
        matrixD = new int[nodeNumber][nodeNumber];
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
    }

    public String try2FindScehme(int outageNode){
        String result="Restoration scheme does not exist!";
        for(int i=0;i<tieBranch.length;i++) {
            if ((tieBranch[i][0] == outageNode) | (tieBranch[i][1] == outageNode)) {
                result=(int)tieBranch[i][0]+"-"+(int)tieBranch[i][1];
                return result;
            }
        }
        return result;
    }

    public void closeTie(double tieNode){
        int faultFather =0;
        for(int i=0;i<matrixC.length;i++){
            if(matrixC[i][faultNode-1]==1) faultFather=i+1;
        }
        //region 联络开关闭合后更新branch矩阵
        for(int i=0;i<tieBranch.length;i++){
            if((tieBranch[i][0] ==tieNode)|(tieBranch[i][1] ==tieNode)){
                for(int j=0;j<branch.length;j++){
                    if(((branch[j][0]==faultFather)&(branch[j][1]==faultNode))|((branch[j][0]==faultNode)&(branch[j][1]==faultFather))){
                        double[][] branchTemp=this.branch;
                        branchTemp[j][0] = tieBranch[i][0];
                        branchTemp[j][1] = tieBranch[i][1];
                        branchTemp[j][2] = tieBranch[i][2];
                        branchTemp[j][3] = tieBranch[i][3];
                    }
                }
            }
        }
        //endregion
        //region 更新matrixC矩阵和matrixH矩阵。
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
    }

    //region 搜索停电区域
    public LinkedList toFindOutage(int faultNode){
        this.faultNode = faultNode;
        outage = new LinkedList();
        outage.add(faultNode);
        //结点入队列
        LinkedList queue = new LinkedList();
        isVisited = new boolean[nodeNumber];
        isVisited[faultNode-1] =true;
        int u,w;
        queue.addLast(faultNode -1);
        while (!queue.isEmpty()) {

            u=((Integer)queue.removeFirst()).intValue();
            w=getFirstNeighbor(u);
            while(w!=-1) {
                if (!isVisited[w]) {
                    isVisited[w]=true;
                    outage.add(w+1);
                    //入队列
                    queue.addLast(w);
                }
                //寻找下一个邻接结点
                w=getNextNeighbor(u,w);
            }
        }
        return outage;
    }
    //取得第一个邻结点编号
    private int getFirstNeighbor(int index) {
        for(int j=0;j<matrixC.length;j++) {
            if (matrixC[index][j]>0) {
                return j;
            }
        }
        return -1;
    }

    //根据前一个邻接结点的下标来取得下一个邻接结点
    private int getNextNeighbor(int v1,int v2) {
        for (int j=v2+1;j<matrixC.length;j++) {
            if (matrixC[v1][j]>0) {
                return j;
            }
        }
        return -1;
    }
    //endregion

    //region Getter and Setter
    public double[][] getLoadApparentPower() {
        return loadApparentPower;
    }
    public double getLoadApparentPower(int node) {
        return loadApparentPower[node-1][0];
    }
    public void setLoadApparentPower(String url) {
        int rows;
        int columns;
        File file = new File(url);
        try {
            InputStream in = new FileInputStream(file);
            Workbook workbook = Workbook.getWorkbook(in);
            Sheet sheet = workbook.getSheet(0);
            rows = sheet.getRows();
            columns = sheet.getColumns();

            loadApparentPower = new double[rows][columns];
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    //System.out.println(sheet.getCell(j, i).getContents());
                    loadApparentPower[i][j] = Double.parseDouble(sheet.getCell(j, i).getContents());
                }
            nodeNumber = rows;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setLoadApparentPower(double[][] loadApparentPower){
        this.loadApparentPower = loadApparentPower;
    }
    public double[] getNodeKV() {
        return nodeKV;
    }

    public void setNodeKV(double nodeKV) {
        this.nodeKV = new double[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            if(i == root-1){
                this.nodeKV[root-1] = nodeKV;
            }else this.nodeKV[i] = nodeKV*0.95;
        }
    }

    public int getRoot() {
        return root;
    }

    public void setFaultNode(int faultNode) {
        this.faultNode = faultNode;
    }

    public int getFaultNode() {
        return faultNode;
    }

    public double[][] getTieBranch() {
        return tieBranch;
    }

    public void setTieBranch(double[][] tieBranch) {
        this.tieBranch = tieBranch;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
        //region 生成网形结构矩阵和分层矩阵。
        matrixD = new int[nodeNumber][nodeNumber];
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
    }
    //endregion
}

