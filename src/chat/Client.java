package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import static chat.ChatConst.PORT;

public class Client implements Runnable{

    private final DatagramSocket ds;

    public Client(DatagramSocket ds) {
        this.ds = ds;
    }


    @Override
    public void run() {
        byte[] packetMassiv = new byte[8000];
        while (true) {
            DatagramPacket packet = new DatagramPacket(packetMassiv, 8000);
            try {
                ds.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData(), 0, packet.getLength());
            if (message.contains("roomPort:")) {
                String[] strArr = message.split(":");
                System.out.println(strArr[1]);
                ChatClient.port = Integer.decode(strArr[1]);
                System.out.println("Добро пожаловать в комнату.");
            } else {
                System.out.println(message);
            }
            if (message.equals("quit")) break;
        }
    }
}
