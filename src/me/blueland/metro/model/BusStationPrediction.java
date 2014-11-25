package me.blueland.metro.model;

public class BusStationPrediction {

	private String ROOTID;
	private String MIN;
	private String DIRECTIONTEXT;

	public BusStationPrediction(String rOOTID, String mIN, String dIRECTIONTEXT) {
		ROOTID = rOOTID;
		MIN = mIN;
		DIRECTIONTEXT = dIRECTIONTEXT;
	}

	public String getROOTID() {
		return ROOTID;
	}

	public void setROOTID(String rOOTID) {
		ROOTID = rOOTID;
	}

	public String getMIN() {
		return MIN;
	}

	public void setMIN(String mIN) {
		MIN = mIN;
	}

	public String getDIRECTIONTEXT() {
		return DIRECTIONTEXT;
	}

	public void setDIRECTIONTEXT(String dIRECTIONTEXT) {
		DIRECTIONTEXT = dIRECTIONTEXT;
	}

}