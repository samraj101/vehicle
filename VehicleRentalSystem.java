import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Vehicle {
    String id, model;
    double ratePerHour;
    boolean isAvailable;

    Vehicle(String id, String model, double ratePerHour) {
        this.id = id;
        this.model = model;
        this.ratePerHour = ratePerHour;
        this.isAvailable = true;
    }
}

class Rental {
    Vehicle vehicle;
    LocalDateTime startTime;
    LocalDateTime endTime;
    double totalPayment;

    Rental(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.endTime = endTime;

        // ✅ Validate time order
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }

        // ✅ Calculate total hours (round up any fraction to the next hour)
        long minutes = Duration.between(startTime, endTime).toMinutes();
        long hours = (long) Math.ceil(minutes / 60.0);

        this.totalPayment = hours * vehicle.ratePerHour;
    }

    void printReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        System.out.println("\n--- Rental Receipt ---");
        System.out.println("Vehicle: " + vehicle.model + " (" + vehicle.id + ")");
        System.out.println("Start Time: " + startTime.format(formatter));
        System.out.println("End Time: " + endTime.format(formatter));
        System.out.println("Total Payment: ₹" + totalPayment);
    }
}

public class VehicleRentalSystem {
    static List<Vehicle> vehicles = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        vehicles.add(new Vehicle("V001", "Honda City", 150));
        vehicles.add(new Vehicle("V002", "Suzuki Swift", 120));

        System.out.println("Available Vehicles:");
        for (Vehicle v : vehicles) {
            System.out.println(v.id + " - " + v.model + " - ₹" + v.ratePerHour + "/hr");
        }

        System.out.print("\nEnter Vehicle ID to rent: ");
        String id = sc.nextLine();
        Vehicle selected = null;
        for (Vehicle v : vehicles) {
            if (v.id.equalsIgnoreCase(id) && v.isAvailable) {
                selected = v;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Vehicle not available.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        try {
            System.out.print("Enter rental start time (dd-MM-yyyy HH:mm): ");
            LocalDateTime start = LocalDateTime.parse(sc.nextLine(), fmt);

            System.out.print("Enter rental end time (dd-MM-yyyy HH:mm): ");
            LocalDateTime end = LocalDateTime.parse(sc.nextLine(), fmt);

            Rental rental = new Rental(selected, start, end);
            selected.isAvailable = false;
            rental.printReceipt();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
