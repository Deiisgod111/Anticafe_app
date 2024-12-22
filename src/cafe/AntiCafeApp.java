package cafe;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.stage.Stage;

/**
 * AntiCafeApp - приложение для управления антикафе.
 * <p>
 * Позволяет контролировать состояние столиков, отображает текущую и архивную статистику,
 * а также имеет анимированный интерфейс с кнопками для каждого столика.
 * </p>
 *
 * Основные функции:
 * <ul>
 *     <li>Управление состоянием столиков (свободен/занят).</li>
 *     <li>Просмотр текущей и архивной статистики.</li>
 *     <li>Анимация кнопок и использование фона.</li>
 * </ul>
 */
public class AntiCafeApp extends Application {
    /**
     * Массив объектов {@link Table}, представляющий столики в антикафе.
     */
    private Table[] tables = new Table[10];
    /**
     * Стоимость за минуту пребывания в антикафе.
     */
    private double costPerMinute = 5.0;
    /**
     * Объект для управления и хранения статистики работы антикафе.
     */
    private CafeStatistics statistics = new CafeStatistics();
    /**
     * Сетка для размещения кнопок столиков.
     */
    private GridPane grid;
    /**
     * Главный метод приложения. Запускает JavaFX приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Метод инициализации интерфейса приложения.
     *
     * @param primaryStage основной контейнер JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AntiCafe Management");

        // Загружаем изображение фона
        Image cafeImage = new Image(getClass().getResource("/1.jpg").toExternalForm());
        ImageView backgroundImageView = new ImageView(cafeImage);
        backgroundImageView.setFitWidth(800);  // Устанавливаем ширину по размеру сцены
        backgroundImageView.setFitHeight(600); // Устанавливаем высоту по размеру сцены

        // Создаем контейнер StackPane для фона и других элементов
        StackPane root = new StackPane();
        root.getChildren().add(backgroundImageView); // Устанавливаем фон на самый нижний слой

        // Инициализация столиков
        for (int i = 0; i < 10; i++) {
            tables[i] = new Table(i + 1, costPerMinute);
        }

        // Инициализация сетки для кнопок
        grid = new GridPane();
        grid.setVgap(10);  // Вертикальные отступы между кнопками
        grid.setHgap(10);  // Горизонтальные отступы между кнопками

        // Создаем кнопки для столиков
        for (int i = 0; i < 10; i++) {
            Button tableButton = new Button("Table " + (i + 1) + " (is free)");
            int tableIndex = i;

            tableButton.setOnAction(e -> {
                handleTableAction(tableIndex);
                tableButton.setText("Table " + (tableIndex + 1) +
                        (tables[tableIndex].isOccupied() ? " (not free)" : " (free)"));
            });

            // Анимация увеличения кнопки при наведении
            tableButton.setOnMouseEntered((MouseEvent e) -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(200), tableButton);
                st.setToX(1.2);
                st.setToY(1.2);
                st.play();
            });

            tableButton.setOnMouseExited((MouseEvent e) -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(200), tableButton);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });

            // Цвет кнопок (разные цвета для разных кнопок)
            tableButton.setStyle("-fx-background-color: #" + getRandomColor() + ";");

            // Размер кнопок
            tableButton.setPrefWidth(150);
            tableButton.setPrefHeight(70);

            // Добавление кнопки в сетку
            grid.add(tableButton, i % 5, i / 5);
        }

        // Добавим кнопки статистики и применим к ним те же стили
        Button currentStatsButton = new Button("Текущая статистика");
        currentStatsButton.setOnAction(e -> statistics.printCurrentStatistics(tables));
        styleButton(currentStatsButton);

        // Помещаем кнопку на центральное место (параметры индексов: 2, 6)
        grid.add(currentStatsButton, 0, 40);

        Button archivedStatsButton = new Button("Архивная статистика");
        archivedStatsButton.setOnAction(e -> statistics.printArchivedStatistics());
        styleButton(archivedStatsButton);

        // Помещаем кнопку на центральное место (параметры индексов: 3, 6)
        grid.add(archivedStatsButton, 1, 40);

        // Добавим элементы сетки на главный контейнер
        root.getChildren().add(grid); // Сетку с кнопками добавляем поверх фона

        // Создаем сцену и показываем её
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Обработчик действия для кнопок столиков.
     *
     * @param tableIndex индекс столика в массиве {@code tables}
     */
    private void handleTableAction(int tableIndex) {
        Table table = tables[tableIndex];
        Button tableButton = (Button) grid.getChildren().get(tableIndex);
        if (table.isOccupied()) {
            table.endSession();
            statistics.updateStatistics(table);
            System.out.println("Table " + (tableIndex + 1) + " is free.");
        } else {
            table.startSession();
            System.out.println("Table " + (tableIndex + 1) + " is occupied.");
        }
    }
    /**
     * Генерация случайного цвета в формате HEX для кнопок.
     *
     * @return строка, представляющая цвет в HEX
     */
    private String getRandomColor() {
        // Генерируем случайный цвет в формате HEX
        String[] hexColors = {"FF5733", "33FF57", "3357FF", "F0E68C", "FF1493", "800080", "FFD700"};
        return hexColors[(int) (Math.random() * hexColors.length)];
    }

    /**
     * Стилизация кнопок (цвет, размер, анимация при наведении).
     *
     * @param button кнопка для стилизации
     */
    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #" + getRandomColor() + ";");
        button.setPrefWidth(150);
        button.setPrefHeight(70);

        // Анимация при наведении
        button.setOnMouseEntered((MouseEvent e) -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.2);
            st.setToY(1.2);
            st.play();
        });

        button.setOnMouseExited((MouseEvent e) -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
}
