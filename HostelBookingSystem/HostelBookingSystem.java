import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Room {
    private int roomNumber;
    private String roomType;
    private int capacity;
    private double pricePerNight;
    private boolean availability;

    public Room(int roomNumber, String roomType, int capacity, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.availability = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void bookRoom() {
        availability = false;
    }

    public void releaseRoom() {
        availability = true;
    }
}

class Booking {
    private int bookingId;
    private int roomNumber;
    private String checkInDate;
    private String checkOutDate;
    private String occupants;

    public Booking(int bookingId, int roomNumber, String checkInDate, String checkOutDate, String occupants) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.occupants = occupants;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getOccupants() {
        return occupants;
    }
}

class Hostel {
    private HashMap<Integer, Room> rooms;
    private HashMap<Integer, Booking> bookings;

    public Hostel() {
        rooms = new HashMap<>();
        bookings = new HashMap<>();
    }

    public void addRoom(int roomNumber, String roomType, int capacity, double pricePerNight) {
        Room room = new Room(roomNumber, roomType, capacity, pricePerNight);
        rooms.put(roomNumber, room);
    }

    public ArrayList<Room> viewAvailableRooms() {
        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Booking bookRoom(int roomNumber, String checkInDate, String checkOutDate, String occupants) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.isAvailable()) {
            int bookingId = bookings.size() + 1;
            Booking booking = new Booking(bookingId, roomNumber, checkInDate, checkOutDate, occupants);
            bookings.put(bookingId, booking);
            room.bookRoom();
            return booking;
        }
        return null;
    }

    public boolean cancelBooking(int bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            Room room = rooms.get(booking.getRoomNumber());
            if (room != null) {
                room.releaseRoom();
            }
            bookings.remove(bookingId);
            return true;
        }
        return false;
    }

    public ArrayList<Booking> viewBookingHistory(String user) {
        ArrayList<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getOccupants().equals(user)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }
}

class User {
    private String name;
    private Hostel hostel;

    public User(String name, Hostel hostel) {
        this.name = name;
        this.hostel = hostel;
    }

    public void bookRoom(int roomNumber, String checkInDate, String checkOutDate) {
        Booking booking = hostel.bookRoom(roomNumber, checkInDate, checkOutDate, name);
        if (booking != null) {
            System.out.println("Booking successful. Your booking ID is: " + booking.getBookingId());
        } else {
            System.out.println("Failed to book the room. Room may not be available.");
        }
    }

    public void cancelBooking(int bookingId) {
        if (hostel.cancelBooking(bookingId)) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void viewBookingHistory() {
        ArrayList<Booking> userBookings = hostel.viewBookingHistory(name);
        if (userBookings.isEmpty()) {
            System.out.println("No booking history found for " + name);
        } else {
            System.out.println("Booking history for " + name + ":");
            for (Booking booking : userBookings) {
                System.out.println("Booking ID: " + booking.getBookingId() +
                        ", Room Number: " + booking.getRoomNumber() +
                        ", Check-in Date: " + booking.getCheckInDate() +
                        ", Check-out Date: " + booking.getCheckOutDate());
            }
        }
    }
}
public class HostelBookingSystem {
    public static void main(String[] args) {
        Hostel hostel = new Hostel(); // Creating a new hostel instance

        // Adding some rooms
        hostel.addRoom(101, "Single", 1, 50.0);
        hostel.addRoom(102, "Single", 1, 50.0);
        hostel.addRoom(103, "Double", 2, 80.0);
        hostel.addRoom(104, "Double", 2, 80.0);
        hostel.addRoom(105, "Dormitory", 4, 30.0);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Hostel Booking System!");
        System.out.println();

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. View available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Cancel booking");
            System.out.println("4. View booking history");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    displayAvailableRooms(hostel);
                    break;
                case 2:
                    bookRoom(hostel, scanner);
                    break;
                case 3:
                    cancelBooking(hostel, scanner);
                    break;
                case 4:
                    viewBookingHistory(hostel, scanner);
                    break;
                case 5:
                    System.out.println("Thank you for using Hostel Booking System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
            System.out.println();
        }
    }

    private static void displayAvailableRooms(Hostel hostel) {
        ArrayList<Room> availableRooms = hostel.viewAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms at the moment.");
        } else {
            System.out.println("Available Rooms:");
            for (Room room : availableRooms) {
                System.out.println("Room Number: " + room.getRoomNumber() +
                        ", Type: " + room.getRoomType() +
                        ", Capacity: " + room.getCapacity() +
                        ", Price per Night: $" + room.getPricePerNight());
            }
        }
    }

    private static void bookRoom(Hostel hostel, Scanner scanner) {
        System.out.println("Enter the details to book a room:");
        System.out.print("Room Number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Check-in Date (YYYY-MM-DD): ");
        String checkInDate = scanner.nextLine();
        System.out.print("Check-out Date (YYYY-MM-DD): ");
        String checkOutDate = scanner.nextLine();

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();

        Booking booking = hostel.bookRoom(roomNumber, checkInDate, checkOutDate, userName);
        if (booking != null) {
            System.out.println("Booking successful. Your booking ID is: " + booking.getBookingId());
        } else {
            System.out.println("Failed to book the room. Room may not be available.");
        }
    }

    private static void cancelBooking(Hostel hostel, Scanner scanner) {
        System.out.print("Enter your booking ID to cancel: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (hostel.cancelBooking(bookingId)) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    private static void viewBookingHistory(Hostel hostel, Scanner scanner) {
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        User user = new User(userName, hostel);
        user.viewBookingHistory();
    }
}
