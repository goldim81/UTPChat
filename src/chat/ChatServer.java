package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static chat.ChatConst.PORT;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket(PORT);
        byte[] packetMassiv = new byte[8000];
        List<ClientAddres> clientList = new ArrayList<>();

        while (true) {
            DatagramPacket packet = new DatagramPacket(packetMassiv, 8000);
            ds.receive(packet);
            ClientAddres clientAddres = new ClientAddres(packet.getAddress(), packet.getPort());
            if (!clientList.contains(clientAddres)) {
                clientList.add(clientAddres);
            }
            for (ClientAddres client : clientList){
                if (!(client.getAddres().equals(packet.getAddress()) & client.getPort()==packet.getPort())) {
                    DatagramPacket reply = new DatagramPacket(packet.getData(), packet.getLength(),
                            client.getAddres(), client.getPort());
                    ds.send(reply);
                }
            }
            String message = new String(packet.getData(), 0, packet.getLength());
            if (message.equals("quit")) break;
            System.out.println(packet.getAddress() + " " + packet.getPort() + ": " + message);

        }
        ds.close();
    }
}
