package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;

    public int getSize() {
        return size;
    }

    private int size;

    public Data(String type, int size){

    }

    /**
     * @return the Type by the string type
     * <p>
     * @param type the type
     */
    private Data.Type FromStringToType(String type){
        switch (type) {
            case ("Text"):
                return Data.Type.Text;
            case ("Images"):
                return Data.Type.Images;
            case ("Tabular"):
                return Data.Type.Tabular;
            default:
                return null;
        }
    }
}
