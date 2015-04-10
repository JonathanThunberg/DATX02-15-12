package datx021512.chalmers.se.greenme.adapters;


public class ShopItem {


    private String mName;
    private double mCO2;
    private double quantity;

    public ShopItem(String name, double c02)
    {
        this.mName = name;
        this.mCO2 = c02;
        this.quantity =1;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmCO2() {
        return mCO2;
    }

    public void setmCO2(double mCO2) {
        this.mCO2 = mCO2;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
