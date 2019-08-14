package model;

import model.interfaces.Coin;
import model.interfaces.CoinPair;

public final class CoinPairImpl implements CoinPair {
    private CoinImpl coin1;
    private CoinImpl coin2;

    public CoinPairImpl() {
        coin1 = new CoinImpl(1);
        coin2 = new CoinImpl(2);
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
        if (coinPair instanceof CoinPair)
            return equals((CoinPair) coinPair);

        return false;
    }

    @Override
    public int hashCode() {


        return super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s, %s", coin1.toString(), coin2.toString());
    }
}
