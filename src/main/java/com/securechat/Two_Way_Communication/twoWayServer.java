package com.securechat.Two_Way_Communication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.SecretKey;

import com.securechat.JAVA_DES.*;
public class twoWayServer {
    private static final String AUTH_PASS = System.getenv("AUTH_PASS");
    public static void main(String[] args) throws Exception{
        DES a = new DES();
        SecretKey secretKey = a.generateKey();
        int port = 8000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server Started at port " + port + ", Waiting for the Client...");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected Successfully");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        pw.println("AUTH_REQUIRED");
        String clientAuth = br.readLine();
        if(!clientAuth.equals(AUTH_PASS)){
            pw.println("AUTH_FAILED");
            System.out.println("Client Authentication Failed, Connection closed");
            serverSocket.close();
            socket.close();
            return;
        }
        else{
            pw.println("AUTH_SUCCESS");
            System.out.println("Client Authenticated Successfully");
        }
        String encodedKey = a.keyToKeyString(secretKey);
        pw.println(encodedKey);
        System.out.println("Secret Key sent to the client Successfully");
        String serverMsg, clientMsg;
        while(true){
            clientMsg = br.readLine();
            clientMsg = a.decrypt(clientMsg, secretKey);
            if(clientMsg.equalsIgnoreCase("exit")){
                System.out.println("Client Disconnected from the Server");
                break;
            }
            System.out.println("Client Message: " + clientMsg);
            System.out.print("Server(Type tour Message): ");
            serverMsg = br1.readLine();
            if(serverMsg.equalsIgnoreCase("exit")){
                pw.println(a.encrypt("exit", secretKey));
                System.out.println("Server Disconnected");
                break;
            }
            else pw.println(a.encrypt(serverMsg, secretKey));
        }
        br.close();
        br1.close();
        pw.close();
        socket.close();
        serverSocket.close();
    }
}
