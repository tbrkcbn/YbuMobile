package com.example.ybumobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneFragment extends Fragment {

    // JSoup Librariy Setting
    TextView fragment_blue_txt_title;
    LinearLayout fragment_blue_j_soup_pnl_add;
    View blueView;

    private String webSiteUrl;
    public String findContent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
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
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        setFindContent("cnContent");
        setWebSiteUrl("https://aybu.edu.tr/muhendislik/bilgisayar/");
        blueView = inflater.inflate(R.layout.fragment_one, container, false);

        fragment_blue_txt_title = blueView.findViewById(R.id.fragment_blue_txt_title);
        fragment_blue_j_soup_pnl_add = blueView.findViewById(R.id.fragment_blue_j_soup_pnl_add);

        getWebSite();

        return blueView; // inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_blue, menu);
    }
    public void setWebSiteUrl(String siteUrl) {
        this.webSiteUrl = siteUrl;
    }
    public void setFindContent(String findContent) {
        this.findContent = findContent;
    }

    public void getWebSite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(webSiteUrl).get();

                    String title = doc.title();

                    builder.append(title).append("\n");

                    Elements links = doc.select("div.cnContent a[href]");
                    log("Here : ", "News");
                    for(Element link : links) {
                        builder.append("\n").append(link.attr("href"))
                                .append("\n").append(link.text());
                    }

                }catch (IOException e) {
                    builder.append("\n").append("Error : ").append(e.getMessage()).append("\n");
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String[] text = builder.toString().split("\n");
                        log("fragment_red_txt_title.setText(text[0]) : text[0]", text[0]);
                        fragment_blue_txt_title.setText("News");
                        fragment_blue_j_soup_pnl_add.removeAllViews();
                        String textLink = "";
                        for (int i=2; i < text.length; i++) {
                            if(i%2 == 0) {
                                textLink = text[i].toString();
                            } else {
                                Button button = new Button(getContext());
                                log("Text : ", text[i].toString());

                                final String finalTextLink = textLink;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Intent intent = new Intent();
                                            intent.setAction(Intent.ACTION_VIEW);
                                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                            intent.setData(Uri.parse("https://aybu.edu.tr/muhendislik/bilgisayar/" + finalTextLink));
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            log("ERROR : ", e.getMessage());
                                        }
                                    }
                                });

                                button.setText(text[i]);

                                fragment_blue_j_soup_pnl_add.addView(button);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private static void log(String msg, String vals) {
        System.out.println(String.format("\n" + msg + " " + vals));
    }
}
