package TheGameMinesweeper;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

/********Игра "Сапер"**************
 Напишем увлекательную игру "Сапер".

 Правила игры:
 1. Игровое поле разделено на смежные ячейки (квадраты), некоторые из которых «заминированы».
 2. Цель игры — разминировать поле, то есть открыть все ячейки без мин.
 3. Если открыта ячейка с миной, игра считается проигранной.
 Мины расставляются в случайном порядке.
 4. Если под открытой ячейкой мины нет, в ней появляется число, показывающее,
 сколько соседствующих с открытой ячейкой "заминировано". Используя эти числа,
 можно рассчитать расположение мин.
 5. Если под соседними ячейками тоже нет мин, открывается некоторая "незаминированная" область
 до ячеек, в которых есть цифры.
 6. "Заминированные" ячейки можно помечать флагом, чтобы случайно не открыть их.
 Количество флагов равно количеству мин на игровом поле.
 7. Если все "незаминированные" ячейки открыты, игра считается выигранной.

 Теперь приступаем к реализации игры:

 - Для начала создадим класс игры "MinesweeperGame", который будет реализовывать логику игры.
 Данный класс должен быть унаследован от класса "Game", который является частью
 крутого "игрового движка" JavaRush.
 - Этот и другие классы "движка" находятся в пакете com.javarush.engine.cell,
 и чтобы ими пользоваться, класс "MinesweeperGame" должен содержать такой импорт:
 import com.javarush.engine.cell.*;

 Если ты все сделал правильно, после запуска приложения увидишь игровое поле.

 Прежде чем ты начнешь, советуем прочесть статью про раздел "Игры".
 В ней ты найдешь информацию, которая будет полезна при написании игр.
 Требования:
 •	Должен существовать публичный класс "MinesweeperGame".
 •	Класс "MinesweeperGame" должен быть наследником класса "Game".
 •	В классе "MinesweeperGame" должен быть импорт всего содержимого
 пакета com.javarush.engine.cell.
 */

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private final GameObject[][] gameField = new GameObject[SIDE][SIDE];
    //кол-во мин на поле
    private int countMinesOnField;
    private int score = 0;
    //кол-во закрытых ячеек поля
    private int countClosedTiles = SIDE * SIDE;
    //в помощь игроку даются флаги. Они нужны, чтобы помечать
    // потенциально "заминированные" ячейки (countFlags)
    private int countFlags;
    //флаг остановки игры
    private boolean isGameStopped;
    private static final String MINE = "\u058D"; //символ мины
    private static final String FLAG = "\u26F3";

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {
            restart();
            return;
        }
        openTile(x, y);
    }

    /**
     * Метод ставит или снимает флаг в игре с помощью клика правой кнопки мыши
     * @param x - координата x
     * @param y - координата y
     */
    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    /**
     * Метод создания игрового поля и инициализаци игры.
     */
    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                int randomNumber = getRandomNumber(10); //получаем цифры 0..9
                boolean isMine = randomNumber < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    /**
     * Метод объявляет игрока проигравшим и заканчивает игру
     */
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "YOU LOSE!GAME OVER", Color.BLACK, 12);

    }

    /**
     * Метод объявляет игрока победителем и заканчивает игру
     */
    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "You WIN!", Color.BLUE, 12);
    }


    private void restart() {
        isGameStopped = false;
        score = 0;
        countClosedTiles = SIDE * SIDE;
        countMinesOnField = 0;

        setScore(score);
        createGame();
    }

    /**
     * Метод проверяет все объекты игрового поля gameField на наличие мин.
     * Если в ячейке находится мина, то кладем объект в массив
     * @param gameObject - экземпляр класса GameObject
     * @return List - список объектов GameObject
     */
    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    /**
     * Считаем кол-во примыкающих ячеек с минами
     * Метод проверяет каждую ячейку "gameField" на наличие мины. Если мины нет, то
     * значит мы считаем кол-во примыкающих ячеек с минами. Кол-во записываем в
     * переменную объекта "countMineNeighbors"
     */
    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField[y][x];
                if (!gameObject.isMine) {
                    for (GameObject neighbor : getNeighbors(gameObject)) {
                        if (neighbor.isMine) {
                            gameObject.countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Если в ячейке есть мина (isMine == true), то
     * метод отрисовывает в ячейке символ мины.
     * Иначе - метод отрисовывает количество примыкающих ячеек-мин
     *
     * @param x - координата x
     * @param y - координата y
     */
    private void openTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        //если ячейка уже открыта, то ничего не делаем
        if (isGameStopped || gameObject.isOpen || gameObject.isFlag) {
            return;
        }
        gameObject.isOpen = true; //флаг, что ячейка открыта
        countClosedTiles--; //уменьшаем кол-во закрытых ячеек поля
        if (countClosedTiles == countMinesOnField && !gameObject.isMine) {
            win();
        }
            setCellColor(x, y, Color.GREEN); //красим открытую ячейку Зеленым цветом
        if (gameObject.isMine) {
            //метод setCellValueEx отрисовывает MINE на красном поле и
            // заканчивает игру, если gameObject по текущим координатам является миной
            setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);
            gameOver();
            return;
        } else if (gameObject.countMineNeighbors == 0) {
            setCellValue(gameObject.x, gameObject.y, "");
            List<GameObject> neighbors = getNeighbors(gameObject);
            for (GameObject neigbor : neighbors) {
                if (!neigbor.isOpen) {
                    openTile(neigbor.x, neigbor.y);
                }
            }
        } else {
            //метод setCellNumber отрисовывает количество соседей-мин,
            // если gameObject по текущим координатам не является миной
            setCellNumber(x, y, gameObject.countMineNeighbors);
        }
        score += 5;
        setScore(score);
    }

    /**
     * Задачи метода:
     * 1. отмечать ячейку на игровом поле флагом
     *    (gameObject.ifFlag=true) или снимать флаг (gameObject.ifFlag=false);
     * 2. следить за количеством флагов (countFlags);
     * 3. заниматься отрисовкой и стиранием флагов на игровом поле;
     * 4. менять цвет ячейки поля, если в ней устанавливается флаг и
     *   возвращать цвет обратно, если флаг снимается.
     * @param x - координата x ячейки
     * @param y - координата y ячейки
     */
    private void markTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        if (isGameStopped || gameObject.isOpen || (countFlags == 0 && !gameObject.isFlag)) {
            return;
        }
        if (!gameObject.isFlag) {
            gameObject.isFlag = true;
            countFlags--;
            setCellValue(gameObject.x, gameObject.y, FLAG);
            setCellColor(gameObject.x, gameObject.y, Color.YELLOW);
        } else {
            gameObject.isFlag = false;
            countFlags++;
            setCellValue(gameObject.x, gameObject.y, "");
            setCellColor(gameObject.x, gameObject.y, Color.ORANGE);
        }
    }
}














