package io.javagame.snake.thread;

import io.javagame.snake.ui.GameFrame;
import io.javagame.snake.ui.GamePanel;

public class GameThread implements Runnable{
    @Override
    public void run() {
        new GameFrame(new GamePanel());
    }
}
