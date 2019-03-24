package map.android.mr_auspicious.bukarteadmin.Model;



public class Request_ {

    private String b_name;
    private String c_id;
    private String p_name;
    private String a_name;
    private String request_time;
    private String phone_no;

    public Request_(String b_name, String c_id, String p_name, String a_name, String request_time,String phone_no) {
        this.b_name = b_name;
        this.c_id = c_id;
        this.p_name = p_name;
        this.a_name = a_name;
        this.request_time = request_time;
        this.phone_no = phone_no;
    }

    public String getB_name() {
        return b_name;
    }

    public String getC_id() {
        return c_id;
    }

    public String getP_name() {
        return p_name;
    }

    public String getA_name() {
        return a_name;
    }

    public String getRequest_time() {
        return request_time;
    }

    public String getPhone_no() {
        return phone_no;
    }
}
