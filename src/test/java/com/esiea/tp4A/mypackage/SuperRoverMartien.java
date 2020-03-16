package com.esiea.tp4A.mypackage;

public class SuperRoverMartien implements MarsRover {
	Position position;
	Planet planetIn;
	public static int laserRange;

	public SuperRoverMartien(Position position, Planet planetIn) {
		initialize(position);
		this.planetIn = planetIn;		
	}

	@Override
	public MarsRover initialize(Position position) {
		this.position = Position.of(position.getX(), position.getY(), position.getDirection());
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
				if(!moveForward(this.position.getDirection(), false))
					return this.position = previousPosition;
			} else if(command.charAt(i) == 's') {
				if(!moveForward(this.position.getDirection(), true))
					return this.position = previousPosition;
			} else if(command.charAt(i) == 'e') { //tir de laser
				//faire tirer le laser ?
			}
			//System.out.println("====");
			//System.out.println("X : " + this.position.getX() + "Y : " + this.position.getY() + "Direction : " + this.position.getDirection() + "\n ======");

		}
		return this.position;
	}

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
		return isObstacleOnTheWay();
	}
	
	private boolean isObstacleOnTheWay() {
		for(Position obstaclePos : this.planetIn.obstaclePositions()) {
			if(obstaclePos.getX() == this.position.getX() && obstaclePos.getY() == this.position.getY())
				return false;
		}
		return true;
	}

	private int calculateX(int newXPos) {
		int highestPlanetPos = this.planetIn.getPlanetSize() / 2;
		int lowestPlanetPos = this.planetIn.getPlanetSize() / -2 + 1;
		if(highestPlanetPos < newXPos) {
			newXPos = lowestPlanetPos;
		}
		if(lowestPlanetPos > newXPos) {
			newXPos = highestPlanetPos;
		}
		return newXPos;
	}

	private int calculateY(int newYPos) {
		int highestPlanetPos = this.planetIn.getPlanetSize() / 2;
		int lowestPlanetPos = this.planetIn.getPlanetSize() / -2 + 1;
		if(highestPlanetPos < newYPos) {
			newYPos = lowestPlanetPos;
		}
		if(lowestPlanetPos > newYPos) {
			newYPos = highestPlanetPos;
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

	
		String str = "zzz";		
		int x = 0;
		int y = 0;
		Direction direction = Direction.NORTH;

		Position pos = Position.of(x, y, direction);
		Planet mars = new Planet(100);		
		SuperRoverMartien rover = new SuperRoverMartien(pos, mars);
		SuperRoverMartien.laserRange = 30;		
		rover.move(str);
		System.out.println("X:"+rover.position.getX() +" Y:"+ rover.position.getY() + "\nDirection: " + rover.position.getDirection());
	}
}
