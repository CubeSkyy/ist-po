package mmt.core;

import mmt.core.exceptions.NoSuchStationNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowServicesArrivingAtStation extends ShowServicesAbstract {

    List<Service> getServices(String name, Map<Integer, Service> _serviceMap){
        List<Service> servicesByArrivalList = new ArrayList<>();
        for (Service service : _serviceMap.values()) { // Gets all services with arriving station name
            if (service.getLastDeparture().getStation().toString().equals(name))
                servicesByArrivalList.add(service);
        }
        return servicesByArrivalList;
    }

    List<String> getInfo(String name, Map<String, Station> _stationMap, Map<Integer,Service> _serviceMap) throws NoSuchStationNameException{
        return showServices(name, _stationMap, _serviceMap);
    }

}