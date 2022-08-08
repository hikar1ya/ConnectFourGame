package hikariya;

import java.util.Scanner;

/**
 * Класс для игры "Четыре в ряд".
 */
public class ConnectFourGame {

    private static final int MAX_STEPS = 42;

    private Field field;
    private int step = -1;
    private boolean hasWinner = false;

    /**
     * Начало игры.
     */
    public void startGame() {
        field = new Field();
        while (!hasWinner && ++step < MAX_STEPS) {
            step();
        }
        field.print();
        if (hasWinner) {
            System.out.println("Winner is player " + getPlayer());
        } else {
            System.out.println("Draw");
        }
    }

    /**
     * Игровой шаг.
     */
    private void step() {
        field.print();
        int column = getColumn();
        field.insertDisc(column, getPlayer());
        hasWinner = isWinner(column);
    }

    /**
     * Получение столбца из консоли.
     *
     * @return индекс столбца
     */
    private int getColumn() {
        System.out.println("Player " + getPlayer() + "'s turn");
        System.out.println("Choose column (from 1 to 7)");
        int column = -1;
        while (column < 0) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                column = scanner.nextInt();
                if (column < 1 || column > 7) {
                    System.out.println("You can input value only from 1 to 7");
                    column = -1;
                    continue;
                }
                if (field.isCanInsertDisc(column - 1)) {
                    System.out.println("Column is full, try choose another");
                    column = -1;
                }
            } else {
                System.out.println("Invalid value");
            }
        }
        return column - 1;
    }

    /**
     * Метод для получения текущего игрока.
     *
     * @return текущий игрок
     */
    private char getPlayer() {
        return step % 2 == 0 ? 'R' : 'G';
    }

    /**
     * Проверка, образует ли последняя вставка диска выигрышную комбинацию.
     *
     * @param column индекс столбца
     * @return есть ли выигрышная комбинация
     */
    private boolean isWinner(int column) {
        return field.isConnectedFour(column);
    }


    /**
     * Класс для работы с игровым полем.
     */
    private class Field {

        private static final int MAX_ROWS = 6;
        private static final int MAX_COLUMNS = 7;

        private final char[][] elements = new char[MAX_ROWS][MAX_COLUMNS];
        private final int[] columnsIndex = new int[]{5, 5, 5, 5, 5, 5, 5};

        /**
         * Вывод поля с дисками.
         */
        void print() {
            for (char[] row : elements) {
                for (char c : row) {
                    System.out.print("|");
                    System.out.print(c == '\u0000' ? ' ' : c);
                }
                System.out.println("|");
            }
        }

        /**
         * Проверка, можно ли вставить диск в столбец.
         *
         * @param columnIndex индекс столбца
         * @return возможна ли вставка
         */
        boolean isCanInsertDisc(int columnIndex) {
            return columnsIndex[columnIndex] < 0;
        }

        /**
         * Вставка диска по индексу столбца.
         *
         * @param columnIndex индекс столбца
         * @param player      диск игрока
         */
        void insertDisc(int columnIndex, char player) {
            elements[columnsIndex[columnIndex]][columnIndex] = player;
            columnsIndex[columnIndex]--;
        }

        /**
         * Проверка, соединены ли 4 диска с последним вставленным в столбец диском.
         *
         * @param columnIndex индекс столбца
         * @return есть ли соединение 4-х дисков
         */
        boolean isConnectedFour(int columnIndex) {
            return isConnectedHorizontally(columnIndex) || isConnectedVertically(columnIndex)
                    || isConnectedMainDiagonally(columnIndex) || isConnectedMinorDiagonally(columnIndex);
        }

        /**
         * Проверка, соединены ли 4 диска по горизонтали.
         *
         * @param columnIndex индекс столбца
         * @return есть ли соединение 4-х дисков
         */
        private boolean isConnectedHorizontally(int columnIndex) {
            int counter = 1;
            int rowIndex = columnsIndex[columnIndex] + 1;
            char player = elements[rowIndex][columnIndex];
            for (int currentColumn = columnIndex - 1; currentColumn > -1; currentColumn--) {
                if (elements[rowIndex][currentColumn] != player) break;
                counter++;
            }
            for (int currentColumn = columnIndex + 1; currentColumn < MAX_COLUMNS; currentColumn++) {
                if (elements[rowIndex][currentColumn] != player) break;
                counter++;
            }
            return counter > 3;
        }

        /**
         * Проверка, соединены ли 4 диска по вертикали.
         *
         * @param columnIndex индекс столбца
         * @return есть ли соединение 4-х дисков
         */
        private boolean isConnectedVertically(int columnIndex) {
            int counter = 1;
            int rowIndex = columnsIndex[columnIndex] + 1;
            char player = elements[rowIndex][columnIndex];
            for (int currentRow = rowIndex - 1; currentRow > -1; currentRow--) {
                if (elements[currentRow][columnIndex] != player) break;
                counter++;
            }
            for (int currentRow = rowIndex + 1; currentRow < MAX_ROWS; currentRow++) {
                if (elements[currentRow][columnIndex] != player) break;
                counter++;
            }
            return counter > 3;
        }

        /**
         * Проверка, соединены ли 4 диска по главной диагонали.
         *
         * @param columnIndex индекс столбца
         * @return есть ли соединение 4-х дисков
         */
        private boolean isConnectedMainDiagonally(int columnIndex) {
            int counter = 1;
            int rowIndex = columnsIndex[columnIndex] + 1;
            char player = elements[rowIndex][columnIndex];
            int currentColumn = columnIndex - 1;
            for (int currentRow = rowIndex - 1; currentRow > -1; currentRow--) {
                if (currentColumn < 0 || elements[currentRow][currentColumn] != player) break;
                counter++;
                currentColumn--;
            }
            currentColumn = columnIndex + 1;
            for (int currentRow = rowIndex + 1; currentRow < MAX_ROWS; currentRow++) {
                if (currentColumn >= MAX_COLUMNS || elements[currentRow][currentColumn] != player) break;
                counter++;
                currentColumn++;
            }
            return counter > 3;
        }

        /**
         * Проверка, соединены ли 4 диска по побочной диагонали.
         *
         * @param columnIndex индекс столбца
         * @return есть ли соединение 4-х дисков
         */
        private boolean isConnectedMinorDiagonally(int columnIndex) {
            int counter = 1;
            int rowIndex = columnsIndex[columnIndex] + 1;
            char player = elements[rowIndex][columnIndex];
            int currentColumn = columnIndex + 1;
            for (int currentRow = rowIndex - 1; currentRow > -1; currentRow--) {
                if (currentColumn >= MAX_COLUMNS || elements[currentRow][currentColumn] != player) break;
                counter++;
                currentColumn++;
            }
            currentColumn = columnIndex - 1;
            for (int currentRow = rowIndex + 1; currentRow < MAX_ROWS; currentRow++) {
                if (currentColumn < 0 || elements[currentRow][currentColumn] != player) break;
                counter++;
                currentColumn--;
            }
            return counter > 3;
        }
    }
}
