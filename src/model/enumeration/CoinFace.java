package model.enumeration;

/**
 * <pre> Provided enum type for Further Programming representing a side (i.e. a face) of a Coin</pre>
 *
 * @author Caspar Ryan
 */
public enum CoinFace {
    HEADS {
        public String toString() {
            return "Heads";
        }
    }, TAILS {
        public String toString() {
            return "Tails";
        }
    }
}