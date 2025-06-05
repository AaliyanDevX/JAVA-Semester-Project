package flightReservation;
import java.util.*;
import java.io.*;

public class flightReservation{
    public static void main(String[] args){
        //Declaring stuff
        Scanner input = new Scanner(System.in);
        int operation;
        //This String array list will track the changes
        ArrayList<String> list = new ArrayList<>();
        //This array contains the initial data
        String[][] details = 
            {
            {"P01 ","islamabad ","karachi ","65"},
            {"P02 ","islamabad ","lahore ","65"},
            {"P03 ","islamabad ","gilgit ","65"},
            {"P04 ","karachi ","Quetta ","65"},
            {"P05 ","karachi ","Faisalabad ","65"},
            {"P06 ","karachi ","Lahore ","65"},
            {"P07 ","Lahore ","Multan ","65"},
            {"P08","Lahore","Sialkot ","65"},
            {"P09 ","Lahore ","Rawalpindi ","65"},
            {"P10 ","Multan ","islamabad ","65"},
            {"P11 ","Multan ","Peshawar ","65"},
            {"P12 ","Multan ","Quetta","65"},
            {"P13 ","Faisalabad ","Lahore","65"},
            {"P14 ","Faisalabad ","Sialkot","65"},
            {"P15 ","Faisalabad ","karachi","65"},
            {"P16 ","Rawalpindi ","islamabad","65"},
            {"P17 ","Rawalpindi ","Gilgit","65"},
            {"P18 ","Rawalpindi ","islamabad","65"},
            {"P19 ","Gilgit ","Quetta","65"},
            {"P20 ","Gilgit ","Multan","65"},
            {"P21 ","Gilgit ","Lahore","65"},
            {"P22 ","Peshawar ","Faisalabad","65"},
            {"P23 ","Peshawar ","Gilgit","65"},
            {"P24 ","Peshawar ","Rawalpindi","65"},
            {"P25 ","Quetta ","karachi ","65"},
            {"P26 ","Quetta ","Sialkot ","65"},
            {"P27 ","Quetta ","islamabad ","65"},
            {"P28 ","Sialkot ","Peshawar ","65"},
            {"P29 ","Sialkot ","Multan ","65"},
            {"P30 ","Sialkot ","karachi ","65"}
        };
        //get the length of array which contains all data
        int rows = details.length;
        int cols = details[0].length;
        File file = new File("C:\\Users\\local user\\Documents\\JAVA-Semester-Project\\src\\flightReservation\\test.txt");
        //New File is created only when it doesn't exist before
        //Writing the data into file for storage
        if(!file.exists()){
            try{
                FileWriter writer = new FileWriter(file);
                for (int i = 0; i < 30; i++) {
                        writer.write(details[i][0].trim()+" "+details[i][1].trim()+" "+details[i][2].trim()+" "+details[i][3].trim()+"\n");
                }
                writer.close();
            }
            catch(IOException e){
                System.out.println("IOException");
            }
        }
        //Bringing back the data into a new array to handle it in program
        //Writing in main method so that it could be accessed from every method
        try{
            Scanner reader = new Scanner(file);
            String[][] data = new String[rows][cols];
            int counter = 0;
            while(reader.hasNextLine()){
                String currentLine = reader.nextLine();
                String[] parts = currentLine.split("\\s+");
                for (int i = 0; i < 4; i++) {
                    data[counter][i] = parts[i];
                }
                counter++;
            }
            reader.close();
        
            String continueChoice;
            try{
                do{
                    System.out.println("Welcome to Ehsaan Airlines!\nSelect your operation:\n1. Search flights\n2. Book Flight\n3. View Booking\n4. Exit");
                        operation = input.nextInt();
                        input.nextLine();
                        switch(operation){
                            case 1: flightSearch();
                            break;
                            case 2: bookFlight(rows,cols,data,list);
                            break;
                            case 3: bookingHistory(data,list);
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
            catch(InputMismatchException e){
                System.out.println("Invalid Input");
            }
        }
        catch(IOException e){
            System.out.println("IO Exception in main method");
        }
    }
    //Flight Search Method
    public static void flightSearch(){
        try{
            Scanner input = new Scanner(System.in);
            File file = new File("C:\\Users\\local user\\Documents\\JAVA-Semester-Project\\src\\flightReservation\\test.txt");
            Scanner reader = new Scanner(file);

            boolean found = false;
            try{
                System.out.println("Enter the deparure city:");
                String depCity = input.nextLine().toLowerCase().trim();
                System.out.println("Enter the destination city:");
                String destCity = input.nextLine().toLowerCase().trim();
                while(reader.hasNextLine()){
                    String currentLine = reader.nextLine();
                    String[] parts = currentLine.split("\\s+");
                    if(parts[1].toLowerCase().equals(depCity)&&parts[2].toLowerCase().equals(destCity)){
                        System.out.printf("Flight found!\nFlight ID: %s\nFrom: %s\nTo: %s\nAvailable Seats: %s",parts[0],parts[1],parts[2],parts[3]);
                        found = true;
                    }
                }
                if(!found){
                    System.out.println("Flight not found");
                }
            }
            catch(InputMismatchException e ){
                System.out.println("Invalid input");
            }
            reader.close();
        }
        catch(IOException e){
            System.out.println("IO Exception in FlightSearch method");
        }
    }

    //Booking flight method
    public static void bookFlight(int rows, int cols,String[][] data,ArrayList<String> list){
        try{
            File file = new File("C:\\Users\\local user\\Documents\\JAVA-Semester-Project\\src\\flightReservation\\test.txt");
            Scanner input = new Scanner(System.in);
            Scanner reader = new Scanner(file);

            try{
                System.out.println("Enter the flight ID(check Flight ID by searching the flight)");
                String ID = input.nextLine().trim().toLowerCase();
                boolean found = false;
                int seats;
                String temp = " ";
                for (int i = 0; i < rows; i++) {
                    if(ID.equals(data[i][0].toLowerCase().trim())){
                        found = true;
                        seats = Integer.parseInt(data[i][3]);
                        if(seats>0){
                            seats--;
                            String str = String.valueOf(seats);
                            data[i][3]=str;
                            System.out.println("Flight found!");
                            System.out.println("Flight details:");
                            System.out.printf("From: %s\tTo: %s\tSeats left: %s",data[i][1],data[i][2],data[i][3]);
                            //This will be used to record the change
                            temp = data[i][0]+" "+data[i][1]+" "+data[i][2];
                            
                        }
                        else{
                            System.out.println("No seats left!");
                        }
                    }
                }
                temp = "Booking details: "+"\n"+temp+"\n";
                list.add(temp);
                if(!found){
                    System.out.println("No available Flights.");
                }
            }
            catch(InputMismatchException e){
                System.out.println("Invalid Input.");
            }
            reader.close();
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < 30; i++) {
                for(int j =0; j<4 ;j++){
                    if(j<3){
                        writer.write(data[i][j].trim()+" ");
                    }
                    else{
                        writer.write(data[i][j].trim()+"\n");
                    }
                }
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("Error");
        }
    }
    //booking History Method
    public static void bookingHistory(String[][] data,ArrayList<String> list){
        try{
            File file = new File("C:\\Users\\local user\\Documents\\JAVA-Semester-Project\\src\\flightReservation\\history.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file,true);
            Scanner reader = new Scanner(file);
            System.out.println("Your booking histroy:");
            for(String s : list){
                writer.write(s);
            }
            writer.close();
            while(reader.hasNextLine()){
                System.out.print(reader.nextLine());
                System.out.println();
            }
            reader.close();
        }
        catch(IOException e){
            System.out.println("IO Exception in Histroy file");
        }
    }
}