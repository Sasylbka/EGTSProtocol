package com.company;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;

class Vehicle{

    int omniNumber;
    double azimuth;
    double longitude; //долгота по модулю, градусы/180 · 0xFFFFFFFF и взята целая часть;
    double latitude;  //широта по модулю, градусы/90 · 0xFFFFFFFF и взята целая часть;
    double speed;
    Vehicle(int omniNumber, double azimuth,double longitude, double latitude, Double speed){
        this.longitude=longitude;
        this.latitude=latitude;
        this.speed=speed;
        this.azimuth=azimuth;
        this.omniNumber=omniNumber;
    }
}



public class Main {
    static public void ConvertEGTS(Vehicle vehicle,Socket socket) throws ParseException, IOException, InterruptedException {
        try(
            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream());)
        {
            System.out.println("Client connected to socket.");
            while(!socket.isOutputShutdown()){
                if(br.ready()){
                    System.out.println("Client start writing in channel...");
                    Thread.sleep(1000);
                }
                int newLongitude = (short)vehicle.longitude/90 ;
                int newLatitude = (short)vehicle.latitude/90;
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                long d1 = format.parse("2010/01/01 00:00:00").getTime();
                long d2 = System.currentTimeMillis()-d1;
                long navigationTime=d2/1000 % 60;
                oos.write(newLongitude);
                oos.write(newLatitude);
                oos.writeLong(navigationTime);
                socket.shutdownOutput();
                System.out.println("Data sent");
            }
        }
    }
    public static void main(String[] args) throws ParseException, IOException, InterruptedException {
        Vehicle vehicle = new Vehicle(36590,0.0,59.88485,30.341246599999998,0.0);
        Socket socket = new Socket("46.243.177.79", 8507);
        ConvertEGTS(vehicle,socket);
    }
    }
