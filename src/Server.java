import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class EchoThread extends Thread {
    private Socket socket;

    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
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
                } catch (IOException e) { System.out.println(e); }
            }

            System.out.print("Closing Connection");
            socket.close();
        } catch (IOException e) { System.out.println(e); }
    }
}

public class Server {
    private static final int PORT = 5000;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        //opens the server
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...\n");
        } catch (IOException e) { System.out.println(e); }

        //while loop to accept multiple clients
        while(true) {
            try {
                socket = serverSocket.accept();
                System.out.println("Client accepted!\n");
            } catch (IOException e) { System.out.println(e); }

            //starts the server thread
            new EchoThread(socket).start();
        }
    }
}