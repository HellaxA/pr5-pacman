package com.bereznikov.pacman;

import com.bereznikov.pacman.search.Search;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class RobotMovementImpl implements RobotMovement {
    private Board theBoard;
    private Robot robot;
    private Search search;

    public RobotMovementImpl() {
    }

    public RobotMovementImpl(Robot robot, Board theBoard, Search search) {
        this.robot = robot;
        this.theBoard = theBoard;
        this.search = search;

    }

    public void start() {
        search.setFieldForPacman(search.copyArray(theBoard.getLevelData()));

        startGame();
        List<String> moves = null;
        for (int i = 0; i < 3; i++) {
            moves = search.findAlgo();
        }
        System.out.println(moves);
        goToPoint(moves);

    }

    private void goToPoint(List<String> moves) {
        for (String move : moves) {
            if (move.equals("down")) {
                moveDown();
            } else if (move.equals("right")) {
                moveRight();
            } else if (move.equals("up")) {
                moveUp();
            } else if (move.equals("left")) {
                moveLeft();
            }
        }
    }

    private void startGame() {
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_S);
    }

    @Override
    public void moveRight() {
        move(KeyEvent.VK_RIGHT);
    }

    @Override
    public void moveLeft() {
        move(KeyEvent.VK_LEFT);
    }

    @Override
    public void moveUp() {
        move(KeyEvent.VK_UP);
    }

    @Override
    public void moveDown() {
        move(KeyEvent.VK_DOWN);
    }

    private void move(int button) {
        robot.keyPress(button);
        robot.delay(970);
    }


    public Board getTheBoard() {
        return theBoard;
    }

    public void setTheBoard(Board theBoard) {
        this.theBoard = theBoard;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
