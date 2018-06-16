package sk.upjs.sk.illhave.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import sk.upjs.sk.illhave.AllProducts;
import sk.upjs.sk.illhave.R;
import sk.upjs.sk.illhave.ViewHolder;
import sk.upjs.sk.illhave.entity.Product;

public class VelkyListokActivity extends AppCompatActivity {

    private ListView listView;
    private static final String BUNDLE_KEY = "selected";
    private int[] selected;

    private final static DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velky_listok);

        List<Product> list = AllProducts.allProducts;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            selected = new int[list.size()];
        } else {
            selected = (int[]) savedInstanceState.getSerializable(BUNDLE_KEY);
        }

        listView = findViewById(R.id.velkyListokListView);

        ProductAdapter adapter = new ProductAdapter(this,
                R.layout.jedalny_listok_produkt, list);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VelkyListokActivity.this
                        , PotvrdenieObjednavkyActivity.class);
                val products = new ArrayList<Integer>();
                val quantities = new ArrayList<Integer>();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i] != 0) {
                        products.add(i + 1);
                        quantities.add(selected[i]);
                    }
                }
                intent.putIntegerArrayListExtra("productIds", products);
                intent.putIntegerArrayListExtra("quantities", quantities);
                startActivity(intent);
            }
        });
    }

    private class ProductAdapter extends ArrayAdapter<Product> {

        public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater inflater = getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.jedalny_listok_produkt, null, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final val finalHolder = holder;
            val cenaProduktu = AllProducts.allProducts.get(position).price();

            if (selected[position] > 0) {
                finalHolder.getCheckBox().setChecked(true);
                finalHolder.getSpolu().setText(df.format(selected[position] * cenaProduktu));
                finalHolder.getPocet().setText(String.valueOf(selected[position]));
            }
            finalHolder.getNazovProduktu().setText(AllProducts.allProducts.get(position).name());
            finalHolder.getCena().setText(String.valueOf(cenaProduktu));
            finalHolder.getPocet().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    val pocetEditable = finalHolder.getPocet().getText();
                    int pocet = 0;
                    if (pocetEditable.length() > 0) {
                        pocet = Integer.parseInt(pocetEditable.toString());
                    }
                    finalHolder.getSpolu().setText(df.format(pocet * cenaProduktu));
                    if (finalHolder.getCheckBox().isChecked()) {
                        selected[position] = pocet;
                    }
                }
            });

            finalHolder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        val pocetEditable = finalHolder.getPocet().getText();
                        int pocet = 0;
                        if (pocetEditable.length() > 0) {
                            pocet = Integer.parseInt(pocetEditable.toString());
                        }
                        selected[position] = pocet;
                        finalHolder.getSpolu().setText(df.format(pocet * cenaProduktu));
                    } else {
                        selected[position] = 0;
                        finalHolder.getSpolu().setText(String.valueOf(0));
                    }
                }
            });
            return convertView;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY, selected);
    }
}
