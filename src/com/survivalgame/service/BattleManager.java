package com.survivalgame.service;

import entity.Monster;
import entity.Player;

public class BattleManager {

    private static final BattleManager instance = new BattleManager();
    private BattleManager() {}
    public static BattleManager getInstance() {
        return instance;
    }

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void startBattle(Monster monster) {
        while (!monster.isDead() && player.getHp() > 0) {
            boolean killSuccess = player.attackMonster(monster);
            if (killSuccess) {
                monster.die(player);
                System.out.println("战斗胜利！");
                break;
            }
            monster.attack(player);
            if (player.getHp() <= 0) {
                System.out.println("战斗失败，玩家死亡");
                player.checkGameOver();
                break;
            }
        }
    }
}