package gameUI.controller;

import entity.Player2;

public class NextDay {
    private gameUI.controller.Main main;
    public void setMainController(Main main) {
        this.main = main;
    }
    private Player2 player = Player2.getInstance();
    public void nextDay(){
        player.next_day();
    }
}
