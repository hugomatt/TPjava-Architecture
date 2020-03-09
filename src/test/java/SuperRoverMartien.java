import com.esiea.tp4A.mypackage.*;
import java.util.Scanner;

public class SuperRoverMartien implements MarsRover {
	Position position;

	public SuperRoverMartien(Position position) {
		initialize(position);
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
		for(int i = 0; i < command.length(); i++) {
			if(command.charAt(i) == 'q') {
				rotateLeft(this.position.getDirection());							
			} else if(command.charAt(i) == 'd') {
				rotateRight(this.position.getDirection());			
			} else if(command.charAt(i) == 'z') {
				moveForward(this.position.getDirection(), false);
			} else if(command.charAt(i) == 's') {
				moveForward(this.position.getDirection(), true);
			}
			//System.out.println("====");
			//System.out.println("X : " + this.position.getX() + "Y : " + this.position.getY() + "Direction : " + this.position.getDirection() + "\n ======");

		}
		return this.position;
	}

	private void moveForward(Direction direction, Boolean reverse) { //true pour reculer, false pour avancer
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
	}

	private int calculateX(int newXPos) {
		return newXPos;
	}

	private int calculateY(int newYPos) {
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

		/*Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir un mot :");
		String str = sc.nextLine();
		System.out.println("Vous avez saisi : " + str);
		sc.close();*/
		
		String str = "zzqs";
		
		int x = 0;
		int y = 0;
		Direction direction = Direction.NORTH;

		Position pos = Position.of(x, y, direction);
		SuperRoverMartien rover = new SuperRoverMartien(pos);
		rover.move(str);
		System.out.println("X: "+rover.position.getX()+"|"+" Y:"+rover.position.getY() +"| Direction:"+rover.position.getDirection());
	}

}
