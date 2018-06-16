package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;

import org.springframework.web.client.RestTemplate;

import lombok.val;
import sk.upjs.sk.illhave.entity.Product;

public class GetAllProductsAsyncTask extends AsyncTask<String, Void, Product[]> {

    protected Product[] doInBackground(String... url) {
        val responseEntity = new RestTemplate().getForEntity(
                "https://i-will-have.herokuapp.com/product/all", Product[].class);
        return responseEntity.getBody();
    }
}
