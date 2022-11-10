package eliascregard.client.main;

import eliascregard.client.packets.AddConnectionPacket;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("localhost", 25565);
        client.connect();

        AddConnectionPacket packet = new AddConnectionPacket();
        client.sendObject(packet);
    }
}
