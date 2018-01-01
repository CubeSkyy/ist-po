package mmt.core;

import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Itinerary implements java.io.Serializable {

	private double _cost;
	private LocalDate _date;
	private Duration _totalTime;
	private List<Segment> _segList;

	private static Comparator<Itinerary> _compareByDepTime = new Comparator<Itinerary>() {
		public int compare(Itinerary i1, Itinerary i2) {
			if(i1.getIniDep().getTime().equals(i2.getIniDep().getTime())){
				return _compareByArrivalTime.compare(i1, i2); //se for igual, desempatamos com Arrival Time;
			}else if(i1.getIniDep().getTime().isBefore(i2.getIniDep().getTime())){
				return -1;
			}else {
				return 1;
			}
		}
	};

	private static Comparator<Itinerary> _compareByArrivalTime = new Comparator<Itinerary>() {
		public int compare(Itinerary i1, Itinerary i2) {
			if(i1.getLastDep().getTime().equals(i2.getLastDep().getTime())){
				return 1; //se for igual, tomamos como i1>i2;
			}else if(i1.getLastDep().getTime().isBefore(i2.getLastDep().getTime())){
				return -1;
			}else {
				return 1;
			}
		}
	};

	private static Comparator<Itinerary> _compareByDate = new Comparator<Itinerary>() {
		public int compare(Itinerary i1, Itinerary i2) {
			return i1.getDate().compareTo(i2.getDate());
		}
	};


	Itinerary( LocalDate date) {
		_segList = new ArrayList<>();
		_date = date;
		_totalTime = Duration.ZERO;
	}

	Itinerary( LocalDate date,List<Segment> segList ) {
		this(date);
		_segList = segList;
		doCost();
		doTime();
	}

	void addSegment(Segment segment){
		_segList.add(segment);
		_cost += segment.getCost();
		doTime();
	}

	private void doCost() {
		for( Segment s : _segList)
			_cost += s.getCost();
	}


	private void doTime(){
		_totalTime = Duration.between(getIniDep().getTime(),getLastDep().getTime());
		}

	static Comparator<Itinerary> getCompareByDepTime(){
		return _compareByDepTime;
	}

	static Comparator<Itinerary> getCompareByDate(){
		return _compareByDate;
	}

	Departure getLastDep(){
		return _segList.get(_segList.size()-1).getEndDep();
	}

	private Departure getIniDep(){
		return _segList.get(0).getIniDep();
	}

	Duration getTotalTime(){
		return _totalTime;
	}

	double getCost(){
		return _cost;
	}

	private LocalDate getDate(){
		return _date;
	}

	public String toString(int index){
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Itinerário ").append(index).append(" para ").append(_date).append(" @ ").append(String.format("%.2f", _cost)).append("\n");

		for(Segment seg : _segList) {
			stringBuilder.append(seg);
			stringBuilder.append("");
		}
		return stringBuilder.toString();
	}
}
