package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private Snake snake;
    private int turnDelay;
    private Apple apple;
    private boolean isGameStopped;
    private static final int GOAL = 28;
    private int score;

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            createNewApple();
            score += 5;
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
        }
        if (!snake.isAlive)
            gameOver();
        if (snake.getLength() > GOAL)
            win();
        drawScene();
    }


    private void createGame(){
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        isGameStopped = false;
        score = 0;
        setScore(score);
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    private void win(){
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.DARKSEAGREEN, "YOU WIN", Color.RED, 32);
    }

    private void gameOver(){
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.DARKSEAGREEN, "GAME OVER", Color.RED, 32);
    }

    private void createNewApple(){
        apple = new Apple(getRandomNumber(HEIGHT), getRandomNumber(WIDTH));
        while (snake.checkCollision(apple))
            apple = new Apple(getRandomNumber(HEIGHT), getRandomNumber(WIDTH));
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.LEFT)
            snake.setDirection(Direction.LEFT);
        if (key == Key.RIGHT)
            snake.setDirection(Direction.RIGHT);
        if (key == Key.UP)
            snake.setDirection(Direction.UP);
        if (key == Key.DOWN)
            snake.setDirection(Direction.DOWN);
        if (key == Key.SPACE && isGameStopped == true)
            createGame();
    }



    private void drawScene(){
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++)
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
        }
        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }
}
