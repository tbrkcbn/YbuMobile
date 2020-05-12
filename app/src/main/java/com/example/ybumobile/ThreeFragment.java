package com.example.ybumobile;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThreeFragment extends Fragment {

    // JSoup Librariy Setting
    TextView fragment_red_txt_title;
    LinearLayout fragment_red_j_soup_pnl_add;
    View redView;

    private String webSiteUrl;
    public String findContent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThreeFragment newInstance(String param1, String param2) {
        ThreeFragment fragment = new ThreeFragment();
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
        setFindContent("Aylık Yemek Menüsü");
        setWebSiteUrl("http://web.archive.org/web/20190406185041/https://aybu.edu.tr/sks/");
        redView = inflater.inflate(R.layout.fragment_three, container, false);

        fragment_red_txt_title = redView.findViewById(R.id.fragment_red_txt_title);
        fragment_red_j_soup_pnl_add = redView.findViewById(R.id.fragment_red_j_soup_pnl_add);

        getWebSite();



        return redView;//inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_red, menu);
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
                final StringBuilder builderFoodToday = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(webSiteUrl).get();

                    String title = doc.title();

                    builder.append(title).append("\n");

                    Elements links = doc.select("a[href]");
                    log("Here : ", "Yemek Menu");
                    for(Element link : links) {
                        if(findContent.equalsIgnoreCase(link.text())) {
                            builder.append("\n").append(link.attr("href"))
                                    .append("\n").append(link.text());
                        }
                    }

                    Document doc1 = Jsoup.connect("http://web.archive.org/web/20190406185041/https://aybu.edu.tr/sks/").get();
                    Elements tables = doc1.select("table tr td:has(table)");

                    for (Element table2 : tables) {
                        Elements trs = table2.select("tr");
                        String[][] trtd = new String[trs.size()][];
                        for (int a = 0; a < trs.size(); a++) {
                            Elements tds = trs.get(a).select("td");
                            trtd[a] = new String[tds.size()];
                            for (int b = 0; b < tds.size(); b++) {
                                trtd[a][b] = tds.get(b).text();
                                builderFoodToday.append("\n").append(trtd[a][b]);
                            }
                            System.out.println( );
                        }
                        // trtd now contains the desired array for this table
                    }

                }catch (IOException e) {
                    builder.append("\n").append("Error : ").append(e.getMessage()).append("\n");
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_red_j_soup_pnl_add.removeAllViews();

                        String[] textFoodToday = builderFoodToday.toString().split("\n");
                        fragment_red_txt_title.setText("Food List");
                        for (int i=12; i < textFoodToday.length; i++) {
                            TextView textView = new TextView(getContext());
                            textView.setTextSize(20);
                            textView.setText(i-11 + " ) " + textFoodToday[i]);
                            fragment_red_j_soup_pnl_add.addView(textView);
                            log("==============================> textFoodToday[" + i + "]", textFoodToday[i]);
                        }

                        String[] text = builder.toString().split("\n");


                        log("fragment_red_txt_title.setText(text[0]) : text[0]", text[0]);


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
                                        log("Link : ", finalTextLink);
                                        String filename = "https://aybu.edu.tr" + finalTextLink;
                                        log("filename : ", filename);
                                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ filename);
                                        Intent target = new Intent();
                                        target.setAction(Intent.ACTION_VIEW);
                                        target.setDataAndType(Uri.parse(filename), "application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Intent intent = Intent.createChooser(target, "Open File");
                                        try {
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            log("ERROR : ", e.getMessage());
                                        }
                                    }
                                });
                                button.setText(text[i]);
                                fragment_red_j_soup_pnl_add.addView(button);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private static void log(String msg, String vals) {
        System.out.println("\n" + msg + " " + vals);
    }
}