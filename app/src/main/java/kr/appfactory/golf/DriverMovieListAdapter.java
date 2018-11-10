package kr.appfactory.golf;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DriverMovieListAdapter extends BaseAdapter {
    private Context context;
    private Fragment parent;
    private String videoId;
    private String videodesc;
    private String videotitle;
    private List<DriverMovie> driverMovieList;

    public ListView driverMovieListView;

    public DriverMovieListAdapter(Context context, List<DriverMovie> driverMovieList, Fragment parent) {
        this.context = context;
        this.parent = parent;
        this.driverMovieList = driverMovieList;

    }
    public void refreshAdapter(List<DriverMovie> driverMovieList) {

        this.driverMovieList.clear();

        this.driverMovieList.addAll(driverMovieList);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return driverMovieList.size();
    }

    @Override
    public Object getItem(int i) {
        return driverMovieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        if(v == null) {
             v = View.inflate(context, R.layout.driver, null);
        }
        ImageView thum_pic = (ImageView) v.findViewById(R.id.thumimg);
        TextView subjectText = (TextView) v.findViewById(R.id.subjectText);
        TextView viewDate = (TextView) v.findViewById(R.id.viewDate);
        TextView viewCount = (TextView) v.findViewById(R.id.viewCount);



        if(!TextUtils.isEmpty(driverMovieList.get(i).getThum_img())) {

            //Log.e("getThum_img", driverMovieList.get(i).getThum_img());

            Picasso.with(context)
                    .load(driverMovieList.get(i).getThum_img())
                    .into(thum_pic);
        }
        //thum_pic.setImageBitmap(back.class.etBitmapFromURL(driverMovieList.get(i).getThum_img()));

        //thum_pic.setImageURI(Uri.parse(driverMovieList.get(i).getThum_img()));
        subjectText.setText(driverMovieList.get(i).getMovie_title());
        viewCount.setText(driverMovieList.get(i).getMovie_count());
        viewDate.setText(driverMovieList.get(i).getMovie_date());
         videoId = driverMovieList.get(i).getMovie_videoId();
        videodesc = driverMovieList.get(i).getMovie_desc();
        videotitle = driverMovieList.get(i).getMovie_title();

        v.setTag(driverMovieList.get(i).getMovie_title());


        //Log.e("subjectText", ""+driverMovieList.get(i).getMovie_title());


/*     Button viewButton = (Button) v.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MoviePlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                intent.putExtra("videoId", ""+  videoId);
                intent.putExtra("videodesc", ""+  videodesc);
                intent.putExtra("title",""+ videotitle);


                Toast.makeText (context, "클릭" + videoId , Toast.LENGTH_SHORT).show();
                view.getContext().startActivity(intent);

            }

        });
 */

        return v;
    }

}