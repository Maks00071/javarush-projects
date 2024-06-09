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
     * Сеттер смены направления движения змейки. Если актуальное
     * направление движения змейки и требуемое от пользователя, противоположно
     * направленные (= 180 градусов), то направление изменено не будет
     *
     * @param direction - направление движения змейки
     */
    public void setDirection(Direction direction) {

        if ((this.direction == direction.LEFT
                || this.direction == direction.RIGHT)
                && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;
        }

        if ((this.direction == direction.UP
                || this.direction == direction.DOWN)
                && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }

        if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return;
        } else if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        } else if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        } else if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }
        this.direction = direction;
    }

    /**
     * Метод проверяет границы движения змейки: если вышли за
     * границу игрового поля, то проиграл, иначе продолжаем
     * движение змейки
     */
    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        int headX = newHead.x;
        int headY = newHead.y;
        if ((headX >= SnakeGame.WIDTH
                || headX < 0)
                || (headY >= SnakeGame.HEIGHT
                || headY < 0)) {
            isAlive = false;
            return;
        }
        //если змейка пересекает себя, то игра останавливается
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }
        snakeParts.add(0, newHead);
        //"змейка" и "яблоко" встречаются
        if (apple.x == headX && apple.y == headY) {
            //"яблоко" съедено
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    /**
     * Метод реализует создание "головы" змейки (отрисовка в нужной ячейке),
     * в зависимости от требуемого направления движения змейки
     *
     * @return - новый объект GameObject
     */
    public GameObject createNewHead() {
        //записываем в переменную объект GameObject, где в
        // настоящий ммомент находится "голова" змейки
        GameObject oldHead = snakeParts.get(0);

        //реализуем движение змейки, в зависимости от направления
        if (direction == direction.LEFT) {
            return new GameObject(oldHead.x - 1, oldHead.y);
        } else if (direction == direction.RIGHT) {
            return new GameObject(oldHead.x + 1, oldHead.y);
        } else if (direction == direction.UP) {
            return new GameObject(oldHead.x, oldHead.y - 1);
        } else {
            return new GameObject(oldHead.x, oldHead.y + 1);
        }
    }

    /**
     * Метод удаляет "хвост" змейки (последний объект GameObject - ячейку)
     */
    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    /**
     * Метод проверяет пересекает ли "голова" объекта "змейка"
     * свое "тело"
     *
     * @param gameObject - проверяемая ячейка
     * @return - есть ли пересечение или нет
     */
    public boolean checkCollision(GameObject gameObject) {
        for (GameObject part : snakeParts) {
            if (part.x == gameObject.x && part.y == gameObject.y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод определяет и возвращает количество
     * элементов (ячеек) в теле змейки
     * @return - длина змейки (кол-во ячеек)
     */
    public int getLength() {
        return snakeParts.size();
    }
}


















