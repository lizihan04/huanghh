package gameUI.controller;

import entity.Player2;

public class DoRest {
    private Player2 player = Player2.getInstance();
    public void doRest(){
        player.rest();
    }
}
