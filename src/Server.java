/*
    Original Author : Ooi Junjie Dylan 0120185
    Finished Date   : 30/9/19

    DISCLAIMER      :   This assignment is done through MY own self study and referencing from online sources.

    Thank you :)
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class EchoThread extends Thread {
    private static List<Socket> socketList = new CopyOnWriteArrayList<>();
    private Socket socket;

    //constructor
    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
        socketList.add(socket); //adds clients to socketList
    }

    @Override
    public void run() {
        DataInputStream inp = null;

        try {
            inp = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            //print whatever client is saying as long as it is not "Over"
            String line = "";
            while (!line.equals("Over")) {
                try {
                    line = inp.readUTF();
                    System.out.println(line);

                    forwardMessageToClients(line); //forwards message to other clients
                } catch (IOException e) { System.out.println(e); break;}
            }

            //closes connection when client terminates the connection
            System.out.print("Closing Connection");
            socket.close();
        } catch (IOException e) { System.out.println(e); }
    }

    //forward message to clients method
    private void forwardMessageToClients(String line) throws IOException {
        for (Socket other : socketList) {
            if (other == socket) { continue; }
            DataOutputStream output = new DataOutputStream(other.getOutputStream());
            output.writeUTF(line);
        }
    }
}

public class Server {
    private static final int PORT = 5000;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        //starts the server
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...\n");
        } catch (IOException e) { System.out.println(e); }

        //while loop to accept multiple clients
        int count = 1;
        while(true) {
            try {
                socket = serverSocket.accept();
                System.out.println("Client " + count + " accepted!");
                count++;
            } catch (IOException e) { System.out.println(e); }

            //starts the server thread
            new EchoThread(socket).start();
        }
    }
}