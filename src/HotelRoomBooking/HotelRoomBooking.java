package HotelRoomBooking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class HotelRoomBooking {
    // The array now needs 8 columns to hold the new data for 3 room types.
    // Format: [Name, City, StdPrice, StdRooms, DlxPrice, DlxRooms, StePrice, SteRooms]
    private static String[][] hotels = new String[20][8];
    private static int hotelCount = 0;

    public static void main(String[] args) {
        loadHotelsFromFile();
        Scanner userInput = new Scanner(System.in);
        int userChoice = 0;

        while (userChoice != 5) {
            System.out.println("\n*** Welcome to the Advanced Hotel Booker ***");
            System.out.println("1. Find Hotels in a City");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel a Booking");
            System.out.println("4. See All Hotels Status");
            System.out.println("5. Exit");
            System.out.print("Please enter a number (1-5): ");

            String inputLine = userInput.nextLine();

            try {
                userChoice = Integer.parseInt(inputLine);

                if (userChoice == 1) {
                    findHotels(userInput);
                } else if (userChoice == 2) {
                    bookRoom(userInput);
                } else if (userChoice == 3) {
                    cancelBooking(userInput);
                } else if (userChoice == 4) {
                    seeAllHotels();
                } else if (userChoice == 5) {
                    System.out.println("Goodbye! Thanks for using the system.");
                } else {
                    System.out.println("That's not a valid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter only a number (like 1, 2, 3...).");
            }
        }
        userInput.close();
    }

    public static void findHotels(Scanner userInput) {
        System.out.print("Which city are you looking for? (Case-Sensitive) ");
        String cityToFind = userInput.nextLine();
        System.out.println("\n--- Hotels available in " + cityToFind + " ---");
        boolean hotelFound = false;

        for (int i = 0; i < hotelCount; i++) {
            if (hotels[i][1].equals(cityToFind)) {
                System.out.println("\n--> Hotel: " + hotels[i][0]);
                System.out.println("    - Standard Room: $" + hotels[i][2] + " (" + hotels[i][3] + " left)");
                System.out.println("    - Deluxe Room:   $" + hotels[i][4] + " (" + hotels[i][5] + " left)");
                System.out.println("    - Suite Room:    $" + hotels[i][6] + " (" + hotels[i][7] + " left)");
                hotelFound = true;
            }
        }
        if (!hotelFound) {
            System.out.println("Sorry, no hotels found in " + cityToFind + ".");
        }
    }

    public static void bookRoom(Scanner userInput) {
        System.out.print("Please enter the name of the hotel to book: ");
        String hotelToBook = userInput.nextLine();
        int hotelIndex = -1;

        // First, find the index of the hotel.
        for (int i = 0; i < hotelCount; i++) {
            if (hotels[i][0].equalsIgnoreCase(hotelToBook)) {
                hotelIndex = i;
                break;
            }
        }

        if (hotelIndex == -1) {
            System.out.println("Error: Could not find a hotel with that name.");
            return; // Exit the function if hotel not found.
        }

        // Show room options and get user's choice.
        System.out.println("Which type of room would you like?");
        System.out.println("1. Standard ($" + hotels[hotelIndex][2] + ")");
        System.out.println("2. Deluxe   ($" + hotels[hotelIndex][4] + ")");
        System.out.println("3. Suite    ($" + hotels[hotelIndex][6] + ")");
        System.out.print("Enter your choice (1-3): ");
        int roomChoice = Integer.parseInt(userInput.nextLine());

        System.out.print("How many nights would you like to book (1-7)? ");
        int nights = Integer.parseInt(userInput.nextLine());

        // Validate number of nights.
        if (nights < 1 || nights > 7) {
            System.out.println("Invalid number of nights. Booking must be between 1 and 7 nights.");
            return;
        }

        int priceIndex = 0;
        int roomsIndex = 0;
        String roomTypeName = "";

        // Determine array indices based on room choice.
        if (roomChoice == 1) {
            priceIndex = 2; roomsIndex = 3; roomTypeName = "Standard";
        } else if (roomChoice == 2) {
            priceIndex = 4; roomsIndex = 5; roomTypeName = "Deluxe";
        } else if (roomChoice == 3) {
            priceIndex = 6; roomsIndex = 7; roomTypeName = "Suite";
        } else {
            System.out.println("Invalid room choice.");
            return;
        }

        int roomsAvailable = Integer.parseInt(hotels[hotelIndex][roomsIndex]);
        if (roomsAvailable > 0) {
            int pricePerNight = Integer.parseInt(hotels[hotelIndex][priceIndex]);
            int totalCost = pricePerNight * nights;

            hotels[hotelIndex][roomsIndex] = String.valueOf(roomsAvailable - 1);
            saveChangesToFile();

            System.out.println("\nSuccess! Your booking is confirmed.");
            System.out.println("Hotel: " + hotels[hotelIndex][0]);
            System.out.println("Room Type: " + roomTypeName);
            System.out.println("Nights: " + nights);
            System.out.println("Total Cost: $" + totalCost);
        } else {
            System.out.println("Sorry, there are no " + roomTypeName + " rooms available at this hotel.");
        }
    }


    public static void cancelBooking(Scanner userInput) {
        System.out.print("Please enter the name of the hotel to cancel booking for: ");
        String hotelToCancel = userInput.nextLine();
        int hotelIndex = -1;

        for (int i = 0; i < hotelCount; i++) {
            if (hotels[i][0].equalsIgnoreCase(hotelToCancel)) {
                hotelIndex = i;
                break;
            }
        }

        if (hotelIndex == -1) {
            System.out.println("Error: Could not find a hotel with that name.");
            return;
        }

        System.out.println("Which type of room are you canceling?");
        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");
        System.out.print("Enter your choice (1-3): ");
        int roomChoice = Integer.parseInt(userInput.nextLine());

        int roomsIndex = 0;
        if (roomChoice == 1) roomsIndex = 3;
        else if (roomChoice == 2) roomsIndex = 5;
        else if (roomChoice == 3) roomsIndex = 7;
        else {
            System.out.println("Invalid room choice.");
            return;
        }

        int roomsAvailable = Integer.parseInt(hotels[hotelIndex][roomsIndex]);
        hotels[hotelIndex][roomsIndex] = String.valueOf(roomsAvailable + 1);
        saveChangesToFile();
        System.out.println("Success! The booking has been canceled.");
    }

    public static void seeAllHotels() {
        System.out.println("\n--- Full Hotel Status Report ---");
        for (int i = 0; i < hotelCount; i++) {
            System.out.println("\nHotel: " + hotels[i][0] + " | City: " + hotels[i][1]);
            System.out.println("  - Standard: $" + hotels[i][2] + " (" + hotels[i][3] + " rooms)");
            System.out.println("  - Deluxe:   $" + hotels[i][4] + " (" + hotels[i][5] + " rooms)");
            System.out.println("  - Suite:    $" + hotels[i][6] + " (" + hotels[i][7] + " rooms)");
        }
        System.out.println("--- End of Report ---");
    }

    public static void loadHotelsFromFile() {
        try {
            File myFile = new File("hotel.txt");
            Scanner fileReader = new Scanner(myFile);
            hotelCount = 0;

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] hotelData = line.split(",");
                if (hotelData.length == 8) { // Check for the new format.
                    hotels[hotelCount] = hotelData;
                    hotelCount++;
                }
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: hotel.txt not found or format is incorrect!");
            System.out.println("Please ensure hotel.txt is in the project root folder and uses the new 8-column format.");
        }
    }

    public static void saveChangesToFile() {
        try {
            PrintStream fileWriter = new PrintStream(new FileOutputStream("hotel.txt"));

            for (int i = 0; i < hotelCount; i++) {
                // Join all 8 elements back into a single string with commas.
                String line = String.join(",", hotels[i]);
                fileWriter.println(line);
            }
            fileWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Could not save changes to hotel.txt!");
        }
    }
}
