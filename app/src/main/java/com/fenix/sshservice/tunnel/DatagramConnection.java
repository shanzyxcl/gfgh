package com.fenix.sshservice.tunnel;

import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.SkStatus;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DatagramConnection {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private boolean isConnected;
    private Settings mConfig;

    public DatagramConnection(String serverIP, int serverPort) {
        try {

            String udp_server = mConfig.getPrivString(Settings.SERVIDOR_KEY);
            this.serverAddress = InetAddress.getByName(udp_server);
            this.serverPort = 22;
            // Leer el valor del servidor desde mConfig y asignarlo a udp_server

            this.socket = new DatagramSocket();
            this.isConnected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startConnection() {

        // Inicia un hilo para enviar datagramas a intervalos regulares
        Thread sendThread = new Thread(() -> {
            try {
                while (isConnected) {
                    byte[] sendData = "Ping".getBytes(); // Contenido del datagrama
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                    socket.send(sendPacket);
                    System.out.println("Datagrama enviado: " + new String(sendData));

                    String packetData = new String(sendPacket.getData(), sendPacket.getOffset(), sendPacket.getLength());
                    SkStatus.logInfo("Datagrama enviado" + packetData);
                    Thread.sleep(1000); // Intervalo de envío de datagramas (5 segundos)
                }
            } catch (Exception e) {
                e.printStackTrace();
                SkStatus.logInfo("ERROR Datagrama enviado" + e);
            }
        });
        sendThread.start();

        // Hilo para recibir datagramas de respuesta (opcional)
        Thread receiveThread = new Thread(() -> {
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                while (isConnected) {
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Respuesta recibida: " + response);
                    SkStatus.logInfo("Respuesta recibida: " + response);

                }
            } catch (Exception e) {
                e.printStackTrace();
                SkStatus.logInfo("ERROR Datagrama enviado" + e);
            }
        });
        receiveThread.start();
    }

    public void stopConnection() {
        isConnected = false;
        socket.close();
    }
}