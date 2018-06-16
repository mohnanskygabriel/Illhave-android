package sk.upjs.sk.illhave;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.val;
import sk.upjs.sk.illhave.async.GetAllProductsAsyncTask;
import sk.upjs.sk.illhave.entity.Product;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<Fragment> fragments = null;
    private ArrayList<LinkedList<String>> categorizedProductList = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        val productList = getAllProducts();
        if (productList != null) {
            for (Product product : productList) {
                if (categorizedProductList.size() < product.category().id()) {
                    val listOfProductsName = new LinkedList<String>();
                    listOfProductsName.add(product.name());
                    categorizedProductList.add(product.category().id().intValue() - 1, listOfProductsName);
                } else {
                    categorizedProductList.get(product.category().id().intValue() - 1).add(product.name());
                }
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null) {
            fragments = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                fragments.add(new PlaceholderFragment());
            }
        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return categorizedProductList.size();
    }

    private List<Product> getAllProducts() {
        Product[] products = null;
        try {
            products = new GetAllProductsAsyncTask().execute().get();
            Log.e("products:", Arrays.toString(products));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (products == null) {
            return null;
        }
        return Arrays.asList(products);
    }
}