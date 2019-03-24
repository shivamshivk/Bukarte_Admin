package map.android.mr_auspicious.bukarteadmin.Model;


public class User {

    private String c_name;
    private String c_id;
    private String c_email;
    private String c_mobile;
    private String cdoj;

    public User(String c_name, String c_id, String c_email, String c_mobile, String cdoj) {
        this.c_name = c_name;
        this.c_id = c_id;
        this.c_email = c_email;
        this.c_mobile = c_mobile;
        this.cdoj = cdoj;
    }

    public String getC_name() {
        return c_name;
    }

    public String getC_id() {
        return c_id;
    }

    public String getC_email() {
        return c_email;
    }

    public String getC_mobile() {
        return c_mobile;
    }

    public String getCdoj() {
        return cdoj;
    }
}
