import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by yediaoling on 2017/3/15.
 */

interface CLibrary extends Library{
    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("demo",CLibrary.class);
    public void HelloWorld();
    //public int  cGetMemConnectWord();
    //public int add(int a,int b);
}

public class Communication {

    public static void main(String[] args){
        System.out.println("1");
        CLibrary.INSTANCE.HelloWorld();
       /// CLibrary.INSTANCE.cGetMemConnectWord();
        System.out.println("2");
        //System.out.println(CLibrary.INSTANCE.add(24,1000));

        iceclient ic = new iceclient();
        System.out.println(ic.getIceClientInstance().getTablemRID(32));
    }
}
