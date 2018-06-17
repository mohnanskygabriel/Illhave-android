package sk.upjs.sk.illhave;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ViewHolder {
    private View row;
    private TextView nazovProduktu, cena, spolu;
    private EditText pocet;
    private CheckBox checkBox;

    public ViewHolder(View row) {
        this.row = row;
    }

    public TextView getProductName() {
        if (this.nazovProduktu == null) {
            this.nazovProduktu = row.findViewById(R.id.product_name_text_view);
        }
        return this.nazovProduktu;
    }

    public TextView getPrice() {
        if (this.cena == null) {
            this.cena = row.findViewById(R.id.price_text_view);
        }
        return this.cena;
    }

    public EditText getCount() {
        if (this.pocet == null) {
            this.pocet = row.findViewById(R.id.count_edit_text);
        }
        return this.pocet;
    }

    public TextView getSpolu() {
        if (this.spolu == null) {
            this.spolu = row.findViewById(R.id.total_price_text_view);
        }
        return this.spolu;
    }

    public CheckBox getCheckBox() {
        if (this.checkBox == null) {
            this.checkBox = row.findViewById(R.id.row_food_menu_check_box);
        }
        return this.checkBox;
    }
}