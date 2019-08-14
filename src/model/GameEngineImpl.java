package model;

import model.enumeration.BetType;
import model.interfaces.CoinPair;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import view.interfaces.GameEngineCallback;

import java.util.*;

//TODO : IMPLEMENT METHODS + CONSTRUCTOR?

public class GameEngineImpl implements GameEngine {
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private List<GameEngineCallback> gameEngineCallbacks = new ArrayList<>();

    public GameEngineImpl() {

    }

    @Override
    public void spinPlayer(Player player, int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {
        //call Player setResult


        //update view
        for (GameEngineCallback gameEngineCallback : gameEngineCallbacks) {
            gameEngineCallback.playerCoinUpdate(player, coin, this);
        }
    }

    @Override
    public void spinSpinner(int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {


        //update view
        for (GameEngineCallback gameEngineCallback : gameEngineCallbacks) {
            gameEngineCallback.spinnerCoinUpdate(coin, this);
        }

    }

    @Override
    public void applyBetResults(CoinPair spinnerResult) {
        //set player points here


    }

    @Override
    public void addPlayer(Player player) {
        Player p = players.putIfAbsent(player.getPlayerId(), player);

        if (p != null) {
            players.replace(player.getPlayerId(), player);
        }
    }

    @Override
    public Player getPlayer(String id) {
        return players.get(id);
    }

    @Override
    public boolean removePlayer(Player player) {
        if (players.get(player.getPlayerId()) == null)
            return false;

        players.remove(player.getPlayerId());
        return true;
    }

    @Override
    public void addGameEngineCallback(GameEngineCallback gameEngineCallback) {
        gameEngineCallbacks.add(gameEngineCallback);
    }

    @Override
    public boolean removeGameEngineCallback(GameEngineCallback gameEngineCallback) {
        return gameEngineCallbacks.remove(gameEngineCallback);
    }

    @Override
    public Collection<Player> getAllPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    @Override
    public boolean placeBet(Player player, int bet, BetType betType) {
        if (player.setBet(bet)) {
            player.setBetType(betType);
            return true;
        } else
            player.resetBet();

        return false;
    }
}
