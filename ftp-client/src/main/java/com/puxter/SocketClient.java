package com.puxter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public final static int bufferSize = 8192;

    public static void main(String[] args) {
        FileOutputStream fileOutputStream;
        Socket socket;
        try {
            System.out.println("Enter server ip address.");
            String ip = readFromInput();
            socket = new Socket(ip, 6666);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String fileNames = in.readUTF();
            System.out.println("List of files in this directory:\n");
            for (String temp : fileNames.split(" ")) {
                System.out.println(temp);
            }
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Enter file's name.");
            String fileName = readFromInput();
            dataOutputStream.writeUTF(fileName);
            dataOutputStream.flush();

            byte[] buffer = new byte[bufferSize];
            InputStream inputStream = socket.getInputStream();
            fileOutputStream = new FileOutputStream(fileName);
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.close();
            socket.close();
            dataOutputStream.close();
        } catch (Exception e) {
            System.out.println("ServerSocket is closed.");
        }
    }

    private static String readFromInput() {
        return new Scanner(System.in).nextLine();
    }
}