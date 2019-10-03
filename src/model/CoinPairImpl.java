package model;

import model.interfaces.Coin;
import model.interfaces.CoinPair;

import java.util.Objects;

public final class CoinPairImpl implements CoinPair {
    private Coin coin1;
    private Coin coin2;

    public CoinPairImpl() {
        coin1 = new CoinImpl(1);
        coin2 = new CoinImpl(2);
    }

    public CoinPairImpl(Coin coin1, Coin coin2) {
        if (coin1 == null || coin2 == null)
            throw new IllegalArgumentException();

        this.coin1 = coin1;
        this.coin2 = coin2;
    }

    @Override
    public Coin getCoin1() {
        return coin1;
    }

    @Override
    public Coin getCoin2() {
        return coin2;
    }

    @Override
    public boolean equals(CoinPair coinPair) {
        return coinPair.getCoin1().equals(coin1) && coinPair.getCoin2().equals(coin2);
    }

    @Override
    public boolean equals(Object coinPair) {
        if (coinPair == this)
            return true;

        if (!(coinPair instanceof CoinPair)) {
            return false;
        }

        return equals((CoinPair) coinPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coin1, coin2);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", coin1.toString(), coin2.toString());
    }
}
