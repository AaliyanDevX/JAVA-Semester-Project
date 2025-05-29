package flightReservation;
import java.util.*;
import java.io.*;
public class flightReservation{
    public static void main(String[] args){
        try{
            Scanner input = new Scanner(System.in);
            File file = new File("details.txt");
            file.createNewFile();
            while(true){
                System.out.println("Wlecome to Ehsaan Airlines! Select your Operation:\n1. Flight Search\n2. Flight Booking\n3. View Booking\n4. Exit");
                int n = input.nextInt();
                switch(n) {
                    case 1: flightSearch();
                    break;
                    case 2: bookFlight();
                    break;
                    case 3: viewBooking();
                    break;
                    case 4: System.exit(0);
                    break;
                    default: System.out.println("Wrong input");
                    break; 
                }
            }
        }
        catch(IOException e){
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }
    public static void flightSearch(){
        System.out.println("flightSearch is working");
    }
    public static void bookFlight(){
        System.out.println("bookFlight is working");
    }
    public static void viewBooking(){
        System.out.println("viewBooking is working");
    }
}