package mmt.core;

import mmt.core.exceptions.NoSuchStationNameException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class ShowServicesAbstract {


    private static Comparator<Service> _comparator = new Comparator<Service>() { // Sorts services by initial departure time
        public int compare(Service s1, Service s2) {
            return s1.getIniDeparture().getTime().compareTo(s2.getIniDeparture().getTime());
        }
    };


     final List<String> showServices(String name, Map<String, Station> _stationMap, Map<Integer,Service> _serviceMap) throws NoSuchStationNameException{


        checkStation(name, _stationMap);

        List<Service> servicesList = getServices(name,_serviceMap);

        sort(servicesList, _comparator);

       return getServicesInfo(servicesList);

    }


    private void checkStation(String name, Map<String, Station> _stationMap) throws NoSuchStationNameException{
        if(_stationMap.get(name) == null){
            throw new NoSuchStationNameException(name);
        }
    }


    abstract List<Service> getServices(String name, Map<Integer, Service> _serviceMap);

    private void sort(List<Service> list,Comparator<Service> comparator){
        list.sort(comparator);
    }

    private List<String> getServicesInfo(List<Service> servicesList){
        List<String> serviceInfo = new ArrayList<>();
        for(Service service : servicesList) { // Builds String Array to send to App
            serviceInfo.add(service.toString());
        }
        return serviceInfo;
    }

}
