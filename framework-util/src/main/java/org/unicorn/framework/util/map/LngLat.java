package org.unicorn.framework.util.map;
/**
 * 
 * @author xiebin
 *
 */
public class LngLat {
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
