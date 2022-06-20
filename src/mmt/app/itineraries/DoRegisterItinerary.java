package mmt.app.itineraries;

import mmt.core.Itinerary;
import mmt.core.TicketOffice;
import mmt.app.exceptions.BadDateException;
import mmt.app.exceptions.BadTimeException;
import mmt.app.exceptions.NoSuchItineraryException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.app.exceptions.NoSuchStationException;
import mmt.core.exceptions.BadDateSpecificationException;
import mmt.core.exceptions.BadTimeSpecificationException;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NoSuchStationNameException;
import mmt.core.exceptions.NoSuchItineraryChoiceException;
import pt.tecnico.po.ui.*;

import java.util.ArrayList;
import java.util.List;

//FIXME import other classes if necessary

/**
 * §3.4.3. Add new itinerary.
 */
public class DoRegisterItinerary extends Command<TicketOffice> {

  private Input<Integer> _passId;
  private Input<String> _iniStation;
  private Input<String> _lastStation;
  private Input<String> _date;
  private Input<String> _time;
  private Input<Integer> _choice;

  private Form _askForm;


  private int currNum = 1;

  /**
   * @param receiver
   */
  public DoRegisterItinerary(TicketOffice receiver) {
    super(Label.REGISTER_ITINERARY, receiver);
    _passId = _form.addIntegerInput(mmt.app.passenger.Message.requestPassengerId());
    _iniStation = _form.addStringInput(mmt.app.itineraries.Message.requestDepartureStationName());
    _lastStation = _form.addStringInput(mmt.app.itineraries.Message.requestArrivalStationName());
    _date = _form.addStringInput(mmt.app.itineraries.Message.requestDepartureDate());
    _time = _form.addStringInput(mmt.app.itineraries.Message.requestDepartureTime());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    List<Itinerary> finalItiList;
    try {
      finalItiList = _receiver.searchItineraries(_passId.value(),_iniStation.value(),_lastStation.value(),_date.value(),_time.value());

      if(finalItiList.isEmpty()){
        return;
      }

      _display.add("\n");
      for(Itinerary i : finalItiList){
        _display.addLine(i.toString(currNum));
        currNum++;
      }

      _display.display();

      _askForm =  new Form();
      _choice = _askForm.addIntegerInput(mmt.app.itineraries.Message.requestItineraryChoice());
      _askForm.parse();

      if(_choice.value() > 0 && _choice.value() <= finalItiList.size() ){
        _receiver.commitItinerary(finalItiList.get(_choice.value()-1), _passId.value());
      }else if(_choice.value() == 0 ) {
        currNum = 1;
        return;
      }else{
        currNum = 1;
        throw new NoSuchItineraryException(_passId.value(),_choice.value());
      }

    } catch (NoSuchPassengerIdException e) {
      throw new NoSuchPassengerException(e.getId());
    } catch (NoSuchStationNameException e) {
      throw new NoSuchStationException(e.getName());
    } catch (BadDateSpecificationException e) {
      throw new BadDateException(e.getDate());
    } catch (BadTimeSpecificationException e) {
      throw new BadTimeException(e.getTime());
    }

  }
}
