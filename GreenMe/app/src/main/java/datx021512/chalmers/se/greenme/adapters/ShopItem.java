package datx021512.chalmers.se.greenme.adapters;


public class ShopItem {


    private String mName;
    private double mCO2;

    public ShopItem(String name, double c02)
    {
        this.mName = name;
        this.mCO2 = mCO2;
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
}
