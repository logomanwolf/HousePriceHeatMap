package bean;

import java.io.Serializable;

public class Position implements Serializable {
	private String lat;// 纬度
	private String lng;// 经度
	 private String count;//权重
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}
