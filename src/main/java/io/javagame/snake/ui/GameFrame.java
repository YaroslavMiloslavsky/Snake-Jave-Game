package io.javagame.snake.ui;

import javax.swing.JFrame;

public class GameFrame extends JFrame implements UI {

    public GameFrame(GamePanel panel) {
        this.setTitle("Java Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(dimension);
        final var loc = getPanelLocation();
        this.setLocation(loc.width, loc.height);
        this.add(panel);
        this.addKeyListener(panel);
        this.setFocusable(true);
        this.setVisible(true);
    }

    private Location getPanelLocation() {
        final var toolKit = this.getToolkit();
        final var screenSize = toolKit.getScreenSize();
        final var screenHeight = screenSize.getHeight();
        final var screenWidth = screenSize.getWidth();
        final var frameHeight = dimension.getHeight();
        final var frameWidth = dimension.getWidth();
        return new Location((int) ((screenWidth - frameWidth) / 2), (int) ((screenHeight - frameHeight) / 2));
    }

    private record Location(int width, int height) {
    }

}
