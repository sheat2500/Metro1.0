package me.blueland.metro.model;

public class RailStation {

	private String id;
	private String line;
	private String latitude;
	private String longitude;
	private String stationCode;
	private String stationName;

	public RailStation(String id, String line, String stationCode,
			String stationName, String latitude, String longitude) {
		this.id = id;
		this.line = line;
		this.latitude = latitude;
		this.longitude = longitude;
		this.stationCode = stationCode;
		this.stationName = stationName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}