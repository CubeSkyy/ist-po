package mmt.core;

import mmt.core.exceptions.NoSuchStationNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowServicesByDepartingStation extends ShowServicesAbstract {

    List<Service> getServices(String name, Map<Integer, Service> _serviceMap){
        List<Service> servicesByDepartureList = new ArrayList<>();
        for (Service service : _serviceMap.values()) { // Gets all services with departing station name
            if (service.getIniDeparture().getStation().toString().equals(name))
                servicesByDepartureList.add(service);
        }
        return servicesByDepartureList;
    }

    List<String> getInfo(String name, Map<String, Station> _stationMap, Map<Integer,Service> _serviceMap) throws NoSuchStationNameException{
        return showServices(name, _stationMap, _serviceMap);
    }

}
