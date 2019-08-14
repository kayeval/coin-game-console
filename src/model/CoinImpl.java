package model;

import model.enumeration.CoinFace;
import model.interfaces.Coin;

import java.util.Random;

public class CoinImpl implements Coin {
    private int number;
    private CoinFace coinFace;

    public CoinImpl(int number) {
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
        //TODO: TEST for all boolean methods: IF NULL PARAMETER, return val = false
        //need to check if same number or not?
        return coin.getFace() == this.getFace() && coin.getNumber() == this.getNumber();
    }

    @Override
    public boolean equals(Object coin) {

        if (coin instanceof Coin)
            return equals((Coin) coin);

        return false;
    }

    @Override
    public int hashCode() {
        //NOTE: if equals() is true then generated hashCode should also be equal


        return 0;
    }

    @Override
    public String toString() {
        return String.format("Coin %d: %s", number, (getFace().toString().substring(0, 1) + getFace().toString().substring(1).toLowerCase()));
    }
}
