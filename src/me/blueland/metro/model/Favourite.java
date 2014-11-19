package me.blueland.metro.model;

public class Favourite {
	private String _ID;
	private String LINE;
	private String STATIONCODE;
	private String STATIONNAME;
	private String LONGITUDE;
	private String LATITUDE;
	private String RAIL;

	public Favourite(String _ID, String lINE, String sTATIONCODE,
			String sTATIONNAME, String lONGITUDE, String lATITUDE, String rAIL) {
		this._ID = _ID;
		LINE = lINE;
		STATIONCODE = sTATIONCODE;
		STATIONNAME = sTATIONNAME;
		LONGITUDE = lONGITUDE;
		LATITUDE = lATITUDE;
		RAIL = rAIL;
	}

	public String get_ID() {
		return _ID;
	}

	public void set_ID(String _ID) {
		this._ID = _ID;
	}

	public String getLINE() {
		return LINE;
	}

	public void setLINE(String lINE) {
		LINE = lINE;
	}

	public String getSTATIONCODE() {
		return STATIONCODE;
	}

	public void setSTATIONCODE(String sTATIONCODE) {
		STATIONCODE = sTATIONCODE;
	}

	public String getSTATIONNAME() {
		return STATIONNAME;
	}

	public void setSTATIONNAME(String sTATIONNAME) {
		STATIONNAME = sTATIONNAME;
	}

	public String getRAIL() {
		return RAIL;
	}

	public void setRAIL(String rAIL) {
		RAIL = rAIL;
	}

	public String getLONGITUDE() {
		return LONGITUDE;
	}

	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}

	public String getLATITUDE() {
		return LATITUDE;
	}

	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}

}
