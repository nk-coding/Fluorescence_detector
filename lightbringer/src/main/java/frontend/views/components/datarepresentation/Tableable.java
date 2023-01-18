package frontend.views.components.datarepresentation;

import java.util.List;

public interface Tableable {

    public enum Title{

        TIME("Time"),
        CONCENTRATION("Concentration"),
        DEVIATION("Deviation"),
        RESISTANCE("Resistance");

        Title(String title){
            this.title = title;
        }

        private final String title;

        public String getString(){
            return this.title;
        }
    }

    public class UnsupportedColumnException extends Exception{
        public UnsupportedColumnException(Class msg, Title title){
            super("The class "+ msg.getName() + " does not support displaying of " +title+" columns.");
        }
    }

    Title[] columnsList();

    double getValueColumn(Title title) throws UnsupportedColumnException;


}

