package src.GUI;

import src.Game.GC;
import src.Game.GW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndScreen extends JFrame {
    private JFrame menuFrame;
    private JButton start = new JButton("Restart");
    private JButton end = new JButton("End");
    private JLabel winText = new JLabel();
    private JLabel scoreText = new JLabel();

    public void init(){

        this.menuFrame = new JFrame("Tank Game");
        this.menuFrame.setSize(GC.SCREEN_WIDTH, GC.SCREEN_HEIGHT + 30);

        this.menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.menuFrame.setLayout(new BorderLayout());
       // this.menuFrame.add(this);
        this.menuFrame.setResizable(false);
        this.menuFrame.setLocationRelativeTo(null);
        this.menuFrame.setVisible(false);

        JPanel titlePanel= new JPanel();

        winText.setPreferredSize(new Dimension(300,300));
        titlePanel.add(winText);
        titlePanel.add(scoreText,BOTTOM_ALIGNMENT);
        menuFrame.add(titlePanel,BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
       // buttonPanel.add(start);
        buttonPanel.add(end);
        JLabel myName = new JLabel();
        myName.setText("Dylan Luong, 413-03");
        buttonPanel.add(myName, BorderLayout.PAGE_END);
        menuFrame.add(buttonPanel,BorderLayout.SOUTH);
/*        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TRE.reset();
                TRE.setIsOn(true);
                TRE.getJf().setVisible(true);
                menuFrame.setVisible(false);
                menuFrame.dispose();
            }
        });*/
        end.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public void execute(int playerID, boolean isWin){
        int score =GW.getScore();
        GW.getJf().setVisible(false);
        GW.setIsOn(false);
        this.menuFrame.setVisible(true);
        if(isWin)
            winText.setText("Player "+playerID+" Wins");
        else
            winText.setText("Player "+playerID+" Lost");
        scoreText.setText("\nScore: "+score);
    }

}
