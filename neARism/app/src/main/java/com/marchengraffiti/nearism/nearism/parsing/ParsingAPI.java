package com.marchengraffiti.nearism.nearism.parsing;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class ParsingAPI extends AppCompatActivity {

    private DatabaseReference mPostReference, mPostReference2;

    BufferedReader br = null, br2 = null;

    boolean initem = false, inaddr1 = false, incat3 = false, incontentid = false;
    boolean incontenttypeid = false, infirstimage = false, infirstimage2 = false, inmapx = false;
    boolean inmapy = false, inmlevel = false, intel = false, inplacetitle = false;

    boolean incontentid2 = false;
    String contentid2 = null;

    String addr1 = null, cat3 = null, contentid = null, contenttypeid = null;
    String firstimage = null, firstimage2 = null, mapx = null, mapy = null;
    String mlevel = null, tel = null, placeTitle = null;

    boolean initem2 = false, insubid = false, indetailalt = false, indetailimg = false;
    boolean inoverview = false, insubname = false, insubnum = false;

    String subid = null, detailalt = null, detailimg = null, overview = null, subname = null, subnum = null;

    URL url, url2;

    public void connection() throws Exception {
        /*String url12 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList" +
                "?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D" +
                "&contentTypeId=12&MobileOS=ETC&MobileApp=AppTest&pageNo=10&numOfRows=1000";*/

        String url25 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList" +
                "?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D" +
                "&contentTypeId=25&MobileOS=ETC&MobileApp=AppTest";

        // contentTypeId=25인 정보를 가져온 다음에 contentID 에 해당하는 상세정보 가져와야함.

        //url = new URL(url12);
        url2 = new URL(url25);

        //final HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
        final HttpURLConnection urlconnection2 = (HttpURLConnection) url2.openConnection();

        //urlconnection.setRequestMethod("GET");
        urlconnection2.setRequestMethod("GET");
        new Thread() {
            public void run() {
                //locationBasedData(urlconnection);
                locationBasedData2(urlconnection2);
            }
        }.start();
    }

    public void locationBasedData2 (HttpURLConnection urlconnection2) { // contentTypeId = 25인 모든 데이터.
        try {
            br2 = new BufferedReader(new InputStreamReader(urlconnection2.getInputStream(), "UTF-8"));
            String result = "";
            String line;

            while ((line = br2.readLine()) != null) {
                result = result + line + "\n";
            }

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            Log.d("Parsing", "parsing start");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("contentid"))
                            incontentid2 = true;
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(incontentid2) {
                            contentid2 = parser.getText();
                            courseData(contentid2);
                            incontentid2 = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            CourseList();
                            Log.d("ParsingCourseResult", "parsing course result OK");
                            initem2 = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {

        }
    }

    public void courseData(String contentid) {
        try {
            Log.d("IDRESULT", contentid);
            String courseURL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailInfo?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D&"
                    + "contentId=" + contentid + "&contentTypeId=25&MobileOS=ETC&MobileApp=AppTest";

            url2 = new URL(courseURL);
            final HttpURLConnection urlconnection = (HttpURLConnection) url2.openConnection();

            urlconnection.setRequestMethod("GET");
            Log.d("incourseTAG" , "URL OK");

            new Thread() {
                public void run() {
                    try {
                        Log.d("incourseTAG" , "THREAD OK");
                        br2 = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                        String result = "";
                        String line;

                        while ((line = br2.readLine()) != null) {
                            result = result + line + "\n";
                        }

                        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserCreator.newPullParser();

                        parser.setInput(url2.openStream(), null);

                        int parserEvent = parser.getEventType();
                        Log.d("Parsing", "parsing start");

                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            switch (parserEvent) {
                                case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                                    if (parser.getName().equals("subcontentid"))  //title 만나면 내용을 받을수 있게 하자
                                        insubid = true;
                                    if (parser.getName().equals("subdetailalt"))
                                        indetailalt = true;
                                    if (parser.getName().equals("subdetailimg"))
                                        indetailimg = true;
                                    if (parser.getName().equals("subdetailoverview"))
                                        inoverview = true;
                                    if (parser.getName().equals("subname"))
                                        insubname = true;
                                    if (parser.getName().equals("subnum"))
                                        insubnum = true;
                                    break;

                                case XmlPullParser.TEXT://parser가 내용에 접근했을때
                                    if(insubid) {
                                        subid = parser.getText();
                                        insubid = false;
                                    }
                                    if(indetailalt) {
                                        detailalt = parser.getText();
                                        indetailalt = false;
                                    }
                                    if(indetailimg) {
                                        detailimg = parser.getText();
                                        indetailimg = false;
                                    }
                                    if(inoverview) {
                                        overview = parser.getText();
                                        inoverview = false;
                                    }
                                    if(insubname) {
                                        subname = parser.getText();
                                        insubname = false;
                                    }
                                    if(insubnum) {
                                        subnum = parser.getText();
                                        insubnum = false;
                                    }
                                    break;

                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("item")) {
                                        Log.d("CourseParsing", "\n" + "subid : " + subid + "\ndetailalt : " + detailalt +
                                                "\ndetailimg : " + detailimg + "\noverview : " + overview + "\nsubname : " + subname + "\nsubnum : " + subnum);
                                        initem2 = false;
                                    }
                                    break;
                            }
                            parserEvent = parser.next();
                        }
                    } catch (Exception e) {

                    }
                }
            }.start();

        } catch (Exception e) {

        }
    }

    /*public void locationBasedData(HttpURLConnection urlconnection) {
        try {
            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
            String result = "";
            String line;
            while ((line = br.readLine()) != null) {
                result = result + line + "\n";
                Log.d("result log", result);
            }
            Log.d(" logging", "5");

            Log.d("APITAG Result", result);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            Log.d("Parsing", "parsing start");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("addr1"))  //title 만나면 내용을 받을수 있게 하자
                            inaddr1 = true;
                        if (parser.getName().equals("cat3"))
                            incat3 = true;
                        if (parser.getName().equals("contentid"))
                            incontentid = true;
                        if (parser.getName().equals("contenttypeid"))
                            incontenttypeid = true;
                        if (parser.getName().equals("firstimage"))
                            infirstimage = true;
                        if (parser.getName().equals("firstimage2"))
                            infirstimage2 = true;
                        if (parser.getName().equals("mapx"))
                            inmapx = true;
                        if (parser.getName().equals("mapy"))
                            inmapy = true;
                        if (parser.getName().equals("mlevel"))
                            inmlevel = true;
                        if (parser.getName().equals("tel"))
                            intel = true;
                        if (parser.getName().equals("title"))
                            inplacetitle = true;
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inaddr1) {
                            addr1 = parser.getText();
                            inaddr1 = false;
                        }
                        if(incat3) {
                            cat3 = parser.getText();
                            incat3 = false;
                        }
                        if(incontentid) {
                            contentid = parser.getText();
                            incontentid = false;
                        }
                        if(incontenttypeid) {
                            contenttypeid = parser.getText();
                            //if (contenttypeid.equals("25"))
                            //    incourse = true;
                            incontenttypeid = false;
                        }
                        if(infirstimage) {
                            firstimage = parser.getText();
                            infirstimage = false;
                        }
                        if(infirstimage2) {
                            firstimage2 = parser.getText();
                            infirstimage2 = false;
                        }
                        if(inmapx) {
                            mapx= parser.getText();
                            inmapx = false;
                        }
                        if(inmapy) {
                            mapy = parser.getText();
                            inmapy = false;
                        }
                        if(inmlevel) {
                            mlevel = parser.getText();
                            inmlevel = false;
                        }
                        if(intel) {
                            tel = parser.getText();
                            intel = false;
                        }
                        if(inplacetitle) {
                            placeTitle = parser.getText();
                            inplacetitle = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            PlaceList();
                            Log.d("ParsingResult", "\n" + "addr1 : " + addr1 + "\ncat3 : " + cat3 + "\ncontentid : " + contentid +
                                    "\ncontenttypeid : " + contenttypeid + "\nfirstimage : " + firstimage + "\nfirstimage2 : " + firstimage2 +
                                    "\nmapx : " + mapx + "\nmapy : " + mapy + "\nmlevel : " + mlevel + "\ntel : " + tel + "\nplacetitle : " + placeTitle);
                            initem = false;
                        }

                        break;
                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            Log.d("APITAG", e.toString());
        }
    }*/

    public void PlaceList() {
        /*mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String key = mPostReference.child("posts").push().getKey();

        FirebasePost post = new FirebasePost(addr1, cat3, contentid, contenttypeid,
                firstimage, firstimage2, mapx, mapy, mlevel, tel, placeTitle);
        Map<String, Object> postValues = post.toMap();
        Log.d("firebaseLog", String.format("%s",postValues));

        childUpdates.put("/place_list/" + key, postValues);
        mPostReference.updateChildren(childUpdates);*/
    }

    public void CourseList() {
        /*mPostReference2 = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String key = mPostReference2.child("posts").push().getKey();


        CourseData courseData = new CourseData(subid, detailalt, detailimg, overview, subname, subnum);
        Map<String, Object> postValues = courseData.toMap();
        Log.d("firebaseLog", String.format("%s",postValues));

        childUpdates.put("/course_list/" + key + courseKey, postValues);
        mPostReference2.updateChildren(childUpdates);*/
    }
}