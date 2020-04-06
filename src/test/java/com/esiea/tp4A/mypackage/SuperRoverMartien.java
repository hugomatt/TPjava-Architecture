package com.esiea.tp4A.mypackage;
import java.util.Random;

public class SuperRoverMartien implements MarsRover {
	Position position;
	Planet planetIn;

	public SuperRoverMartien(Planet planetIn) {
		this.planetIn = planetIn;
		generateRoverPosition(planetIn);
		initialize(this.position);		
	}

	private Position generateRoverPosition(Planet planetIn) {
		Random random = new Random();
		int tries=0; //Tu peux enlever, c'est juste pour info
		do {
			//calcul d'une position (sauf la direction, on s'en branle un peu)
			final int x = random.nextInt(planetIn.getPlanetSize()) - planetIn.getPlanetSize() / 2 + 1;
			final int y = random.nextInt(planetIn.getPlanetSize()) - planetIn.getPlanetSize() / 2 + 1;
			this.position = Position.of(x, y, Direction.NORTH);
			tries++;
		}
		while(!isRouteFree());
		System.out.println(tries + " spawn tries for this rover. (nombre de tentative pour placer le rover sur la carte)"); //Tu peux enlever aussi
		return this.position;
	}

	@Override
	public MarsRover initialize(Position position) {
		return this;
	}

	@Override
	public MarsRover updateMap(PlanetMap map) {
		return this;
	}

	@Override
	public MarsRover configureLaserRange(int range) {
		return this;
	}

	@Override
	public Position move(String command) {
		System.out.println("Position de d�part X : " + this.position.getX() + " Y : " + this.position.getY() + " Direction : " + this.position.getDirection() + "\n ====");
		Position previousPosition = this.position;
		for(int i = 0; i < command.length(); i++) {
			if(command.charAt(i) == 'q') {
				rotateLeft(this.position.getDirection());							
			} else if(command.charAt(i) == 'd') {
				rotateRight(this.position.getDirection());			
			} else if(command.charAt(i) == 'z') {
				System.out.println("Position X : " + this.position.getX() + " Y : " + this.position.getY() + " Direction : " + this.position.getDirection() + "\n ====");
				if(!moveForward(this.position.getDirection(), false)) {
					System.out.println("rover bloqu�"); 
					return this.position = previousPosition;
				}
			} else if(command.charAt(i) == 's') {
				if(!moveForward(this.position.getDirection(), true))
					return this.position = previousPosition;
			} else if(command.charAt(i) == 'e') {				
				shoot(this.position.getDirection());				
			}
		}
		System.out.println(" ====");
		System.out.println("Position d'arriv�e X : " + this.position.getX() + " Y : " + this.position.getY() + " Direction : " + this.position.getDirection() + "\n ====");
		return this.position;
	}

	private void shoot(Direction direction) {
		Position laserPosition = this.position;
		for(int i=1; i <= planetIn.LASER_RANGE; i++) {
			for(Position obstaclePos : this.planetIn.obstaclePositions()) {
				if(direction == Direction.NORTH) {
					laserPosition = Position.of(this.position.getX(), calculateY(this.position.getY() + i), Direction.NORTH);
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						planetIn.obstaclePositions().remove(obstaclePos); System.out.println(planetIn.obstaclePositions().size()+" - Obstacle touch�"); return;//TODO afficher la position de l'obstacle d�truit pour �tre s�r
					}
				}
				if(direction == Direction.WEST) {
					laserPosition = Position.of(calculateX(this.position.getX() - i), this.position.getY(), Direction.NORTH);
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						planetIn.obstaclePositions().remove(obstaclePos); System.out.println("Obstacle touch�"); return;
					}		
				}
				if(direction == Direction.SOUTH) {
					laserPosition = Position.of(this.position.getX(), calculateY(this.position.getY() - i), Direction.NORTH);
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						planetIn.obstaclePositions().remove(obstaclePos); System.out.println("Obstacle touch�"); return;
					}
				}	
				if(direction == Direction.EAST) {
					laserPosition = Position.of(calculateX(this.position.getX() + i), this.position.getY(), Direction.NORTH); 
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						planetIn.obstaclePositions().remove(obstaclePos); System.out.println("Obstacle touch�"); return;
					}
				}
			}
		}
	}

	/**
	 * Permet de faire avancer ou reculer le rover tout en v�rifiant les obstacles �ventuels.
	 * 
	 * @param direction		la direction du rover
	 * @param reverse		true pour reculer, false pour avancer
	 * @return				retourne true si le rover a pu avancer, sinon false
	 */
	private boolean moveForward(Direction direction, Boolean reverse) { //true pour reculer, false pour avancer
		int flag = 1;
		if(reverse)
			flag = -1;		
		if(direction == Direction.NORTH)
			this.position = Position.of(this.position.getX(), calculateY(this.position.getY() + 1 * flag), Direction.NORTH);
		if(direction == Direction.WEST)
			this.position = Position.of(calculateX(this.position.getX() - 1 * flag), this.position.getY(), Direction.WEST);
		if(direction == Direction.SOUTH)
			this.position = Position.of(this.position.getX(), calculateY(this.position.getY() - 1 * flag), Direction.SOUTH);		
		if(direction == Direction.EAST)
			this.position = Position.of(calculateX(this.position.getX() + 1 * flag), this.position.getY(), Direction.EAST);	
		System.out.println("tryingtomoveforward");
		return isRouteFree();
	}

	public boolean isRouteFree() {
		for(Position obstaclePos : this.planetIn.obstaclePositions()) {
			if(obstaclePos.getX() == this.position.getX() && obstaclePos.getY() == this.position.getY()) {
				System.out.println(obstaclePos.getX() +"  Y:" +obstaclePos.getY());
				return false;	
			}
		}
		return true;
	}

	private int calculateX(int newXPos) {
		int highestPlanetPos = this.planetIn.getPlanetSize() / 2;
		int lowestPlanetPos = this.planetIn.getPlanetSize() / -2 + 1;
		if(highestPlanetPos < newXPos) {
			newXPos = lowestPlanetPos + newXPos - highestPlanetPos - 1;
		}
		if(lowestPlanetPos > newXPos) {
			newXPos = highestPlanetPos + newXPos - lowestPlanetPos + 1;  
		}
		return newXPos;
	}

	private int calculateY(int newYPos) {
		int highestPlanetPos = this.planetIn.getPlanetSize() / 2;
		int lowestPlanetPos = this.planetIn.getPlanetSize() / -2 + 1;
		if(highestPlanetPos < newYPos) {
			newYPos = lowestPlanetPos + newYPos - highestPlanetPos - 1;
		}
		if(lowestPlanetPos > newYPos) {
			newYPos = highestPlanetPos + newYPos - lowestPlanetPos + 1;
		}
		return newYPos;
	}

	public void rotateLeft(Direction direction) {
		if(direction == Direction.NORTH)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.WEST);
		if(direction == Direction.WEST)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.SOUTH);
		if(direction == Direction.SOUTH)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.EAST);		
		if(direction == Direction.EAST)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.NORTH);
	}

	public void rotateRight(Direction direction) {
		if(direction == Direction.NORTH)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.EAST);
		if(direction == Direction.EAST)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.SOUTH);
		if(direction == Direction.SOUTH)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.WEST);
		if(direction == Direction.WEST)
			this.position = Position.of(this.position.getX(), this.position.getY(), Direction.NORTH);
	}

	public static void main (String[] args) {		
		Random random = new Random();		

		//calcul al�atoire de la taille de la plan�te
		final int i = random.nextInt(3);
		final int planetSize;
		if(i == 0)
			planetSize = 100;
		else if(i == 1)
			planetSize = 300;			
		else
			planetSize = 600;
System.out.println("la taille de la map est de "+ planetSize + "*" +planetSize);
		//commande pour le mouvement du rover, TODO ajouter une entr�e utilisateur
		String command = "eeeezzzz";		

		//On cr�e une plan�te avec une taille al�atoire
		Planet mars = new Planet(planetSize);

		//On cr�e un rover avec les param�tres d�finis juste au dessus (plan�te)
		SuperRoverMartien rover = new SuperRoverMartien(mars);		
		rover.move(command);

	}

}