package MySnakeGame;

public class GameObject {
    public int x;
    public int y;

    /**
     * Конструктор создает игровую ячейку
     * @param x - координата x
     * @param y - координата y
     */
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
