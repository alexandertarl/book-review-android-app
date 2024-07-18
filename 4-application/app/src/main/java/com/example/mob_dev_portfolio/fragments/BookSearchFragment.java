package com.example.mob_dev_portfolio.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentBookSearchBinding;
import com.example.mob_dev_portfolio.tasks.DownloadImageTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookSearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // used with notifs
    public static final String EXAMPLE_CHANNEL_ID = "type1";
    private String notifText;
    private List<String> tbrList = new ArrayList<>();

    private SharedPreferences toBeReadBooksSP;
    private SharedPreferences.Editor toBeReadBooksEditor;

    public String getNotifText() {
        return notifText;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookSearchFragment() {
        // Required empty public constructor
    }

    public static BookSearchFragment newInstance(String param1, String param2) {
        BookSearchFragment fragment = new BookSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_search, container, false);
        FragmentBookSearchBinding binding = FragmentBookSearchBinding.inflate(inflater, container, false);

        // adds users saved books to shared prefs
        toBeReadBooksSP = getActivity().getPreferences(Context.MODE_PRIVATE);
        toBeReadBooksEditor = toBeReadBooksSP.edit();

        // use search bar to input search
        SearchView searchView = (SearchView) view.findViewById(R.id.search);

        searchView.setActivated(true);
        searchView.setQueryHint("Search for a book here");
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        // make buttons not visible yet
        Button tbr1 = (Button) view.findViewById(R.id.tbrButton1);
        tbr1.setVisibility(View.GONE);
        Button tbr2 = (Button) view.findViewById(R.id.tbrButton2);
        tbr2.setVisibility(View.GONE);
        Button tbr3 = (Button) view.findViewById(R.id.tbrButton3);
        tbr3.setVisibility(View.GONE);

        // listens to see what user types in
        // text typed in is searched using API
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // set text to empty, so on re-search the data is not appended to old data
                TextView name1 = (TextView) view.findViewById(R.id.name1);
                TextView name2 = (TextView) view.findViewById(R.id.name2);
                TextView name3 = (TextView) view.findViewById(R.id.name3);
                TextView link1 = (TextView) view.findViewById(R.id.link1);
                TextView link2 = (TextView) view.findViewById(R.id.link2);
                TextView link3 = (TextView) view.findViewById(R.id.link3);
                name1.setText("");
                name2.setText("");
                name3.setText("");
                link1.setText("");
                link2.setText("");
                link3.setText("");

                searchView.setIconified(true);

                // adds unique key to link so can use api
                // previous api key was deleted
                String anotherUrl = "secret google api link"

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                JsonObjectRequest exampleRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        anotherUrl,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray items = response.getJSONArray("items");

                                    // list through all book results
                                    for (int i = 0; i < 3; i++) {
                                        // list titles for book searched
                                        String titles = items.getJSONObject(i).getJSONObject("volumeInfo").getString("title");
                                        // get link to googlebooks page
                                        String bookLink = items.getJSONObject(i).getJSONObject("volumeInfo").getString("previewLink");
                                        // get image link for each
                                        String imageUrl = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");


                                        if (i == 0) {

                                            new DownloadImageTask((ImageView) view.findViewById(R.id.image1))
                                                    .execute(imageUrl);

                                            name1.append(titles+" by: ");

                                            // code to create hyperlink
                                            // taken from Stack Overflow post by Andrei 24-03-2012
                                            // accessed 12-04-2023
                                            // https://stackoverflow.com/questions/9852184/android-textview-hyperlink
                                            link1.setText(
                                                    Html.fromHtml(
                                                            "<a href="+bookLink+">"+bookLink+"</a>"
                                                    ));
                                            link1.setMovementMethod(LinkMovementMethod.getInstance());
                                            // end of code - but is also used below

                                            // onclick listener for clicking link
                                            link1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    // this should work fine on the university laptop
                                                    // but on my personal computer i had to implement this:
                                                    // Stack Overflow post by onenowy 25-01-2022
                                                    // https://stackoverflow.com/questions/69134922/google-chrome-browser-in-android-12-emulator-doesnt-load-any-webpages-internet

                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookLink));
                                                    startActivity(browserIntent);
                                                }
                                            });

                                            tbr1.setVisibility(View.VISIBLE);
                                            tbr1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Toast.makeText(getContext(), "Added "+ titles +" to TBR", Toast.LENGTH_LONG).show();
                                                    tbrList.add(titles);
                                                }
                                            });
                                        }
                                        if (i == 1) {

                                            new DownloadImageTask((ImageView) view.findViewById(R.id.image2))
                                                    .execute(imageUrl);

                                            name2.append(titles+" by: ");

                                            link2.setText(
                                                    Html.fromHtml(
                                                            "<a href="+bookLink+">"+bookLink+"</a>"
                                                    ));
                                            link2.setMovementMethod(LinkMovementMethod.getInstance());

                                            // onclick listener for clicking link
                                            link2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookLink));
                                                    startActivity(browserIntent);
                                                }
                                            });

                                            tbr2.setVisibility(View.VISIBLE);
                                            tbr2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Toast.makeText(getContext(), "Added "+ titles +" to TBR", Toast.LENGTH_LONG).show();
                                                    tbrList.add(titles);
                                                }
                                            });
                                        }
                                        if (i == 2) {

                                            new DownloadImageTask((ImageView) view.findViewById(R.id.image3))
                                                    .execute(imageUrl);

                                            name3.append(titles+" by: ");

                                            link3.setText(
                                                    Html.fromHtml(
                                                            "<a href="+bookLink+">"+bookLink+"</a>"
                                                    ));
                                            link3.setMovementMethod(LinkMovementMethod.getInstance());

                                            // onclick listener for clicking link
                                            link3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookLink));
                                                    startActivity(browserIntent);
                                                }
                                            });

                                            tbr3.setVisibility(View.VISIBLE);
                                            tbr3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Toast.makeText(getContext(), "Added "+ titles +" to TBR", Toast.LENGTH_LONG).show();
                                                    tbrList.add(titles);
                                                }
                                            });
                                        }

                                        // list author(s) for book searched
                                        JSONArray authors = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors");
                                        for (int j = 0; j < authors.length(); j++) {
                                            String authorVal = authors.getString(j);

                                            if (i == 0) {
                                                if (j == authors.length()-1) {
                                                    name1.append(authorVal+".");
                                                } else {
                                                    name1.append(authorVal +", ");
                                                }
                                            }
                                            if (i == 1) {
                                                if (j == authors.length()-1) {
                                                    name2.append(authorVal+".");
                                                } else {
                                                    name2.append(authorVal +", ");
                                                }
                                            }
                                            if (i == 2) {
                                                if (j == authors.length()-1) {
                                                    name3.append(authorVal+".");
                                                } else {
                                                    name3.append(authorVal +", ");
                                                }
                                            }
                                        }
                                    }

                                    // when clicking button to list tbr books
                                    Button tbrAll = (Button) view.findViewById(R.id.tbrButtonAll);
                                    tbrAll.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            // remove value from shared prefs, so rewrites instead of appending
                                            toBeReadBooksEditor.remove("tbr");
                                            toBeReadBooksEditor.apply();

                                            notifText = "";
                                            // set list of tbr books to string of books
                                            for (int i=0; i<tbrList.size(); i++) {
                                                if (i==0) {
                                                    notifText = notifText + tbrList.get(i);
                                                } else {
                                                    notifText = new StringBuilder().append(notifText).append(", ").append(tbrList.get(i)).toString();
                                                }
                                            }

                                            // adds value to shared prefs
                                            toBeReadBooksEditor.putString("tbr", notifText);
                                            toBeReadBooksEditor.commit();

                                            String notificationContent = "Books you saved to read soon: " + toBeReadBooksSP.getString("tbr", "") + ".";

                                            // send notif of books to read, when leaving search activity
                                            Context c = getContext();
                                            NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                if (notificationManager != null && notificationManager.getNotificationChannel(EXAMPLE_CHANNEL_ID) == null) {
                                                    NotificationChannel channel = new NotificationChannel(
                                                            EXAMPLE_CHANNEL_ID,
                                                            "example_channel",
                                                            NotificationManager.IMPORTANCE_DEFAULT
                                                    );

                                                    channel.setDescription("Example");
                                                    notificationManager.createNotificationChannel(channel);
                                                }
                                            }

//                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);

                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, EXAMPLE_CHANNEL_ID)
                                                    .setSmallIcon(R.drawable.ic_baseline_menu_book_24)
                                                    .setContentTitle("Book Review App")
                                                    .setContentText(notificationContent)
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                                                    .setDefaults(Notification.DEFAULT_ALL);
                                            Notification notificationCompat = mBuilder.build();
                                            notificationManager.notify(0, notificationCompat);
                                        }
                                    });



                                } catch (JSONException e) {
                                    System.out.println("error finding book name");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //something
                                System.out.println("error");
                            }
                        }
                );

                requestQueue.add(exampleRequest);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
