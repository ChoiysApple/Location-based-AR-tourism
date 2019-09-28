package com.marchengraffiti.nearism.nearism.parsing;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class ParsingAPI extends AppCompatActivity {

    private DatabaseReference mPostReference, mPostReference2, mPostReference3;
    BufferedReader br = null, br2 = null;
    URL url, url2;

    boolean initem = false, inaddr1 = false, incat3 = false, incontentid = false;
    boolean incontenttypeid = false, infirstimage = false, infirstimage2 = false, inmapx = false;
    boolean inmapy = false, inmlevel = false, intel = false, inplacetitle = false;
    String addr1 = null, cat3 = null, contentid = null, contenttypeid = null;
    String firstimage = null, firstimage2 = null, mapx = null, mapy = null;
    String mlevel = null, tel = null, placeTitle = null;

    boolean initem2 = false, insubid = false, indetailalt = false, indetailimg = false;
    boolean inoverview = false, insubname = false, insubnum = false;
    String subid = null, detailalt = null, detailimg = null, overview = null, subname = null, subnum = null;

    List<String> cidList = new ArrayList();


    /*
    connection()
    URL만 String으로 저장 후 parsing method에 넘긴다.
     */

    public void connection() {
        // contenttypeid=12(관광)인 데이터 request URL
        final String url12 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList" +
                "?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D" +
                "&contentTypeId=12&MobileOS=ETC&MobileApp=AppTest&pageNo=1&numOfRows=9548";

        // contenttypeid=25(코스)인 데이터 request URL
        final String url25 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList" +
                "?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D" +
                "&contentTypeId=25&MobileOS=ETC&MobileApp=AppTest&pageNo=1&numOfRows=325";

        new Thread() {
            public void run() {
                // 함수 호출 (위치 기반 관광 데이터)
                //locationBasedData(url12);
                locationBasedData(url25);
            }
        }.start();

        //locationBasedData(url25);
        //courseData();
    }


    /*
    locationBasedData
    기본 데이터 파싱 (위치 기반 데이터 파싱)
    contenttypeid = 12(관광), 25(코스)인 데이터
     */

    public void locationBasedData(String newURL) {
        try {
            // URL connection
            url = new URL(newURL);
            final HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

            // GET 방식 request
            urlconnection.setRequestMethod("GET");

            // XML 로 받아온 response data를 result(string)에 저장
            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
            String result = "";
            String line;
            while ((line = br.readLine()) != null) {
                result = result + line + "\n";
            }

            // Xml parser
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            Log.d("Parsing", "parsing start");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG: // parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("addr1"))  // title 이 addr1 이면
                            inaddr1 = true; // addr1 값 존재 (boolean 변수 = true)
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

                    case XmlPullParser.TEXT: //parser가 내용에 접근했을때
                        if(inaddr1) { // 내용이 있으면
                            addr1 = parser.getText(); // 해당하는 값 가져와서 변수에 저장
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
                            if(contenttypeid.equals("25"))
                                cidList.add(contentid);
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

                    case XmlPullParser.END_TAG: // end tag
                        if (parser.getName().equals("item")) {
                            if(contenttypeid.equals("12"))
                                PlaceList();  // 값 디비에 저장하는 메소드
                            else if(contenttypeid.equals("25"))
                                CourseBasicList(); // 코스 기본정보

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
    }


    /*
    courseData()
    contenttypeid=25인 코스 데이터를 모두 가져온 후
    그 데이터들의 contentid만 cidList에 저장
    cidList에 저장된 contentid를 기준으로 코스 디테일 데이터 파싱하는 메소드
     */

    public void courseData() {
        try {
            int idx;
            Log.d("courseData", String.valueOf(cidList));

            for(idx = 0; idx < cidList.size(); idx++) {
                String courseURL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailInfo?" +
                        "ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D" +
                        "&contentId=" + cidList.get(idx) + "&contentTypeId=25&MobileOS=ETC&MobileApp=AppTest";

                url2 = new URL(courseURL);
                final HttpURLConnection urlconnection = (HttpURLConnection) url2.openConnection();

                urlconnection.setRequestMethod("GET");

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
                            if (insubid) {
                                subid = parser.getText();
                                insubid = false;
                            }
                            if (indetailalt) {
                                detailalt = parser.getText();
                                indetailalt = false;
                            }
                            if (indetailimg) {
                                detailimg = parser.getText();
                                indetailimg = false;
                            }
                            if (inoverview) {
                                overview = parser.getText();
                                inoverview = false;
                            }
                            if (insubname) {
                                subname = parser.getText();
                                insubname = false;
                            }
                            if (insubnum) {
                                subnum = parser.getText();
                                insubnum = false;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                CourseList(idx);
                                Log.d("CourseParsing", "\n" + "contentid : " + cidList.get(idx) + "\nsubid : " + subid + "\ndetailalt : " + detailalt +
                                        "\ndetailimg : " + detailimg + "\noverview : " + overview + "\nsubname : " + subname + "\nsubnum : " + subnum);
                                initem2 = false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            }

        } catch (Exception e) {

        }
    }


    /*
    PlaceList()
    파이어베이스에 contenttypeid=12인 관광 데이터 저장하는 메소드
     */

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


    /*
    CourseList()
    파이어베이스에 contenttypeid=25인 코스 데이터 저장하는 메소드
     */

    public void CourseList(int idx) {
        /*mPostReference2 = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String key = cidList.get(idx);
        String courseKey = subid;

        CourseData courseData = new CourseData(subid, detailalt, detailimg, overview, subname, subnum);
        Map<String, Object> postValues = courseData.toMap();
        Log.d("firebaseLog", String.format("%s",postValues));

        //childUpdates.put("/course_list/" + key + "/" + courseKey, postValues);
        //mPostReference2.updateChildren(childUpdates);
        */
    }


    /*
    CourseBasicList()
    파이어베이스에 contenttypeid=25인 코스 데이터 저장하는 메소드
     */

    public void CourseBasicList() {
        /*mPostReference3 = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String key = mPostReference3.child("posts").push().getKey();

        FirebasePost post = new FirebasePost(addr1, cat3, contentid, contenttypeid,
                firstimage, firstimage2, mapx, mapy, mlevel, tel, placeTitle);
        Map<String, Object> postValues = post.toMap();
        Log.d("firebaseLog", String.format("%s",postValues));

        childUpdates.put("/course_basic_info/" + key, postValues);
        mPostReference3.updateChildren(childUpdates);*/
    }
}