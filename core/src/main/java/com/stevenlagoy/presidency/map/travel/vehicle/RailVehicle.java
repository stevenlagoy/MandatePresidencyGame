package com.stevenlagoy.presidency.map.travel.vehicle;

public class RailVehicle extends Vehicle {

    public String type;

    public RailVehicle(double speed, double costPerMile, String type) {
        super(speed, costPerMile);
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

}
