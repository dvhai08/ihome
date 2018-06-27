package haidv.iky.ihome.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Board {
    public String name;
    public String out1;
    public String out2;
    public String out3;
    public int status;

    public Board() {
    }

    public Board(String name, String out1, String out2, String out3, int status) {
        this.name = name;
        this.out1 = out1;
        this.out2 = out2;
        this.out3 = out3;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOut1() {
        return out1;
    }

    public void setOut1(String out1) {
        this.out1 = out1;
    }

    public String getOut2() {
        return out2;
    }

    public void setOut2(String out2) {
        this.out2 = out2;
    }

    public String getOut3() {
        return out3;
    }

    public void setOut3(String out3) {
        this.out3 = out3;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("out1", out1);
        result.put("out2", out2);
        result.put("out3", out3);
        result.put("status", status);

        return result;
    }
}
