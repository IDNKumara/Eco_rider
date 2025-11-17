import models.Car;
import database.CarDB;

public class InitCars {
    public static void main(String[] args) {
        CarDB.addCar(new Car("C001", "Toyota Vitz", "Compact Petrol", 5000, 100, 50, 10, "Available"));
        CarDB.addCar(new Car("C002", "Toyota Aqua", "Hybrid", 7500, 150, 60, 12, "Available"));
        CarDB.addCar(new Car("C003", "Nissan Leaf", "Electric", 10000, 200, 40, 8, "Available"));
        CarDB.addCar(new Car("C004", "BMW X5", "Luxury SUV", 15000, 250, 75, 15, "Available"));

        System.out.println("Cars Initialized ✅");
    }
}
