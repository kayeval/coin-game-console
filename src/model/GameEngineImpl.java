package model;

import model.enumeration.BetType;
import model.interfaces.Coin;
import model.interfaces.CoinPair;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import view.interfaces.GameEngineCallback;

import java.util.*;

//TODO : IMPLEMENT METHODS + CONSTRUCTOR?

public class GameEngineImpl implements GameEngine {
    private HashMap<String, Player> players = new HashMap<>();
    private List<GameEngineCallback> gameEngineCallbacks = new ArrayList<>();

    public GameEngineImpl() {

    }

    @Override
    public void spinPlayer(Player player, int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {
        CoinPair finalCoins = spinCoins(player, player.getResult().getCoin1(), player.getResult().getCoin2(), initialDelay1, finalDelay1, delayIncrement1, initialDelay2, finalDelay2, delayIncrement2);
        player.setResult(finalCoins);

        for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
            gameEngineCallback.playerResult(player, finalCoins, this);
    }

    @Override
    public void spinSpinner(int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {
        CoinPair coinPair = new CoinPairImpl();
        CoinPair finalCoins = spinCoins(null, coinPair.getCoin1(), coinPair.getCoin2(), initialDelay1, finalDelay1, delayIncrement1, initialDelay2, finalDelay2, delayIncrement2);

        for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
            gameEngineCallback.spinnerResult(coinPair, this);
    }

    @Override
    public void applyBetResults(CoinPair spinnerResult) {
        for (Player player : players.values()) {
            player.getBetType().applyWinLoss(player, spinnerResult);
        }
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

    private CoinPair spinCoins(Player player, Coin coin1, Coin coin2, int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) {
        if (initialDelay1 < 0 || finalDelay1 < 0 || delayIncrement1 < 0 || initialDelay2 < 0 || finalDelay2 < 0 || delayIncrement2 < 0)
            throw new IllegalArgumentException();

        if (finalDelay1 < initialDelay1 || finalDelay2 < initialDelay2)
            throw new IllegalArgumentException();

        if (delayIncrement1 > (finalDelay1 - initialDelay1) || delayIncrement2 > (finalDelay2 - initialDelay2))
            throw new IllegalArgumentException();

        int delay = initialDelay1;

        while (delay < finalDelay1) {
            coin1.flip();
            coin2.flip();

            for (Coin coin : new Coin[]{coin1, coin2})
                for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                    if (player == null)
                        gameEngineCallback.spinnerCoinUpdate(coin, this);
                    else
                        gameEngineCallback.playerCoinUpdate(player, coin, this);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            delay += delayIncrement1;
        }

        return new CoinPairImpl(coin1, coin2);
    }
}
