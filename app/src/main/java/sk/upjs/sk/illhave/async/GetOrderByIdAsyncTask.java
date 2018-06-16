package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;

import org.springframework.web.client.RestTemplate;

import sk.upjs.sk.illhave.entity.Order;

public class GetOrderByIdAsyncTask extends AsyncTask<Long, Void, Order> {


    @Override
    protected Order doInBackground(Long... longs) {
        return new RestTemplate().getForObject(
                "https://i-will-have.herokuapp.com/order/" + longs[0],
                Order.class);
    }
}
