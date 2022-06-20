package mmt.app.passenger;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;

import pt.tecnico.po.ui.Form;

import java.util.List;

/**
 * §3.3.1. Show all passengers.
 */
public class DoShowAllPassengers extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllPassengers(TicketOffice receiver) {
    super(Label.SHOW_ALL_PASSENGERS, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    List<String> passInfo = _receiver.showAllPassengers();
    for (String s: passInfo) {
      _display.addLine(s);
    }
    
    _display.display();
  }
}