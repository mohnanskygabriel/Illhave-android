package sk.upjs.sk.illhave.async;


import android.os.AsyncTask;

import org.springframework.web.client.RestTemplate;

public class GetAllTablesNameAsyncTask extends AsyncTask<String, Void, String[]> {

    protected String[] doInBackground(String... url) {
        return new RestTemplate().getForObject("https://i-will-have.herokuapp.com/table/all", String[].class);
    }
}
