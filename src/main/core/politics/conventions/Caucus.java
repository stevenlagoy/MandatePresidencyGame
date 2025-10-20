package main.core.politics.conventions;
import java.util.ArrayList;
import java.util.List;

public class Caucus extends Convention {
    public static List<Primary> instances = new ArrayList<>();

    public boolean isClosed;

    public Caucus(boolean isClosed){
        this.isClosed = isClosed;
    }

    public void convene(){

    }
}
