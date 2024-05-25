package MyFirstGame;

import com.javarush.engine.cell.*;

/*
Сегодня начнем писать одну интересную и "залипательную" игру — 2048. Готов? Тогда поехали.

*****Правила игры:

>>> Игровое поле имеет форму квадрата. Размер стандартного поля — 4x4 плитки.
>>> Цель игры — получить плитку номиналом 2048.
>>> В каждом раунде появляется плитка номиналом 2 (с вероятностью 90%) или 4 (с вероятностью 10%).
>>> Можно сдвинуть все плитки игрового поля в одну из 4 сторон, нажав соответствующую
    клавишу: вверх, вниз, вправо или влево. Если при сдвиге две плитки одного
    номинала "налетают" друг на друга, они "слипаются" в одну. Ее номинал равен сумме
    соединившихся плиток.
>>> После каждого хода на свободной секции поля появляется новая плитка номиналом 2 или 4.
>>> Если при нажатии клавиши местоположение плиток или их номинал не изменится, ход не совершается.
>>> Если в одной строчке или столбце находится более двух плиток одного номинала, при сбрасывании
    они слипаются с той стороны, в которую были направлены. Например, находящиеся в одной строке
    плитки (4, 4, 4, 0) после хода влево превратятся в (8, 4, 0, 0), а после
    хода вправо — в (0, 0, 4, 8). Данная обработка неоднозначности позволяет более точно
    формировать стратегию игры.
>>> За каждое соединение игровые очки увеличиваются на номинал получившейся плитки.
>>> Игра заканчивается поражением, если после очередного хода невозможно совершить действие.
>>> Игра заканчивается победой, если на поле появится клетка номиналом 2048.

******Теперь приступаем к реализации игры:******
*/

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private boolean isGameStopped = false;
    private int score = 0;
    private int[][] gameField = new int[SIDE][SIDE];  //основное игровое поле

    @Override
    public void initialize() {
        //меняем размер окна
        setScreenSize(SIDE, SIDE);  //отображаем игровое поле
        createGame();
        drawScene();
    }

    //метод задает соответствующее действие по нащатию клавиши клавиатуры
    // (вниз, вверх, влево, вправо) (переопределяем метод класса)
    @Override
    public void onKeyPress(Key key) {
        //при назатии клавишы "ПРОБЕЛ" игра заново запускается
        if (isGameStopped) {
            if (key == key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            } else {
                return;
            }
        }

        if (!canUserMove()) {
            gameOver();
            return;
        }

        if (key == key.LEFT) {
            moveLeft();
        } else if (key == key.RIGHT) {
            moveRight();
        } else if (key == key.UP) {
            moveUp();
        } else if (key == key.DOWN) {
            moveDown();
        } else {
            return;
        }
        drawScene();
    }

    //метод рандомно создает номер ячейки
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    //метод меняет цвет каждой ячейки в зависимости от величины цифры
    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    //метод случайным образом выбирает ячейку матрицы "gameField" со значением "0"
    private void createNewNumber() {
        if (getMaxTileValue() >= 2048) {
            win();
            return;
        }

        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);

        //пока не найдем ячейку со значением "0"
        while (gameField[y][x] != 0) {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        }
        //заносим в найденную ячейку значение 4 (если выпадет 9 (10%))
        gameField[y][x] = getRandomNumber(10) == 9 ? 4 : 2;
    }

    //метод возвращает цвет клетки по значению ее цифры
    private Color getColorByValue(int value) {
        return switch (value) {
            case 0 -> Color.WHITE;
            case 2 -> Color.PLUM;
            case 4 -> Color.SLATEBLUE;
            case 8 -> Color.DODGERBLUE;
            case 16 -> Color.DARKTURQUOISE;
            case 32 -> Color.MEDIUMSEAGREEN;
            case 64 -> Color.LIMEGREEN;
            case 128 -> Color.DARKORANGE;
            case 256 -> Color.SALMON;
            case 512 -> Color.ORANGERED;
            case 1024 -> Color.DEEPPINK;
            case 2048 -> Color.MEDIUMVIOLETRED;
            default -> Color.NONE;
        };
    }

    //метод закрашивает ячейку в зависимости от значения ее цифры
    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        String valueString = Integer.toString(value);
        if (value != 0) {
            setCellValueEx(x, y, color, valueString);
        } else {
            setCellValueEx(x, y, color, "");
        }
    }

    //метод сообщает о проигрыше
    private void gameOver() {
        //флаг "isGameStopped" необходим для реализации механизма рестарта игры
        isGameStopped = true;
        showMessageDialog(Color.RED, "ВЫ ПРОИГРАЛИ! Чтобы сыграть еще раз - нажмите ПРОБЕЛ", Color.BLACK, 15);
    }

    //метод выводит сообщение о победе в игре
    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.YELLOW, "ТЫ ПОБЕДИЛ!!!", Color.WHITE, 15);
    }

    //сдвигает все ненулевые элементы массива "row" влево (в сторону нулевого индекса),
    // а нулевые элементы переносит вправо. Если хотя бы один элемент был передвинут,
    // метод возвращает "true", иначе – "false": {0, 0, 2, 4} => {2, 4, 0, 0}
    private boolean compressRow(int[] row) {
        int insertPosition = 0;
        boolean result = false;
        for (int x = 0; x < SIDE; x++) {
            if (row[x] > 0) {
                if (x != insertPosition) {
                    row[insertPosition] = row[x];
                    row[x] = 0;
                    result = true;
                }
                insertPosition++;
            }
        }
        return result;
    }

    //метод соединяет соседние пары одинаковых ненулевых элементов массива "row".
    // Соединение должно происходить при сдвиге влево: {0, 2, 2, 0} => {0, 4, 0, 0}
    private boolean mergeRow(int[] row) {
        boolean result = false;
        for (int i = 0; i < SIDE - 1; i++) {
            if (row[i] != 0 && row[i] == row[i + 1]) {
                result = true;
                row[i] = row[i] + row[i + 1];
                row[i + 1] = 0;
                score += row[i];
                setScore(score);
            }
        }
        return result;
    }

    //метод выполняет движение влево
    private void moveLeft() {
        boolean isNewNumberNeeded = false;

        for (int[] row : gameField) {
            boolean wasCompressed = compressRow(row);
            boolean wasMerged = mergeRow(row);
            if (wasMerged) {  //если было слияние, то необходим сдвинуть ячейки
                compressRow(row);
            }
            //если был сдвиг или слияние, то создаем новую ячейку
            if (wasCompressed || wasMerged) {
                isNewNumberNeeded = true;
            }
        }
        if (isNewNumberNeeded) {
            createNewNumber();
        }
    }

    //метод выполняет движение вправо
    /*В методе "moveRight()" повернем матрицу "gameFiled" два раза по часовой стрелке,
    затем вызовем "moveLeft()", который уже написан, и снова повернем "gameField"
    еще два раза по часовой стрелке. Должно сработать.*/
    private void moveRight() {
        rotateClockwise();  //1-й оборот (90)
        rotateClockwise();  //2-й оборот (180)
        moveLeft();         // выполняем преобразование
        rotateClockwise();  //3-й оборот (270)
        rotateClockwise();  //4-й оборот (360) - исходное положение
    }

    //метод выполняет движение вверх
    /*В методе "moveUp()" сначала поворачиваем на 90 градусов три раза,
    затем вызываем "moveLeft()" и еще один раз поворачиваем на 90 градусов.*/
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();  //исходное положение
    }

    //метод выполняет движение вниз
    /*В методе "moveDown()" поворачиваем на 90 градусов один раз,
    затем вызываем "moveLeft()" и еще 3 раза поворачиваем на 90 градусов.*/
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();  //исходное положение
    }

    //метод переворачивает матрицу на 90 градусов по часовой стрелке
    // (для методов вниз, вверх, вправо)
    /*(2, 2, 2, 2),  => (0, 8, 4, 2),
      (4, 4, 4, 4),  => (0, 8, 4, 2),
      (8, 8, 8, 8),  => (0, 8, 4, 2),
      (0, 0, 0, 0),  => (0, 8, 4, 2)*/
    private void rotateClockwise() {
        int[][] tempArray = new int[SIDE][SIDE];

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                tempArray[j][SIDE - i - 1] = gameField[i][j];
            }
        }
        gameField = tempArray;
    }

    //метод ищет на игровом поле максимальное число
    private int getMaxTileValue() {
        int maxValue = Integer.MIN_VALUE;

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > maxValue) {
                    maxValue = gameField[i][j];
                }
            }
        }
        return maxValue;
    }

    //метод проверяет, что ход можно сделать, если есть хотя бы одна пустая плитка
    //или пустых плиток нет, но есть возможность их соединения
    private boolean canUserMove() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0) {
                    return true;
                } else if ((y < SIDE - 1) && (gameField[y][x] == gameField[y + 1][x])) {
                    return true;
                } else if ((x < SIDE - 1) && (gameField[y][x] == gameField[y][x + 1])) {
                    return true;
                }
            }
        }
        return false;
    }
}















