package com.securechat.Two_Way_Communication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.crypto.SecretKey;

import com.securechat.JAVA_DES.*;
public class twoWayClient {
    private static final String AUTH_PASS = System.getenv("AUTH_PASS");
    public static void main(String[] args) throws Exception{
        DES a = new DES();
        Socket socket = new Socket("host.docker.internal", 8000);
        System.out.println("Client Connected to the Server Successfully");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String serverAuth = br.readLine();
        if(serverAuth.equals("AUTH_REQUIRED")){
            pw.println(AUTH_PASS);
            String authResponse = br.readLine();
            if(authResponse.equals("AUTH_FAILED")){
                System.out.println("Authentication failed, Closing the Connection");
                socket.close();
                return;
            }
            else System.out.println("Authenticated successfully with the Server");
        }
        String keyString = br.readLine();
        SecretKey secKey = a.keyStringToKey(keyString);
        System.out.println("Secret Key received from the Server Successfully");
        String serverMsg, clientMsg;
        while(true){
            System.out.print("Client(Type your Message): ");
            clientMsg = br1.readLine();
            if(clientMsg.equalsIgnoreCase("exit")){
                pw.println(a.encrypt("exit", secKey));
                System.out.println("Connection clsoed by the Client");
                socket.close();
                break;
            }
            else pw.println(a.encrypt(clientMsg, secKey));
            serverMsg = br.readLine();
            serverMsg = a.decrypt(serverMsg, secKey);
            if(serverMsg.equalsIgnoreCase("exit")){
                System.out.println("Server Disconnected from the Client");
                break;
            }
            else System.out.println("Server Message: " + serverMsg);
        }
        br.close();
        br1.close();
        pw.close();
        socket.close();
    }
}
