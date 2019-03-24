package map.android.mr_auspicious.bukarteadmin.Model;


public class Categories  {


    private String mCategoryName;
    private String mCategoryID;


    public Categories(String categoryName,String categoryID) {
        mCategoryName = categoryName;
        mCategoryID = categoryID;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public String getCategoryID() {
        return mCategoryID;
    }

}
