
package org.unicorn.framework.util.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
*
*@author xiebin
*
*/
public class LngAndLatUtil {

	
	public static Map<String,Double> getLngAndLat(String address) throws JSONException{
        Map<String,Double> map=new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=fvb2HN4OjKygCXA58exgW7FhtUmNlR1c";
        String json = loadJSON(url);
        JSONObject obj = new JSONObject(json);
        if(obj.get("status").toString().equals("0")){
            double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
            map.put("lng", lng);
            map.put("lat", lat);
            //System.out.println("经度："+lng+"---纬度："+lat);
        }else{
            //System.out.println("未找到相匹配的经纬度！");
        }
        return map;
    }

    public static String loadJSON (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return json.toString();
    }
    public static void main(String[] args) throws JSONException{
        /*把代码中的ak值（红色字部分）更改为你自己的ak值，在百度地图API中注册一下就有。
        调用方式：
        ak=F454f8a5efe5e577997931cc01de3974
        */ 

        Map<String,Double> map=LngAndLatUtil.getLngAndLat("长沙园林生态园");
        System.out.println("经度："+map.get("lng")+"---纬度："+map.get("lat"));
        
        System.out.println(CoodinateCovertorUtil.bd09ToGcj02(new  LngLat(map.get("lng"),map.get("lat"))));  
    }
	
}

