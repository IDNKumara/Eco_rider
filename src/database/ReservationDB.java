package database;

import models.Reservation;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReservationDB {

    private static final String FILE = "data/reservations.json";

    public static JSONArray loadAll() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE)));
            if (content.trim().isEmpty()) return new JSONArray();
            return new JSONArray(content);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static void saveReservation(Reservation res) {
        JSONArray arr = loadAll();

        JSONObject o = new JSONObject();
        o.put("reservationId", res.getReservationId());
        o.put("customerNic", res.getCustomerNic());
        o.put("carId", res.getCarId());
        o.put("startDate", res.getStartDate());
        o.put("numberOfDays", res.getNumberOfDays());
        o.put("totalKm", res.getTotalKm());
        o.put("createdDate", res.getCreatedDate());

        arr.put(o);

        try (FileWriter file = new FileWriter(FILE)) {
            file.write(arr.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveArray(JSONArray arr) {
    try (FileWriter file = new FileWriter(FILE)) {
        file.write(arr.toString(4));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
