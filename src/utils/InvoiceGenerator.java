package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import models.Car;
import models.Reservation;
import database.CarDB;
import database.CustomerDB;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class InvoiceGenerator {

    public static void generatePDF(Reservation res, String fileName) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 12);

            doc.add(new Paragraph("EcoRide Car Rental Invoice", titleFont));
            doc.add(new Paragraph("\n"));

            // Get Customer
            JSONArray cust = CustomerDB.loadAll();
            JSONObject customer = null;
            for(int i=0;i<cust.length();i++){
                if(cust.getJSONObject(i).getString("nic").equals(res.getCustomerNic()))
                    customer = cust.getJSONObject(i);
            }

            // Get Car
            JSONArray cars = CarDB.loadAll();
            JSONObject car = null;
            for(int i=0;i<cars.length();i++){
                if(cars.getJSONObject(i).getString("carId").equals(res.getCarId()))
                    car = cars.getJSONObject(i);
            }

            int basePrice = car.getInt("dailyRate") * res.getNumberOfDays();
            int freeKm = car.getInt("freeKm") * res.getNumberOfDays();
            int extraKm = Math.max(0, res.getTotalKm() - freeKm);
            int extraCharge = extraKm * car.getInt("extraKmRate");
            int discount = (res.getNumberOfDays() >= 7) ? (int)(basePrice * 0.10) : 0;
            double tax = ((basePrice - discount) + extraCharge) * (car.getInt("taxRate") / 100.0);
            int finalAmount = (int)((basePrice - discount) + extraCharge + tax - 5000);

            doc.add(new Paragraph("Customer Details", titleFont));
            doc.add(new Paragraph("Name: " + customer.getString("name"), normal));
            doc.add(new Paragraph("NIC/Passport: " + customer.getString("nic"), normal));
            doc.add(new Paragraph("Phone: " + customer.getString("phone"), normal));
            doc.add(new Paragraph("Email: " + customer.getString("email"), normal));
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("Car Details", titleFont));
            doc.add(new Paragraph("Car: " + car.getString("model") + " (" + car.getString("category") + ")", normal));
            doc.add(new Paragraph("Daily Rate: LKR " + car.getInt("dailyRate"), normal));
            doc.add(new Paragraph("Start Date: " + res.getStartDate(), normal));
            doc.add(new Paragraph("Rental Days: " + res.getNumberOfDays(), normal));
            doc.add(new Paragraph("Total KM Driven: " + res.getTotalKm(), normal));
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("Charges Breakdown", titleFont));
            doc.add(new Paragraph("Base Price: LKR " + basePrice));
            doc.add(new Paragraph("Extra KM (" + extraKm + " km): LKR " + extraCharge));
            doc.add(new Paragraph("Discount: LKR " + discount));
            doc.add(new Paragraph("Tax: LKR " + (int)tax));
            doc.add(new Paragraph("Deposit Deducted: LKR 5000"));
            doc.add(new Paragraph("\nFinal Amount Payable: LKR " + finalAmount, titleFont));

            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
