package map.android.mr_auspicious.bukarteadmin.Model;


import java.io.Serializable;

public class Order implements Serializable {

    private String id;
    private String imageurl;
    private String bookname;
    private String bookPrice;
    private String qty;

    public Order(String id,String imageurl, String bookname, String bookPrice,String qty) {
        this.id = id;
        this.imageurl = imageurl;
        this.bookname = bookname;
        this.bookPrice = bookPrice;
        this.qty = qty;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getBookname() {
        return bookname;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getQty() {
        return qty;
    }


    public String getId() {
        return id;
    }

}

