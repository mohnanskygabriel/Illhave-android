package sk.upjs.sk.illhave;

import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import sk.upjs.sk.illhave.async.GetAllProductsAsyncTask;
import sk.upjs.sk.illhave.entity.Product;

public class AllProducts {

    public static List<Product> allProducts = getAllProducts();

    private AllProducts() {

    }

    private static List<Product> getAllProducts() {
        Product[] products = null;
        try {
            products = new GetAllProductsAsyncTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("getAllProducts", "exception", e);
        }
        if (products == null) {
            return null;
        }
        return Arrays.asList(products);
    }
}
