import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by pablorivas on 10/1/17.
 */
public class EthTwitter {

    public static void main(String [] args) throws IOException{

        double priceBefore = -1;
        Twitter twitter = TwitterFactory.getSingleton();
        String resp;
        try {
            while(true) {

                resp = run("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD,EUR");
                ObjectMapper mapper = new ObjectMapper();
                EthPrice price = mapper.readValue(resp, EthPrice.class);

                twitter.updateStatus("Ethereum price USD: " + price.USD + " EUR: " + price.EUR);

                if(priceBefore == -1){
                    priceBefore = price.USD;
                }

                if(price.USD >= (priceBefore + 5)){
                    twitter.updateStatus("Price Swing Up Alert!! ETH price USD: " + price.USD);
                }
                if(price.USD <= (priceBefore - 5)){
                    twitter.updateStatus("Price Swing Down Alert!! ETH price USD: " + price.USD);
                }
                priceBefore = price.USD;
                Thread.sleep(60 * 60 * 1000);
            }
        }
        catch (TwitterException ex) {
            System.out.println("Twitter Exception: " + ex);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException Exception: " + e);
        }
    }

    public static String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
