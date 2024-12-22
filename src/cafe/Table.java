package cafe;

/**
 * Класс Table представляет столик в антикафе и управляет состоянием его использования.
 * <p>
 * Каждый столик имеет номер, состояние (свободен или занят), а также
 * может вычислять стоимость использования на основе времени сессии и тарифа.
 * </p>
 */
public class Table {
    /** Номер столика. */
    private int tableNumber;

    /** Указывает, занят ли столик в данный момент. */
    private boolean isOccupied;

    /** Время начала текущей сессии в миллисекундах. */
    private long startTime;

    /** Время окончания последней сессии в миллисекундах. */
    private long endTime;

    /** Стоимость за минуту использования столика. */
    private double costPerMinute;

    /** Общая стоимость последней завершенной сессии. */
    private double totalCost;

    /**
     * Конструктор для создания столика.
     *
     * @param tableNumber   номер столика
     * @param costPerMinute стоимость за минуту использования столика
     */
    public Table(int tableNumber, double costPerMinute) {
        this.tableNumber = tableNumber;
        this.costPerMinute = costPerMinute;
        this.isOccupied = false;
    }

    /**
     * Начинает новую сессию использования столика.
     * Устанавливает состояние столика как занятого и фиксирует время начала.
     */
    public void startSession() {
        this.isOccupied = true;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Завершает текущую сессию использования столика.
     * Устанавливает состояние столика как свободного, фиксирует время окончания
     * и рассчитывает общую стоимость сессии.
     */
    public void endSession() {
        this.isOccupied = false;
        this.endTime = System.currentTimeMillis();
        calculateTotalCost();
    }

    /**
     * Рассчитывает общую стоимость текущей или завершенной сессии
     * на основе времени использования и тарифа.
     */
    private void calculateTotalCost() {
        long durationMinutes = (endTime - startTime) / (1000 * 60);
        this.totalCost = durationMinutes * costPerMinute;
    }

    /**
     * Возвращает время, проведенное за столиком, в минутах.
     *
     * @return время использования столика в минутах
     */
    public long getTimeSpentMinutes() {
        if (isOccupied) {
            return (System.currentTimeMillis() - startTime) / (1000 * 60);
        }
        return (endTime - startTime) / (1000 * 60);
    }

    /**
     * Возвращает общую стоимость текущей или завершенной сессии.
     *
     * @return общая стоимость сессии
     */
    public double getTotalCost() {
        if (isOccupied) {
            long durationMinutes = (System.currentTimeMillis() - startTime) / (1000 * 60);
            return durationMinutes * costPerMinute;
        }
        return totalCost;
    }

    /**
     * Проверяет, занят ли столик в данный момент.
     *
     * @return true, если столик занят; false, если столик свободен
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Возвращает номер столика.
     *
     * @return номер столика
     */
    public int getTableNumber() {
        return tableNumber;
    }
}
