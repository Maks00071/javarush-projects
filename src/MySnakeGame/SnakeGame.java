package MySnakeGame;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {

    //габариты игрового поля
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    //создаем объект "змейка"
    private Snake snake;

    /**
     * Переопределенный метод родительского класса,
     * который выполняет однократные действия при запуске игры
     */
    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    /**
     * Метод выполняет требования по
     * созданию игры
     */
    private void createGame() {
        //создаем новую змейку
        snake = new Snake(WIDTH/2, HEIGHT/2);
        //отрисовка игрового поля
        drawScene();
    }

    /**
     * Метод отрисовывает ячейки игрового поля
     * в заданный цвет и отрисовывает змейку
     */
    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellColor(x, y, Color.DARKSEAGREEN);
            }
        }
        snake.draw(this);
    }
}




















