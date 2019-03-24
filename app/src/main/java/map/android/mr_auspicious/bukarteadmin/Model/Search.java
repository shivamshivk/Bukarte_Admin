package map.android.mr_auspicious.bukarteadmin.Model;


public class Search {

    private String book_name;
    private String time;

    public Search(String book_name, String time) {
        this.book_name = book_name;
        this.time = time;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getTime() {
        return time;
    }
}
