package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import static chat.ChatConst.PORT;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        new Thread(new Client(ds)).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        byte [] sendData = new byte[1024];
        while (true) {
            String message = reader.readLine();
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            sendData = message.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, ip, PORT);
            ds.send(packet);
            if (message.equals("quit")) break;
        }
        ds.close();

    }
}
