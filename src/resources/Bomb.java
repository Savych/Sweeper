package resources;

class Bomb {

    private Matrix bombMap;
    private int totalBombs;

    Bomb (int totalBombs) {
        this.totalBombs = totalBombs;
        fixBombsCount();
    }

    void start() {
        bombMap = new Matrix(Box.ZERO);
        for (int i = 0; i < totalBombs; i++)
            placeBomb();
    }

    Box getBox(Coord coord) {
        return bombMap.getBox(coord);
    }

    private void fixBombsCount() {
        int maxBombs = Ranges.getSize().x * Ranges.getSize().y / 2;
        if (totalBombs > maxBombs)
            totalBombs = maxBombs;
    }

    private void placeBomb() {
        while (true) {
            Coord coord = Ranges.getRandomCoord();
            if (Box.BOMB == bombMap.getBox(coord))
                continue;
            bombMap.set(coord, Box.BOMB);
            incNumbersAroundBomb(coord);
            break;
        }

    }

    private void incNumbersAroundBomb(Coord coord) {
        for (Coord coord1 : Ranges.getCoordsAround(coord))
            if (Box.BOMB != bombMap.getBox(coord1))
                bombMap.set(coord1, bombMap.getBox(coord1).getNextNumberBox());
    }

    int getTotalBombs() {
        return totalBombs;
    }
}
