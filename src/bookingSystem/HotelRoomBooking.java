package bookingSystem;
import java.io.*;
import java.util.*;

public class HotelRoomBooking {
    static Scanner input = new Scanner(System.in);
    static ArrayList<String[]> bookings = new ArrayList<>();
    static String currentUserEmail = "";

    public static void hotelMenu() {
        initializeFiles(); // 🔹 Auto-creates hotel.txt and bookings.txt if missing
        if (!authenticateUser()) return;
        loadBookings();
        while (true) {
            showMenu();
            int choice = input.nextInt(); input.nextLine();
            switch (choice) {
                case 1 -> bookRoom();
                case 2 -> viewBookings();
                case 3 -> viewHotels();
                case 4 -> extraServices();
                case 5 -> cancelBooking();
                case 6 -> checkout();
                case 0 -> { System.out.println("Thank you for using the system."); return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\n---- Hotel Booking System ----");
        System.out.println("1. Book a Room\n2. View Booking Details\n3. View Available Hotels\n4. Avail Extra Services\n5. Cancel Booking\n6. Checkout\n0. Exit");
        System.out.print("Enter your choice: ");
    }

    static boolean authenticateUser() {
        System.out.print("Enter Email: ");
        String email = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(email) && parts[2].equals(password)) {
                    currentUserEmail = email;
                    System.out.println("\nLogin successful! Welcome " + parts[1]);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.txt");
        }
        System.out.println("Login failed. Invalid credentials.");
        return false;
    }

    static void bookRoom() {
        System.out.print("Enter Name: ");
        String name = input.nextLine();
        System.out.print("Enter City: ");
        String city = input.nextLine();
        ArrayList<String[]> hotelsInCity = getHotelsByCity(city);
        if (hotelsInCity.isEmpty()) {
            System.out.println("No hotels found in this city."); return;
        }
        System.out.println("Available Hotels in " + city + ":");
        for (int i = 0; i < hotelsInCity.size(); i++) {
            String[] h = hotelsInCity.get(i);
            System.out.println((i + 1) + ". " + h[0] + " | Standard PKR " + h[4] + " | Deluxe PKR " + h[6]);
        }
        System.out.print("Select Hotel (1 or 2): ");
        int hotelChoice = input.nextInt() - 1; input.nextLine();
        if (hotelChoice < 0 || hotelChoice >= hotelsInCity.size()) {
            System.out.println("Invalid hotel selection."); return;
        }
        String[] selectedHotel = hotelsInCity.get(hotelChoice);
        System.out.print("Room Type (Standard/Deluxe): ");
        String roomType = input.nextLine();
        System.out.print("Enter number of days (max 7): ");
        int days = input.nextInt(); input.nextLine();
        if (days < 1 || days > 7) {
            System.out.println("Invalid number of days."); return;
        }
        int rate = roomType.equalsIgnoreCase("Deluxe") ? Integer.parseInt(selectedHotel[6]) : Integer.parseInt(selectedHotel[4]);
        int total = rate * days;
        String[] booking = {currentUserEmail, name, city, selectedHotel[0], roomType, String.valueOf(days), String.valueOf(total), "No Service"};
        bookings.add(booking); saveBookingToFile(booking);
        System.out.println("Room booked successfully. Total Cost: PKR " + total);
    }

    static void viewBookings() {
        boolean found = false;
        System.out.println("\n--- Booking Details ---");
        for (String[] b : bookings) {
            if (b[0].equals(currentUserEmail)) {
                System.out.println("Name: " + b[1]);
                System.out.println("City: " + b[2]);
                System.out.println("Hotel: " + b[3]);
                System.out.println("Room Type: " + b[4]);
                System.out.println("Days: " + b[5]);
                System.out.println("Total Cost: PKR " + b[6]);
                System.out.println("Service: " + b[7]);
                System.out.println("---------------------------");
                found = true;
            }
        }
        if (!found) System.out.println("No bookings found for current user.");
    }

    static void viewHotels() {
        try (BufferedReader reader = new BufferedReader(new FileReader("hotel.txt"))) {
            String line;
            System.out.println("\n--- Hotels List ---");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println(parts[0] + " | City: " + parts[1] + " | Standard: PKR " + parts[4] + " | Deluxe: PKR " + parts[6]);
            }
        } catch (IOException e) {
            System.out.println("Error reading hotel.txt");
        }
    }

    static void extraServices() {
        List<Integer> userBookingIndices = new ArrayList<>();
        System.out.println("Choose Booking to Add Service:");
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i)[0].equals(currentUserEmail)) {
                userBookingIndices.add(i);
                System.out.println(userBookingIndices.size() + ". " + bookings.get(i)[1] + " | " + bookings.get(i)[3]);
            }
        }
        if (userBookingIndices.isEmpty()) {
            System.out.println("No booking to add service to.");
            return;
        }
        int index = input.nextInt() - 1; input.nextLine();
        if (index < 0 || index >= userBookingIndices.size()) {
            System.out.println("Invalid choice."); return;
        }
        int bookingIndex = userBookingIndices.get(index);
        System.out.println("Available Services:\n1. Breakfast - PKR 300\n2. Airport Pickup - PKR 1500\n3. Late Checkout - PKR 800");
        System.out.print("Choose a service (1-3): ");
        int service = input.nextInt(); input.nextLine();
        String[] services = {"Breakfast", "Airport Pickup", "Late Checkout"};
        int[] costs = {300, 1500, 800};
        if (service < 1 || service > 3) { System.out.println("Invalid selection."); return; }
        String[] booking = bookings.get(bookingIndex);
        booking[6] = String.valueOf(Integer.parseInt(booking[6]) + costs[service - 1]);
        booking[7] = services[service - 1];
        saveAllBookingsToFile();
        System.out.println("Service added. New Total: PKR " + booking[6]);
    }

    static void cancelBooking() {
        List<Integer> userBookingIndices = new ArrayList<>();
        System.out.println("\n--- Your Bookings ---");
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i)[0].equals(currentUserEmail)) {
                userBookingIndices.add(i);
                String[] b = bookings.get(i);
                System.out.println(userBookingIndices.size() + ". " + b[1] + " | " + b[3] + " | PKR " + b[6]);
            }
        }
        if (userBookingIndices.isEmpty()) {
            System.out.println("No bookings available to cancel."); return;
        }
        System.out.print("Enter the number of the booking to cancel: ");
        int cancelIndex = input.nextInt() - 1; input.nextLine();
        if (cancelIndex < 0 || cancelIndex >= userBookingIndices.size()) {
            System.out.println("Invalid selection."); return;
        }
        int removeIndex = userBookingIndices.get(cancelIndex);
        String[] removed = bookings.remove(removeIndex);
        System.out.println("Booking for " + removed[1] + " at " + removed[3] + " has been canceled.");
        saveAllBookingsToFile();
    }

    static void checkout() {
        List<Integer> userBookingIndices = new ArrayList<>();
        System.out.println("\n--- Checkout ---");
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i)[0].equals(currentUserEmail)) {
                userBookingIndices.add(i);
                String[] b = bookings.get(i);
                System.out.println(userBookingIndices.size() + ". " + b[1] + " | " + b[3] + " | Total: PKR " + b[6]);
            }
        }
        if (userBookingIndices.isEmpty()) {
            System.out.println("No bookings available for checkout."); return;
        }
        System.out.print("Select booking to checkout: ");
        int index = input.nextInt() - 1; input.nextLine();
        if (index < 0 || index >= userBookingIndices.size()) {
            System.out.println("Invalid selection."); return;
        }
        String[] b = bookings.get(userBookingIndices.get(index));
        System.out.println("\n--- Final Bill ---");
        System.out.println("Name: " + b[1]);
        System.out.println("Hotel: " + b[3]);
        System.out.println("Room Type: " + b[4]);
        System.out.println("Days: " + b[5]);
        System.out.println("Service Availed: " + b[7]);
        System.out.println("Total Payable Amount: PKR " + b[6]);
    }

    static ArrayList<String[]> getHotelsByCity(String city) {
        ArrayList<String[]> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("hotel.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equalsIgnoreCase(city)) result.add(parts);
            }
        } catch (IOException e) {
            System.out.println("Error reading hotel.txt");
        }
        return result;
    }

    static void saveBookingToFile(String[] booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt", true))) {
            writer.write(String.join(",", booking));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving booking.");
        }
    }

    static void saveAllBookingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt"))) {
            for (String[] b : bookings) {
                writer.write(String.join(",", b));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating bookings file.");
        }
    }

    static void loadBookings() {
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) bookings.add(line.split(","));
        } catch (IOException e) {
            System.out.println("Error loading bookings.");
        }
    }

    static void initializeFiles() {
        String hotelData = """
            Islamabad Serena Hotel,Islamabad,200,16,387,10,550,4
            Islamabad Marriott Hotel,Islamabad,180,20,290,15,450,8
            Pearl Continental Hotel,Karachi,250,20,450,15,650,10
            Avari Towers Karachi,Karachi,220,25,320,20,500,12
            Avari Hotel,Lahore,150,20,250,15,350,7
            The Nishat Hotel,Lahore,170,22,280,18,420,9
            Four Points by Sheraton,Multan,120,15,200,10,300,5
            Hotel One Lalazar,Multan,100,20,180,12,250,6
            Hotel One,Faisalabad,110,18,190,12,280,7
            Faisalabad Serena Hotel,Faisalabad,120,20,220,15,340,8
            Pearl Continental Hotel,Rawalpindi,160,25,260,15,400,10
            Safari Club 1,Rawalpindi,90,30,150,20,220,5
            Gilgit Serena Hotel,Gilgit,80,10,120,8,180,4
            Park Hotel Gilgit,Gilgit,70,15,110,10,160,5
            Peshawar Serena Hotel,Peshawar,120,18,200,12,300,6
            Shelton's Rezidor,Peshawar,80,20,140,15,210,5
            Quetta Serena Hotel,Quetta,110,15,190,10,280,7
            Lourdes Hotel,Quetta,75,20,130,15,200,5
            The Royaute,Sialkot,90,15,160,10,240,6
            Hotel Javson,Sialkot,85,18,150,12,230,5
        """;
        createFileIfMissing("hotel.txt", hotelData);
        createFileIfMissing("bookings.txt", "");
    }

    static void createFileIfMissing(String filename, String content) {
        File file = new File(filename);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content.trim());
                writer.newLine();
                System.out.println("Created: " + filename);
            } catch (IOException e) {
                System.out.println("Error creating " + filename);
            }
        }
    }
}
