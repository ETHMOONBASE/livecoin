

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyPairs {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("maxBid")
    @Expose
    private String maxBid;
    @SerializedName("minAsk")
    @Expose
    private String minAsk;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(String maxBid) {
        this.maxBid = maxBid;
    }

    public String getMinAsk() {
        return minAsk;
    }

    public void setMinAsk(String minAsk) {
        this.minAsk = minAsk;
    }

}