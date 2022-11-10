package eliascregard.server.main;

import eliascregard.server.packets.AddConnectionPacket;
import eliascregard.server.packets.RemoveConnectionPacket;

public class EventListener {

    public void received(Object p,Connection connection) {
        if (p instanceof AddConnectionPacket packet) {
            packet.id = connection.id;
            for(int i=0; i<ConnectionHandler.connections.size(); i++) {
                Connection c = ConnectionHandler.connections.get(i);
                if(c != connection) {
                    c.sendObject(packet);
                }
            }

        }
        else if (p instanceof RemoveConnectionPacket packet) {
            System.out.println("Connection: " + packet.id + " has disconnected");
            ConnectionHandler.connections.get(packet.id).close();
            ConnectionHandler.connections.remove(packet.id);
        }
    }

}