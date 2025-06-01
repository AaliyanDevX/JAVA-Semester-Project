package flightReservation;
import java.util.*;
import java.io.*;

public class flightReservation{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int operation;
        String[][] details = {
            {"P01 ","islamabad ","karachi\n"},
            {"P02 ","islamabad ","lahore\n"},
            {"P03 ","islamabad ","gilgit\n"},
            {"P04 ","karachi ","Quetta\n"},
            {"P05 ","karachi ","Faisalabad\n"},
            {"P06 ","karachi ","Lahore\n"},
            {"P07 ","Lahore ","Multan\n"},
            {"P08","Lahore","Sialkot"},
            {"P09 ","Lahore ","Rawalpindi\n"},
            {"P10 ","Multan ","islamabad\n"},
            {"P11 ","Multan ","Peshawar\n"},
            {"P12 ","Multan ","Quetta\n"},
            {"P13 ","Faisalabad ","Lahore\n"},
            {"P14 ","Faisalabad ","Sialkot\n"},
            {"P15 ","Faisalabad ","karachi\n"},
            {"P16 ","Rawalpindi ","islamabad\n"},
            {"P17 ","Rawalpindi ","Gilgit\n"},
            {"P18 ","Rawalpindi ","islamabad\n"},
            {"P19 ","Gilgit ","Quetta\n"},
            {"P20 ","Gilgit ","Multan\n"},
            {"P21 ","Gilgit ","Lahore\n"},
            {"P22 ","Peshawar ","Faisalabad\n"},
            {"P23 ","Peshawar ","Gilgit\n"},
            {"P24 ","Peshawar ","Rawalpindi\n"},
            {"P25 ","Quetta ","karachi\n"},
            {"P26 ","Quetta ","Sialkot\n"},
            {"P27 ","Quetta ","islamabad\n"},
            {"P28 ","Sialkot ","Peshawar\n"},
            {"P29 ","Sialkot ","Multan\n"},
            {"P30 ","Sialkot ","karachi"},
        };
        try{
            FileWriter writer = new FileWriter("C:\\Users\\local user\\Documents\\JAVA-Semester-Project\\src\\flightReservation\\test.txt");
            for (int i = 0; i < 30; i++) {
                    writer.write(details[i][0].trim()+" "+details[i][1].trim()+" "+details[i][2].trim()+"\n");
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("IOException");
        }
        String continueChoice;
        do{
            System.out.println("Welcome to Ehsaan Airlines!\nSelect your operation:\n1. Search flights\n2. Book Flight\n3. View Booking\n4. Exit");
            operation = input.nextInt();
            input.nextLine();
            switch(operation){
                case 1: flightSearch("test.txt");
                break;
                case 2: bookFlight();
                break;
                case 3: bookingHistory();
                break;
                case 4: System.out.println("Thankyou for using Ehsaan Airlines!");
                System.exit(0);
                break;
                default: System.out.println("Select correct option");
                break;
            }
            System.out.println("\nDo you want another operation?(yes/no)?");
            continueChoice = input.nextLine().trim().toLowerCase();
        }while(continueChoice.equals("yes"));
    }
    public static void flightSearch(String fileName){
        try{
            Scanner input = new Scanner(System.in);
            boolean found = false;
            Scanner reader = new Scanner(new File("C:\\Users\\local user\\Documents\\JAVA-Semester-Project\\src\\flightReservation\\test.txt"));
            System.out.println("Enter the deparure city:");
            String depCity = input.nextLine().toLowerCase().trim();
            System.out.println("Enter the destination city:");
            String destCity = input.nextLine().toLowerCase().trim();
            while(reader.hasNextLine()){
                String currentLine = reader.nextLine();
                String[] parts = currentLine.split("\\s+");
                if(parts[1].toLowerCase().equals(depCity)&&parts[2].toLowerCase().equals(destCity)){
                    System.out.printf("Flight found!\nFlight ID: %s\nFrom: %s\nTo: %s",parts[0],parts[1],parts[2]);
                    found = true;
                }
            }
            if(!found){
                System.out.println("Flight not found");
            }
            reader.close();
        }
        catch(IOException e){
            System.out.println("Error fetching the file");
        }
    }
    public static void bookFlight(){
        System.out.println("ok");
    }
    public static void bookingHistory(){
        System.out.println("ok");
    }
    
}