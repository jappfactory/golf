package kr.appfactory.golf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    public  ListView driverMovieListView;
    public List<DriverMovie> driverMovieList;
    public DriverMovieListAdapter driveradapter;
    private  ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    public int loading = 0;
    public int loadingresult = 0;
    public int viewcnt = 0;
    private static  int networkYn = 0;
    Toolbar myToolbar;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Activity activity;

    DBHelper dbHelper;

    String target ;
    private OnFragmentInteractionListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (Activity) getActivity();
    }
    public FavoritesFragment() {}

    public static FavoritesFragment newInstance( ) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle b) {
        super.onActivityCreated(b);


        SharedPreference.putSharedPreference(getActivity(), "viewcnt", 0);
        driverMovieListView  = (ListView) getView().findViewById(R.id.subFavoritesListView);
        driverMovieList = new ArrayList<DriverMovie>();
        driveradapter = new DriverMovieListAdapter(activity, driverMovieList, this);
        driverMovieListView.setAdapter(driveradapter);


        progressBar.setVisibility(View.GONE);
        dbHelper = new DBHelper(getActivity());

        //즐겨찾기목록
        Cursor cursor = dbHelper.getResult_MyVideo();
        int i = 0;
        while (cursor.moveToNext()) {


            String videoId = cursor.getString(1);
            String subjectText = cursor.getString(2);
            String descriptionText = cursor.getString(3);
            String publishedAt = cursor.getString(4);
            String thum_pic = cursor.getString(5);
            String viewCnt="";

            DriverMovie drivermovie = new DriverMovie(thum_pic, subjectText, publishedAt, viewCnt, videoId , descriptionText);
            driverMovieList.add(drivermovie);
        i++;
        }


        String totalResults = String.valueOf(i);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        totalResults = decimalFormat.format(Double.parseDouble(totalResults.toString().replaceAll(",", "")));
        TextView searchcnt = (TextView) getView().findViewById(R.id.searchcnt);
        searchcnt.setText(totalResults);

        driverMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), MoviePlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videoId", ""+  driverMovieList.get(position).getMovie_videoId());
                intent.putExtra("title",""+ driverMovieList.get(position).getMovie_title());
                intent.putExtra("videodesc", ""+  driverMovieList.get(position).getMovie_desc());
                intent.putExtra("publishedAt",""+ driverMovieList.get(position).getMovie_date());
                intent.putExtra("thum_pic",""+ driverMovieList.get(position).getThum_img());

                view.getContext().startActivity(intent);

            }
        });

    }

    public void progressBarShow(){

        driverMovieListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                // 여기서 이벤트를 막습니다.
                return true;
            }
        });
        // 로딩중을 알리는 프로그레스바를 보인다.
        progressBar.setVisibility(View.VISIBLE);
    }

    public void progressBarHidden(){

        driverMovieListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                // 여기서 이벤트를 막습니다.
                return false;
            }
        });
        // 로딩중을 알리는 프로그레스바를 숨기기.
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        networkYn = ((MainActivity)getActivity()).Online();
        if(networkYn==2) ((MainActivity)getActivity()).NotOnline();

        View view=inflater.inflate(R.layout.fragment_favorites, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);


        myToolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView title = (TextView) getActivity().findViewById(R.id.toolbar_title);
        actionBar.setTitle("My 즐겨찾기");



        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


