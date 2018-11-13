package kr.appfactory.golf;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MenuItemAdapter  extends BaseAdapter {

    private Context  context;
    private  List<MenuItema> itemList ;
    public ListView mnuListView;


    public MenuItemAdapter(Context context, List<MenuItema> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.menu_item, null);

        TextView menu_title = (TextView) v.findViewById(R.id.menu_title);
        TextView menu_url = (TextView) v.findViewById(R.id.menu_url);


        menu_title.setText(itemList.get(i).getMenu_title());
        menu_url.setText(itemList.get(i).getMenu_link());

        return v;

    }
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String link  ) {
        MenuItema item = new MenuItema("","" );

        item.setMenu_title(title);
        item.setMenu_link(link);
        //item.setMenu_type(link);

        itemList.add(item);
    }

}

class MenuItema {

    String menu_title;
    String menu_link;

    public MenuItema(String menu_title, String menu_link ) {
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
