package com.esiea.tp4A.mypackage;
import java.util.HashSet;
import java.util.Set;

public class Planet implements PlanetMap {
    private int planetSize;
    private Set<Position> obstaclePositions;
    
    public Planet(int planetSize) {
        this.setPlanetSize(planetSize);
        obstaclePositions = new HashSet<Position>();
        obstaclePositions.add(Position.of(0, 3, Direction.NORTH));
        obstaclePositions.add(Position.of(0, -3, Direction.NORTH));
    }

    @Override
    public Set<Position> obstaclePositions() {        
        return this.obstaclePositions;
    }

    public int getPlanetSize() {
        return planetSize;
    }

    public void setPlanetSize(int planetSize) {
        this.planetSize = planetSize;
    }

}
