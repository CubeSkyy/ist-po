package mmt.core;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.ImportFileException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NonUniquePassengerNameException;

import java.util.List;

/**
 * Façade for handling persistence and other functions.
 */
public class TicketOffice {

  /** The object doing most of the actual work. */
  private TrainCompany _trainCompany;
  private String _saveFile = null;


  public TicketOffice() {
    _trainCompany = new TrainCompany();
  }

  public String getSaveFile() {
    return _saveFile;
  }

  public void setSaveFile(String savefile){
    _saveFile = savefile;
  }

  public void reset() {
    _trainCompany.reset();
  }

  public void save(String filename) throws IOException {
    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
    out.writeObject(_trainCompany);
    out.close();
  }

  public void load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {

    ObjectInputStream inob = new ObjectInputStream(new FileInputStream(filename));
    _trainCompany = (TrainCompany) inob.readObject();
    inob.close();
  }

  public void importFile(String datafile) throws ImportFileException {
    NewParser parser = new NewParser();
    _trainCompany = parser.parseFile(datafile);
  }


  public void registerPassenger(String name) throws NonUniquePassengerNameException {
    _trainCompany.registerPassenger(name);
  }

  public void changePassengerName(int id, String name) throws NoSuchPassengerIdException, NonUniquePassengerNameException {
    _trainCompany.changePassengerName(id, name);
  }

  public List<String> showAllPassengers() {
    return _trainCompany.showAllPassengers();
  }

  public String showPassengerById(int id) throws NoSuchPassengerIdException {
    return _trainCompany.showPassengerById(id);
  }

  public List<String> showAllServices() {
    return _trainCompany.showAllServices();
  }

  public String showServiceByNumber(int id) throws NoSuchServiceIdException {
    return _trainCompany.showServiceByNumber(id);
  }

  public List<String> showServicesArrivingAtStation(String name) throws NoSuchStationNameException {
    return _trainCompany.showServicesArrivingAtStation(name);
  }

  public List<String> showServicesDepartingAtStation(String name) throws NoSuchStationNameException {
    return _trainCompany.showServicesDepartingAtStation(name);
  }

  public List<String> showAllItineraries() {
    return _trainCompany.showAllItineraries();
  }

  public  List<String> showPassengerItineraries(int id) throws NoSuchPassengerIdException{
    return _trainCompany.showPassengerItineraries(id);
   }

  public List<Itinerary> searchItineraries(int id, String iniStationName, String lastStationName, String dateName, String timeName)
          throws NoSuchPassengerIdException, NoSuchStationNameException,
          BadDateSpecificationException, BadTimeSpecificationException {

   return _trainCompany.searchItineraries(id, iniStationName, lastStationName, dateName, timeName);
  }


  public void commitItinerary(Itinerary itinerary, int id){
    _trainCompany.commitItinerary(itinerary, id);
  }

  }
