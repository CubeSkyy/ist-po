package mmt.app.main;

import java.io.IOException;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

/**
 * §3.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<TicketOffice> {

  private Input<String> _fileName;

  /**
   * @param receiver
   */
  public DoSave(TicketOffice receiver) {
    super(Label.SAVE, receiver);
    _fileName = _form.addStringInput(Message.newSaveAs());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    try {
      if (_receiver.getSaveFile() == null) {
        _form.parse();
        _receiver.setSaveFile(_fileName.value());
      }
      _receiver.save(_receiver.getSaveFile());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
