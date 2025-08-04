package io.javagame.snake;

import io.javagame.snake.thread.GameThread;

import javax.swing.SwingUtilities;


public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GameThread());
    }
}
