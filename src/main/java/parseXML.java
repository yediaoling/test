/**
 * Created by yediaoling on 2016/4/22.
 */

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class parseXML {

    public Document parse(URL url) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        System.out.println("读取XML文件成功");
        return document;
    }

    public void parse_Subs(Document document) throws DocumentException, JSONException {
        interactWithDB doDB = new interactWithDB();
        JSONObject jsonObj = new JSONObject();
        String table = "PW_SUBSTATION";
        Element root = document.getRootElement();

        for (Iterator i = root.elementIterator("Substation"); i.hasNext();){
            Element element = (Element) i.next();

            jsonObj.put("Subs_Id",element.attributeValue("ID"));
            jsonObj.put("Subs_Name",element.element("Naming.name").getData());
            jsonObj.put("Subs_Type",element.element("PowerSystemResource.PSRType").attributeValue("resource"));
            jsonObj.put("Subs_Circuits",element.element("Substation.Circuits").attributeValue("resource"));
            jsonObj.put("Subs_Zones",element.element("PowerSystemResource.Zones").attributeValue("resource"));
            jsonObj.put("Subs_BaseVoltage",element.element("Substation.BaseVoltage").attributeValue("resource"));

            try {
                doDB.Write(jsonObj,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Substation数据存入数据库成功");
    }

    public void parse_ACLineSegment(Document document) throws DocumentException, JSONException {
        interactWithDB doDB = new interactWithDB();
        JSONObject jsonObj = new JSONObject();
        String table = "PW_ACLINESEGMENT";
        Element root = document.getRootElement();

        for (Iterator i = root.elementIterator("ACLineSegment"); i.hasNext();){
            Element element = (Element) i.next();
            if(element.element("Conductor.length").getData().equals("0"))
                continue;

            jsonObj.put("ACLINE_ID", element.attributeValue("ID"));
            jsonObj.put("ACLINE_NAME",element.element("Naming.name").getData());
            jsonObj.put("ACLINE_TYPE",element.element("PowerSystemResource.PSRType").attributeValue("resource"));
            jsonObj.put("ACLINE_LENGTH",element.element("Conductor.length").getData());
            jsonObj.put("ACLINE_CIRCUITS",element.element("PowerSystemResource.Circuits").attributeValue("resource"));
            jsonObj.put("ACLINE_ZONES",element.element("PowerSystemResource.Zones").attributeValue("resource"));

            for(Iterator iter = root.elementIterator("Terminal"); iter.hasNext();){
                Element ele = (Element) iter.next();
                String TERM_L = "TERM_" + element.attributeValue("ID") + "_L";
                String TERM_R = "TERM_" + element.attributeValue("ID") + "_R";
                if(ele.attributeValue("ID").equals(TERM_L)){
                    jsonObj.put("CONNECTIVITYNODE_L",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
                if(ele.attributeValue("ID").equals(TERM_R)){
                    jsonObj.put("CONNECTIVITYNODE_R",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
            }

            try {
                doDB.Write(jsonObj,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ACLineSegment数据存入数据库成功");
    }

    public void parse_BusbarSection(Document document) throws DocumentException, JSONException {
        interactWithDB doDB = new interactWithDB();
        JSONObject jsonObj = new JSONObject();
        String table = "PW_BUSBARSECTION";
        Element root = document.getRootElement();

        for (Iterator i = root.elementIterator("BusbarSection"); i.hasNext();){
            Element element = (Element) i.next();

            jsonObj.put("BUS_ID", element.attributeValue("ID"));
            jsonObj.put("BUS_NAME",element.element("Naming.name").getData());

            for(Iterator iter = root.elementIterator("Terminal"); iter.hasNext();){
                Element ele = (Element) iter.next();
                String TERM_L = "TERM_" + element.attributeValue("ID") + "_L";
                if(ele.attributeValue("ID").equals(TERM_L)){
                    jsonObj.put("CONNECTIVITY",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
            }

            try {
                doDB.Write(jsonObj,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("BusbarSection数据存入数据库成功");
    }

    public void parse_Switch(Document document) throws DocumentException, JSONException {
        interactWithDB doDB = new interactWithDB();
        JSONObject jsonObj = new JSONObject();
        String table = "PW_SWITCH";
        Element root = document.getRootElement();

        for (Iterator i = root.elementIterator("Breaker"); i.hasNext();){
            Element element = (Element) i.next();

            jsonObj.put("SWITCH_ID", element.attributeValue("ID"));
            jsonObj.put("SWITCH_NAME",element.element("Naming.name").getData());

            for(Iterator iter = root.elementIterator("Terminal"); iter.hasNext();){
                Element ele = (Element) iter.next();
                String TERM_L = "TERM_" + element.attributeValue("ID") + "_L";
                String TERM_R = "TERM_" + element.attributeValue("ID") + "_R";
                if(ele.attributeValue("ID").equals(TERM_L)){
                    if(ele.element("Terminal.ConnectivityNode").attributeValue("resource").equals("")){
                        jsonObj.put("CONNECTIVITY_L","#0000000");
                    }
                    else
                        jsonObj.put("CONNECTIVITY_L",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
                if(ele.attributeValue("ID").equals(TERM_R)){
                    jsonObj.put("CONNECTIVITY_R",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
            }

            try {
                doDB.Write(jsonObj,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Iterator i = root.elementIterator("Disconnector"); i.hasNext();){
            Element element = (Element) i.next();

            jsonObj.put("SWITCH_ID", element.attributeValue("ID"));
            jsonObj.put("SWITCH_NAME",element.element("Naming.name").getData());

            for(Iterator iter = root.elementIterator("Terminal"); iter.hasNext();){
                Element ele = (Element) iter.next();
                String TERM_L = "TERM_" + element.attributeValue("ID") + "_L";
                String TERM_R = "TERM_" + element.attributeValue("ID") + "_R";
                if(ele.attributeValue("ID").equals(TERM_L)){
                    jsonObj.put("CONNECTIVITY_L",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
                if(ele.attributeValue("ID").equals(TERM_R)){
                    jsonObj.put("CONNECTIVITY_R",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
            }

            try {
                doDB.Write(jsonObj,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Iterator i = root.elementIterator("LoadBreakSwitch"); i.hasNext();){
            Element element = (Element) i.next();

            jsonObj.put("SWITCH_ID", element.attributeValue("ID"));
            jsonObj.put("SWITCH_NAME",element.element("Naming.name").getData());

            for(Iterator iter = root.elementIterator("Terminal"); iter.hasNext();){
                Element ele = (Element) iter.next();
                String TERM_L = "TERM_" + element.attributeValue("ID") + "_L";
                String TERM_R = "TERM_" + element.attributeValue("ID") + "_R";
                if(ele.attributeValue("ID").equals(TERM_L)){
                    jsonObj.put("CONNECTIVITY_L",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
                if(ele.attributeValue("ID").equals(TERM_R)){
                    jsonObj.put("CONNECTIVITY_R",ele.element("Terminal.ConnectivityNode").attributeValue("resource"));
                }
            }

            try {
                doDB.Write(jsonObj,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Switch数据存入数据库成功");
    }

    public static void main(String[] args) throws Exception{
        URL url = new URL("file:///C:/Users/yediaoling/Desktop/宏远变电站_宏远F22上滘线.xml");
        parseXML parsexml = new parseXML();
        Document document = parsexml.parse(url);
//        parsexml.parse_Subs(document);
//        parsexml.parse_ACLineSegment(document);
//        parsexml.parse_BusbarSection(document);
//        parsexml.parse_Switch(document);
    }
}
