package com.example.kosta.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private List<WeatherDTO> data;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView)findViewById(R.id.list);

        final WeatherTask task = new WeatherTask();
        task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=58&gridy=125");

        data = new ArrayList<>();
        adapter = new WeatherAdapter(this, data);

        list.setAdapter(adapter);

        findViewById(R.id.jeju).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
                if(task.isCancelled()) {
                    data.removeAll(data);
                    new WeatherTask().execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=58&gridy=35");
                }
            }
        });
    }

    private class WeatherTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
//            기상청 사이트 연결
            try {
                URL url = new URL(params[0]);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));

                NodeList nodeList = doc.getElementsByTagName("data");
                for(int i = 0 ; i < nodeList.getLength() ; i++) {
                    WeatherDTO dto = new WeatherDTO();

                    Node node = nodeList.item(i);

                    Element element = (Element)node;

//                    온도 데이터 추출
                    NodeList tempList = element.getElementsByTagName("temp");
                    Element tempElement = (Element)tempList.item(0);
                    tempList = tempElement.getChildNodes();
                    dto.setTemp("온도 : " + ((Node)tempList.item(0)).getNodeValue());

//                    날씨 데이터 추출
                    NodeList weatherList = element.getElementsByTagName("wfKor");
                    Element weatherElement = (Element)weatherList.item(0);
                    weatherList = weatherElement.getChildNodes();
                    dto.setWeather("날씨 : " + ((Node)weatherList.item(0)).getNodeValue());

                    data.add(dto);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }
}
