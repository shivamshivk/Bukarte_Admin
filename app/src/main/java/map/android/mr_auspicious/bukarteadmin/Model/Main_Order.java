package map.android.mr_auspicious.bukarteadmin.Model;

import java.util.ArrayList;
import java.util.List;


public class Main_Order {

    private String order_id;
    private String addr_id;
    private String ord_size;
    private String ord_date;
    private String payable_amount;
    private String wallet_pay;
    private String rest_pay;
    private String order_medium;
    private String order_status;
    private String c_id;

    public Main_Order(String payable_amount,String ord_size,String addr_id,String ord_date,String order_id,String c_id,String wallet_pay,String rest_pay,String order_medium,String order_status) {
        this.payable_amount = payable_amount;
        this.ord_size = ord_size;
        this.addr_id = addr_id;
        this.ord_date = ord_date;
        this.order_id = order_id;
        this.c_id = c_id;
        this.wallet_pay = wallet_pay;
        this.rest_pay = rest_pay;
        this.order_medium = order_medium;
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }


    public String getAddr_id() {
        return addr_id;
    }

    public String getOrd_size() {
        return ord_size;
    }

    public String getOrd_date() {
        return ord_date;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public String getWallet_pay() {
        return wallet_pay;
    }

    public String getRest_pay() {
        return rest_pay;
    }

    public String getOrder_medium() {
        return order_medium;
    }

    public String getC_id() {
        return c_id;
    }

    public String getOrder_status() {
        return order_status;
    }
}
