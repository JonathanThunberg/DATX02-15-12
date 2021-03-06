package datx021512.chalmers.se.greenme.adapters;


public class ShopItem {


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String country;
    // private int quantity;
    private String mName;
    private double mCO2;
    private double mQuantity;
    private int mEco;
    private double mWeight;

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
    //OCR
    public ShopItem(String name,int amount, double weight, double co2)
    {
        this.mName = name;
        this.mQuantity = amount;
        this.mWeight = weight;
        this.mCO2 = co2;
    }

    public ShopItem(String name,int amount, double co2)
    {
        this.mName = name;
        this.mQuantity = amount;
        this.mCO2 = co2;
    }

    public ShopItem(String text, double c02) {
        this.mName = text;
        this.mCO2 = c02;
    }

    public ShopItem(String text, double c02, String country, double quant, int eco) {
        this.mName = text;
        this.mCO2 = c02;
        this.mQuantity = quant;
        this.country = country;
        this.mEco = eco;
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

    public double getWeight() {
        return this.mWeight==0?this.mQuantity:this.mWeight;
    }

    public void setWeight(double weight) {
        this.mWeight = weight;
    }

    @Override
    public String toString()
    {
        return "";
    }
}
