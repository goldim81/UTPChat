package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import static chat.ChatConst.PORT;

public class ChatServer {
    private static DatagramSocket ds;

    static {
        try {
            ds = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //TODO делаем массив комнат, появляется возможность добавлять комнаты
        RoomThread room1 = new RoomThread("Room 1");
        RoomThread room2 = new RoomThread("Room 2");

        new Thread(room1).start();
        new Thread(room2).start();


        byte[] packetMassiv = new byte[8000];
        Set<ClientAddres> clientSet = new HashSet<>();
        String roomList = "1. Комната 1\n2. Комната 2\n3. Комната 3\n";
        DatagramPacket pIn = null;
        DatagramPacket pOut = null;

        while (true) {
            pIn = new DatagramPacket(new byte[8000], 8000);
            ds.receive(pIn);

            ClientAddres clientAddres = new ClientAddres(pIn.getAddress(), pIn.getPort());
            //Если клиент новый добавляем его в общий список и переносим в комнату
            if (clientSet.add(clientAddres)) {
                //Если успешно добавлен значит новый
                //Отсылаем список комнат
                packetMassiv = roomList.getBytes();
                ds.send(new DatagramPacket(packetMassiv, packetMassiv.length,
                                            clientAddres.getAddres(), clientAddres.getPort()));

            } else {

                //Переносим в выбранную комнату
                String roomNum = new String(pIn.getData(), 0, pIn.getLength());

                switch (roomNum) {
                    case "1" : room1.addClient(clientAddres);break;
                    case "2" : room2.addClient(clientAddres);break;
                    case "3" : break;
                }
                //Удаляем со своего списка
                clientSet.remove(clientAddres);
            }


            for (ClientAddres client : clientSet){
                if (!(client.isSender(pIn.getAddress(), pIn.getPort()))) {
                    DatagramPacket reply = new DatagramPacket(pIn.getData(), pIn.getLength(),
                            client.getAddres(), client.getPort());
                    ds.send(reply);
                }
            }
            String message = new String(pIn.getData(), 0, pIn.getLength());
            if (message.equals("quit")) break;
            System.out.println(pIn.getAddress() + " " + pIn.getPort() + ": " + message);

        }
        ds.close();
    }

    private static void sendMessage(ClientAddres clientAddres, byte[] byteMessage) {
        try {
            ds.send(new DatagramPacket(byteMessage, byteMessage.length,
                    clientAddres.getAddres(), clientAddres.getPort()));
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения.");
            e.printStackTrace();
        }
    }
}
