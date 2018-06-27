package haidv.iky.ihome.util;

public class ViewUtil {

    public static boolean validateText(String editText){
        if(editText == null || editText.isEmpty()){
            return false;
        }else {
            return true;
        }
    }
}
