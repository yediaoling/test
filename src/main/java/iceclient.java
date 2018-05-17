
import Ice.ObjectPrx;
import PNS.*;
import com.njpn.se9200.wyjPublic;
import com.sun.jna.StringArray;


public class iceclient {
    private static final int DEFAULT_TIME_OUT = 2000;
    private static iceclient __iceclient = null;
    private String identId;
    private PNS.PnsRearPrx twowayRear = null;
    private Ice.Communicator communicator = null;
    //	protected Logger logger= LoggerFactory.getLogger(iceclient.class.getName());
    public iceclient(){
//        String mbHost = wyjPublic.getConfigRecordValue("mbHostName", "localhost");
//        String iceport = wyjPublic.getConfigRecordValue("icePort", "10000");
        String mbHost = "192.168.0.7";
        String iceport = "10000";
        identId = "tcp -h "+mbHost+" -p " + iceport;
//        loadProxy();
    }


    public static iceclient getIceClientInstance()
    {
        if(__iceclient==null)
            __iceclient = new iceclient();
        return __iceclient;
    }

    private static long lastProxyTime = 0;
    private boolean loadProxy() {
        if((System.currentTimeMillis()-lastProxyTime)<5000)return false;
        lastProxyTime = System.currentTimeMillis();
        System.out.println("loadProxy");
        System.out.println(identId);
        try {
            if(communicator==null)
                communicator = Ice.Util.initialize();
            ObjectPrx objPrxRear = communicator.stringToProxy("pnsserver:"+identId);
            objPrxRear.ice_timeout(DEFAULT_TIME_OUT);
            twowayRear = PNS.PnsRearPrxHelper.checkedCast(objPrxRear.ice_twoway());
            if(twowayRear==null)
            {
                System.out.println("twowayRear==null");
                wyjPublic.SaveErrorLog("loadProxy error:twowayRear==null;proxyText="+identId);
                return false;
            }
            System.out.println("loadProxy ok");
            wyjPublic.SaveErrorLog("loadProxy ok");
//            Thread.sleep(5000);

//            for(int i = 0; i<500000;i++);

        } catch (Exception e) {
            //    logger.error("execute proxy cause error:" + e);
            System.out.println("loadProxy error:"+e.getMessage());
            e.printStackTrace();
            return false;
            //throw new RuntimeException(e);
        } finally {

        }
        return true;
    }

    public void destroyCommunicatior()
    {
        System.out.println("destroyCommunicatior");
        System.out.println("destroyCommunicatior");
        if (communicator != null) {
            try {
                communicator.destroy();
                communicator = null;
            } catch (Exception e) {
                // do nothing   
            }
        }
    }

    public String getTodayCurveData(String mRID)
    {
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return "";
        String linevalue = "";
        try
        {
            byte app=0;
            byte state=0;
            Ice.IntHolder SamplePeriod = new Ice.IntHolder();
            Ice.IntHolder CurSampleDot = new Ice.IntHolder();
            floatArrayHolder values = new floatArrayHolder();
            if(twowayRear.getCurveData(app, state, mRID, SamplePeriod, CurSampleDot, values))
            {
                for(int i=0;i<values.value.length && i<=CurSampleDot.value;i++)
                {
                    if(i==0)
                        linevalue = values.value[i]+"";
                    else
                        linevalue +="," + values.value[i];
                    System.out.println("曲线数据:"+i+":"+values.value[i]);
                }
                System.out.println("曲线数据:"+mRID);
                System.out.println("采样周期:"+SamplePeriod.value);
                System.out.println("当前采样点:"+CurSampleDot.value);
            }
            else
            {
                System.out.println("读取失败!");
                wyjPublic.SaveErrorLog("getTodayCurveData 读取失败");
            }
            System.out.println("曲线:"+linevalue);
        }
        catch(Exception e)
        {
            System.out.println("getTodayCurveData error:"+e.getMessage());
            e.printStackTrace();
            wyjPublic.SaveErrorLog("getTodayCurveData error:"+e.getMessage());
        }
        return linevalue;
    }
    public String[] getTablemRID(int tableid)
    {
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return null;
        stringArrayHolder values = new stringArrayHolder();
        try
        {
            if(twowayRear.GetOneTablemRIDs((byte)0,(byte)0, tableid, values)>0)
            {
                return values.value;
            }
            else
            {
                wyjPublic.SaveErrorLog("读取失败(getYcValues)!");
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            wyjPublic.SaveErrorLog("读取失败(getYcValues):"+ex.getMessage());
        }
        return null;
    }
    public float[] getYcValues(long[]mRIDs)
    {
        if(mRIDs==null) return null;
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return null;
        floatArrayHolder values = new floatArrayHolder();
        //	for(int i=0;i<mRIDs.length;i++)
        //	{
        //		wyjPublic.SaveErrorLog("mRIDs["+i+"]"+mRIDs[i]);
        //	}
        try
        {
            if(twowayRear.getYcBymRIds((byte)0,(byte)0, mRIDs, values))
            {
                return values.value;
            }
            else
            {
                wyjPublic.SaveErrorLog("读取失败(getYcValues)!");
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            wyjPublic.SaveErrorLog("读取失败(getYcValues):"+ex.getMessage());
        }
        return null;
    }


    public float[] getFloatValues(String[]mRIDs,String[] fieldnames)
    {
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return null;
        if(mRIDs==null||fieldnames==null||mRIDs.length!=fieldnames.length) return null;
        floatArrayHolder values = new floatArrayHolder();
        try
        {
            if(twowayRear.getFloatValues((byte)0,(byte)0,mRIDs,fieldnames,values))
            {
                return values.value;
            }
            else
            {
                wyjPublic.SaveErrorLog("读取失败(getFloatValues)!");
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            wyjPublic.SaveErrorLog("读取失败(getFloatValues):"+ex.getMessage());
        }
        return null;
    }

    public String[] getStrValues(String[]mRIDs,String[] fieldnames)
    {
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return null;
        if(mRIDs==null||fieldnames==null||mRIDs.length!=fieldnames.length) return null;
        stringArrayHolder values = new stringArrayHolder();
        try
        {
            if(twowayRear.getStrValues((byte)0,(byte)0,mRIDs,fieldnames,values))
            {
                return values.value;
            }
            else
            {
                wyjPublic.SaveErrorLog("读取失败(getFloatValues)!");
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            wyjPublic.SaveErrorLog("读取失败(getFloatValues):"+ex.getMessage());
        }
        return null;
    }

    public int[] getIntValues(String[]mRIDs,String[] fieldnames)
    {
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return null;
        if(mRIDs==null||fieldnames==null||mRIDs.length!=fieldnames.length) return null;
        intArrayHolder values = new intArrayHolder();
        try
        {
            if(twowayRear.getintValues((byte)0,(byte)0,mRIDs,fieldnames,values))
            {
                return values.value;
            }
            else
            {
                wyjPublic.SaveErrorLog("读取失败(getFloatValues)!");
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            wyjPublic.SaveErrorLog("读取失败(getFloatValues):"+ex.getMessage());
        }
        return null;
    }

    public int[] getDevStatus(String[]mRIDs)
    {
        if(twowayRear==null) loadProxy();
        if(twowayRear==null) return null;
        if(mRIDs==null) return null;
        byteArrayHolder values = new byteArrayHolder();
        try
        {
            twowayRear.getDevRunStatus((byte)0,(byte)0,mRIDs,values);
            int[] t = new int[values.value.length];
            for(int i=0;i<t.length;i++)
                t[i] = values.value[i];
            return t;

        }catch(Exception ex)
        {
            ex.printStackTrace();
            wyjPublic.SaveErrorLog("读取失败(getFloatValues):"+ex.getMessage());
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        iceclient ic = new iceclient();
        //Thread.sleep(5000);

        String[] ID = ic.getIceClientInstance().getTablemRID(32);
        for(int i = 0;i<ID.length;i++)
            System.out.println(ID[i]);

        String[] fields = new String[ID.length];
        for(int i=0; i<ID.length; i++){
            fields[i] = "name";
        }

        String[] kaiguan;
        kaiguan = ic.getIceClientInstance().getStrValues (ID,fields);
        for(int i = 0;i<ID.length;i++)
            System.out.println(kaiguan[i]);

    }

}


