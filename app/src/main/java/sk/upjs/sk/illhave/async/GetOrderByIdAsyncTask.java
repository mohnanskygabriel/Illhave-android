package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;
import android.util.Log;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import sk.upjs.sk.illhave.entity.Order;

public class GetOrderByIdAsyncTask extends AsyncTask<Long, Void, Order> {


    @Override
    protected Order doInBackground(Long... longs) {
        Order order = null;
        try {
            order = new RestTemplate().getForObject(
                    "https://i-will-have.herokuapp.com/order/" + longs[0],
                    Order.class);
        } catch (RestClientException ex) {
            Log.e(this.getClass().toString(), "exception: ", ex);
        }

        return order;
    }
}
