package bookingSystem;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BusTicketReservation {
    static final String BUS_FILE = "buses.txt";
    static final String BOOKING_FILE = "bus_bookings.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        ensureFilesExist();
        int choice = 0;
        do {
            System.out.println("\n========= BUS RESERVATION MENU =========");
            System.out.println("1. View Available Buses");
            System.out.println("2. Book a Seat");
            System.out.println("3. Cancel a Booking");
            System.out.println("4. Modify a Booking");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> viewBuses();
                    case 2 -> bookSeat();
                    case 3 -> cancelBooking();
                    case 4 -> modifyBooking();
                    case 5 -> System.out.println("Exiting... Goodbye!");
                    default -> System.out.println("Invalid choice. Please select 1-5.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (choice != 5);
    }

    // 🔧 Create required files if not exist
    public static void ensureFilesExist() {
        File busFile = new File(BUS_FILE);
        File bookingFile = new File(BOOKING_FILE);

        try {
            // Create empty booking file if not exists
            if (!bookingFile.exists()) bookingFile.createNewFile();

            // Create buses.txt with default buses if it doesn't exist or is empty
            if (!busFile.exists() || busFile.length() == 0) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(busFile))) {
                    pw.println("B001,Lahore-Islamabad,2025-06-25,08:00,40,Economy,1500");
                    pw.println("B002,Karachi-Hyderabad,2025-06-26,10:00,50,Business,1200");
                    pw.println("B003,Multan-Lahore,2025-06-27,14:00,45,Economy,1000");
                    pw.println("B004,Peshawar-Islamabad,2025-06-28,09:00,42,Economy,1300");
                    pw.println("B005,Faisalabad-Rawalpindi,2025-06-29,16:00,38,Business,1400");
                    System.out.println("🚍 Default buses added to buses.txt");
                }
            }
        } catch (IOException e) {
            System.out.println("Error ensuring files exist: " + e.getMessage());
        }
    }

    public static void viewBuses() {
        System.out.println("\nAvailable Buses:");
        try (BufferedReader br = new BufferedReader(new FileReader(BUS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] bus = line.split(",");
                if (bus.length >= 7) {
                    System.out.printf("BusID: %s | Route: %s | Date: %s | Time: %s | Seats: %s | Class: %s | Price: %s PKR%n",
                            bus[0], bus[1], bus[2], bus[3], bus[4], bus[5], bus[6]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading buses.txt");
        }
    }

    public static void bookSeat() {
        String name;
        do {
            System.out.print("Enter your name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            }
        } while (name.isEmpty());

        System.out.print("Enter Bus ID to book: ");
        String busId = scanner.nextLine();
        String[] bus = findBus(busId);
        if (bus == null) {
            System.out.println("Bus not found.");
            return;
        }

        String dateStr = bus[2];
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date busDate = sdf.parse(dateStr);
            Date today = sdf.parse(sdf.format(new Date()));
            if (busDate.before(today)) {
                System.out.println("Cannot book a seat for a past date: " + dateStr);
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid date format in bus file: " + dateStr);
            return;
        }

        boolean[] booked = getBookedSeats(busId);
        int totalSeats = Integer.parseInt(bus[4]);
        System.out.println("Available Seats:");
        for (int i = 1; i <= totalSeats; i++) {
            if (!booked[i]) System.out.print(i + " ");
        }
        System.out.println();

        int seat = -1;
        while (true) {
            try {
                System.out.print("Choose seat number: ");
                seat = Integer.parseInt(scanner.nextLine());
                if (seat <= 0 || seat > totalSeats) {
                    System.out.println("Invalid seat number. Must be between 1 and " + totalSeats);
                } else if (booked[seat]) {
                    System.out.println("Seat already booked. Choose a different seat.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        String bookingId = generateBookingId();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(BOOKING_FILE, true))) {
            pw.printf("%s,%s,%s,%d,%s%n", bookingId, name, busId, seat, bus[2]);
            System.out.println("✅ Booking successful. Your Booking ID: " + bookingId);
        } catch (IOException e) {
            System.out.println("Error writing to booking file.");
        }
    }

    public static void cancelBooking() {
        System.out.print("Enter Booking ID to cancel: ");
        String id = scanner.nextLine();
        File input = new File(BOOKING_FILE);
        File temp = new File("temp.txt");
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(input));
             PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(id + ",")) {
                    found = true;
                } else {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading/writing file.");
            return;
        }

        if (input.delete() && temp.renameTo(input)) {
            System.out.println(found ? "Booking cancelled successfully." : "Booking ID not found.");
        }
    }

    public static void modifyBooking() {
        System.out.print("Enter Booking ID to modify: ");
        String id = scanner.nextLine();
        cancelBookingById(id);
        System.out.println("Please re-book your ticket.");
        bookSeat();
    }

    public static void cancelBookingById(String id) {
        File input = new File(BOOKING_FILE);
        File temp = new File("temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(input));
             PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(id + ",")) {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading/writing file.");
        }

        input.delete();
        temp.renameTo(input);
    }

    public static String[] findBus(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(BUS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] bus = line.split(",");
                if (bus[0].equalsIgnoreCase(id)) return bus;
            }
        } catch (IOException e) {
            System.out.println("Error reading bus file.");
        }
        return null;
    }

    public static boolean[] getBookedSeats(String busId) {
        boolean[] seats = new boolean[101]; // index 0 unused
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKING_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] b = line.split(",");
                if (b[2].equalsIgnoreCase(busId)) {
                    int seat = Integer.parseInt(b[3]);
                    if (seat < seats.length) seats[seat] = true;
                }
            }
        } catch (IOException e) {
            // file might not exist yet; that's fine
        }
        return seats;
    }

    public static String generateBookingId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("B");
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
