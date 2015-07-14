package Classes;

/**
 * Created by josurubio on 05/04/15.
 */
public class Model {
    String name;
    int value;/* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */
    String id;
    public Model(String name, int value, String id){
        this.name = name;
        this.value = value;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public void setName(String name) {
        this.name = name;
    }
}
