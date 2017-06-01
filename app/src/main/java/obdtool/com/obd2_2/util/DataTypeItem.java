package obdtool.com.obd2_2.util;

public class DataTypeItem {

    private String type;
    private boolean selected;

    public DataTypeItem(String type) {
        this.type = type;
        this.selected=false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
