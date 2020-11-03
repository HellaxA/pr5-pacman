package com.bereznikov.pacman.search;

import com.bereznikov.pacman.Board;
import com.bereznikov.pacman.console.ConsoleHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DepthSearchImpl extends Search {

    public DepthSearchImpl(Board theBoard) {
        super(new ArrayList<>(), 0, theBoard, new ArrayList<>());
    }

    @Override
    public List<String> findAlgo() {

        int pacmanPos = getPacmanIdInScreenData();
        setTheTree(new MazeSearchTree(pacmanPos));


        long startTime = System.nanoTime();

        makeTree(getTheTree());
        findTrueInTree(getTheTree());

        long endTime = System.nanoTime();

        ConsoleHelper.print("Time: " + (endTime - startTime) + " ns");
        ConsoleHelper.print("Steps: " + getSumOfSteps());
        ConsoleHelper.print("Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB");
        Collections.reverse(getMoves());
        ConsoleHelper.print(getMoves().toString());

        return getMoves();
    }

    @Override
    public MazeSearchTree makeTree(MazeSearchTree theTree) {
        getCheckedPos().add(theTree.getPos());

        if ((getBoard().getScreenData()[theTree.getPos() - 1] & 16) != 0) {
            theTree.setFound(true);
            theTree.setWayOut(true);
            return theTree;
        }
        if ((getBoard().getScreenData()[theTree.getPos() - 1] & 8) == 0 && theTree.getDown() == null) {

            int currPos = theTree.getPos() + getBoard().getN_BLOCKS();
            if (!getCheckedPos().contains(currPos)) {

                MazeSearchTree subtree = new MazeSearchTree(currPos);

                theTree.setDown(subtree);

                makeTree(subtree);

                if (theTree.getDown().isWayOut()) {
                    theTree.setWayOut(true);
                    return theTree;
                }
            }
        }


        if ((getBoard().getScreenData()[theTree.getPos() - 1] & 4) == 0 && theTree.getRight() == null) {

            int currPos = theTree.getPos() + 1;
            if (!getCheckedPos().contains(currPos)) {

                MazeSearchTree subtree = new MazeSearchTree(currPos);

                theTree.setRight(subtree);

                makeTree(subtree);

                if (theTree.getRight().isWayOut()) {
                    theTree.setWayOut(true);
                    return theTree;
                }
            }
        }
        if ((getBoard().getScreenData()[theTree.getPos() - 1] & 2) == 0 && theTree.getUp() == null) {

            int currPos = theTree.getPos() - getBoard().getN_BLOCKS();
            if (!getCheckedPos().contains(currPos)) {

                MazeSearchTree subtree = new MazeSearchTree(currPos);

                theTree.setUp(subtree);

                makeTree(subtree);

                if (theTree.getUp().isWayOut()) {
                    theTree.setWayOut(true);
                    return theTree;
                }
            }
        }
        if ((getBoard().getScreenData()[theTree.getPos() - 1] & 1) == 0 && theTree.getLeft() == null) {
            int currPos = theTree.getPos() - 1;
            if (!getCheckedPos().contains(currPos)) {

                MazeSearchTree subtree = new MazeSearchTree(currPos);

                theTree.setLeft(subtree);

                makeTree(subtree);

                if (theTree.getLeft().isWayOut()) {
                    theTree.setWayOut(true);
                    return theTree;
                }
            }
        }
        return null;
    }

    public int findTrueInTree(MazeSearchTree theTree) {
        setSumOfSteps(getSumOfSteps() + 1);
        int res = theTree.getPos();
        if (theTree.isFound()) {
            return res;
        }

        if (theTree.getDown() != null) {
            int resFromRecur = findTrueInTree(theTree.getDown());
            if (resFromRecur != -1) {
                getMoves().add("down");
                return resFromRecur;
            }
        }

        if (theTree.getRight() != null) {
            int resFromRecur = findTrueInTree(theTree.getRight());
            if (resFromRecur != -1) {
                getMoves().add("right");
                return resFromRecur;
            }
        }

        if (theTree.getUp() != null) {
            int resFromRecur = findTrueInTree(theTree.getUp());
            if (resFromRecur != -1) {
                getMoves().add("up");
                return resFromRecur;
            }

        }

        if (theTree.getLeft() != null) {
            int resFromRecur = findTrueInTree(theTree.getLeft());
            if (resFromRecur != -1) {
                getMoves().add("left");
                return resFromRecur;
            }
        }
        return -1;
    }
}

