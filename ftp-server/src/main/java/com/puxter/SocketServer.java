package com.puxter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Arrays;

public class SocketServer {
    public final static int bufferSize = 8192;

    public static void main(String[] args) {
        FileInputStream fileInputStream;
        OutputStream outputStream;
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(6666);
            socket = serverSocket.accept();//establishes connection
            String path = Paths.get("").toAbsolutePath().toString();
            File f = new File(path);
            String[] filesInDir = f.list();
            String fileNames = Arrays.toString(filesInDir);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(fileNames);
            dataOutputStream.flush();


            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String fileNameToDownload = dis.readUTF();
            byte[] buffer = new byte[bufferSize];
            fileInputStream = new FileInputStream(new File(path + File.separator + fileNameToDownload));
            outputStream = socket.getOutputStream();
            outputStream.flush();
            int count;
            while ((count = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Cannot connect with client. No response from client.");
        }
    }
}