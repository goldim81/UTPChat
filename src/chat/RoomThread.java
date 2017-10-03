package chat;

import javax.xml.crypto.Data;
import javax.xml.transform.Source;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class RoomThread implements Runnable {

    private final String name;
    private final DatagramSocket ds;
//        private byte[] packetMassiv = new byte[1024];
    private DatagramPacket pIn = new DatagramPacket(new byte[1024], 1024);
//    private DatagramPacket pOut;
    Set<ClientAddres> clientSet = new HashSet<>();

    public RoomThread(String name) throws SocketException {
        this.name = name;
        ds = new DatagramSocket();
    }

    public void addClient(ClientAddres clientAddres) {
        clientSet.add(clientAddres);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMessage(clientAddres, ("roomPort:" + ds.getLocalPort()).getBytes());
    }

    private void sendMessage(ClientAddres clientAddres, byte[] byteMessage) {
        try {
            ds.send(new DatagramPacket(byteMessage, byteMessage.length,
                    clientAddres.getAddres(), clientAddres.getPort()));
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения.");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ds.receive(pIn);
            } catch (IOException e) {
                System.out.println("Ошибка получения сообщения.");
                e.printStackTrace();
            }

            sendOtherClient(pIn);
            String message = new String(pIn.getData(), 0, pIn.getLength());

            System.out.println(name + ": " + message);
            if (message.equals("quit")) break;
        }
    }

    private void sendOtherClient(DatagramPacket msgPacket) {
        for (ClientAddres client : clientSet) {
            if (!(client.isSender(msgPacket.getAddress(), msgPacket.getPort()))) {
                sendMessage(client, msgPacket.getData());
            }
        }
    }
}
