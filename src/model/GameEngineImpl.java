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
//      1. coins are initialised at their random starting face
//    * 2. for each coin start at initialDelay then increment the delays for each coin on each iteration
//    * 3. call GameEngineCallback.playerCoinUpdate(...) for each coin separately
//    * 4. continue until both delays {@literal >=} finalDelay
//    * 5. call GameEngineCallback.playerResult(...) to finish
//    * NOTE: for assignment 1 you can assume the values passed for the delays are the same for coins 1 and 2
//          * and therefore optionally use only the first three delay parameters

//        @throws IllegalArgumentException thrown when: <UL>
//    * <LI> if any of the delay params are < 0
//    * <LI> either of the finalDelay < initialDelay
//    * <LI> either of the delayIncrement > (finalDelay - initialDelay)

        int delay = initialDelay1;
        CoinPair coinPair = null;

        while (delay < finalDelay1) {
            coinPair = new CoinPairImpl();

            player.setResult(coinPair);
            Coin[] coins = {coinPair.getCoin1(), coinPair.getCoin2()};

            for (Coin coin : coins)
                for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                    gameEngineCallback.playerCoinUpdate(player, coin, this);

            delay += delayIncrement1;
        }

        for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
            gameEngineCallback.playerResult(player, coinPair, this);
    }

    @Override
    public void spinSpinner(int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {
        int delay = initialDelay1;
        CoinPair coinPair = null;

        while (delay < finalDelay1) {
            coinPair = new CoinPairImpl();
            Coin[] coins = {coinPair.getCoin1(), coinPair.getCoin2()};

            for (Coin coin : coins)
                for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                    gameEngineCallback.spinnerCoinUpdate(coin, this);

            delay += delayIncrement1;
        }

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
}
