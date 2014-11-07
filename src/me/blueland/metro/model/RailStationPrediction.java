package me.blueland.metro.model;

public class RailStationPrediction {

	private String LINE;
	private String MIN;
	private String DESTINATION;
	private String DESTINATIONNAME;

	public RailStationPrediction(String lINE, String mIN, String dESTINATION,
			String dESTINATIONNAME) {
		super();
		LINE = lINE;
		MIN = mIN;
		DESTINATION = dESTINATION;
		DESTINATIONNAME = dESTINATIONNAME;
	}

	public String getLINE() {
		return LINE;
	}

	public void setLINE(String lINE) {
		LINE = lINE;
	}

	public String getMIN() {
		return MIN;
	}

	public void setMIN(String mIN) {
		MIN = mIN;
	}

	public String getDESTINATION() {
		return DESTINATION;
	}

	public void setDESTINATION(String dESTINATION) {
		DESTINATION = dESTINATION;
	}

	public String getDESTINATIONNAME() {
		return DESTINATIONNAME;
	}

	public void setDESTINATIONNAME(String dESTINATIONNAME) {
		DESTINATIONNAME = dESTINATIONNAME;
	}

}
