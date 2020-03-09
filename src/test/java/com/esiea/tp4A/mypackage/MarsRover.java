package com.esiea.tp4A.mypackage;

public interface MarsRover {


    //DEFAULT = La classe n'est accessible qu'aux classes du même PACKAGE
    default MarsRover initialize(Position position) {
        return this;
    }

    default MarsRover updateMap(PlanetMap map) {
        return this;
    }

    default MarsRover configureLaserRange(int range) {
        return this;
    }

    default Position move(String command) {
        return Position.of(0, 0, Direction.NORTH);
    }
}
