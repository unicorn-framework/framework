package org.unicorn.framework.util.map;

public class MapsUtil {
	//R为地球半径，可取平均值 6371.137km 
	public final static  Double R=6371.137;
	/**
	 * 返回中心点dis半径范围内外接矩形的四个点经纬度范围
	 * @param dis 单位km
	 * @param latitude  经度
	 * @param longitude 维度
	 * @return
	 */
	public CoordinateRange getCoordinateRange(Double dis,Double latitude,Double longitude ){
		CoordinateRange coordinateRange=new CoordinateRange();
        double dlng =  2*Math.asin(Math.sin(dis/(2*R))/Math.cos(latitude*Math.PI/180));  
        dlng = dlng*180/Math.PI;//角度转为弧度  
        double dlat = dis/R;  
        dlat = dlat*180/Math.PI;      
        double minlat =latitude-dlat;  
        double maxlat = latitude+dlat;  
        double minlng = longitude -dlng;  
        double maxlng = longitude + dlng;  
        coordinateRange.setMinlat(minlat);
        coordinateRange.setMaxlat(maxlat);
        coordinateRange.setMinlng(minlng);
        coordinateRange.setMaxlng(maxlng);
		return coordinateRange;
	}
	
	
	
	/**
	 * 计算两个经纬度之间的距离(单位：米)
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;	
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * R;
	   s = Math.round(s * 1000);
	   return s;
	}

	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	
	public static void main(String[] args) {
		System.out.println(MapsUtil.getDistance(29.610970488661984,106.5763428005899,29.615467,106.581515));
//		System.out.println(JsonUtils.toJson(MapsUtil.getCoordinateRange(0.5d,29.615467,106.581515)));
	}

	
	
	
	
	
	
	
	static class CoordinateRange{
		private Double minlat;
		private Double maxlat;
		private Double minlng;
		private Double maxlng;
		public Double getMinlat() {
			return minlat;
		}
		public void setMinlat(Double minlat) {
			this.minlat = minlat;
		}
		public Double getMaxlat() {
			return maxlat;
		}
		public void setMaxlat(Double maxlat) {
			this.maxlat = maxlat;
		}
		public Double getMinlng() {
			return minlng;
		}
		public void setMinlng(Double minlng) {
			this.minlng = minlng;
		}
		public Double getMaxlng() {
			return maxlng;
		}
		public void setMaxlng(Double maxlng) {
			this.maxlng = maxlng;
		}
		
		
		
	}

}

