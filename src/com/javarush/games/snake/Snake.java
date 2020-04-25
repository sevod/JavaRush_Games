package com.javarush.games.snake;

import java.util.ArrayList;
import java.util.List;
import com.javarush.engine.cell.*;

public class Snake {
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public int getLength(){
        return snakeParts.size();
    }

    public boolean checkCollision(GameObject gameObject){
        for (GameObject snakePart: snakeParts) {
            if (snakePart.x == gameObject.x && snakePart.y == gameObject.y)
                return true;
        }
        return false;
    }

    public void move(Apple apple){
        GameObject snakeHead = createNewHead();
        if (checkCollision(snakeHead)){
            isAlive = false;
            return;
        }
        if (snakeHead.x > SnakeGame.HEIGHT - 1 || snakeHead.x < 0 || snakeHead.y > SnakeGame.WIDTH - 1 || snakeHead.y < 0) {
            isAlive = false;
            return;
        }

        if (apple.x == snakeHead.x && apple.y == snakeHead.y){
            apple.isAlive = false;
        }else
            removeTail();

        snakeParts.add(0, snakeHead);
    }

    public void removeTail(){
        snakeParts.remove(snakeParts.size() - 1);
    }

    public GameObject createNewHead(){
        GameObject gameObject = null;

        if (direction == Direction.LEFT)
            gameObject = new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
        if (direction == Direction.RIGHT)
            gameObject = new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
        if (direction == Direction.UP)
            gameObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);
        if (direction == Direction.DOWN)
            gameObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);

        return gameObject;
    }

    public void draw(Game game){
        Color snakeColor = null;
        if (isAlive)
            snakeColor = Color.BLACK;
        else
            snakeColor = Color.RED;
        game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, snakeColor, 75);
        for (int i = 1; i < snakeParts.size(); i++){
            game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, snakeColor, 75);
        }
    }

    public void setDirection(Direction direction){
        if (this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x)
            return;
        if (this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x)
            return;
        if (this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y)
            return;
        if (this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y)
            return;

        this.direction = direction;
    }

    public Snake(int x, int y){
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }
}
