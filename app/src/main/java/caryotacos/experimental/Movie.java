package caryotacos.experimental;


import java.util.ArrayList;

public class Movie
{

    private String name;
    private String desc;
    private ArrayList<String> times;

    public Movie()
    {
        name = "";
        desc = "";

    }

    public Movie(String name, String desc)
    {
        this.name = name;
        if (desc.equals(""))
            this.desc = "Two brothers. In a van. And then a meteor hit. And they ran as fast as they could. From giant cat-monsters. And then a giant tornado came. And that's when things got knocked into twelfth gear...";
        else
            this.desc = desc;

    }

    public Movie(String name, String desc, ArrayList<String> times)
    {
        this.name = name;
        this.desc = desc;
        this.times = times;

    }

    public void setTimes(ArrayList<String> times)
    {
        this.times = times;
    }

    public String getName()
    {
        return name;
    }
    
    public String getDesc()
    {
        return desc;
    }

    public String getTimes()
    {
        String s = "";
        for(String time: times)
            s+= time + "  ";
        return s;
    }
    


    public String toString()
    {
        String s = "";
        s += getName();
        s += "\n\t" + getDesc();

        for (String time : times)
            s += "\n\t\t\t " + time;
        return s;
    }

}
