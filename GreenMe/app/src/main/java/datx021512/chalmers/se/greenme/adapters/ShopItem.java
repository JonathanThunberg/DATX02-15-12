package datx021512.chalmers.se.greenme.adapters;


public class ShopItem {


   // private int quantity;
    private String mName;
    private double mCO2;
    private double mQuantity;
    private int mEco;

    public ShopItem(String name, double c02, double amount)
    {
        this.mName = name;
        this.mCO2 = c02;
        this.mQuantity =amount;
    }

    public ShopItem(String name, double c02, double quantity, int mEco) {
        this.mName = name;
        this.mCO2 = c02;
        this.mQuantity = quantity;
        this.mEco = mEco;
    }

    public ShopItem(String name, double c02, String date)
    {
        this.mName = name;
        this.mCO2 = c02;
        this.mQuantity =1;
        this.date = date;
    }
    public ShopItem(String name,int amount, double c02)
    {
        this.mName = name;
        this.mCO2 = c02;
        this.mQuantity =amount;
    }

    public ShopItem(String text, double c02) {
        this.mName = text;
        this.mCO2 = c02;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmCO2() {
        return mCO2;
    }

    public int getEco() {
        return mEco;
    }

    public void setmCO2(double mCO2) {
        this.mCO2 = mCO2;
    }

    public double getQuantity() {
        return this.mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }
}
