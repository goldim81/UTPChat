package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Scanner;

import static chat.ChatConst.PORT;

public class ChatClient {
    public static volatile int port = PORT;

    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
//        ds.setSoTimeout(10000);
        new Thread(new Client(ds)).start();
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        //Отсылаем пустой пакет чтобы уведомить сервер о своем присутсвии
        ds.send(new DatagramPacket(new byte[] {}, 0, ip, PORT));

        //Запрашивем список комнат
//        byte[] packetMassiv = new byte[1024];
//        DatagramPacket pIn = new DatagramPacket(packetMassiv, 1024);
//        ds.receive(pIn);
//        System.out.println(new String(pIn.getData(), 0, pIn.getLength()));

        //Отсылаем выбранный номер комнаты
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String room = reader.readLine();
        byte [] sendData = new byte[1024];
        sendData = room.getBytes();
        ds.send(new DatagramPacket(sendData, sendData.length, ip, PORT));

        while (true) {
            String message = reader.readLine();
            sendData = message.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, ip, port);
            ds.send(packet);
            if (message.equals("quit")) break;
        }
        ds.close();

    }
}
