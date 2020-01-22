/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Components;


import src.Objects.Paddle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 *
 */
public class Control implements KeyListener {

    private Paddle t1;
    private final int right;
    private final int left;
    private final int respawn;
    
    public Control(Paddle t1, int left, int right, int respawn) {
        this.t1 = t1;
        this.right = right;
        this.left = left;
        this.respawn = respawn;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == right) {
           this.t1.toggleRightPressed();
        }
        if (keyPressed == left) {
            this.t1.toggleLeftPressed();
        }
        if (keyPressed == respawn) {
           this.t1.toggleRespawnPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased  == right) {
            this.t1.unToggleRightPressed();
        }
        if (keyReleased == left) {
            this.t1.unToggleLeftPressed();
        }
        if (keyReleased == respawn) {
            this.t1.unToggleRespawnPressed();
        }
    }
}
