package TradeException;

public class LowerVersionTradeException extends Exception {
    public LowerVersionTradeException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}
