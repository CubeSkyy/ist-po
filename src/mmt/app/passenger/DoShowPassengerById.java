package mmt.app.passenger;

import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.app.exceptions.NoSuchPassengerException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Display;

/**
 * §3.3.2. Show specific passenger.
 */
public class DoShowPassengerById extends Command<TicketOffice> {

  private Input<Integer> _passId;

  /**
   * @param receiver
   */
  public DoShowPassengerById(TicketOffice receiver) {
    super(Label.SHOW_PASSENGER_BY_ID, receiver);
    _passId = _form.addIntegerInput(Message.requestPassengerId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    try {
      _display.add(_receiver.showPassengerById(_passId.value()));
    } catch(NoSuchPassengerIdException e) {
      throw new NoSuchPassengerException(e.getId());
    }

    _display.display();
  }
}
