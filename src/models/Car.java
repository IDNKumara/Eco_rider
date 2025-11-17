package models;

public class Car {

    private String carId;
    private String model;
    private String category;
    private int dailyRate;
    private int freeKm;
    private int extraKmRate;
    private int taxRate;
    private String status; // Available, Reserved, Under Maintenance

    public Car(String carId, String model, String category, int dailyRate, int freeKm, int extraKmRate, int taxRate, String status) {
        this.carId = carId;
        this.model = model;
        this.category = category;
        this.dailyRate = dailyRate;
        this.freeKm = freeKm;
        this.extraKmRate = extraKmRate;
        this.taxRate = taxRate;
        this.status = status;
    }

    public String getCarId() { return carId; }
    public String getModel() { return model; }
    public String getCategory() { return category; }
    public int getDailyRate() { return dailyRate; }
    public int getFreeKm() { return freeKm; }
    public int getExtraKmRate() { return extraKmRate; }
    public int getTaxRate() { return taxRate; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
