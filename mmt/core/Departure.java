package mmt.core;

import java.time.LocalTime;

public class Departure implements java.io.Serializable {
	private LocalTime _time;
	private Service _service;
	private Station _station;

	Departure(LocalTime depTime, Service service, Station station) {
		_time = depTime;
		_service = service;
		_station = station;
		_station.addDeparture(this);
		_service.addDeparture(this);
	}

	Station getStation() {
		return _station;
	}

	LocalTime getTime() {
		return _time;
	}

 	Service getService(){
		return _service;
	}

	@Override
	public String toString() {
		return _time + " " + _station;
	}

}