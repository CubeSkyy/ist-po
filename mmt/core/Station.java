package mmt.core;

import java.util.List;
import java.util.ArrayList;


public class Station implements java.io.Serializable {
	private String _name;
	private List<Departure> _depList = new ArrayList<>();

	Station(String name) {
		_name = name;
	}

	public String toString() {
		return _name;
	}

	void addDeparture(Departure departure) {
		_depList.add(departure);
	}


	List<Departure> getDepList(){
		return _depList;
	}
}