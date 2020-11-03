package com.bereznikov.pacman.search;

import com.bereznikov.pacman.Board;
import com.bereznikov.pacman.console.ConsoleHelper;

import java.util.*;

public class ASearchImpl extends Search {
    private int pointId;
    private int lastPacManPos;
    private boolean isFirst = true;
    private List<String> allMovesTogether;

    public ASearchImpl(Board theBoard) {
        super(new ArrayList<>(), 0, theBoard, new ArrayList<>());
        allMovesTogether = new ArrayList<>();
    }

    @Override
    public List<String> findAlgo() {
        printFieldForPacman();
        int pacmanPos;

        if(isFirst) {
            pacmanPos = getPacmanIdInScreenData();
            isFirst = false;
        } else {
            pacmanPos = lastPacManPos + 1;
        }

        MazeSearchTree tree = new MazeSearchTree(pacmanPos);
        setTheTree(tree);
        long startTime = System.nanoTime();

        makeTree(getTheTree());
        findTrueInTree(getTheTree());

        long endTime = System.nanoTime();

        ConsoleHelper.print("Time: " + (endTime - startTime) + " ns");
        ConsoleHelper.print("Steps: " + getSumOfSteps());
        ConsoleHelper.print("Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB");

        Collections.reverse(getMoves());
        allMovesTogether.addAll(getMoves());
        ConsoleHelper.print(getMoves().toString());
        getMoves().clear();

        setSumOfSteps(0);
        getCheckedPos().clear();
        //remove eaten point
        removePoint();
        return allMovesTogether;
    }

    @Override
    public MazeSearchTree makeTree(MazeSearchTree theTree) {
        if (theTree.getPos() - 1 == getPointIdInScreenData()) {
            theTree.setFound(true);
            theTree.setWayOut(true);
            pointId = theTree.getPos() - 1;
            lastPacManPos = pointId;
            return theTree;
        }

        Map<MazeSearchTree, Integer> treeAndValue = new HashMap<>();

        if ((getFieldForPacman()[theTree.getPos() - 1] & 8) == 0 && theTree.getDown() == null) {

            int currPos = theTree.getPos() + getBoard().getN_BLOCKS();
            MazeSearchTree downTree = new MazeSearchTree(currPos);

            downTree.setUp(theTree);
            theTree.setDown(downTree);

            int valueLength = findLengthBetweenPacmanAndPoint(currPos);
            treeAndValue.put(downTree, valueLength);
        }
        if ((getFieldForPacman()[theTree.getPos() - 1] & 4) == 0 && theTree.getRight() == null) {
            int currPos = theTree.getPos() + 1;
            MazeSearchTree rightTree = new MazeSearchTree(currPos);

            rightTree.setLeft(theTree);
            theTree.setRight(rightTree);

            int valueLength = findLengthBetweenPacmanAndPoint(currPos);
            treeAndValue.put(rightTree, valueLength);
        }

        if ((getFieldForPacman()[theTree.getPos() - 1] & 2) == 0 && theTree.getUp() == null) {
            int currPos = theTree.getPos() - getBoard().getN_BLOCKS();
            MazeSearchTree upTree = new MazeSearchTree(currPos);

            upTree.setDown(theTree);
            theTree.setUp(upTree);

            int valueLength = findLengthBetweenPacmanAndPoint(currPos);
            treeAndValue.put(upTree, valueLength);
        }

        if ((getFieldForPacman()[theTree.getPos() - 1] & 1) == 0 && theTree.getLeft() == null) {
            int currPos = theTree.getPos() - 1;
            MazeSearchTree leftTree = new MazeSearchTree(currPos);

            leftTree.setRight(theTree);
            theTree.setLeft(leftTree);

            int valueLength = findLengthBetweenPacmanAndPoint(currPos);
            treeAndValue.put(leftTree, valueLength);
        }

        MazeSearchTree finalTree = getBestTreeByValue(treeAndValue);

        if (finalTree != null) {
            makeTree(finalTree);
            if (finalTree.isWayOut()) {
                theTree.setWayOut(true);
                return theTree;
            }
        }

        treeAndValue.remove(finalTree);

        MazeSearchTree finalTreeAnother = null;
        if (treeAndValue.size() > 0) {
            finalTreeAnother = getBestTreeByValue(treeAndValue);
            makeTree(finalTreeAnother);
        }
        if (finalTreeAnother != null && finalTreeAnother.isWayOut()) {
            theTree.setWayOut(true);
            return theTree;
        }
        return finalTreeAnother;
    }

    private MazeSearchTree getBestTreeByValue(Map<MazeSearchTree, Integer> treeAndValue) {
        MazeSearchTree minTree = null;
        int minValue = Integer.MAX_VALUE;
        for (Map.Entry<MazeSearchTree, Integer> entry : treeAndValue.entrySet()) {
            if (entry.getValue() < minValue) {
                minTree = entry.getKey();
                minValue = entry.getValue();
            }
        }
        return minTree;
    }

    private int findLengthBetweenPacmanAndPoint(int pacmanId) {
        int pointId = getPointIdInScreenData();
        if (pacmanId < pointId) {
            return findValueLength(pacmanId, pointId);
        } else {
            return findValueLength(pointId, pacmanId);
        }

    }

    private int findValueLength(int pacmanId, int pointId) {
        int row = 0;
        int column = 0;

        while (pacmanId != (pointId + 1)) {
            if (pacmanId % 15 == 0) {
                column++;
                row = 0;
            } else {
                row++;
            }
            pacmanId++;
        }

        return row + column;
    }

    private int getPointIdInScreenData() {
        short[] screenData = getFieldForPacman();

        for (int i = 0; i < screenData.length; i++) {
            if ((screenData[i] & 16) != 0) {
                return i;
            }
        }
        return -1;
    }


    private void removePoint() {
        if ((getFieldForPacman()[pointId] & 16) != 0) {
            getFieldForPacman()[pointId] -= 16;
        }
    }

    private void printFieldForPacman() {
        for (int i = 1; i <= getFieldForPacman().length; i++) {
            System.out.print(getFieldForPacman()[i - 1] + " ");
            if(i % 15 == 0) {
                System.out.println();
            }
        }
    }

    public int findTrueInTree(MazeSearchTree theTree) {
        getCheckedPos().add(theTree.getPos());

        setSumOfSteps(getSumOfSteps() + 1);
        int res = theTree.getPos();
        if (theTree.isFound()) {
            return res;
        }


        if (theTree.getDown() != null && theTree.getDown().isWayOut() && !getCheckedPos().contains(theTree.getDown().getPos())) {
            findTrueInTree(theTree.getDown());
            getMoves().add("down");
        }

        else if (theTree.getRight() != null && theTree.getRight().isWayOut() && !getCheckedPos().contains(theTree.getRight().getPos())) {
            findTrueInTree(theTree.getRight());
            getMoves().add("right");
        }

        else if (theTree.getUp() != null && theTree.getUp().isWayOut() && !getCheckedPos().contains(theTree.getUp().getPos())) {
            findTrueInTree(theTree.getUp());
            getMoves().add("up");
        }

        else if (theTree.getLeft() != null && theTree.getLeft().isWayOut() && !getCheckedPos().contains(theTree.getLeft().getPos())) {
            findTrueInTree(theTree.getLeft());
            getMoves().add("left");
        }
        return -1;
    }


}
