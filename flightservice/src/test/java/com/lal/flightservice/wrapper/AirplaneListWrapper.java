package com.lal.flightservice.wrapper;

import com.lal.flightservice.model.Airplane;

import java.util.List;

public class AirplaneListWrapper {

    private List<Airplane> airplaneList;

    public AirplaneListWrapper(){};
    public AirplaneListWrapper(List<Airplane> airplaneList){this.airplaneList=airplaneList;};

    public List<Airplane> getAirplaneList() {
        return airplaneList;
    }

    public void setAirplaneList(List<Airplane> airplaneList) {
        this.airplaneList = airplaneList;
    }
}
