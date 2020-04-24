package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countClosedTiles = SIDE * SIDE;
    private int countMinesOnField;
    private int countFlags;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String  FLAG = "\uD83D\uDEA9";
    private boolean isGameStopped;
    private int score;


    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();
    }


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(x, y, "");
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private void gameOver(){
        isGameStopped = true;
        openAllMine();
        showMessageDialog(Color.GREEN,"Вы проиграли!", Color.RED, 22); //need change
    }

    private void win(){
        isGameStopped = false;
        openAllMine();
        showMessageDialog(Color.GREEN, "Вы подбедили!", Color.RED, 22);
    }


    private void openAllMine(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x].isMine)
                    setCellValueEx(x, y, Color.RED, MINE);
            }
        }
    }

    private void openTile (int x, int y){
        if (gameField[y][x].isOpen || gameField[y][x].isFlag || isGameStopped)
            return;
        if (gameField[y][x].isMine) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
            return;
        }

        if (gameField[y][x].countMineNeighbors != 0)
            setCellNumber(x, y, gameField[y][x].countMineNeighbors);
        else
            setCellValue(x, y, "");
        gameField[y][x].isOpen = true;
        score += 5;
        setScore(score);
        countClosedTiles--;
        setCellColor(x, y, Color.GREEN);

        if (gameField[y][x].countMineNeighbors == 0 && !gameField[y][x].isMine){
            for (GameObject gameObject : getNeighbors(gameField[y][x])) {
                if (!gameObject.isOpen && !gameObject.isMine)
                    openTile(gameObject.x, gameObject.y);
            }
        }

        if (countClosedTiles <= countMinesOnField)
            win();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped)
            restart();
        else
            openTile(x, y);
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void markTile(int x, int y){
        if (gameField[y][x].isOpen || isGameStopped)
            return;
        if (!gameField[y][x].isFlag && countFlags != 0){
            gameField[y][x].isFlag = true;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.YELLOW);
            countFlags--;
        }else if (gameField[y][x].isFlag) {
            gameField[y][x].isFlag = false;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.ORANGE);
            countFlags++;
        }

    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (!gameField[y][x].isMine){
                    for (GameObject neighbors : getNeighbors(gameField[y][x])) {
                        if (neighbors.isMine)
                            gameField[y][x].countMineNeighbors++;
                    }
                    System.out.println(gameField[y][x].countMineNeighbors);
                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
}