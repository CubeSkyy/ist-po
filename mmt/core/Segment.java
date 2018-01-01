package mmt.core;

import java.time.Duration;

import java.time.temporal.ChronoUnit;


public class Segment implements java.io.Serializable {

	private double _cost;
	private Duration _time;
	private Departure _iniDep;
  private Departure _endDep;
  private Service _service;


  Segment(Departure iniDep, Departure endDep, Service service){
    _service = service;
    _iniDep = iniDep;
    _endDep = endDep;

    _time = Duration.between(_iniDep.getTime(), _endDep.getTime());

    _cost = ( _time.get(ChronoUnit.SECONDS) * _service.getCost() ) / ( _service.getTotalTime().get(ChronoUnit.SECONDS) );
  }

    double getCost(){
      return _cost;
    }

    Service getService(){
      return _service;
    }

    Departure getIniDep(){
      return _iniDep;
    }

    Departure getEndDep(){
      return _endDep;
    }

    Duration getTime(){
      return _time;
    }

  @Override
  public String toString() {

      String result = "";
      result += _service.getServiceHeadline();
      result += String.format("%.2f", _cost) + "\n";
      result += _service.getServiceBody(_iniDep, _endDep);

    return result;
  }
}
