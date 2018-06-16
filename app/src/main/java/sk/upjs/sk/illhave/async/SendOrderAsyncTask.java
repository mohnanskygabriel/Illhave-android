package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;

import org.springframework.web.client.RestTemplate;

import sk.upjs.sk.illhave.entity.Order;

public class SendOrderAsyncTask extends AsyncTask<Order, Void, Order> {


    @Override
    protected Order doInBackground(Order... orders) {
        return new RestTemplate().postForObject(
                "https://i-will-have.herokuapp.com/order/create",
                orders[0],
                Order.class
        );
    }
}
