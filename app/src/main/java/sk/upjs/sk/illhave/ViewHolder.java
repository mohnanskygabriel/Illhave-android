package sk.upjs.sk.illhave;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ViewHolder {
    private View row;
    private TextView nazovProduktu = null, cena = null, spolu = null;
    private EditText pocet = null;
    private CheckBox checkBox = null;

    public ViewHolder(View row) {
        this.row = row;
    }

    public TextView getNazovProduktu() {
        if (this.nazovProduktu == null) {
            this.nazovProduktu = (TextView) row.findViewById(R.id.nazovProduktuTextView);
        }
        return this.nazovProduktu;
    }

    public TextView getCena() {
        if (this.cena == null) {
            this.cena = (TextView) row.findViewById(R.id.cenaTextView);
        }
        return this.cena;
    }

    public EditText getPocet() {
        if (this.pocet == null) {
            this.pocet = (EditText) row.findViewById(R.id.pocetEditText);
        }
        return this.pocet;
    }

    public TextView getSpolu() {
        if (this.spolu == null) {
            this.spolu = (TextView) row.findViewById(R.id.cenaSpoluTextView);
        }
        return this.spolu;
    }

    public CheckBox getCheckBox() {
        if (this.checkBox == null) {
            this.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
        }
        return this.checkBox;
    }
}