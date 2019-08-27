package model;

import model.enumeration.CoinFace;
import model.interfaces.Coin;

import java.util.Objects;
import java.util.Random;

public class CoinImpl implements Coin {
    private final int number;
    private CoinFace coinFace;

    public CoinImpl(int number) {
        if (number < 0)
            throw new IllegalArgumentException();

        this.number = number;
        coinFace = CoinFace.values()[new Random().nextInt(CoinFace.values().length)];
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public CoinFace getFace() {
        return coinFace;
    }

    @Override
    public void flip() {
        if (coinFace == CoinFace.HEADS)
            coinFace = CoinFace.TAILS;
        else
            coinFace = CoinFace.HEADS;
    }

    @Override
    public boolean equals(Coin coin) {
        return coin.getFace() == this.getFace() && coin.getNumber() == this.getNumber();
    }

    @Override
    public boolean equals(Object coin) {
        if (coin == this)
            return true;

        if (coin == null || getClass() != coin.getClass()) {
            return false;
        }

        return equals((Coin) coin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, coinFace);
    }

    @Override
    public String toString() {
        return String.format("Coin %d: %s", number, getFace().toString());
    }
}
