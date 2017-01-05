package caryotacos.experimental;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class Scraper
{
    private String url;

    public Scraper()
    {
        url = "";
    }

    public Scraper(String url)
    {
        this.url = url;
    }


    public ArrayList<Movie> getMovies()
    {
        Document doc = new Document("");
        try{
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
        }


        ArrayList<Movie> movieList = new ArrayList<Movie>();
        Elements movieInfos = doc.select("[itemtype='http://schema.org/Movie']");
        for (Element e : movieInfos)
        {
            String name = "";
            String desc = "";


            Element tempName = e.select("[itemprop='name']").first();
            Element tempDesc = e.select("[itemprop='description']").first();


            name = clean(tempName.toString());
            desc = clean(tempDesc.toString());
            Movie newMovie = new Movie(name, desc);
            movieList.add(newMovie);

        }

        Elements movieTimes = doc.select("[itemtype='http://schema.org/TheaterEvent']");
        for (Element e : movieTimes)
        {
            ArrayList<String> times = new ArrayList<String>();
            String tempName = clean(e.select("[itemprop='name']").first().toString());
            Elements tempTimes = e.select("[itemprop='startDate']");
            for (Element time : tempTimes)


                times.add(cleanTime(time.toString()));

            for (Movie m : movieList)
            {
                if (tempName.equals(m.getName()))
                    m.setTimes(times);
            }

        }

        return movieList;
    }

    public String clean(String str)
    {
        String cleaned = "";
        int start = str.indexOf("content") + 9;
        int end = str.indexOf(">") - 1;
        cleaned = str.substring(start,  end);
        return cleaned;
    }
    public String cleanTime(String str)
    {
        String cleaned = "";
        int start = str.indexOf("content") + 20;
        int end = str.indexOf(">") - 10;
        cleaned = str.substring(start,  end);

        int value = Integer.parseInt(cleaned.substring(0,2));
        if (value == 0)
        {
            value += 12;
            String tempClean = value + cleaned.substring(2) + " am";
            cleaned = tempClean;
        }
        else if (value > 12)
        {
            value -= 12;
            String tempClean = value + cleaned.substring(2) + " pm";
            cleaned = tempClean;
        }
        else if (value == 12)
            cleaned+= " pm";
        else
            cleaned+= " am";
        return cleaned;
    }
}
