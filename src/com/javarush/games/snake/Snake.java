package com.javarush.games.snake;

import java.util.ArrayList;
import java.util.List;
import com.javarush.engine.cell.*;

public class Snake {
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    public void draw(Game game){
        game.setCellValue(snakeParts.get(0).x, snakeParts.get(0).y, HEAD_SIGN);
        for (int i = 1; i < snakeParts.size(); i++){
            game.setCellValue(snakeParts.get(i).x, snakeParts.get(i).y, BODY_SIGN);
        }
    }

    public Snake(int x, int y){
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }
}
