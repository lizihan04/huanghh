package entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // 单例
    private static final Player instance = new Player();
    private Player(){}
    public static Player getInstance(){
        return instance;
    }

    // 生存属性（饱和体系 0~100）
    private int hp;         // 血量
    private int hunger;     // 饥饿值
    private int thirst;     // 口渴值
    private int fatigue;    // 疲惫值（精力：**初始30，行动增加，满100死亡**）

    // 行动规则属性
    private int actionPoint; // 每日行动点 上限10
    private int day;        // 生存天数 上限30

    // 战斗属性
    private int baseAttack;  // 基础攻击力
    private int defense;     // 防御力

    // 地图与状态
    private String currentArea; // 当前区域：树林/沙滩/岩石区/海边
    private List<Item> backpack;// 背包
    private int lighthouseProgress; // 灯塔建造进度 0~100

    // 游戏状态
    private boolean isGameOver;
    private boolean isGameWin;

    // 初始化玩家所有数据
    public void initPlayer(){
        this.hp = 100;
        this.hunger = 80;
        this.thirst = 80;
        this.fatigue = 20;

        this.actionPoint = 10;
        this.day = 1;

        this.baseAttack = 10;
        this.defense = 5;

        this.currentArea = "沙滩";
        this.backpack = new ArrayList<>();
        this.lighthouseProgress = 0;

        this.isGameOver = false;
        this.isGameWin = false;
    }

    // 执行一次行动：消耗1行动点 + **疲惫+3**
    public void doAction(String targetArea){
        if(isGameOver || isGameWin || actionPoint <= 0){
            System.out.println("行动力不足或游戏已结束，无法行动！");
            return;
        }
        // 消耗行动点
        actionPoint--;

        // 行动增加疲惫3点
        this.fatigue = Math.min(100, this.fatigue + 3);
        System.out.println("本次行动疲惫+3，当前疲惫值：" + fatigue);


        // 行动后判定死亡（疲惫满100也算死亡）
        checkGameOver();
    }

    // 休息：消耗行动点，不同区域恢复不同数值
    public void rest(){
        if(isGameOver || isGameWin || actionPoint <= 0){
            System.out.println("行动力不足，无法休息！");
            return;
        }
        actionPoint--;
        if("沙滩".equals(currentArea)){
            this.fatigue = Math.max(0, this.fatigue - 20); // 休息**减少**疲惫
            this.hp = Math.min(100, this.hp + 10);
            System.out.println("在沙滩休息，疲惫-20，血量+10");
        }else{
            this.fatigue = Math.max(0, this.fatigue - 10);
            System.out.println("在" + currentArea + "休息，疲惫-10");
        }
        checkGameOver();
    }

    // 进入下一天：重置行动点、扣除饥饿口渴
    public void nextDay(){
        if(isGameOver || isGameWin) return;
        day++;
        // 超过30天判定
        if(day > 30){
            checkGameOver();
            return;
        }
        // 每日重置行动点为10
        actionPoint = 10;
        // 每日自然消耗饥饿、口渴
        this.hunger = Math.max(0, this.hunger - 10);
        this.thirst = Math.max(0, this.thirst - 5);
        System.out.println("===== 第 " + day + " 天 =====");
        checkGameOver();
    }

    // 建造灯塔
    public void buildLighthouse(){
        if(isGameOver || isGameWin) return;
        lighthouseProgress += 20;
        lighthouseProgress = Math.min(100, lighthouseProgress);
        System.out.println("灯塔建造进度：" + lighthouseProgress + "%");
        checkGameOver();
    }

    // 游戏结束判定规则
    public void checkGameOver(){
        // 失败条件：疲惫满100 / 血量/饥饿/口渴归0
        if(fatigue >= 100 || hp <= 0 || hunger <= 0 || thirst <= 0){
            this.isGameOver = true;
            if(fatigue >= 100){
                System.out.println("疲惫值达到100，过劳死亡！游戏结束！");
            }else{
                System.out.println("游戏结束！未在规定时间内完成任务");
            }
            return;
        }
        // 胜利条件：30天内 + 灯塔建造完成
        if(day <= 30 && lighthouseProgress >= 100){
            this.isGameWin = true;
            System.out.println("恭喜！成功在时限内建造完灯塔，游戏通关！");
        }
    }

    // 背包添加物品（自动堆叠）
    public void addItem(Item item){
        for(Item i : backpack){
            if(i.getName().equals(item.getName())){
                i.addCount(item.getCount());
                return;
            }
        }
        backpack.add(item);
    }

    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(100, hp));
        checkGameOver();
    }
    public int getHunger() {
        return hunger;
    }
    public void setHunger(int hunger) {
        this.hunger = Math.max(0, Math.min(100, hunger));
        checkGameOver();
    }
    public int getThirst() {
        return thirst;
    }
    public void setThirst(int thirst) {
        this.thirst = Math.max(0, Math.min(100, thirst));
        checkGameOver();
    }
    public int getFatigue() {
        return fatigue;
    }
    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, Math.min(100, fatigue));
        checkGameOver();
    }
    public int getActionPoint() {
        return actionPoint;
    }
    public int getDay() {
        return day;
    }
    public String getCurrentArea() {
        return currentArea;
    }
    public void setCurrentArea(String currentArea) {
        this.currentArea = currentArea;
    }
    public List<Item> getBackpack() {
        return backpack;
    }
    public int getBaseAttack() {
        return baseAttack;
    }
    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }
    public int getDefense() {
        return defense;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public boolean isGameWin() {
        return isGameWin;
    }
    public int getLighthouseProgress() {
        return lighthouseProgress;
    }
}