package command;

import dao.TransportDAO;
import model.Transport;

public class TransportUpdateCommand implements Command {
    private Transport oldTransport;
    private Transport newTransport;

    public TransportUpdateCommand(Transport transport) {
        this.newTransport = transport;
    }

    @Override
    public void execute() {
        TransportDAO dao = new TransportDAO();
        this.oldTransport = dao.find(newTransport.get_id());
        dao.update(newTransport.get_id(), newTransport);
    }

    @Override
    public void undo() {
        TransportDAO dao = new TransportDAO();
        dao.update(oldTransport.get_id(), oldTransport);
    }

    @Override
    public void redo() {
        TransportDAO dao = new TransportDAO();
        dao.update(newTransport.get_id(), newTransport);
    }
}
