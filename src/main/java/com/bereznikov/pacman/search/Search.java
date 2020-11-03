package com.bereznikov.pacman.search;

import com.bereznikov.pacman.Board;

import java.util.Arrays;
import java.util.List;

public abstract class Search {
    private List<Integer> checkedPos;
    private int sumOfSteps;
    private Board board;
    private MazeSearchTree theTree;
    private List<String> moves;
    private short[] fieldForPacman;

    public Search() {
    }

    public Search(List<Integer> checkedPos, int sumOfSteps, Board board, List<String> moves) {
        this.checkedPos = checkedPos;
        this.sumOfSteps = sumOfSteps;
        this.board = board;
        this.moves = moves;

    }

    public short[] copyArray(short[] screenData) {
        short[] result = new short[screenData.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = screenData[i];
        }
        return result;
    }

    public abstract List<String> findAlgo();

    public abstract MazeSearchTree makeTree(MazeSearchTree theTree);

    public int getSumOfSteps() {
        return sumOfSteps;
    }

    public void setSumOfSteps(int sumOfSteps) {
        this.sumOfSteps = sumOfSteps;
    }

    public Board getBoard() {
        return board;
    }

    public MazeSearchTree getTheTree() {
        return theTree;
    }

    public void setTheTree(MazeSearchTree theTree) {
        this.theTree = theTree;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public List<Integer> getCheckedPos() {
        return checkedPos;
    }

    public void setCheckedPos(List<Integer> checkedPos) {
        this.checkedPos = checkedPos;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getPacmanIdInScreenData() {
        int pacmanCellX = getCellId(getBoard().getPacman_x(), getBoard().getBLOCK_SIZE());
        int pacmanCellY = getCellId(getBoard().getPacman_y(), getBoard().getBLOCK_SIZE());
        return getBoard().getN_BLOCKS() * (pacmanCellY - 1) + pacmanCellX;
    }

    private int getCellId(int pacmanCoord, int blockSize) {
        int sum = 1;
        while (pacmanCoord - blockSize >= 0) {
            pacmanCoord -= blockSize;
            sum++;
        }
        return sum;
    }

    public short[] getFieldForPacman() {
        return fieldForPacman;
    }

    public void setFieldForPacman(short[] fieldForPacman) {
        this.fieldForPacman = fieldForPacman;
    }
}
