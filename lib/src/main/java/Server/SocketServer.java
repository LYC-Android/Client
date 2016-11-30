package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mr.cheng on 2016/10/17.
 */

public class SocketServer {


    public static void main(String[] args) {
        new SocketServer().startServer();
    }

    public void startServer() {
        Socket ClientSocket=null;
        ServerSocket serverSocket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            serverSocket = new ServerSocket(9898);
            System.out.println("服务器启动了");
            ClientSocket = serverSocket.accept();
            System.out.println("有人连进来了" + ClientSocket.hashCode());
            writer = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            String receiveMsg;
            while ((receiveMsg = reader.readLine()) != null) {
                writer.write("I have Receive"+"\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                ClientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
