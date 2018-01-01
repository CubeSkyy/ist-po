package mmt.app.itineraries;

import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.app.exceptions.NoSuchPassengerException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;

import java.util.List;

/**
 * §3.4.2. Show all itineraries (for a specific passenger).
 */
public class DoShowPassengerItineraries extends Command<TicketOffice> {

  private Input<Integer> _passId;

  /**
   * @param receiver
   */
  public DoShowPassengerItineraries(TicketOffice receiver) {
    super(Label.SHOW_PASSENGER_ITINERARIES, receiver);
    _passId = _form.addIntegerInput(mmt.app.passenger.Message.requestPassengerId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    int id = _passId.value();

    try {
      List<String> itineraryInfo = _receiver.showPassengerItineraries(id);

      if (itineraryInfo == null){
        _display.add(mmt.app.itineraries.Message.noItineraries(id));

      }else{

        for (String s: itineraryInfo) {
          _display.addLine(s);
        }
      }

      _display.display();
    } catch(NoSuchPassengerIdException e) {
      throw new NoSuchPassengerException(e.getId());
    }
  }

}
