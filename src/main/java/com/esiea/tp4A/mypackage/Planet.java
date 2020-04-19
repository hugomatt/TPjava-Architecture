package com.esiea.tp4A.mypackage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Planet implements PlanetMap {
	public final int LASER_RANGE;
	final int planetSize;
	final String name;
	Set<Position> obstaclePositions;	
	Map<String, Rover> players;

	public Planet(String name) {
		this.players = new HashMap<String, Rover>(0);
		this.name = name;
		//calcul al�atoire de la taille de la plan�te
		Random random = new Random();
		final int i = random.nextInt(3);
		final int randomPlanetSize;
		if(i == 0)
			randomPlanetSize = 100;
		else if(i == 1)
			randomPlanetSize = 300;			
		else
			randomPlanetSize = 600;
		//On calcule une port�e al�atoire du laser, qui ne doit pas faire le tour de la carte
		LASER_RANGE = random.nextInt(randomPlanetSize/2);
		this.planetSize = randomPlanetSize;
		obstaclePositions = new HashSet<Position>();
		this.generateObstacles();
		System.out.println("Planet " + this.name + " created.");
	}

	private void generateObstacles() {
		final int obstaclesToGenerate = (int)(0.15 * planetSize * planetSize); //15% d'obstacles		
		for(int i=0; i < obstaclesToGenerate; i++) {			
			Position positionToCheck = null;
			boolean isCollidingWithAnotherObstacle = true;
			while(isCollidingWithAnotherObstacle) {
				Random random = new Random();
				//calcul d'une position al�atoire pour un obstacle, la direction importe peu
				final int x = random.nextInt(planetSize) - planetSize / 2 + 1;
				final int y = random.nextInt(planetSize) - planetSize / 2 + 1;
				positionToCheck = Position.of(x, y, Direction.NORTH);

				isCollidingWithAnotherObstacle = false;				
					
				for(Position obstaclePos : this.obstaclePositions()) { //apr�s, il suffit de v�rifier dans la liste s'il y a un obstacle avec la m�me position
					if(positionToCheck.getX() == obstaclePos.getX() && positionToCheck.getY() == obstaclePos.getY()) { //Si la position al�atoire est la m�me que celle d'un obstacle de la liste
						
						isCollidingWithAnotherObstacle = true;
						break; //on s'arr�te de v�rifier la position des obstacles, il faut cr�er une nouvelle position
					}
				}
			}
			obstaclePositions.add(positionToCheck); //Si l'obstacle n'est pas en collision avec un autre obstacle
		}
		System.out.println(this.obstaclePositions().size() + " obstacles generated of " + obstaclesToGenerate);
	}

	@Override
	public Set<Position> obstaclePositions() {
		return this.obstaclePositions;
	}

	public int getPlanetSize() {
		return planetSize;
	}
	
	public void updatePlayerPosition(Rover rover) {
		players.put(rover.getName(), rover);
	}
	
	public Map<String, Rover> getPlayers() {
		return players;
	}

}
