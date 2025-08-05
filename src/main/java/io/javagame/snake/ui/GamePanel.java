package io.javagame.snake.ui;

import io.javagame.snake.model.Direction;
import io.javagame.snake.model.Food;
import io.javagame.snake.model.Snake;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;

public class GamePanel extends JPanel implements UI, KeyListener {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private static final int FPS = 60;
    private static final long ONE_SECOND = 1_000_000_000;
    private static final long DELAY = (ONE_SECOND / FPS);

    private long currentCountingPeriod = System.nanoTime();
    private int howManyFrames = 0;
    private float calculatedFPS = 0;

    private Snake snake;
    private List<Food> foods;
    private List<Food> removeFoods;

    private static final int SIZE = 15;

    private Random random;

    private static float DEF_SPEED = 0.1f;
    private int score;
    private boolean isGameOver;

    private boolean isShowFPS;

    public GamePanel() {
        isShowFPS = false;
        init();
    }

    private void init() {
        random = Random.from(RandomGenerator.getDefault());
        score = 0;
        isGameOver = false;
        this.setBackground(Color.GRAY);

        snake = Snake.getInstance();
        snake.addCoordinate(new Point(dimension.width / 2, dimension.height / 2));
        foods = new ArrayList<>();
        removeFoods = new LinkedList<>();
        int delay = (int) (DELAY / 1_000_000L);
        final var timer = new Timer(delay - 1, event -> {
            updateGame();
            this.repaint();
        });
        timer.start();
    }

    private void updateGame() {
        updateFPS();
        updateMovement();
        updateFood();
    }

    private boolean isCollide(Food food) {
        // Get the coordinates and size of the snake's head
        final var snakeHead = snake.getHead();

        // Get the coordinates and size of the food
        final var foodPoint = food.point();

        // Check for horizontal overlap
        boolean xOverlap = (snakeHead.x < foodPoint.x + SIZE) &&
                (snakeHead.x + SIZE > foodPoint.x);

        // Check for vertical overlap
        boolean yOverlap = (snakeHead.y < foodPoint.y + SIZE) &&
                (snakeHead.y + SIZE > foodPoint.y);

        // A collision occurs if there is overlap on both axes
        return xOverlap && yOverlap;
    }

    private void updateFood() {
        if (foods.isEmpty()) {
            final int gridX = random.nextInt(50, dimension.width - 50);
            final int gridY = random.nextInt(50, dimension.height - 50);
            foods.add(new Food(new Point(gridX, gridY)));
        }
    }

    private void updateMovement() {
        boolean isCollided = false;
        for (final var food : foods) {
            if (isCollide(food)) {
                log.info("Collision!!!");
                removeFoods.addFirst(food);
                isCollided = true;
                DEF_SPEED += 0.2f;
            }
        }
        foods.removeAll(removeFoods);
        removeFoods.clear();

        var newHead = new Point(snake.getHead().x, snake.getHead().y);
        final int movementVector = (int) (SIZE * snake.getSpeed());

        switch (snake.getDirection()) {
            case UP -> newHead.y -= movementVector;
            case DOWN -> newHead.y += movementVector;
            case LEFT -> newHead.x -= movementVector;
            case RIGHT -> newHead.x += movementVector;
            default -> {
            }
        }

        boolean isHitWall = newHead.x > (dimension.width - SIZE) || newHead.x < 0 || newHead.y > dimension.height - SIZE || newHead.y < 0;
        boolean isHitSelf = false;
        if (snake.getCoordinates().size() > 1)
            for (final var coordinate : snake.getCoordinates().subList(1, snake.getCoordinates().size() - 1)) {
                if (coordinate.equals(snake.getHead())) {
                    isHitSelf = true;
                    break;
                }
            }
        if (isHitSelf || isHitWall)
            isGameOver = true;
        if (!isCollided) snake.getCoordinates().removeLast();
        else score += 1;
        snake.getCoordinates().addFirst(new Point(newHead.x, newHead.y));
    }

    private void updateFPS() {
        howManyFrames += 1;
        var currentMoment = System.nanoTime();
        if (currentMoment - currentCountingPeriod >= ONE_SECOND) {
            final long elapsedTime = currentMoment - currentCountingPeriod;
            calculatedFPS = ((float) howManyFrames / (float) elapsedTime) * ONE_SECOND;
            currentCountingPeriod = System.nanoTime();
            howManyFrames = 0;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final var graphics = (Graphics2D) g;
        if (!isGameOver) {
            graphics.setColor(Color.RED);
            graphics.drawString("Score: " + score, (dimension.width / 2) - 5, 20);
            graphics.setColor(Color.GREEN);
            graphics.setFont(Font.getFont(Font.SERIF));
            graphics.drawString(String.format("FPS: %.0f", calculatedFPS), 10, 20);
            graphics.setColor(Color.BLACK);
            for (final var coordinate : snake.getCoordinates()) {
                graphics.fillRect(coordinate.x, coordinate.y, SIZE, SIZE);
            }
            graphics.setColor(Color.RED);
            for (final var food : foods) {
                graphics.fillRect(food.point().x, food.point().y, SIZE, SIZE);
            }
        } else {
            graphics.setColor(Color.RED);
            graphics.drawString("Game Over, Score: " + score, (int) (dimension.getWidth() / 2) - 100, (int) (dimension.getHeight() / 2));
            graphics.drawString("Press SPACE to try again :)", (int) (dimension.getWidth() / 2) - 100, (int) (dimension.getHeight() / 2) - 100);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final var pressed = e.getKeyCode();
        if (KeyEvent.VK_UP == pressed || KeyEvent.VK_W == pressed) {
            snake.setDirection(Direction.UP);
        } else if (KeyEvent.VK_DOWN == pressed || KeyEvent.VK_S == pressed) {
            snake.setDirection(Direction.DOWN);
        } else if (KeyEvent.VK_LEFT == pressed || KeyEvent.VK_A == pressed) {
            snake.setDirection(Direction.LEFT);
        } else if (KeyEvent.VK_RIGHT == pressed || KeyEvent.VK_D == pressed) {
            snake.setDirection(Direction.RIGHT);
        } else if (KeyEvent.VK_SPACE == pressed) {
            if (isGameOver) {
                log.info("Again!");
                isGameOver = false;
                snake.getCoordinates().clear();
                foods.clear();
                DEF_SPEED = 0.1f;
                snake.setSpeed(DEF_SPEED);
                init();
            } else {
                if (snake.getSpeed() + 0.2f < 1 && snake.getSpeed() + 0.2f < DEF_SPEED + 0.5f)
                    snake.setSpeed(snake.getSpeed() + 0.2f);
            }
        } else if (KeyEvent.VK_Q == pressed) {
            if (snake.getSpeed() - 0.2f > DEF_SPEED)
                snake.setSpeed(snake.getSpeed() - 0.2f);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
