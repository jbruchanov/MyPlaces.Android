package com.scurab.android.myplaces.datamodel;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.util.AppUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <br/>
 * Use {@link #addCon(String)} {@link #addPro(String)} resp. remove... to be sure to update adapter data to buble all changes to visual elements like listView
 *
 * @author Joe Scurab
 */
public class MapItem extends MapElement implements Serializable {
    private long id;
    private String type = "";

    private String name = "";
    private String country = "";
    private String city = "";
    private String street = "";
    private String web = "";
    private String streetViewLink = "";
    private String author = "";
    private String contact = "";

    private double x;
    private double y;

    private int rating;

    private List<String> pros;
    private List<String> cons;
    private List<Detail> details;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String link) {
        web = link;
    }

    public String getStreetViewLink() {
        return streetViewLink;
    }

    public void setStreetViewLink(String streetViewLink) {
        this.streetViewLink = streetViewLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = AppUtils.round(x, 6);
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = AppUtils.round(y, 6);
    }

    public List<String> getPros() {
        return pros;
    }

    public void setPros(List<String> pros) {
        this.pros = pros;
    }

    public List<String> getCons() {
        return cons;
    }

    public void setCons(List<String> cons) {
        this.cons = cons;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int getIconResId() {
        String t = getType();
        if (t == null) {
            return R.drawable.ico_pin;
        } else {
            t = t.toLowerCase();
        }

        if (t.equals("hospoda")) {
            return R.drawable.ico_beer;
        } else if (t.equals("bar")) {
            return R.drawable.ico_drink;
        } else if (t.equals("kavárna")) {
            return R.drawable.ico_cafe;
        } else if (t.equals("café")) {
            return R.drawable.ico_cafe;
        } else if (t.equals("restaurace")) {
            return R.drawable.ico_restaurant;
        } else if (t.equals("pizzerie")) {
            return R.drawable.ico_pizza;
        } else if (t.equals("fastfood")) {
            return R.drawable.ico_fastfood;
        } else if (t.equals("club")) {
            return R.drawable.ico_music;
        } else if (t.equals("zahrádka")) {
            return R.drawable.ico_patio;
        } else if (t.equals("sushi")) {
            return R.drawable.ico_sushi;
        } else {
            return R.drawable.ico_search;
        }
    }

    public String getTitle() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        String result = name + "\n";
        if (street != null) {
            result += street + "\n";
        }
        if (city != null) {
            result += city + "\n";
        }
        if (contact != null) {
            result += contact + "\n";
        }
        if (web != null) {
            result += web;
        }
        return result;
    }

    private transient List<MapItemDetailItem> mDataForAdapter;

    public List<MapItemDetailItem> getDataForAdapter() {
        if (mDataForAdapter == null) {
            mDataForAdapter = new ArrayList<MapItemDetailItem>();
            if (pros != null) {
                for (String pro : pros)
                    mDataForAdapter.add(new MapItemDetailItem(pro, MapItemDetailItem.TYPE_PRO));
            }
            if (cons != null) {
                for (String con : cons)
                    mDataForAdapter.add(new MapItemDetailItem(con, MapItemDetailItem.TYPE_CON));
            }
            if (details != null) {
                for (Detail d : details)
                    mDataForAdapter.add(new MapItemDetailItem(d));
            }
        }
        return mDataForAdapter;
    }

    public void addPro(String s) {
        if (pros == null) {
            pros = new ArrayList<String>();
        }
        pros.add(s);
        if (mDataForAdapter != null) {
            mDataForAdapter.add(new MapItemDetailItem(s, MapItemDetailItem.TYPE_PRO));
        }
    }


    public void addCon(String s) {
        if (cons == null) {
            cons = new ArrayList<String>();
        }
        cons.add(s);
        if (mDataForAdapter != null) {
            mDataForAdapter.add(new MapItemDetailItem(s, MapItemDetailItem.TYPE_CON));
        }
    }

    public void removePro(String s) {
        if (pros != null) {
            pros.remove(s);
        }
        if (mDataForAdapter != null) {
            for (MapItemDetailItem mdi : mDataForAdapter) {
                if (mdi.getType() == MapItemDetailItem.TYPE_PRO && mdi.getValue().equals(s)) {
                    mDataForAdapter.remove(mdi);
                    break;
                }
            }
        }
    }

    public void removeCon(String s) {
        if (cons != null) {
            cons.remove(s);
        }
        if (mDataForAdapter != null) {
            for (MapItemDetailItem mdi : mDataForAdapter) {
                if (mdi.getType() == MapItemDetailItem.TYPE_CON && mdi.getValue().equals(s)) {
                    mDataForAdapter.remove(mdi);
                    break;
                }
            }
        }
    }

    public void addDetail(Detail newValue) {
        if (details == null) {
            details = new ArrayList<Detail>();
        }
        details.add(newValue);
        if (mDataForAdapter != null) {
            mDataForAdapter.add(new MapItemDetailItem(newValue));
        }
    }

    public void removeDetail(Detail value) {
        if (details != null) {
            details.remove(value);
        }

        if (mDataForAdapter != null) {
            for (MapItemDetailItem mdi : mDataForAdapter) {
                if (mdi.getType() == MapItemDetailItem.TYPE_DETAIL && mdi.getDetailValue().equalsByValues(value)) {
                    mDataForAdapter.remove(mdi);
                    break;
                }
            }
        }
    }

    /**
     * Returns uri value for StreetView intent<br/>
     * if StreetViewLink is null returns null
     *
     * @return
     */
    public String getStreetViewUriLink() {
        if (streetViewLink == null) {
            return null;
        }
        HashMap<String, Double> data = getStreetViewValues();
        return String.format("google.streetview:cbll=%s,%s&cbp=1,%s,,%s,%s", data.get(Y), data.get(X), data.get(YAW), data.get(PITCH), data.get(ZOOM));
    }

    private static final String X = "X";
    private static final String Y = "Y";
    private static final String YAW = "YAW";
    private static final String PITCH = "PITCH";
    private static final String ZOOM = "ZOOM";
    private static final String VALUE_SEPARATOR = ";";

    private HashMap<String, Double> getStreetViewValues() {
        //		X=14.431627;Y=50.077044;YAW=308.41;PITCH=-5.73;ZOOM=1
        HashMap<String, Double> result = new HashMap<String, Double>();
        String[] data = streetViewLink.split("\\" + VALUE_SEPARATOR);

        for (String item : data) {
            try {
                String[] values = item.split("=");
                String key = values[0];
                double value = Double.parseDouble(values[1]);
                result.put(key, value);
            } catch (Exception e) {
                //should not never be thrown
            }
        }
        return result;
    }
}