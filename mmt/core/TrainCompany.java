package mmt.core;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NonUniquePassengerNameException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 *
 * @author Barbara Caracol 84703
 * @author Miguel Coelho 87687
 * @version 1.0
 */


public class TrainCompany implements java.io.Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  /** ID to be used on the next Passenger created. */
  private int _nextPassengerId = 0;
  /** Map that contains passenger IDs as keys and Passenger objects as values */
  private Map<Integer, Passenger> _passengerMap = new HashMap<>();
  /** List that contains all services */
  private Map<Integer, Service> _serviceMap = new HashMap<>();
  /** Map that contains station names as keys and Station objects as values */
  private Map<String, Station> _stationMap = new HashMap<>();


  /**
   * Resets the app; destroys all Passengers' information.
   */
  void reset() {
    _passengerMap.clear();
    _nextPassengerId = 0;
  }

  /**
   * Creates new passenger and adds it to passenger map.
   *
   * @param name passenger name.
   */
  void registerPassenger(String name) throws NonUniquePassengerNameException {
    for (Passenger passenger : _passengerMap.values()) {
      if (passenger.getName().equals(name)) {
        throw new NonUniquePassengerNameException(name);
      }
    }
    _passengerMap.put(_nextPassengerId, new Passenger(_nextPassengerId, name));
    _nextPassengerId++;
  }

  /**
   * Changes a passenger's name to a given name.
   *
   * @param id passenger's id.
   * @param newName passenger's new given name.
   */
  void changePassengerName(int id, String newName) throws NoSuchPassengerIdException, NonUniquePassengerNameException {
    if (!_passengerMap.containsKey(id)){
      throw new NoSuchPassengerIdException(id);
    }

    for (Passenger passenger : _passengerMap.values()) {
      if (passenger.getName().equals(newName)){
        throw new NonUniquePassengerNameException(newName);
      }
    }
    getPassenger(id).setName(newName);
  }


  /**
   * Creates a String list with every Passenger in the app.
   *
   * @return String list with passenger's information (id, name, category and itinerary info).
   */
  List<String> showAllPassengers() {
    List<Passenger> passengersByIdList = new ArrayList<>(_passengerMap.values());
    List<String> passengersInfo = new ArrayList<>();

    Collections.sort(passengersByIdList);

    for (Passenger p : passengersByIdList) {
      passengersInfo.add(p.toString());
    }
    return passengersInfo;
  }


  /**
   * Finds a passenger's information by a given id.
   *
   * @param id passenger id.
   *
   * @return the passenger's information (id, name, category and itinerary info).
   */
  String showPassengerById(int id) throws NoSuchPassengerIdException {
    Passenger passenger = getPassenger(id);

    if (passenger == null){
      throw new NoSuchPassengerIdException(id);
    }
    return passenger.toString();
  }

  /**
   * Creates new service and adds it to service list.
   *
   * @param serviceId service id.
   * @param cost service price.
   *
   * @return the created service instance.
   */
  Service registerService(int serviceId, double cost) {
    Service service = new Service(serviceId, cost);
    _serviceMap.put(serviceId, service);
    return service;
  }

  /**
   * Creates a String list with every Service in the app.
   *
   * @return String list with services' information (id, price and departures).
   */
  List<String> showAllServices() {
    List<String> serviceInfo = new ArrayList<>();

    List<Service> servicesById = new ArrayList<>(_serviceMap.values());

    servicesById.sort(Service.getCompareById());

    for (Service service : servicesById) {
      serviceInfo.add(service.toString());
    }
    return serviceInfo;
  }

  /**
   * Finds a service's information by a given id.
   *
   * @param id passenger id.
   *
   * @return the service's information (id, price and departures).
   */
  String showServiceByNumber(int id) throws NoSuchServiceIdException {
    for (Service service : _serviceMap.values()) {
      if (service.getId() == id){
        return service.toString();
      }
    }
    throw new NoSuchServiceIdException(id);
  }

  /**
   * Finds all services with the same given arrival station and sorts them by initial departure time.
   *
   * @param name arrival station name.
   *
   * @return String list with services' information (id, price and departures).
   */
  List<String> showServicesArrivingAtStation(String name) throws NoSuchStationNameException {
    ShowServicesArrivingAtStation showArriving = new ShowServicesArrivingAtStation();

    return showArriving.getInfo(name, _stationMap, _serviceMap);
  }

  /**
   * Finds all services with the same given departing station and sorts them by initial departure time.
   *
   * @param name departing station name.
   *
   * @return String list with services' information (id, price and departures).
   */
  List<String> showServicesDepartingAtStation(String name) throws NoSuchStationNameException {
    ShowServicesByDepartingStation showDeparting = new ShowServicesByDepartingStation();

    return showDeparting.getInfo(name, _stationMap, _serviceMap);
  }

  /**
   * Creates new station and adds it to station map, except if it already exists.
   *
   * @param stationName station's name.
   *
   * @return the created/existing station instance.
   */
  Station registerStation(String stationName){
    Station station = _stationMap.get(stationName);

    if (station != null) {
      return station;
    }
    else {
      station = new Station(stationName);
      _stationMap.put(stationName, station);
      return station;
    }
  }

  /**
   * Gets the service with given id
   *
   * @param id service's id.
   *
   * @return the service instance.
   */

  Service getService(int id){
    return _serviceMap.get(id);
  }

/**
 * Gets the passenger with given id
 *
 * @param id passenger's id.
 *
 * @return the passenger instance.
 */

  Passenger getPassenger(int id){
    return _passengerMap.get(id);
  }



/**
 * Forms a list with all itineraries information from all passengers, sorted by passenger id
 * Does not show any text if passenger has no itineraries.
 *
 * @return list with all itineraries information
 */

  List<String> showAllItineraries() {

    int currNum =1;

    List<String> itineraryInfo = new ArrayList<>();

    List<Passenger> passengersByIdList = new ArrayList<>(_passengerMap.values());

    Collections.sort(passengersByIdList);

    for (Passenger passenger : passengersByIdList) {

      if(passenger.getItiList().isEmpty()){
        continue;

      }
      StringBuilder stringBuilder = new StringBuilder();

      List<Itinerary> itiList = passenger.getItiList();

      stringBuilder.append("== Passageiro ").append(passenger.getId()).append(": ").append(passenger.getName()).append(" ==\n");
      itineraryInfo.add(stringBuilder.toString());

      for(Itinerary itinerary : itiList){
        itineraryInfo.add(itinerary.toString(currNum));
        currNum++;
      }

    }
    return itineraryInfo;
  }


  /**
   * Forms a list with all itineraries information from a passenger with given id, sorted by itinerary date
   * Returns null if passenger has no itineraries.
   * Throws NoSuchPassengerIdException when no passenger exists with given id.
   *
   * @return list with all itineraries information from passenger with given id.
   */

  List<String> showPassengerItineraries(int id) throws NoSuchPassengerIdException{
    Passenger passenger = getPassenger(id);
    if (passenger == null){
      throw new NoSuchPassengerIdException(id);
    }

    int currNum = 1;

    List<Itinerary> itiList = passenger.getItiList();

    itiList.sort(Itinerary.getCompareByDate());
//
    List<String> itineraryInfo = new ArrayList<>();



    if(itiList.isEmpty()){
      return null;
    }

    String result = "";

    result += "== Passageiro " + passenger.getId() + ": " + passenger.getName() + " ==\n";

    itineraryInfo.add(result);

    for(Itinerary itinerary : itiList){
      itineraryInfo.add(itinerary.toString(currNum));
      currNum++;
    }

    return itineraryInfo;
  }


  /**
   * Searches all itineraries possible with given departing station, arriving station and minimum time to depart to a passenger with a given id.
   * Also recieves the date of the desired itinerary.
   * This forms a list with all itineraries possible, considering one itinerary per initial service.
   * If there is several itineraries found with the same initial service, the one with earlier departing time is saved, all others are discarded.
   *
   * The given itineraries found are returned to user.
   *
   * Throws exceptions when no there is no such passenger or stations
   *   or when date or time parsing fails.
   *
   *
   * @param id passenger's id
   * @param iniStationName Departing station's name
   * @param lastStationName Arriving station's name
   * @param dateName String with date, formated as <i> YYYY-MM-DD</i>
   * @param timeName String with minimum time, formated as <i> HH:MM</i>
   *

   * @return list with all itineraries found.
   */

  List<Itinerary> searchItineraries(int id, String iniStationName, String lastStationName, String dateName, String timeName)
                                                  throws NoSuchPassengerIdException, NoSuchStationNameException,
                                                         BadDateSpecificationException, BadTimeSpecificationException {
    LocalDate date;
    LocalTime time;

    Passenger passenger = getPassenger(id);

    if (passenger == null){
      throw new NoSuchPassengerIdException(id);
    }

    Station iniStation = _stationMap.get(iniStationName);

    if(iniStation == null ){
      throw new NoSuchStationNameException(iniStationName);
    }

    Station lastStation = _stationMap.get(lastStationName);

    if(lastStation == null ){
      throw new NoSuchStationNameException(lastStationName);
    }

    try{
      date = LocalDate.parse(dateName);
    }catch (DateTimeParseException e){
      throw new BadDateSpecificationException(dateName);
    }

    try{
        time = LocalTime.parse(timeName);
    }catch (DateTimeParseException e){
      throw new BadTimeSpecificationException(timeName);
    }

    Map<String, Station> stationMap = new HashMap<>();

    Map<Integer, Service> serviceMap = new HashMap<>();

    List<Segment> segList = new ArrayList<>();

    List<Itinerary> finalItiList = new ArrayList<>();

    for(Departure departure : iniStation.getDepList()){
      List<Itinerary> itiList = new ArrayList<>();

      departure.getService().determinarItinerario(iniStation, lastStation, date, time, stationMap, serviceMap, segList, itiList );

      if(itiList.isEmpty()){
        continue;
      }

      Itinerary selectedIti = itiList.get(0);
      for (Itinerary itinerary: itiList){
        if(itinerary.getLastDep().getTime().isBefore(selectedIti.getLastDep().getTime())){
          selectedIti = itinerary;
        }
      }
      finalItiList.add(selectedIti);
    }

    finalItiList.sort(Itinerary.getCompareByDepTime());

      return  finalItiList;
  }

  /**
   * Adds the given Itinerary to the passenger's itinerary list.
   *
   * @param itinerary Itinerary instance to add.
   * @param id passenger's id
   *
   *
   * @return list with all itineraries found.
   */

  void commitItinerary(Itinerary itinerary, int id ){
    Passenger passenger = getPassenger(id);
    passenger.addItinerary(itinerary);
  }

}
