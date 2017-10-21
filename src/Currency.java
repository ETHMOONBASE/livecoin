import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("currencyPairs")
    @Expose
    private List<CurrencyPairs> currencyPairs = null;

    public List<CurrencyPairs> getCurrencyPairs() {
        return currencyPairs;
    }

    public void setCurrencyPairs(List<CurrencyPairs> currencyPairs) {
        this.currencyPairs = currencyPairs;
    }

}