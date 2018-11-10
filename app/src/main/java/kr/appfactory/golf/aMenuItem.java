package kr.appfactory.golf;

public class aMenuItem {

    String menu_title;
    String menu_link;

    public aMenuItem(String menu_title, String menu_link) {
        this.menu_title = menu_title;
        this.menu_link = menu_link;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public String getMenu_link() {
        return menu_link;
    }

    public void setMenu_link(String menu_link) {
        this.menu_link = menu_link;
    }


}
