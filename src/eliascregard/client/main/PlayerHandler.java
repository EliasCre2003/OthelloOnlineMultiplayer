package eliascregard.client.main;

import eliascregard.client.packets.AddConnectionPacket;
import eliascregard.client.packets.RemoveConnectionPacket;

import java.util.HashMap;

public class PlayerHandler {

    public static HashMap<Integer, NetPlayer> players = new HashMap<Integer, NetPlayer>();

    public static class EventListener {

        public void received(Object p) {
            if (p instanceof AddConnectionPacket packet) {
                players.put(packet.id, new NetPlayer(packet.name, packet.id));
                System.out.println("Player: " + packet.name + " with id: " + packet.id + " has joined the game.");
            }
            else if (p instanceof RemoveConnectionPacket packet) {
                players.remove(packet.id);
                System.out.println("Player with id: " + packet.id + " has left the game.");
            }
        }
    }
}