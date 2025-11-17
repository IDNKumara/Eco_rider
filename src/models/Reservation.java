package models;

public class Reservation {
    private String reservationId;
    private String customerNic;
    private String carId;
    private String startDate;
    private int numberOfDays;
    private int totalKm;
    private String createdDate;

    public Reservation(String reservationId, String customerNic, String carId, String startDate, int numberOfDays, int totalKm, String createdDate) {
        this.reservationId = reservationId;
        this.customerNic = customerNic;
        this.carId = carId;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        this.totalKm = totalKm;
        this.createdDate = createdDate;
    }

    public String getReservationId() { return reservationId; }
    public String getCustomerNic() { return customerNic; }
    public String getCarId() { return carId; }
    public String getStartDate() { return startDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public int getTotalKm() { return totalKm; }
    public String getCreatedDate() { return createdDate; }
}
