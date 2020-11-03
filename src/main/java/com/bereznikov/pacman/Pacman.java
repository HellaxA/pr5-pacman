package com.bereznikov.pacman;

import com.bereznikov.pacman.search.ASearchImpl;
import com.bereznikov.pacman.search.DepthSearchImpl;
import com.bereznikov.pacman.search.GreedySearchImpl;
import com.bereznikov.pacman.search.Search;
import com.bereznikov.pacman.util.UtilMethods;

import java.awt.*;
import javax.swing.JFrame;

public class Pacman extends JFrame {
    public Pacman(Board theBoard) {

        initUI(theBoard);
    }

    private void initUI(Board theBoard) {

        add(theBoard);

        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 440);
        setLocationRelativeTo(null);

    }

    public static void main(String[] args) throws AWTException {
        Board theBoard = new Board();

        Pacman pacman = new Pacman(theBoard);

        EventQueue.invokeLater(() -> {
            pacman.setVisible(true);
        });
        //choose depth or a*

        //Search search = new DepthSearchImpl(theBoard);//Time: 192700 ns Steps: 22
        Search search = new ASearchImpl(theBoard);//Time: 377800 ns Steps: 22
        //Search search = new GreedySearchImpl(theBoard);//Time: 377800 ns Steps: 22

        RobotMovement robot = new RobotMovementImpl(new Robot(), theBoard, search);
        robot.start();

    }


}
