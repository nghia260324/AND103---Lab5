package com.ph41626.and103_lab5.Services;

import static com.ph41626.and103_lab5.Services.ApiServices.BASE_URL;

import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Fruit;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Services {
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String filePathAddressInfo = "AddressInfo.txt";
    public static final  <T> T findObjectById(ArrayList<T> list, String id) {
        for (T item : list) {
            if (item instanceof Distributor) {
                Distributor distributor = (Distributor) item;
                if (distributor.getId().equals(id)) {
                    return item;
                }
            } else if (item instanceof Fruit) {
                Fruit fruit = (Fruit) item;
                if (fruit.get_id().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }
    public static final String convertLocalhostToIpAddress(String url) {
        int index = url.indexOf("3000/");
        String newUrl = "";
        if (index != -1) {
            newUrl = BASE_URL + url.substring(index + 5);
        } else {
            newUrl = url;
        }
        return newUrl;
    }
    public static final String formatPrice(double n, String currency) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String formattedPrice = decimalFormat.format(n).replaceAll("\\.00$", "");
        return formattedPrice + currency;
    }
    public static final int findDistributorIndexById(ArrayList<Distributor> listDistributors, String distributorId) {
        for (int i = 0; i < listDistributors.size(); i++) {
            Distributor distributor = listDistributors.get(i);
            if (distributor.getId().equals(distributorId)) {
                return i;
            }
        }
        return -1;
    }
}
