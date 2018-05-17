package util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by zjuCHENW on 2016/5/5.
 * 功能：将matrixD邻接矩阵转换成matrixC和matrixH
 */
public class MatrixOperator {
    private int[][] matrixD;//邻接矩阵
    private int[][] matrixC;
    private int[] matrixH;
    private int n;
    private int root;

    public MatrixOperator(int[][] matrixD,int root) {
        this.matrixD = matrixD;
        this.root = root;
        n=matrixD.length;
        matrixH = new int[n];
        matrixC = new int[n][n];
    }

    //生成C弧结构矩阵、H节点分层矩阵
    public ArrayList generateCH(){
        ArrayList listOfCH = new ArrayList();
        boolean[] isVisited=new boolean[n];
        for (int i=0;i<n;i++) {
            isVisited[i]=false;
        }
        ArrayList locationsOfOne ;
        locationsOfOne = broadFirstSearch(isVisited, root-1);
        int x,y;
        for(int i=0;i<locationsOfOne.size();i++){
            x= (Integer) locationsOfOne.get(i);
            y= (Integer) locationsOfOne.get(++i);
            matrixC[x-1][y-1]=1;
        }
        listOfCH.add(matrixC);
        listOfCH.add(matrixH);
        return listOfCH;
    }
    //广度优先搜索算法
    private ArrayList broadFirstSearch(boolean[] isVisited,int i) {
        int u,w;
        LinkedList queue=new LinkedList();
        ArrayList locationTemp = new ArrayList();


        //访问结点i
        //System.out.println((i+1)+"节点属于第1层");
        matrixH[i]=1;
        isVisited[i]=true;
        //结点入队列
        queue.addLast(i);
        while (!queue.isEmpty()) {

            u=((Integer)queue.removeFirst()).intValue();
            w=getFirstNeighbor(u);
            while(w!=-1) {
                if(!isVisited[w]) {
                    //访问该结点
                    //System.out.println((u+1)+""+(w+1));
                    matrixH[w]=matrixH[u]+1;
                    //System.out.println((w+1)+"节点属于第"+matrixH[w]+"层");
                    locationTemp.add(u+1);
                    locationTemp.add(w+1);
                    //标记已被访问
                    isVisited[w]=true;
                    //入队列
                    queue.addLast(w);
                }
                //寻找下一个邻接结点
                w=getNextNeighbor(u, w);
            }
        }
        return locationTemp;
    }

    //取得第一个邻结点编号
    private int getFirstNeighbor(int index) {
        for(int j=0;j<n;j++) {
            if (matrixD[index][j]>0) {
                return j;
            }
        }
        return -1;
    }

    //根据前一个邻接结点的下标来取得下一个邻接结点
    private int getNextNeighbor(int v1,int v2) {
        for (int j=v2+1;j<n;j++) {
            if (matrixD[v1][j]>0) {
                return j;
            }
        }
        return -1;
    }
}
