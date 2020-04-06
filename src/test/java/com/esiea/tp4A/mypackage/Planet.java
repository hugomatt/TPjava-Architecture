package com.esiea.tp4A.mypackage;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Planet implements PlanetMap {
	public final int LASER_RANGE;
	private int planetSize;
	private Set<Position> obstaclePositions;

	public Planet(int planetSize) {
		//On calcule une port�e al�atoire du laser
		Random random = new Random();
		LASER_RANGE = random.nextInt(planetSize/2);
		this.setPlanetSize(planetSize);
		obstaclePositions = new HashSet<Position>();
		this.generateObstacles();		
	}

	private void generateObstacles() {
		final int obstaclesToGenerate = (int)(0.15 * planetSize * planetSize); //99% d'obstacles		
		for(int i=0; i < obstaclesToGenerate; i++) {			
			Position positionToCheck = null;
			boolean isCollidingWithAnotherObstacle = true;
			while(isCollidingWithAnotherObstacle) {
				Random random = new Random();
				//calcul d'une position (sauf la direction, on s'en branle un peu)
				final int x = random.nextInt(planetSize) - planetSize / 2 + 1;
				final int y = random.nextInt(planetSize) - planetSize / 2 + 1;
				positionToCheck = Position.of(x, y, Direction.NORTH);

				isCollidingWithAnotherObstacle = false;
				/*if(this.obstaclePositions.contains(positionToCheck)) {
					System.out.println("obColision");
					isCollidingWithAnotherObstacle = true;
				}*/
					
				for(Position obstaclePos : this.obstaclePositions()) { //apr�s, il suffit de v�rifier dans la liste s'il y a un obstacle avec la m�me position
					if(positionToCheck.getX() == obstaclePos.getX() && positionToCheck.getY() == obstaclePos.getY()) { //Si la position al�atoire est la m�me que celle d'un obstacle de la liste
						//System.out.println("obColision");
						//System.out.println(obstaclePos == positionToCheck);
						isCollidingWithAnotherObstacle = true;
						break; //on s'arr�te de v�rifier la position des obstacles, il faut cr�er une nouvelle position TODO use contains, this is too slow
					}
				}
			}
			obstaclePositions.add(positionToCheck); //Si l'obstacle n'est pas en collision avec un autre obstacle
		}
		System.out.println(this.obstaclePositions().size() + " Obstacles g�n�r�s, de " + obstaclesToGenerate);
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

	public void showRovers() {
		System.out.println();
	}

}
