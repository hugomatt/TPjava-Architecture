package com.esiea.tp4A.mypackage;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Planet implements PlanetMap {
	public final int LASER_RANGE;
	private int planetSize;
	private Set<Position> obstaclePositions;

	public Planet(int planetSize) {
		//On calcule une portee aleatoire du laser
		Random random = new Random();
		LASER_RANGE = random.nextInt(planetSize/2); //la portee ne doit pas etre trop longue, il ne faut pas que le laser fasse le tour du monde.
		this.setPlanetSize(planetSize);
		obstaclePositions = new HashSet<Position>();
		this.generateObstacles();		
	}

	private void generateObstacles() {
		final int obstaclesToGenerate = (int)(0.15 * planetSize * planetSize); //15% d'obstacles		
		for(int i=0; i < obstaclesToGenerate; i++) {			
			Position positionToCheck = null;
			boolean isCollidingWithAnotherObstacle = true;
			while(isCollidingWithAnotherObstacle) {
				Random random = new Random();
				//calcul d'une position
				final int x = random.nextInt(planetSize) - planetSize / 2 + 1;
				final int y = random.nextInt(planetSize) - planetSize / 2 + 1;
				positionToCheck = Position.of(x, y, Direction.NORTH);

				isCollidingWithAnotherObstacle = false;
				/*if(this.obstaclePositions.contains(positionToCheck)) {
					System.out.println("obColision");
					isCollidingWithAnotherObstacle = true;
				}*/
					
				for(Position obstaclePos : this.obstaclePositions()) { //apres, il suffit de verifier dans la liste s'il y a un obstacle avec la meme position
					if(positionToCheck.getX() == obstaclePos.getX() && positionToCheck.getY() == obstaclePos.getY()) { //Si la position aleatoire est la meme que celle d'un obstacle de la liste
						//System.out.println("obColision");
						//System.out.println(obstaclePos == positionToCheck);
						isCollidingWithAnotherObstacle = true;
						break; //on s'arrete de verifier la position des obstacles, il faut creer une nouvelle position TODO use contains, this is too slow
					}
				}
			}
			obstaclePositions.add(positionToCheck); //Si l'obstacle n'est pas en collision avec un autre obstacle
		}
		System.out.println(this.obstaclePositions().size() + " Obstacles generes sur " + obstaclesToGenerate);
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
