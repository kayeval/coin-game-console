package model;

import model.enumeration.BetType;
import model.interfaces.Coin;
import model.interfaces.CoinPair;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import view.interfaces.GameEngineCallback;

import java.util.*;

public class GameEngineImpl implements GameEngine {
    private Map<String, Player> players;
    private List<GameEngineCallback> gameEngineCallbacks;
    private Thread coin1Spin, coin2Spin;
    private CoinPair spinnerCoinPair;

    public GameEngineImpl() {
        players = new HashMap<>();
        gameEngineCallbacks = new ArrayList<>();
    }

    @Override
    public void spinPlayer(Player player, int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CoinPair finalCoins = spinCoins(player, player.getResult().getCoin1(), player.getResult().getCoin2(), initialDelay1, finalDelay1, delayIncrement1, initialDelay2, finalDelay2, delayIncrement2);

                    coin1Spin.join();
                    coin2Spin.join();

                    player.setResult(finalCoins);

                    for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                        gameEngineCallback.playerResult(player, finalCoins, GameEngineImpl.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void spinSpinner(int initialDelay1, int finalDelay1, int delayIncrement1, int initialDelay2, int finalDelay2, int delayIncrement2) throws IllegalArgumentException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spinnerCoinPair = new CoinPairImpl();
                    CoinPair finalCoins = spinCoins(null, spinnerCoinPair.getCoin1(), spinnerCoinPair.getCoin2(), initialDelay1, finalDelay1, delayIncrement1, initialDelay2, finalDelay2, delayIncrement2);

                    coin1Spin.join();
                    coin2Spin.join();

                    applyBetResults(finalCoins);

                    for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                        gameEngineCallback.spinnerResult(finalCoins, GameEngineImpl.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
        return players.remove(player.getPlayerId()) != null;
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

        coin1Spin = new Thread(new Runnable() {
            @Override
            public void run() {
                int delay1 = initialDelay1;

                while (delay1 < finalDelay1) {
                    coin1.flip();

                    for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                        if (player == null)
                            gameEngineCallback.spinnerCoinUpdate(coin1, GameEngineImpl.this);
                        else
                            gameEngineCallback.playerCoinUpdate(player, coin1, GameEngineImpl.this);

                    try {
                        Thread.sleep(delay1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    delay1 += delayIncrement1;
                }
            }
        });

        coin1Spin.start();

        coin2Spin = new Thread(new Runnable() {
            @Override
            public void run() {
                int delay2 = initialDelay2;

                while (delay2 < finalDelay2) {
                    coin2.flip();

                    for (GameEngineCallback gameEngineCallback : gameEngineCallbacks)
                        if (player == null)
                            gameEngineCallback.spinnerCoinUpdate(coin2, GameEngineImpl.this);
                        else
                            gameEngineCallback.playerCoinUpdate(player, coin2, GameEngineImpl.this);

                    try {
                        Thread.sleep(delay2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    delay2 += delayIncrement2;
                }
            }
        });

        coin2Spin.start();

        return new CoinPairImpl(coin1, coin2);
    }
}
