package database;

import models.Customer;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomerDB {

    private static final String FILE = "data/customers.json";

    public static void saveCustomer(Customer customer) {
        JSONArray arr = loadAll();

        JSONObject obj = new JSONObject();
        obj.put("nic", customer.getNic());
        obj.put("name", customer.getName());
        obj.put("phone", customer.getPhone());
        obj.put("email", customer.getEmail());

        arr.put(obj);

        try (FileWriter file = new FileWriter(FILE)) {
            file.write(arr.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray loadAll() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE)));
            if (content.trim().isEmpty()) return new JSONArray();
            return new JSONArray(content);
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}

