package view;

import model.interfaces.Coin;
import model.interfaces.CoinPair;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import view.interfaces.GameEngineCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Skeleton implementation of GameEngineCallback showing Java logging behaviour
 *
 * @author Caspar Ryan
 * @see view.interfaces.GameEngineCallback
 */

public class GameEngineCallbackImpl implements GameEngineCallback {
    private static final Logger logger = Logger.getLogger(GameEngineCallback.class.getName());

    public GameEngineCallbackImpl() {
        // NOTE need to also set the console to FINE in %JRE_HOME%\lib\logging.properties
        logger.setLevel(Level.FINE);
    }

    public void playerCoinUpdate(Player player, Coin coin, GameEngine engine) {
        // intermediate results logged at Level.FINE
        //TODO: CHANGE LEVEL BACK TO FINE
        logger.log(Level.INFO, String.format("%s coin %d flipped to %s", player.getPlayerName(), coin.getNumber(),
                coin.getFace().toString().substring(0, 1) + coin.getFace().toString().substring(1).toLowerCase()));
    }

    @Override
    public void spinnerCoinUpdate(Coin coin, GameEngine engine) {
        //TODO: CHANGE LEVEL BACK TO FINE
        logger.log(Level.INFO, String.format("Spinner coin %d flipped to %s", coin.getNumber(),
                coin.getFace().toString().substring(0, 1) + coin.getFace().toString().substring(1).toLowerCase()));
    }

    public void playerResult(Player player, CoinPair coinPair, GameEngine engine) {
        // final results logged at Level.INFO
        logger.log(Level.INFO, String.format("%s, final result=%s", player.getPlayerName(), coinPair.toString()));
    }

    @Override
    public void spinnerResult(CoinPair coinPair, GameEngine engine) {
        logger.log(Level.INFO, String.format("Spinner, final result=%s", coinPair.toString()));
    }
}
