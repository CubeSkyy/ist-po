package mmt.app.service;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Display;

import java.util.List;

/**
 * 3.2.1 Show all services.
 */
public class DoShowAllServices extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllServices(TicketOffice receiver) {
    super(Label.SHOW_ALL_SERVICES, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    List<String> serviceInfo = _receiver.showAllServices();
    for(String s: serviceInfo){
      _display.addLine(s);
    }
    
    _display.display();
  }

}
