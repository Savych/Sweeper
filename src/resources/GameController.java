package resources;

public class GameController {

    private Bomb bomb;
    private Flag flag;
    private GameState state;

    public int getCountOfFlags() {
        return flag.getCountFlags();
    }

    public GameState getState() {
        return state;
    }

    public GameController(int colums, int rows, int countBombs) {
        Ranges.setSize(new Coord(colums, rows));
        bomb = new Bomb(countBombs);
        flag = new Flag();
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
    }

    public Box getBox(Coord coord) {
        if (flag.get(coord) == Box.OPENED)
            return bomb.getBox(coord);
        else
            return flag.get(coord);
    }

    public void pressLB(Coord coord) {
        if (gameOver()) return;
        openBox(coord);
        checkWinner();
    }

    private void checkWinner() {
        if (state == GameState.PLAYED)
            if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs())
                state = GameState.WINNER;
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED: setOpenedToClosedBoxesAroundNumber(coord);
                         return;
            case FLAGED: return;
            case CLOSED:
                switch (bomb.getBox(coord)) {
                    case ZERO: openBoxesAround(coord);
                               return;
                    case BOMB: openBombs(coord);
                               return;
                    default  : flag.setOpenedToBox(coord);
                               return;
                }
        }
    }

    private void openBombs(Coord bombed) {
        state = GameState.BOMBED;
        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCoords()) {
            if (bomb.getBox(coord) == Box.BOMB)
                flag.setOpenedToCloseBombBox(coord);
            else
                flag.setNoBombToFlagedSafeBox(coord);
        }
    }

    private void openBoxesAround(Coord coord) {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordsAround(coord))
            openBox(around);
    }

    public void pressRB(Coord coord) {
        if (gameOver()) return;
        flag.toggleFlagedToBox(coord);
    }

    private boolean gameOver() {
        if (state == GameState.PLAYED)
            return false;
        return true;
    }

    private void setOpenedToClosedBoxesAroundNumber(Coord coord) {
        if (bomb.getBox(coord) != Box.BOMB)
            if (flag.getCountOfFlagedBoxesAround(coord) == bomb.getBox(coord).getNumber())
                for (Coord around : Ranges.getCoordsAround(coord))
                    if (flag.get(around) == Box.CLOSED)
                        openBox(around);
    }
}
