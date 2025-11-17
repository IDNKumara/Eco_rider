package database;

import models.Car;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CarDB {

    private static final String FILE = "data/cars.json";

    public static JSONArray loadAll() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE)));
            if (content.trim().isEmpty()) return new JSONArray();
            return new JSONArray(content);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static void saveAll(JSONArray arr) {
        try (FileWriter file = new FileWriter(FILE)) {
            file.write(arr.toString(4));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static boolean addCar(Car car) {
        JSONArray arr = loadAll();
        // check unique ID
        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getString("carId").equalsIgnoreCase(car.getCarId())) {
                return false;
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("carId", car.getCarId());
        obj.put("model", car.getModel());
        obj.put("category", car.getCategory());
        obj.put("dailyRate", car.getDailyRate());
        obj.put("freeKm", car.getFreeKm());
        obj.put("extraKmRate", car.getExtraKmRate());
        obj.put("taxRate", car.getTaxRate());
        obj.put("status", car.getStatus());
        arr.put(obj);
        saveAll(arr);
        return true;
    }

    public static boolean updateCar(String carId, Car updatedCar) {
        JSONArray arr = loadAll();
        boolean found = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            if (o.getString("carId").equalsIgnoreCase(carId)) {
                // replace fields
                o.put("model", updatedCar.getModel());
                o.put("category", updatedCar.getCategory());
                o.put("dailyRate", updatedCar.getDailyRate());
                o.put("freeKm", updatedCar.getFreeKm());
                o.put("extraKmRate", updatedCar.getExtraKmRate());
                o.put("taxRate", updatedCar.getTaxRate());
                o.put("status", updatedCar.getStatus());
                found = true;
                break;
            }
        }
        if (found) saveAll(arr);
        return found;
    }

    public static boolean removeCar(String carId) {
        JSONArray arr = loadAll();
        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getString("carId").equalsIgnoreCase(carId)) {
                arr.remove(i);
                saveAll(arr);
                return true;
            }
        }
        return false;
    }

    public static void updateStatus(String carId, String newStatus) {
        JSONArray arr = loadAll();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject c = arr.getJSONObject(i);
            if (c.getString("carId").equalsIgnoreCase(carId)) {
                c.put("status", newStatus);
                break;
            }
        }
        saveAll(arr);
    }
}

