package kr.appfactory.golf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
/**dc dddd
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    final AppCompatActivity activity = this;

    private String  target ="http://www.appfactory.kr/gms/reg/Golf";;
    private  String nextPageToken;
    private static Context context;
    private static  int networkYn = 0;
    private SharedPreferences PageToken;
    private SharedPreferences.Editor pt;
    private DrawerLayout mDrawerLayout;
    private View drawerView;
    private ActionBarDrawerToggle mToggle;
    Toolbar myToolbar;
    private ListView mnuListView;
    private ListView mnuListView2;
    public List<MenuItema> itemList;
    public List<MenuItema> itemList2;
    public MenuItemAdapter menuItemAdapter;
    public MenuItemAdapter menuItemAdapter2;
    private MaterialSearchView searchView;
    MyFirebaseInstanceIDService mf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "token::" + token);

        //   target = target + token;

        SharedPreferences  PageToken = getSharedPreferences(nextPageToken, 0);
        setContentView(R.layout.activity_main);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 화면을 landscape(가로) 화면으로 고정하고 싶은 경우

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);



        new gms_reg().execute();
        new versionCheck(activity).execute();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerView = (View) findViewById(R.id.nav_view);



        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        //추가된 소스코드, Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_menu); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        searchView = findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        // searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //SearchProduct(getApplicationContext(), query);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, SearchFragment.newInstance(query));
                fragmentTransaction.commit();

                //Toast.makeText(getApplicationContext(),"Query: " + query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchView.showSuggestions();
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.showSuggestions();
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
 /*

        //myToolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_menu); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
*/
        /** * 기본 화면 설정 */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new DriverFragment());
        fragmentTransaction.commit();

        AdsFull.getInstance(getApplicationContext()).setAds(this);


        //AdsFull.getInstance(activity).setAdsFull();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setVerticalFadingEdgeEnabled(false);
        navigationView.setVerticalScrollBarEnabled(false);
        navigationView.setHorizontalScrollBarEnabled(false);


        updateIconBadge(activity,  0);


        Button MyfavoritesButton = (Button) findViewById(R.id.MyfavoritesButton);

        //즐겨찾기저장추가
        MyfavoritesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new FavoritesFragment());
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawers();

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        // Log.d(TAG, "페이지이동 ");

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {

        AdsFull.getInstance(getApplicationContext()).setAdsFull();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        builder.setIcon(R.drawable.billiard_icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.exitmsg);
        builder.setPositiveButton(R.string.exitmsgY, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setNegativeButton(R.string.exitmsgN, null);
        AlertDialog dialog = builder.show();


    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(true);
        switch (item.getItemId()){

            case 2131230744: {

                Toast.makeText (activity, "" + item.getItemId() , Toast.LENGTH_SHORT).show();
                break;
            }

            case android.R.id.home: {


                // 데이터 원본 준비
                itemList = new ArrayList<MenuItema>();


                //어댑터 생성
                menuItemAdapter = new MenuItemAdapter(activity, itemList);

                //어댑터 연결
                mnuListView = (ListView) findViewById(R.id.club_lesson);

                //Toast.makeText (activity, "클릭3" + mnuListView  , Toast.LENGTH_LONG).show();
                mnuListView.setAdapter(menuItemAdapter);


                itemList.add(new MenuItema("드라이버 레슨 영상", "driver"));
                itemList.add(new MenuItema("우드 레슨 영상", "wood"));
                itemList.add(new MenuItema("아이언 레슨 영상", "iron"));
                itemList.add(new MenuItema("웨지 레슨 영상", "wedge"));
                itemList.add(new MenuItema("퍼터 레슨 영상", "putter"));
                //  menuItemAdapter = new MenuItemAdapter(context,  itemList, this);

                mnuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        if (itemList.get(position).getMenu_link() == "driver")
                            fragmentTransaction.replace(R.id.fragment, new DriverFragment());
                        if (itemList.get(position).getMenu_link() == "wood")
                            fragmentTransaction.replace(R.id.fragment, new WoodFragment());
                        if (itemList.get(position).getMenu_link() == "iron")
                            fragmentTransaction.replace(R.id.fragment, new IronFragment());
                        if (itemList.get(position).getMenu_link() == "wedge")
                            fragmentTransaction.replace(R.id.fragment, new WedgeFragment());
                        if (itemList.get(position).getMenu_link() == "putter")
                            fragmentTransaction.replace(R.id.fragment, new PutterFragment());


                        fragmentTransaction.commit();
                        mDrawerLayout.closeDrawers();

                        // Toast.makeText (activity, "클릭 getMenu_title" + itemList.get(position).getMenu_title()  , Toast.LENGTH_SHORT).show();
                        //Toast.makeText (activity, "클릭 getMenu_link" + itemList.get(position).getMenu_link()  , Toast.LENGTH_SHORT).show();
                    }
                });

                // 데이터 원본 준비
                itemList2 = new ArrayList<MenuItema>();

                itemList2.add(new MenuItema("명품스윙 에이미 조 골프 레슨", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PUKby5Cq9jArperhkdbX1X6g&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("왕초보 골프입문 시리즈 - 심짱", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLCW1r5BxeqUXcarRbwfJuxEN94fS7XQb8&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("독학골퍼를 위한 셀프골프레슨 - 심짱", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLCW1r5BxeqUXIYGmydOpcrt3HxiEugQpP&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("계속보면 좋은 골프기초 - 심짱", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLCW1r5BxeqUWtGBztPJqu088ygieNfyQV&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("박대성프로의 1분레슨", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLUT6r8FkJUXgS5hOfQBiM5OPt2zWiXKdK&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("굿샷김프로 - 동영상 골프레슨", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLatlCU2UD5ayXEonr7kzJjAhuh_e807AM&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("정프로의 클라쓰 몰아보기!", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLJjPXwl6J0C8Djy7W-4osYWsL6bJdU_hO&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("상위1%골프 레슨", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLTKm3iaNcU4JHavt0JzjiD62DO46CqjKf&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("중급 싱글되기 골프레슨 ~ ^^", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL4OYEpDO4EFttKj5gBtE9lmlBHOgadIW9&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("비거리는 곧 자신감 장타레슨", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLBRks3VEudicyy188_sKojJm0G7DuebDF&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("스코어의 꽃 숏게임", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLBRks3VEudie9OjfJMgepjJx4YXSwuZSB&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("잘 배운 버릇 평생가는 골프기본기", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLBRks3VEudif6y3TYCqpiJOv2u0sdeC_4&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("초보 골프 입문 골퍼분들께 ~ ^^", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL4OYEpDO4EFu4wPcPtZP_ptWC5luU9LO6&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("[초보레슨]윤소원의 비기너스 골프", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLsoAKjawncjXreuI8oh52DesY3CJCJ1Ya&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("[레슨]박교희의 쉬운골프", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLsoAKjawncjWKyfVHpswFXvxRCij2VBQq&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("[레슨]이동익의 홈메이드 골프", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLsoAKjawncjX5jn8zDZJ1GrjwF05InL2G&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("[레슨]임진한의 스페셜레슨", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLsoAKjawncjVbOlr72df-mVl1WIKMtEBt&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("PGA Class A 함순웅의 골프채널", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=UUluR-EQnCekFsG0IJwM2Ihg&maxResults=&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("이근화프로 (Monit Golf)", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLNqL0LyfO8Tiep6g2ub79ksKBhd_Ng_q9&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("문성모프로 (Monit Golf)", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLNqL0LyfO8Tg_iXxRyJVqFUUvuZwRKw81&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("전수빈프로 (Monit Golf)", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLNqL0LyfO8TgWl3dupeExzdIS2KwdETMU&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("이효주프로 (Monit Golf)", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLNqL0LyfO8TgymOrkKPxCDuXakkFp2SIj&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("김종석프로 (Monit Golf)", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLNqL0LyfO8TgllZn3IIma6wanpfSCAEdZ&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));
                itemList2.add(new MenuItema("정효민프로 (Monit Golf)", "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLNqL0LyfO8TjLFkVBX4a4Ook7HH7yIO2m&maxResults=10&key=AIzaSyBn4fOG4zKOYVbYtcMtGj8gGsVVpTYb68g&pageToken="));


                //  menuItemAdapter = new MenuItemAdapter(context,  itemList, this);

                //어댑터 생성
                menuItemAdapter2 = new MenuItemAdapter(activity, itemList2);

                //어댑터 연결
                mnuListView2 = (ListView) findViewById(R.id.pro_lesson);

                //Toast.makeText (activity, "클릭3" + mnuListView  , Toast.LENGTH_LONG).show();
                mnuListView2.setAdapter(menuItemAdapter2);


                mnuListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, ChannelFragment.newInstance(itemList2.get(position).getMenu_link(), itemList2.get(position).getMenu_title()));
                        fragmentTransaction.commit();
                        mDrawerLayout.closeDrawers();
                        //Toast.makeText (activity, "클릭 getMenu_title" + itemList2.get(position).getMenu_title()  , Toast.LENGTH_SHORT).show();


                    }
                });
                mDrawerLayout.openDrawer(drawerView);
                //mDrawerLayout.closeDrawers();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(networkYn==2){

                NotOnline();
                return true;

            }else {

                Log.d("check URL", url);
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);


            }
        }
    }
    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void updateIconBadge(Context context, int notiCnt) {
        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", notiCnt);
        badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context));
        context.sendBroadcast(badgeIntent);
    }
    public void NotOnline() {
        final String networkmsg = getString(R.string.networkmsg);

        //mWebView.loadUrl("javascript:alert('"+networkmsg+"')");



        new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setIcon(R.drawable.billiard_icon)
                .setTitle(R.string.app_name)
                .setMessage(""+networkmsg+"")
                .setNegativeButton(R.string.exitmsgN, null)
                .setPositiveButton(R.string.exitmsgY,new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int whichButton)
                    {
                        finish();
                        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    }
                }).show();


        //startActivity(new Intent(getApplicationContext(), OfflineActivity.class));


    }
    /**
     * getURLEncode
     */
    public static String getURLEncode(String content){

        try {
//          return URLEncoder.encode(content, "utf-8");   // UTF-8
            return URLEncoder.encode(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * getURLDecode
     */
    public static String getURLDecode(String content){

        try {
//          return URLEncoder.encode(content, "utf-8");   // UTF-8
            return URLDecoder.decode(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public  int  Online() {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {

            Log.d("연결됨" , "연결이 되었습니다.");
            networkYn =1;


        } else {
            Log.d("연결 안 됨" , "연결이 다시 한번 확인해주세요");
            networkYn =2;

        }
        return networkYn;
    }


}



class LoadMovieTask extends AsyncTask<Void, Void, String> {


    private SharedPreferences PageToken;
    private SharedPreferences.Editor pt;

    private  String location;
    private  Context mContext;
    private DriverMovieListAdapter driveradapter;
    private List<DriverMovie> driverMovieList;
    private ListView driverMovieListView;
    String target;

    private MainActivity activity;



    public LoadMovieTask(Context context, List<DriverMovie> driverMovieList, ListView view, DriverMovieListAdapter driveradapter, String target, String location) {
        this.mContext = context;
        this.driverMovieList = driverMovieList;
        this.driveradapter = driveradapter;
        this.driverMovieListView = view;
        this.target = target;
        this.location = location;

    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            URL url = new URL(target);
            //Log.e("주소 url", ""+url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();



            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));

            String temp;
            StringBuilder stringBuilder = new StringBuilder();

            while ((temp = bufferedReader.readLine()) != null) {
                // Log.e("temp", ""+temp);
                stringBuilder.append(temp + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    protected void onPostExecute(String result) {
        String nextPageToken="";
        //Log.e("드라이버2", ""+result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            String totalResults = jsonObject.getJSONObject("pageInfo").getString("totalResults");

            try {
                nextPageToken = jsonObject.getString("nextPageToken");
            }  catch (Exception e) {
                //e.printStackTrace();
                nextPageToken="";

            }


            //Toast.makeText (mContext, "클릭" + totalResults , Toast.LENGTH_SHORT).show();


            SharedPreference.putSharedPreference(mContext, "totalResults", totalResults);
            SharedPreference.putSharedPreference(mContext, "nextPageToken", nextPageToken);


            int count = 0;
            String thum_pic, subjectText, descriptionText, viewCount, viewDate, viewCnt, videoId;

            // Toast.makeText (mContext, "클릭" + jsonArray.length() , Toast.LENGTH_SHORT).show();

           // Log.e("jsonArray.length", ""+jsonArray.length());

            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);




                if(jsonObject.getString("kind").equals("youtube#playlistItemListResponse")){


                    try {

                        // Toast.makeText (mContext, "클릭" + jsonObject.getString("kind"), Toast.LENGTH_SHORT).show();

                        subjectText = object.getJSONObject("snippet").getString("title");
                        descriptionText = object.getJSONObject("snippet").getString("description");
                        viewDate = object.getJSONObject("snippet").getString("publishedAt")
                                .substring(0, 10);

                        videoId = object.getJSONObject("snippet")
                                .getJSONObject("resourceId").getString("videoId");

                        thum_pic = object.getJSONObject("snippet")
                                .getJSONObject("thumbnails").getJSONObject("medium")
                                .getString("url"); // 썸내일 이미지 URL값



                        viewCnt = "0";
                        DriverMovie drivermovie = new DriverMovie(thum_pic, subjectText, viewDate, viewCnt, videoId , descriptionText);
                        driverMovieList.add(drivermovie);
                    }  catch (Exception e) {
                        //e.printStackTrace();
                        nextPageToken="";

                    }

                }else if(jsonObject.getString("kind").equals("youtube#searchListResponse")){

                    //Toast.makeText (mContext, "클릭" + jsonObject.getString("kind"), Toast.LENGTH_SHORT).show();

                    //   Toast.makeText (mContext, "클릭" + object.getJSONObject("id").getString("videoId") , Toast.LENGTH_SHORT).show();


                    videoId = object.getJSONObject("id").getString("videoId");
                    subjectText = object.getJSONObject("snippet").getString("title");
                    descriptionText = object.getJSONObject("snippet").getString("description");
                    viewDate = object.getJSONObject("snippet").getString("publishedAt")
                            .substring(0, 10);
                    thum_pic = object.getJSONObject("snippet")
                            .getJSONObject("thumbnails").getJSONObject("medium")
                            .getString("url"); // 썸내일 이미지 URL값

                    viewCnt = "0";
                    DriverMovie drivermovie = new DriverMovie(thum_pic, subjectText, viewDate, viewCnt, videoId , descriptionText);
                    driverMovieList.add(drivermovie);
                }





                count++;
            }


            if(location =="main"){
                driverMovieListView.setAdapter(driveradapter);


                driverMovieListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(view.getContext(), MoviePlayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("videoId", ""+  driverMovieList.get(position).getMovie_videoId());
                        intent.putExtra("videodesc", ""+  driverMovieList.get(position).getMovie_desc());
                        intent.putExtra("title",""+ driverMovieList.get(position).getMovie_title());

                        view.getContext().startActivity(intent);

                    }
                });

            }


        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("Buffer Error", "Error converting result " + e.toString());

        }

    }


}



class versionCheck extends AsyncTask<Void, Void, String> {

    String target ="http://www.appfactory.kr/version/version/Golf";

    private  Context mContext;
    private  String version;
    private  String versionName;


    public versionCheck(Context context) {
        this.mContext = context;
    }


    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String temp;
        URL url;


        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);

            versionName = packageInfo.versionName.toString().trim();
        } catch (Exception e) {

        }

        try {
            url = new URL(target);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            stringBuilder = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            String last_version = stringBuilder.toString().trim();


            Log.e("version", "/"+versionName+"/");
            Log.e("last_version", "/"+last_version+"/");



            if ( !last_version.equals(versionName) ) { //false

                String uri = "market://details?id=" +mContext.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mContext.startActivity(intent);

            }




            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    protected void onPostExecute(String result) {



    }

}


class gms_reg extends AsyncTask<Void, Void, String> {
    private  Context mContext;
    String target ="http://www.appfactory.kr/gms/reg/Golf/"+FirebaseInstanceId.getInstance().getToken();

    String target2 ="http://www.appfactory.kr/gms/cnt/Golf/"+FirebaseInstanceId.getInstance().getToken();
    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String temp;
        URL url;

        try {
            url = new URL(target2);
            //Log.e("주소 url 2 ", ""+url);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            stringBuilder = new StringBuilder();
            //Log.e("stringBuilder : ", ""+stringBuilder);
            while ((temp = bufferedReader.readLine()) != null) {
                //Log.e("temp", ""+temp);
                stringBuilder.append(temp + "\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            int numInt = Integer.parseInt(stringBuilder.toString().trim());

            //Log.e("numInt", ""+numInt);
            if (numInt == 0 ) {

                try {

                    url = new URL(target);
                    //Log.e("주소 url 1", ""+url);


                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpURLConnection.getInputStream();

                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                    stringBuilder = new StringBuilder();

                    while ((temp = bufferedReader.readLine()) != null) {
                        // Log.e("temp", ""+temp);
                        stringBuilder.append(temp + "\n");
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }


            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    protected void onPostExecute(String result) {



    }

}



