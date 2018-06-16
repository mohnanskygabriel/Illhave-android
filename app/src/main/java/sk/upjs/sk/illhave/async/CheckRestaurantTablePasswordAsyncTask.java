package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;

import org.springframework.web.client.RestTemplate;

import lombok.val;
import sk.upjs.sk.illhave.entity.RestaurantTable;

public class CheckRestaurantTablePasswordAsyncTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        val url = "https://i-will-have.herokuapp.com/table/check";
        val restaurantTableName = strings[0];
        val password = strings[1];
        val restaurantTable = RestaurantTable.
                builder().
                name(restaurantTableName).
                password(password).
                build();
        val restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, restaurantTable, Boolean.class);
    }
}
