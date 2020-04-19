package com.esiea.tp4A.mypackage;

import java.util.Random;

public class Rover implements MarsRover {
	Position position;
	Planet planetIn;
	boolean isAlive;
	String name;

	public Position getPosition() {
		return position;
	}	

	public Planet getPlanetIn() {
		return planetIn;
	}	

	public Rover(Planet planetIn, String name) {
		this.planetIn = planetIn;
		generateRoverPosition(planetIn);
		initialize(this.position);
		this.name = name;
	}

	private Position generateRoverPosition(Planet planetIn) {
		Random random = new Random();
		do {
			//calcul d'une position al�atoire
			final int x = random.nextInt(planetIn.getPlanetSize()) - planetIn.getPlanetSize() / 2 + 1;
			final int y = random.nextInt(planetIn.getPlanetSize()) - planetIn.getPlanetSize() / 2 + 1;
			this.position = Position.of(x, y, Direction.NORTH);
		}
		while(!isRouteFree());
		return this.position;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void die() {
		planetIn.getPlayers().remove(this.getName(), this);
		isAlive = false;
	}

	public String getName() {
		return name;
	}

	@Override
	public MarsRover initialize(Position position) {
		planetIn.updatePlayerPosition(this);
		isAlive = true;
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
		Position previousPosition = this.position;
		for(int i = 0; i < command.length(); i++) {
			if(command.charAt(i) == 'q') {
				rotateLeft(this.position.getDirection());							
			} else if(command.charAt(i) == 'd') {
				rotateRight(this.position.getDirection());			
			} else if(command.charAt(i) == 'z') {				
				if(!moveForward(this.position.getDirection(), false)) {					
					return this.position = previousPosition;
				}
			} else if(command.charAt(i) == 's') {
				if(!moveForward(this.position.getDirection(), true))
					return this.position = previousPosition;
			} else if(command.charAt(i) == 'e') {				
				shoot(this.position.getDirection());				
			}
		}		
		return this.position;
	}

	private void shoot(Direction direction) {
		Position laserPosition = this.position;
		for(int i=1; i <= planetIn.LASER_RANGE; i++) {

			for(String potentialTarget : this.planetIn.getPlayers().keySet()) { //D�tection du joueur �ventuel � d�truire
				if(potentialTarget==null)
					continue;
				Position potentialTargetPos = this.planetIn.getPlayers().get(potentialTarget).getPosition();
				if(direction == Direction.NORTH) {
					laserPosition = Position.of(this.position.getX(), calculateY(this.position.getY() + i), Direction.NORTH);
					if(laserPosition.getX() == potentialTargetPos.getX() && laserPosition.getY() == potentialTargetPos.getY()) {
						killPlayer(potentialTarget);
						return;
					}
				}
				if(direction == Direction.WEST) {
					laserPosition = Position.of(calculateX(this.position.getX() - i), this.position.getY(), Direction.NORTH);
					if(laserPosition.getX() == potentialTargetPos.getX() && laserPosition.getY() == potentialTargetPos.getY()) {
						killPlayer(potentialTarget);
						return;
					}		
				}
				if(direction == Direction.SOUTH) {
					laserPosition = Position.of(this.position.getX(), calculateY(this.position.getY() - i), Direction.NORTH);
					if(laserPosition.getX() == potentialTargetPos.getX() && laserPosition.getY() == potentialTargetPos.getY()) {
						killPlayer(potentialTarget);
						return;
					}
				}	
				if(direction == Direction.EAST) {
					laserPosition = Position.of(calculateX(this.position.getX() + i), this.position.getY(), Direction.NORTH); 
					if(laserPosition.getX() == potentialTargetPos.getX() && laserPosition.getY() == potentialTargetPos.getY()) {
						killPlayer(potentialTarget);
						return;
					}
				}
			}

			for(Position obstaclePos : this.planetIn.obstaclePositions()) { //D�tection de l'obstacle �ventuel � d�truire
				if(direction == Direction.NORTH) {
					laserPosition = Position.of(this.position.getX(), calculateY(this.position.getY() + i), Direction.NORTH);
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						destroyObstacle(obstaclePos); 
						return;
					}
				}
				if(direction == Direction.WEST) {
					laserPosition = Position.of(calculateX(this.position.getX() - i), this.position.getY(), Direction.NORTH);
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						destroyObstacle(obstaclePos); 
						return;
					}		
				}
				if(direction == Direction.SOUTH) {
					laserPosition = Position.of(this.position.getX(), calculateY(this.position.getY() - i), Direction.NORTH);
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						destroyObstacle(obstaclePos); 
						return;
					}
				}	
				if(direction == Direction.EAST) {
					laserPosition = Position.of(calculateX(this.position.getX() + i), this.position.getY(), Direction.NORTH); 
					if(laserPosition.getX() == obstaclePos.getX() && laserPosition.getY() == obstaclePos.getY()) {
						destroyObstacle(obstaclePos); 
						return;
					}
				}
			}			
		}
	}

	private void destroyObstacle(Position obstaclePos) {
		planetIn.obstaclePositions().remove(obstaclePos);		
	}

	private void killPlayer(String targetName) {
		this.planetIn.getPlayers().get(targetName).die();
	}

	/**
	 * Permet de faire avancer ou reculer le rover tout en v�rifiant les obstacles �ventuels.
	 * 
	 * @param direction		la direction du rover
	 * @param reverse		true pour reculer, false pour avancer
	 * @return				retourne true si le rover a pu avancer, sinon false
	 */
	private boolean moveForward(Direction direction, Boolean reverse) {
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
		return isRouteFree();
	}

	public boolean isRouteFree() {
		for(Position obstaclePos : this.planetIn.obstaclePositions()) {
			if(obstaclePos.getX() == this.position.getX() && obstaclePos.getY() == this.position.getY()) {
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

}
