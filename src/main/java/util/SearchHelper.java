package util;

import java.util.LinkedList;

/**
 * Created by zjuCHENW on 2016/5/17.
 * 功能1：搜寻停电区域
 * 功能2：搜索停电区域相连接的联络开关
 */
public class SearchHelper {
    private int n;
    private double[][] branch;
    private boolean[] isVisited;
    private LinkedList<Integer> outage;
    private LinkedList<Integer> ts;

    public LinkedList toFindOutage(int faultNode,NetParame netParame){
        outage = new LinkedList();
        outage.add(faultNode);
        branch = netParame.getBranch();
        n = branch.length;
        //结点入队列
        LinkedList queue = new LinkedList();
        isVisited = new boolean[n+1];
        int u,w;
        queue.addLast(faultNode);
        while (!queue.isEmpty()) {

            u=((Integer)queue.removeFirst()).intValue();
            w=getFirstNeighbor(u);
            while(w!=-1) {
                //入队列
                queue.addLast(w);
                outage.add(w);
                //System.out.println(w);
                //寻找下一个邻接结点
                w=getNextNeighbor(u);
            }
        }

        return outage;
    }
    //取得第一个邻结点编号
    private int getFirstNeighbor(int index) {
        for(int j=0;j<n;j++) {
            if (branch[j][0] == index) {
                isVisited[(int)branch[j][1]-1] = true;
                return (int)branch[j][1];
            }
        }
        return -1;
    }

    //根据前一个邻接结点的下标来取得下一个邻接结点
    private int getNextNeighbor(int u) {
        for (int j=0;j<n;j++) {
            if ((branch[j][0] == u)&!(isVisited[(int)branch[j][1]-1])) {
                isVisited[(int)branch[j][1]-1] = true;
                return (int)branch[j][1];
            }
        }
        return -1;
    }
}
