package MySnakeGame;

import com.javarush.engine.cell.*;

public class Apple extends GameObject {

    //фигура яблока
    private static final String APPLE_SIGN = "\uD83C\uDF4E";
    //флаг объекта: жив(true) или нет(false)
    boolean isAlive = true;

    /**
     * Конструктор класса Apple вызывает конструктор
     * родительского класса через метод super()
     * @param x - координата x
     * @param y - координата y
     */
    public Apple(int x, int y) {
        super(x, y);
    }

    /**
     * Метод отрисовывает "яблоко" на
     * игровом поле
     * @param game
     */
    public void draw(Game game) {
        game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.YELLOW, 75);
    }
}
















