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
    private int size;

    public Data(String type, int size){
        this.type = FromStringToType(type);
        this.size = size;
        this.processed = 0;
    }

    private Data.Type FromStringToType(String type){
        switch (type.toLowerCase()) {
            case ("text"):
                return Data.Type.Text;
            case ("images"):
                return Data.Type.Images;
            case ("tabular"):
                return Data.Type.Tabular;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "Data{" +
                "type=" + type +
                ", processed=" + processed +
                ", size=" + size +
                '}';
    }

    public Type getType() {
        return type;
    }

    public int getProcessed() {
        return processed;
    }

    public int getSize() {
        return size;
    }
}
