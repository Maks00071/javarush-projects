package MySnakeGame;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {

    //габариты игрового поля
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    //максимальное значение тела змейки
    private static final int GOAL = 28;
    //создаем объект "змейка"
    private Snake snake;
    //переменная определяет продолжительность хода (мс/ход)
    private int turnDelay;
    //объект "яблоко"
    private Apple apple;
    //флаг останова игры
    private boolean isGameStopped;
    //счетчик очков
    private int score;

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
     * Переопределим родительский метод, отвечающий за
     * считывание нажатий клавиш (лево/право/верх/низ)
     * @param key - ключ нажатия клавиши
     */
    @Override
    public void onKeyPress(Key key) {

        //перезапуск игры
        if (key == Key.SPACE && isGameStopped) {
            createGame();
        }

        if (key == Key.LEFT) {
            snake.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            snake.setDirection(Direction.RIGHT);
        } else if (key == Key.DOWN) {
            snake.setDirection(Direction.DOWN);
        } else {
            snake.setDirection(Direction.UP);
        }
    }

    /**
     * Метод выполняет требования по
     * созданию игры
     */
    private void createGame() {
        turnDelay = 300;
        setTurnTimer(turnDelay);
        //создаем новую змейку
        snake = new Snake(WIDTH/2, HEIGHT/2);
        //инициализируем объект "яблоко"
        createNewApple();
        isGameStopped = false;
        //отрисовка игрового поля
        drawScene();
        score = 0;
        setScore(score);
    }

    /**
     * Метод заканчивает игру с сообщением пользователю
     * о проигрыше
     */
    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "GAME OVER!", Color.BLACK, 50);
    }

    /**
     * Метод заканчивает игру с сообщением пользователю
     * о выигрыше
     */
    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "YOU WIN!", Color.YELLOW, 50);
    }

    /**
     * Метод отрисовывает ячейки игрового поля
     * в заданный цвет. Отрисовывает объект "змейку" и
     * объект "яблоко"
     */
    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        //отрисовываем объект "змейка"
        snake.draw(this);
        //отрисовываем объект "яблоко"
        apple.draw(this);
    }

    /**
     * Метод генерирует случайные координаты ячейки
     * в пределах игрового поля, на которой будет
     * появляться новый объект "яблоко" (Apple apple)
     */
    private void createNewApple() {

        do {
            //координаты "нового яблока"
            int appleX = getRandomNumber(WIDTH);
            int appleY = getRandomNumber(HEIGHT);
            apple = new Apple(appleX, appleY);
        }
        while (snake.checkCollision(apple));

        apple.isAlive = true;
    }

    /**
     * Переопределим родительский метод, который
     * отвечает за повороты объекта "змейка". Действия
     * при пересечении объектов "змейка" и "яблоко"
     * @param step
     */
    @Override
    public void onTurn(int step) {
        snake.move(apple);

        //если объект "яблоко" не существует, то создаем его
        if (!apple.isAlive) {
            //увеличиваем очки на 5 за съеденное яблоко
            score += 5;
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
            createNewApple();
        }
        //если "мертва" наша змейка => тогда конец игры
        if (!snake.isAlive) {
            gameOver();
        }
        //если длина змейки > контрольного значения => мы выиграли
        if (snake.getLength() > GOAL) {
            win();
        }
        drawScene();
    }
}




















