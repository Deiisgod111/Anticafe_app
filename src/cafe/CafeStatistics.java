package cafe;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс CafeStatistics предназначен для сбора и хранения статистики работы антикафе.
 * <p>
 * Статистика включает:
 * <ul>
 *     <li>Общий доход.</li>
 *     <li>Общее количество сессий.</li>
 *     <li>Суммарное время всех сессий.</li>
 *     <li>Количество использований каждого столика.</li>
 *     <li>Доход, полученный с каждого столика.</li>
 * </ul>
 * </p>
 */
public class CafeStatistics {

    /** Общий заработок антикафе. */
    private double totalEarnings;

    /** Общее количество сессий. */
    private int totalSessions;

    /** Суммарное время всех сессий в минутах. */
    private long totalSessionTime;

    /** Карта для хранения количества использований каждого столика. */
    private Map<Integer, Integer> tableUsageCount;

    /** Карта для хранения заработка с каждого столика. */
    private Map<Integer, Double> tableEarnings;

    /**
     * Конструктор, инициализирующий начальные значения статистики.
     */
    public CafeStatistics() {
        this.totalEarnings = 0;
        this.totalSessions = 0;
        this.totalSessionTime = 0;
        this.tableUsageCount = new HashMap<>();
        this.tableEarnings = new HashMap<>();
    }

    /**
     * Обновляет статистику на основе данных о завершенной сессии на столике.
     *
     * @param table столик, данные которого используются для обновления статистики
     */
    public void updateStatistics(Table table) {
        int tableNumber = table.getTableNumber();
        double cost = table.getTotalCost();
        long timeSpent = table.getTimeSpentMinutes();

        totalEarnings += cost;
        totalSessions++;
        totalSessionTime += timeSpent;

        tableUsageCount.put(tableNumber, tableUsageCount.getOrDefault(tableNumber, 0) + 1);
        tableEarnings.put(tableNumber, tableEarnings.getOrDefault(tableNumber, 0.0) + cost);
    }

    /**
     * Возвращает общий заработок антикафе.
     *
     * @return общий заработок
     */
    public double getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Вычисляет и возвращает среднее время использования столиков.
     *
     * @return среднее время использования столиков в минутах
     */
    public double getAverageSessionTime() {
        if (totalSessions == 0) return 0;
        return (double) totalSessionTime / totalSessions;
    }

    /**
     * Возвращает номер самого популярного столика (с максимальным количеством использований).
     *
     * @return номер столика
     */
    public int getMostUsedTable() {
        return tableUsageCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Возвращает номер самого прибыльного столика (с максимальным заработком).
     *
     * @return номер столика
     */
    public int getHighestEarningTable() {
        return tableEarnings.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Печатает текущую статистику по каждому столу, включая состояние (свободен/занят),
     * время использования и текущую сумму.
     *
     * @param tables массив столиков, статистика которых анализируется
     */
    public void printCurrentStatistics(Table[] tables) {
        System.out.println("Current statistics:");
        double currentTotal = 0;

        for (Table table : tables) {
            if (table.isOccupied()) {
                long timeSpent = table.getTimeSpentMinutes();
                double cost = table.getTotalCost();
                currentTotal += cost;

                System.out.println("Table " + table.getTableNumber() + ": is not free, time: " + timeSpent +
                        " minute, sum: " + cost + " rub.");
            } else {
                System.out.println("Table " + table.getTableNumber() + ": is free.");
            }
        }

        System.out.println("Money from every tables: " + currentTotal + " rub.");
    }

    /**
     * Печатает архивную статистику, включая общий заработок, среднее время использования столиков,
     * самый популярный и самый прибыльный столики.
     */
    public void printArchivedStatistics() {
        System.out.println("Archive statistics:");
        System.out.println("Total earned: " + totalEarnings + " rub.");
        System.out.println("Average time of using tables: " + getAverageSessionTime() + " minute.");
        System.out.println("The most popular table: " + getMostUsedTable());
        System.out.println("The most profitable table: " + getHighestEarningTable());
    }
}
