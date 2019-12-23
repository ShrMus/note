package cn.shrmus.editor;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {

    private String datePattern = "yyyy-MM-dd";

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        SimpleDateFormat format = new SimpleDateFormat(getDatePattern());  // yyyy-MM-dd
            try {
            Date dateValue = format.parse(text);
            setValue(dateValue);
            System.out.println("调用了自定义的类型转换器" + dateValue);
        } catch (Exception e) {
            System.out.println("日期格式不对");
        }
    }

    @Override
    public String getAsText() {
        return super.getAsText();
    }

}