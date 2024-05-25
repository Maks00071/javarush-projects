package TheGameMinesweeper;

/**
 * Так как в нашей игре будут использоваться игровые объекты (ячейки), создадим для их
 * описания отдельный класс "GameObject"
 */
public class GameObject {
    public int x;
    public int y;
    public boolean isMine; //является ли ячейка миной
    public int countMineNeighbors; //количество "заминированных" соседей ячейки
    public boolean isOpen;
    public boolean isFlag; //является ли ячейка флагом

    /**
     * Конструктор
     * @author Korystov M.
     * @param x - координата x
     * @param y - координата y
     * @param isMine - наличие мины в ячейке
     */
    public GameObject(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }
}
