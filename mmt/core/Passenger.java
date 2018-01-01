package mmt.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Passenger implements Comparable<Passenger>, java.io.Serializable {

	private int _id;
	private String _name;
	private Category _category;
	private int _numItineraries;
	private Duration _timeAcum;

	private LinkedList<Double> _totalPaid = new LinkedList<>();
	private List<Itinerary> _itiList;


	Passenger(int id, String name) {
		_id = id;
		_name = name;
		_category = new Normal();
		_timeAcum = Duration.ZERO;
		_numItineraries = 0;
		_itiList = new ArrayList<>();
	}

	void addItinerary(Itinerary itinerary){
		_numItineraries++;
		_itiList.add(itinerary);
		_timeAcum = _timeAcum.plus(itinerary.getTotalTime());
		_totalPaid.add(itinerary.getCost()*(1-_category.getDiscount()));
		_category = _category.next(getLast10Paid());
	}

	int getId() {
		return _id;
	}

	String getName() {
		return _name;
	}


	private double getLast10Paid() {
		double sum = 0;

		if(_totalPaid.size() > 10){
			for(int i = _totalPaid.size()-10; i < _totalPaid.size(); i++){
				sum += _totalPaid.get(i);
			}
		}else{
			for(double d: _totalPaid){
				sum += d;
			}
		}

		return sum;
	}

	private double getTotalPaid(){
		double sum = 0;

		for(double d: _totalPaid){
			sum += d;
		}
		return sum;
	}

	void setName(String name) {
		_name = name;
	}

	public int compareTo(Passenger p2) {
		return this.getId() - p2.getId();
	}


	@Override
	public String toString() {
		return ("" + getId() + "|" + getName() + "|" + _category + "|" + _numItineraries + "|" + String.format("%.2f", getTotalPaid())  + "|" + String.format("%02d:%02d",_timeAcum.toHours(), _timeAcum.toMinutes()%60) );
	}

	List<Itinerary> getItiList(){
		return _itiList;
	}

}
