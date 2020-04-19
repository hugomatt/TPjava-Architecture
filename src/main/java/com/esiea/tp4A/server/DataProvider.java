package com.esiea.tp4A.server;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.esiea.tp4A.api.InterfaceWeb;
import com.esiea.tp4A.mypackage.Planet;
import com.esiea.tp4A.mypackage.Position;
import com.esiea.tp4A.mypackage.Rover;

public class DataProvider implements InterfaceWeb {
	private Set<Planet> planets;
	final String[] planetNames = {"Mercure", "V�nus", "Terre", "Mars", "Jupiter", "Saturne", "Uranus", "Neptune"};

	public DataProvider() throws Exception {
		planets = new HashSet<>();
		createNewPlanet();		
	}

	public synchronized Planet getFreePlanet() throws Exception {
		for(Planet planet : planets) {
			if(planet.getPlayers().size()==50) {
				return createNewPlanet();
			} else {
				return planet;
			}
		}
		throw new Exception("No planets on this server.");
	}

	private synchronized Planet createNewPlanet() throws Exception {		
		if(planets.isEmpty() || planets.size() < 9) {
			Planet newPlanet = new Planet(planetNames[planets.size()]);
			planets.add(newPlanet);
			return newPlanet;
		} else {
			throw new Exception("Maximum capacity reached.");			
		}
	}

	@Override
	public Position getPlayerPosition(Rover rover) {
		return rover.getPosition();
	}

	@Override
	public Set<Position> getObstaclesPositions(Rover rover) {
		Set<Position> closeObstaclesPositions = new HashSet<Position>();
		rover.getPlanetIn().obstaclePositions().forEach((obstaclePos) -> {
			if(isCloseToPlayer(rover, obstaclePos))
				closeObstaclesPositions.add(obstaclePos);
		});			
		return closeObstaclesPositions;
	}

	@Override
	public Set<Rover> getOtherPlayersPositions(Rover rover) {
		Set<Rover> otherPlayersPositions = new HashSet<Rover>();
		for(String otherRoverName : rover.getPlanetIn().getPlayers().keySet()) {
			if(otherRoverName == null)
				continue;
			final Rover otherPlayerRover = rover.getPlanetIn().getPlayers().get(otherRoverName);
			final Position otherPlayerPosition = otherPlayerRover.getPosition();
			if(isCloseToPlayer(rover, otherPlayerPosition)) {
				otherPlayersPositions.add(otherPlayerRover);
			}
		}
		return otherPlayersPositions;
	}

	private boolean isCloseToPlayer(Rover rover, Position positionToCheck) {
		final int rX = rover.getPosition().getX();
		final int rY = rover.getPosition().getY();
		final int posX = positionToCheck.getX();
		final int posY = positionToCheck.getY();
		if((rX + 15 > posX && rY + 15 > posY) && (rX - 15 < posX && rY - 15 < posY))
			return true;
		else		
			return false;
	}

	@Override
	public int getLaserRange(Rover rover) {
		return rover.getPlanetIn().LASER_RANGE;
	}

	@Override
	public void move(String command, Rover rover) {
		rover.move(command);
		rover.getPlanetIn().updatePlayerPosition(rover);
	}

	@Override
	public void shoot(Rover rover) {
		move("e", rover);
	}

	@Override
	public boolean isPlayerAlive(Rover rover) {
		return rover.isAlive();
	}

	public synchronized Rover newPlayer(String name) throws Exception {
		Set<String> keys = getFreePlanet().getPlayers().keySet();
		if(keys != null) {			
			for(String key : keys) {
				if(key == null)
					continue;
				if(key.equals(name))
					throw new Exception("409 : Player already exists.");
			}
		}
		Rover rover = new Rover(getFreePlanet(), name);
		rover.getPlanetIn().updatePlayerPosition(rover);
		return rover;
	}

	JsonObject getJson(Rover rover) {
		JsonObject rootObject = new JsonObject();

		JsonObject player = new JsonObject();
		JsonObject position = new JsonObject();

		JsonObject localMap = new JsonObject();
		JsonArray obstacles = new JsonArray();
		JsonArray otherPlayers = new JsonArray();

		position.addProperty("x", rover.getPosition().getX());
		position.addProperty("y", rover.getPosition().getY());
		position.addProperty("direction", rover.getPosition().getDirection().toString());

		player.addProperty("name", rover.getName());
		player.addProperty("status", isPlayerAlive(rover));
		player.add("position", position);

		rootObject.add("player", player);
		rootObject.addProperty("laser-range", rover.getPlanetIn().LASER_RANGE);

		getObstaclesPositions(rover).forEach((ObstaclePosition) -> {
			JsonObject obstacle = new JsonObject();
			obstacle.addProperty("x", ObstaclePosition.getX());
			obstacle.addProperty("y", ObstaclePosition.getY());

			obstacles.add(obstacle);						
		});

		getOtherPlayersPositions(rover).forEach((otherPlayerRover) -> {
			JsonObject playerToList = new JsonObject();
			playerToList.addProperty("name", otherPlayerRover.getName());
			playerToList.addProperty("x", otherPlayerRover.getPosition().getX());
			playerToList.addProperty("y", otherPlayerRover.getPosition().getY());

			otherPlayers.add(playerToList);
		});		

		localMap.add("obstacles", obstacles);		
		localMap.add("players", otherPlayers);
		rootObject.add("local-map", localMap);

		return rootObject;

	}

}
