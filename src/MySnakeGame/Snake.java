package MySnakeGame;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    //отрисовка головы змейки
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    //отрисовка тела змейки
    private static final String BODY_SIGN = "\u26AB";
    //массив ячеек нашей змейки
    private List<GameObject> snakeParts = new ArrayList<>();
    //флаг объекта: жив(true) или нет(false)
    boolean isAlive = true;
    //изначально змейка движется влево
    private Direction direction = Direction.LEFT;

    /**
     * Конструктор создает три объекта GameObject в массиве snakeParts
     * (змейку)
     *
     * @param x - координата x
     * @param y - координата y
     */
    public Snake(int x, int y) {
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    /**
     * Метод отрисовывает змейку на игровом поле
     *
     * @param game - объект GameObject (ячейка поля)
     */
    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            //получаем в переменную part объект GameObject по индексу
            GameObject part = snakeParts.get(i);
            //в зависимости от индекса присваиваем или голову, или тело
            String sing = (i != 0) ? BODY_SIGN : HEAD_SIGN;
            //если змейка "жива" то один цвет, иначе другой
            Color color = isAlive ? Color.BLUE : Color.RED;
            //отрисовываем ячейку змейки на поле
            game.setCellValueEx(part.x, part.y, Color.NONE, sing, color, 75);
        }
    }

    /**
     * Сеттер смены направления движения змейки
     * @param direction - направление движения змейки
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}


















