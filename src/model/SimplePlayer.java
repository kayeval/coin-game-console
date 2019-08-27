package model;

import model.enumeration.BetType;
import model.interfaces.CoinPair;
import model.interfaces.Player;

public class SimplePlayer implements Player {
    private final String playerID;
    private String playerName;
    private int points;
    private int bet;
    private BetType betType;
    private CoinPair coinPair;

    public SimplePlayer(String playerID, String playerName, int initialPoints) {
        this.playerID = playerID;
        setPlayerName(playerName);
        setPoints(initialPoints);
        resetBet();
        setResult(new CoinPairImpl());
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void setPlayerName(String playerName) {
        if (playerName.equals(""))
            throw new IllegalArgumentException();

        this.playerName = playerName;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        if (points < 0)
            throw new IllegalArgumentException();

        this.points = points;
    }

    @Override
    public String getPlayerId() {
        return playerID;
    }

    @Override
    public boolean setBet(int bet) {
        if (bet > 0 && (points - bet) >= 0) {
            this.bet = bet;
            return true;
        }

        return false;
    }

    @Override
    public int getBet() {
        return bet;
    }

    @Override
    public void setBetType(BetType betType) {
        if (betType == null)
            throw new IllegalArgumentException();

        this.betType = betType;
    }

    @Override
    public BetType getBetType() {
        return betType;
    }

    @Override
    public void resetBet() {
        this.bet = 0;
        setBetType(BetType.NO_BET);
    }

    @Override
    public CoinPair getResult() {
        return coinPair;
    }

    @Override
    public void setResult(CoinPair coinPair) {
        if (coinPair == null)
            throw new IllegalArgumentException();

        this.coinPair = coinPair;
    }

    @Override
    public String toString() {
        return String.format("Player: id=%s, name=%s, bet=%d, betType=%s, points=%d, " +
                "RESULT .. %s", getPlayerId(), getPlayerName(), getBet(), betType.toString(), getPoints(), coinPair.toString());
    }
}
