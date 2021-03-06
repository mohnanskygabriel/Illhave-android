package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;
import android.util.Log;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.val;
import sk.upjs.sk.illhave.entity.Product;

public class GetAllProductsAsyncTask extends AsyncTask<String, Void, Product[]> {

    protected Product[] doInBackground(String... url) {
        try {
            val responseEntity = new RestTemplate().getForEntity(
                    "https://i-will-have.herokuapp.com/product/all", Product[].class);
            Log.e("CALLED: ", this.getClass().toString());
            if (responseEntity.hasBody()) {
                return responseEntity.getBody();
            }
        } catch (RestClientException ex) {
            Log.e(this.getClass().toString(), "exception: ", ex);
        }

        return new Product[0];
    }
}
