public interface MarsRover {

    default MarsRover initialize(Position position) {
        return this;
    }

    default MarsRover updateMap(PlanetMap map) {
        return this;
    }

    default MarsRover configureLaserRange(int range) {
        return this;
    }

    default Position move(String command) {
        return Position.of(0, 0, Direction.NORTH);
    }
}

public interface Position {
    
    int getX();
    int getY();
    Direction getDirection();
    
    static Position of(int x, int y, Direction direction) {
        return new FixedPosition(x, y, direction);
    }
    
    final class FixedPosition implements Position {

        private final int x;
        private final int y;
        private final Direction direction;

        public FixedPosition(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
        
        @Override
        public Direction getDirection() {
            return direction;
        }
    }
}
public interface PlanetMap {
    
    Set<Position> obstaclePositions();
}
public enum Direction {
    
    NORTH, EAST, SOUTH, WEST;
}
