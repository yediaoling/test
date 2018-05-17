import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by zjuCHENW on 2016/5/5.
 */
public class Matrix {
    //private ArrayList eleIndexList;
    private int[][] matrixD;//邻接矩阵
    private boolean[] isVisited;
    private int n;
    //private int flag;


    public int[][] trans2C(int[][] D,int root){

        n=D.length;
        ArrayList locationsOfOne ;
        matrixD =new int[n][n];
        matrixD = D;

        boolean[] isVisited=new boolean[n];
        for (int i=0;i<n;i++) {
            isVisited[i]=false;
        }

        locationsOfOne = broadFirstSearch(isVisited, root-1);
        int[][] matrixC = new int[n][n];
        int x,y;
        for(int i=0;i<locationsOfOne.size();i++){
            x= (Integer) locationsOfOne.get(i);
            y= (Integer) locationsOfOne.get(++i);
            matrixC[x-1][y-1]=1;
        }

     return matrixC;
    }
    private ArrayList broadFirstSearch(boolean[] isVisited,int i) {
        int u,w;
        LinkedList queue=new LinkedList();
        ArrayList locationTemp = new ArrayList();


        //访问结点i
        //System.out.print(getValueByIndex(i)+"  ");
        isVisited[i]=true;
        //结点入队列
        queue.addLast(i);
        while (!queue.isEmpty()) {
            u=((Integer)queue.removeFirst()).intValue();
            w=getFirstNeighbor(u);
            while(w!=-1) {
                if(!isVisited[w]) {
                    //访问该结点
                    //System.out.println(getValueByIndex(w)+"  ");
//                    System.out.println((u+1)+""+(w+1));
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
    public int getFirstNeighbor(int index) {
        /*
        int firstIndex=-1;
        for (int j = 0; j < n; j++) {
            if ((matrixD[index][j] > 0) && !isVisited[j]) {
                eleIndexList.add((index+1)*10+j+1);
                if(firstIndex==-1)firstIndex=j;
            }
        }
        if(firstIndex!=-1){
            return firstIndex;
        }else return -1;*/
        for(int j=0;j<n;j++) {
            if (matrixD[index][j]>0) {
                return j;
            }
        }
        return -1;
    }

    //根据前一个邻接结点的下标来取得下一个邻接结点
    public int getNextNeighbor(int v1,int v2) {
        for (int j=v2+1;j<n;j++) {
            if (matrixD[v1][j]>0) {
                return j;
            }
        }
        return -1;
    }
}
