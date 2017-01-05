package caryotacos.experimental;


import java.util.ArrayList;


public class MovieList
{
    private ArrayList<Movie> movieList;
    private String url;
    
    public MovieList()
    {
    	url = "";
    	movieList = new ArrayList<Movie>();
    }
    public MovieList(String url)
    {
    	this.url = url;
    	movieList = new ArrayList<Movie>();
    	update();
    }

	public void update()
    {
        Scraper scrap = new Scraper(url);
        movieList = scrap.getMovies();
    }

    public ArrayList<Movie> getMovies()
    {
        update();
        return movieList;
    }
    
	public String toString()
    {
		String s = "";
    	for (Movie m : movieList)
    	{
    		s += m.toString() + "\n\n\n\n";
    	}
		return s;
    }

	
}
