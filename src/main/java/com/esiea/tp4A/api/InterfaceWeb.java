package com.esiea.tp4A.api;

import java.util.Set;

import com.esiea.tp4A.mypackage.Position;
import com.esiea.tp4A.mypackage.Rover;

public interface InterfaceWeb {
	
	Position getPlayerPosition(Rover rover);
	
	Set<Position> getObstaclesPositions(Rover rover);
	
	Set<Rover> getOtherPlayersPositions(Rover rover);
	
	int getLaserRange(Rover rover);
	
	void move(String command, Rover rover);
	
	void shoot(Rover rover);
	
	boolean isPlayerAlive(Rover rover);

}
