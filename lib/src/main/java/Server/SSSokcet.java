package Server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mr.cheng on 2016/10/23.
 */

public class SSSokcet {
    public static void main(String[] args) {
        try {
            new SSSokcet().method();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void method() throws IOException {
        ServerSocket server = new ServerSocket(8855);

        while (true) {
            Socket socket = server.accept();
            System.out.println(socket.hashCode());
            invoke(socket);
        }
    }
    private static void invoke(final Socket socket) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                ObjectInputStream is = null;
                ObjectOutputStream os = null;
                try {
                    is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    os = new ObjectOutputStream(socket.getOutputStream());
                    Object obj = is.readObject();
                    User user = (User)obj;
                    System.out.println("user: " + user.getUsername() + "/" + user.getPassword());
                    user.setUsername(user.getUsername() + "_new");
                    user.setPassword(user.getPassword() + "_new");
                    float[] floats=new float[8192];
                    for (int i = 0; i < 8192; i++) {
                        floats[i]=i+3.0f;
                    }
                    user.setMdatas(floats);
                    os.writeObject(user);
                    os.flush();
                } catch (IOException ex) {

                } catch(ClassNotFoundException ex) {

                } finally {
                    try {
                        is.close();
                    } catch(Exception ex) {}
                    try {
                        os.close();
                    } catch(Exception ex) {}
                    try {
                        socket.close();
                    } catch(Exception ex) {}
                }
            }
        }).start();
    }
}
