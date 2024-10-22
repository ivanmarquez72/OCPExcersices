package org.example.resourcesbundles;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class Tax_en_US extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] { { "tax", 1 } };
    }
    public static void main(String[] args) {
        ResourceBundle rb = ResourceBundle.getBundle(
                "resourcebundles.Tax", Locale.US);
        System.out.println(rb.getObject("tax"));
    }}