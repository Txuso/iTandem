package AndroidChat;

/**
 * Created by josurubio on 24/04/15.
 */
public class Language {

    private String name;
    private String izena;
    private String nombre;

    public Language(String name, String izena, String nombre){
        this.name = name;
        this.izena = izena;
        this.nombre = nombre;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIzena() {
        return izena;
    }

    public String getName() {
        return name;
    }

    public String getNombre() {
        return nombre;
    }
}
