package com.gd.orh.entity;

public class CarLocation {
    private String carId;
    private Location location;

    public CarLocation() {
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
