package bookingSystem;

import java.util.Scanner;
import java.io.*;

 public class LoginSignup {
   static final String USERS_FILE = "users.txt";
   public static File file = new File(USERS_FILE);
   

   public static void main(String[] args) throws IOException {
      Scanner input = new Scanner(System.in);
      if(!file.exists()){
         file.createNewFile();
      }
      while (true) {
         System.out.println("==== Booking Management System ====");
         System.out.println("1 - SignUp");
         System.out.println("2 - Login");
         System.out.println("3 - Exit");
         System.out.print("\n Enter your choice: ");
         int choice=-1;
         try{
            choice = input.nextInt();
         }
         catch(NumberFormatException e){
            System.out.println("Invalid input! Please enter a number.");
         }
         switch (choice) {
            case 1:
               signUp();
               break;
            case 2:
               login();
               break;
            case 3:
               System.out.println("Exiting the System...");
               ;
               return;
            default:
               System.out.println("Invalid input! Kindly give the correct input");
               break;
         }
      }
   }

   static void signUp() throws IOException {
      Scanner input = new Scanner(System.in);
      String email = "";
      String name = "";
      String password = "";
      String passConfirm = "";

      while (true) {
         try {
            System.out.print("Enter your email: ");
            email = input.nextLine().trim();

            if (!email.contains("@")) {
               throw new IllegalArgumentException("Email must contain '@'.");
            }

            if (emailExists(email)) {
               throw new IllegalArgumentException("This email is already registered");
            }
            break;
         } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
         }
      }

      System.out.print("Enter your name: ");
      name = input.nextLine().trim();

      while (true) {
         System.out.print("Enter your password: ");
         password = input.nextLine().trim();

         if (!passCheck(password)) 
         {
            System.out.println("Password must be 8 characters long. Password also must contain atleast 1 Upper Case character, 1 Lower Case character, 1 Digit and atleast 1 speacial character");
            continue;
         }

         System.out.print("Type again to confirm your password: ");
         passConfirm = input.nextLine().trim();

         if (password.equals(passConfirm)) {
            break;
         } else {
            System.out.println("Passwords do not match! Try again.");
         }
      }

      FileWriter fw = new FileWriter(USERS_FILE, true);
      fw.write(email + "," + name + "," + password + "\n");
      fw.close();

      System.out.println("\nSignUp Successful!");
      System.out.println("\nWelcome " + name +"\n");
   }

   static void login() throws IOException {
      Scanner input = new Scanner(System.in);
      String email;

      while (true) {
         System.out.print("Enter your Email: ");
         email = input.nextLine().trim();
         if (!email.contains("@")) {
            System.out.println("Email must contain '@'.");
            continue;
         }
         break;
      }

      System.out.print("Enter your Password: ");
      String password = input.nextLine().trim();

      BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));
      String line;
      boolean success = false;

      while ((line = br.readLine()) != null) {
         String[] parts = line.split(",");
         if (parts.length == 3 && parts[0].equals(email) && parts[2].equals(password)) {
            success = true;
            break;

         }
      }
      br.close();

      if (success) {
         System.out.println("\nLogin Successful!");
         System.out.println("\nWelcome " + email +"\n");
         System.out.println("Select your Operation: \n1. Flights booking\n2. Bus Ticket Booking\n3. Hotel Booking\n4. Exit");
         while(true){
            int op = input.nextInt();
            input.nextLine();
            if(op>=1 && op<=3){
               switch(op){
                  case 1 : flightReservation.flightmenu();
                  return;
                  case 2 : BusTicketReservation.showMenu();
                  return;
                  case 3 : HotelRoomBooking.hotelMenu();
                  return;
               }
            }
            else{
               System.out.println("Invalid Input, Try again");
            }
         }
      } else {
         System.out.println("Your credentials are not valid");
      }

   }

   static boolean emailExists(String email) throws IOException 
   {
      try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].trim().equals(email.trim())) {
                return true;
            }
        }
    }
    return false;
   }

   static boolean passCheck(String password){
      if (password.length() < 8) 
      {
         return false;
      }

      boolean hasUpperCase = false;
      boolean hasLowerCase = false;
      boolean hasDigit = false;
      boolean hasSpecialChar = false;

      for (char ch : password.toCharArray()) 
      {
         if (Character.isUpperCase(ch)) 
         {
            hasUpperCase = true;  
         }
         else if (Character.isLowerCase(ch)) 
         {
            hasLowerCase = true;
         }
         else if (Character.isDigit(ch)) 
         {
            hasDigit = true;
         }
         else if (!Character.isLetterOrDigit(ch)) 
         {
            hasSpecialChar = true;
         }
      }

      return hasDigit && hasLowerCase && hasSpecialChar && hasUpperCase;

   }

}