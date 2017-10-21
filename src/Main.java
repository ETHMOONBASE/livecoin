import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.deploy.net.HttpResponse;
import com.sun.istack.internal.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import static com.sun.deploy.perf.DeployPerfUtil.clear;

/**
 * Created by Franck on 20.10.2017.
 */
public class Main {

    public static void main(String[] args)  {
        try {
            while (true) {
                Thread.sleep(4000);
//        System.out.println(getJSON("https://api.livecoin.net/exchange/maxbid_minask",1000));

                Gson gson = new Gson();
                Currency attributionObj = gson.fromJson(getJSON("https://api.livecoin.net/exchange/maxbid_minask", 1000), Currency.class);
                List<CurrencyPairs> currencyPairsList = attributionObj.getCurrencyPairs();

//        for (int i = 0; i < currencyPairsList.size(); i++) {
//            System.out.println(currencyPairsList.get(i).getSymbol());
//        }

                Set<String> currencies = new HashSet<>();
                for (int i = 0; i < currencyPairsList.size(); i++) {
                    for (int j = 0; j < convertPairs(currencyPairsList.get(i).getSymbol()).length; j++) {
                        currencies.add(convertPairs(currencyPairsList.get(i).getSymbol())[j]);
                    }
                }

                for (Iterator iter = currencies.iterator(); iter.hasNext(); ) {
                    System.out.print(iter.next() + ", ");
                }

                List<String> listCur = new ArrayList<>();
                listCur.addAll(currencies);
                Map<String, Integer> hashMap = new HashMap<>();

                for (int j = 0; j < listCur.size(); j++) {
//            System.out.println(iter.next() + " ");
                    String s = listCur.get(j);
                    int k = 0;
                    for (int i = 0; i < currencyPairsList.size(); i++) {
                        if (currencyPairsList.get(i).getSymbol().startsWith(s) ||
                                currencyPairsList.get(i).getSymbol().endsWith(s)) {
                            k++;
                            System.out.println(s + " symbol : " + (k));
                            hashMap.put(s, k);
                        }
                    }
                }

                System.out.println(currencies.size());

                for (Map.Entry entry : hashMap.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + " Value: "
                            + entry.getValue());
                }

                hashMap.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .forEach(System.out::println);

//        System.out.println(hashMap.size());

                List<String> listPurpose = new ArrayList();


                for (Map.Entry entry : hashMap.entrySet()) {
                    if ((Integer) entry.getValue() >= 2 && (String) entry.getKey() != "USD"
                            && (String) entry.getKey() != "BTC"
                            && (String) entry.getKey() != "ETH"
                            && (String) entry.getKey() != "RUR"
                            && (String) entry.getKey() != "EUR") {
                        for (int i = 0; i < currencyPairsList.size(); i++) {
                            String s = (String) entry.getKey();
                            if (currencyPairsList.get(i).getSymbol().startsWith(s)) {
                                listPurpose.add(currencyPairsList.get(i).getSymbol());
                            }
                        }
                    }
                }

                String BTCETH = "ETH/BTC";
                String ETHUSD = "ETH/USD";
                String BTCUSD = "BTC/USD";
                CurrencyPairs currencyPairsBTCETH = null;
                CurrencyPairs currencyPairsETHUSD = null;
                CurrencyPairs currencyPairsBTCUSD = null;
                for (int i = 0; i < currencyPairsList.size(); i++) {
                    CurrencyPairs currency = currencyPairsList.get(i);
                    if (currency.getSymbol().equals(BTCETH)) {
                        currencyPairsBTCETH = currency;
                    }
                    if (currency.getSymbol().equals(ETHUSD)) {
                        currencyPairsETHUSD = currency;
                    }
                    if (currency.getSymbol().equals(BTCUSD)) {
                        currencyPairsBTCUSD = currency;
                    }
                }

                System.out.println(currencyPairsBTCETH.getMaxBid() + " MaxBid " + currencyPairsBTCETH.getMinAsk() + " Min Ask " +
                        currencyPairsBTCETH.getSymbol());

                System.out.println(currencyPairsBTCUSD.getMaxBid() + " MaxBid " + currencyPairsBTCUSD.getMinAsk() + " Min Ask " +
                        currencyPairsBTCUSD.getSymbol());
                System.out.println(currencyPairsETHUSD.getMaxBid() + " MaxBid " + currencyPairsETHUSD.getMinAsk() + " Min Ask " +
                        currencyPairsETHUSD.getSymbol());

                System.out.println("IN USD " + (
                        (1.0 / Double.parseDouble(currencyPairsBTCETH.getMinAsk())) * Double.parseDouble(currencyPairsETHUSD.getMaxBid())));


                for (int i = 0; i < listPurpose.size(); i++) {
                    System.out.println(listPurpose.get(i));
                }

                List<String> lists = new ArrayList<>();
                HashSet<String> set = new HashSet<>();
                for (int i = 0; i < listPurpose.size(); i++) {
                    String s[] = listPurpose.get(i).split("/");
                    set.add(s[0]);
                }
                lists.addAll(set);
                for (int i = 0; i < lists.size(); i++) {
                    System.out.println(lists.get(i));
                }
                System.out.println(lists.size());

                Map<String, String> map = new HashMap<>();

                for (String i : set)
                    map.put(i, null);

                for (int i = 0; i < lists.size(); i++) {
                    String s = "";
                    for (int j = 0; j < listPurpose.size(); j++) {
                        if (listPurpose.get(j).startsWith(lists.get(i))) {
                            s += listPurpose.get(j) + "&";
                            map.put(lists.get(i), s);


                        }
                    }
                }

                for (Map.Entry entry : map.entrySet()) {
                    System.out.print(entry.getKey() + " " + entry.getValue());
                }
                System.out.println(map.size());


                Map<String, String[]> map2 = new HashMap<>();
                for (String i : set)
                    map2.put(i, null);
                for (Map.Entry entry : map.entrySet()) {
                    String s = (String) entry.getValue();
                    map2.put((String) entry.getKey(), convertPairsTwo(s));
                }

                for (Map.Entry entry : map2.entrySet()) {
                    String[] s = (String[]) entry.getValue();
                    for (int i = 0; i < s.length; i++) {
                        System.out.print(entry.getKey() + " " + s[i] + " ");
                    }
                }
                System.out.println(map2.size());

                for (Map.Entry entry : map2.entrySet()) {
                    String[] s = (String[]) entry.getValue();
                    map2.put((String) entry.getKey(), addETHBTC(s));
                }

                for (Map.Entry entry : map2.entrySet()) {
                    String[] s = (String[]) entry.getValue();
                    for (int i = 0; i < s.length; i++) {
                        System.out.print(entry.getKey() + " " + s[i] + " ");
                    }
                }
                ////////////////////////////////////////////////////////////////////////////////////


                for (Map.Entry entry : map2.entrySet()) {
                    String[] s = (String[]) entry.getValue();
                    for (int i = 0; i < s.length; i++) {
                        System.out.print(entry.getKey() + " " + s[i] + " ");
                    }
                }

                System.out.println("\n");
                System.out.println(whichCoin(currencyPairsList, currencyPairsBTCETH, map2.get("HVN"), "BTC", "ETH"));
                String s = "";

                int k = 0;

                try {
                    for (Map.Entry entry : map2.entrySet()) {

                        System.out.println(entry.getKey() + " " + whichCoin(currencyPairsList, currencyPairsBTCETH,
                                (String[]) entry.getValue(), "BTC", "ETH") + " " + whichCoin2(currencyPairsList, currencyPairsBTCETH,
                                (String[]) entry.getValue(), "BTC", "ETH"));
                        s = (String) entry.getKey();

                    }
                } catch (Exception e) {
                    System.out.println("ошибка" + s);
                }

                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------------------");
                try {
                    for (Map.Entry entry : map2.entrySet()) {
                        String[] str = (String[]) entry.getValue();

                        if (whichCoin(currencyPairsList, currencyPairsBTCETH, str, "BTC", "ETH") > 1.0 &&
                                whichCoin(currencyPairsList, currencyPairsBTCETH, str, "BTC", "ETH") < 100) {
                            System.out.println(entry.getKey() + " " + whichCoin(currencyPairsList, currencyPairsBTCETH,
                                    (String[]) entry.getValue(), "BTC", "ETH"));
                            s = (String) entry.getKey();
                            System.out.println(entry.getKey() + " " + entry.getKey() + " - BTC");
                        }
                        if (whichCoin2(currencyPairsList, currencyPairsBTCETH, str, "BTC", "ETH") > 1.0 &&
                                whichCoin2(currencyPairsList, currencyPairsBTCETH, str, "BTC", "ETH") < 100) {
                            System.out.println(entry.getKey() + " " + whichCoin2(currencyPairsList, currencyPairsBTCETH, str, "BTC", "ETH"));
                            s = (String) entry.getKey();
                            System.out.println(entry.getKey() + " " + entry.getKey() + " - ETH");
                        }

                    }
                } catch (Exception e) {
                    System.out.println("ошибка" + s);
                }
            }
        } catch (Exception e) {
            clear();
            main(null);
        }


    }

    public static double whichCoin2(List<CurrencyPairs> currencyPairses, CurrencyPairs currency, String[] s, String firstCur, String secCur) {
        Double askCoinBTC = 0.0;
        Double askCoinETH = 0.0;
        Double bidCoinBTC = 0.0;
        Double bidCoinETH = 0.0;
        Double bidBTCETH = 100 * Double.parseDouble(currency.getMaxBid());
        Double askBTCETH = 100 * Double.parseDouble(currency.getMinAsk());
        for(int j = 0; j < s.length; j++) {
            for (int i = 0; i < currencyPairses.size(); i++) {
                try {
                    if (currencyPairses.get(i).getSymbol().equals(s[j])) {
                        CurrencyPairs currency1 = currencyPairses.get(i);
                        if (s[j].endsWith(firstCur)) {
                            bidCoinBTC = 100 * Double.parseDouble(currency1.getMaxBid());
                            askCoinBTC = 100 * Double.parseDouble(currency1.getMinAsk());
                        }

                        if (s[j].endsWith(secCur)) {
                            askCoinETH = 100 * Double.parseDouble(currency1.getMinAsk());
                            bidCoinETH = 100 * Double.parseDouble(currency1.getMaxBid());
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
//        System.out.println("ask " + askCoinBTC + " " + askCoinETH + " " + bidBTCETH + " " +
//                bidCoinBTC + " " + bidCoinETH + " " + currency.getSymbol());
//        System.out.println(bidBTCETH + " " + askCoinETH + " " + bidCoinBTC);
        return 100 * (1/askCoinETH) * bidCoinBTC / askBTCETH;
    }

    public static double whichCoin(List<CurrencyPairs> currencyPairses, CurrencyPairs currency, String[] s, String firstCur, String secCur) {
        Double bidCoinBTC = 0.0;
        Double askCoinETH = 0.0;
        Double bidBTCETH = Double.parseDouble(currency.getMaxBid());
        for(int j = 0; j < s.length; j++) {
        for (int i = 0; i < currencyPairses.size(); i++) {
            try {
                if (currencyPairses.get(i).getSymbol().equals(s[j])) {
                    CurrencyPairs currency1 = currencyPairses.get(i);
                    if (s[j].endsWith(firstCur)) {
                        bidCoinBTC = Double.parseDouble(currency1.getMinAsk());
                    }

                    if (s[j].endsWith(secCur)) {
                        askCoinETH = Double.parseDouble(currency1.getMaxBid());
                    }
                }
            } catch (Exception e) {

            }
        }
        }
//
//        System.out.println(bidBTCETH + " " + askCoinETH + " " + bidCoinBTC);
        return (1.0/bidCoinBTC) * askCoinETH * bidBTCETH;
    }





















    public static String[] addETHBTC(String[] s) {
        for (int i = 0; i < s.length; i++) {
            if(s[i].endsWith("ETH") && s[i].endsWith("BTC")) {
                String[] st = new String[s.length + 1];
                for (int j = 0; j < st.length - 1; j++) {
                    st[j] = s[j];
                }
                st[st.length - 1] = "ETH/BTC";
                return st;
            }
        }
        return s;
    }


    public static String[] convertPairsTwo(String s) {
        String[] str = s.split("&");
        return str;
    }

    public static String[] convertPairs(String s) {
        String[] str = s.split("/");
        return str;
    }

    public static String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException ex) {

        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {

                }
            }
        }
        return null;
    }
}
