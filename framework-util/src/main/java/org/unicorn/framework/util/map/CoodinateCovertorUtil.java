
package org.unicorn.framework.util.map;

import java.math.BigDecimal;

/**
 *
 * @author xiebin
 *
 */
public class CoodinateCovertorUtil {
	private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	public static double pi = 3.1415926535897932384626;
	public static double a = 6378140.0;// 1975年国际椭球体长半轴
	public static double ee = 0.0033528131778969143;// 1975年国际椭球体扁率

	/**
	 * 1、WGS－84:大地坐标系/原始坐标系 2、GCJ-02:火星坐标系 3、BD-09:百度坐标系
	 * 
	 */

	/**
	 * 对double类型数据保留小数点后多少位 高德地图转码返回的就是 小数点后7位，为了统一封装一下
	 * 
	 * @param digit
	 *            位数
	 * @param in
	 *            输入
	 * @return 保留小数位后的数
	 */
	static double dataDigit(int digit, double in) {
		return new BigDecimal(in).setScale(7, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 将火星坐标转变成百度坐标
	 * 
	 * @param lngLat_gd
	 * 
	 *            火星坐标（高德、腾讯地图坐标等）
	 * @return 百度坐标
	 */
	public static LngLat gcj02ToBd09(LngLat lngLat_gd) {
		double x = lngLat_gd.getLongitude(), y = lngLat_gd.getLatitude();
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		return new LngLat(dataDigit(6, z * Math.cos(theta) + 0.0065), dataDigit(6, z * Math.sin(theta) + 0.006));

	}

	/**
	 * 将百度坐标转变成火星坐标
	 * 
	 * @param lngLat_bd
	 *            百度坐标（百度地图坐标）
	 * @return 火星坐标(高德、腾讯地图等)
	 */
	public static LngLat bd09ToGcj02(LngLat lngLat_bd) {
		double x = lngLat_bd.getLongitude() - 0.0065, y = lngLat_bd.getLatitude() - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		return new LngLat(dataDigit(6, z * Math.cos(theta)), dataDigit(6, z * Math.sin(theta)));

	}

	/**
	 * 百度坐标转换成wgs坐标
	 * 
	 * @param lngLat_gd
	 *            百度坐标
	 * 
	 * @return 原始坐标
	 */
	public static LngLat bd09ToWgs84(LngLat lngLat_gd) {
		LngLat gcj02 = bd09ToGcj02(lngLat_gd);
		return gcj02ToWgs84(gcj02);
	}

	/**
	 * 火星坐标转换成wgs坐标
	 * 
	 * @param lngLat_bd
	 *            火星坐标
	 * @return 原始坐标
	 */
	public static LngLat gcj02ToWgs84(LngLat lngLat) {
		LngLat gps = transform(lngLat);
        double lontitude = lngLat.getLongitude() * 2 - gps.getLongitude();
        double latitude = lngLat.getLatitude() * 2 - gps.getLatitude();
        return new LngLat(lontitude, latitude);

	}

	private static LngLat transform(LngLat lngLat) {
		if (outOfChina(lngLat)) {
			return lngLat;
		}
		double dLat = transformLat(lngLat.getLongitude() - 105.0, lngLat.getLatitude() - 35.0);
		double dLon = transformLon(lngLat.getLongitude() - 105.0, lngLat.getLatitude() - 35.0);
		double radLat = lngLat.getLatitude() / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lngLat.getLatitude() + dLat;
		double mgLon = lngLat.getLongitude() + dLon;
		return new LngLat(mgLon, mgLat);
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

	private static boolean outOfChina(LngLat lngLat) {
		if (lngLat.getLongitude() < 72.004 || lngLat.getLongitude() > 137.8347)
			return true;
		return lngLat.getLatitude() < 0.8293 || lngLat.getLatitude() > 55.8271;
	}

	/**
	 * 
	 * @author xiebin
	 *
	 */
	static class LngLat {
		private double longitude;// 经度
		private double latitude;// 维度

		public LngLat() {
		}

		public LngLat(double longitude, double latitude) {
			this.longitude = longitude;
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		@Override
		public String toString() {
			return "LngLat{" + "longitude=" + longitude + ", lantitude=" + latitude + '}';
		}
	}

	// 测试代码
	public static void main(String[] args) {
		LngLat lngLat_bd = new LngLat(112.960195000, 28.232849000);
		 System.out.println(bd09ToGcj02(lngLat_bd));
		// System.out.println(bd_encrypt(new LngLat(113.002878, 28.028143)));
	}
}
