package Classes;

/**
 * Created by josurubio on 08/03/15.
 */

//This class will help us to use the menu in the navigation drawer.
public class ItemObject {
        private String title;
        private int icon;

        public ItemObject(String title, int icon){
            this.title = title;
            this.icon = icon;
        }
    public String getTitle(){
        return title;

    }

    public void setTitle(String title){
        this.title = title;
    }
    public int getIcon (){
        return icon;
    }
    public void setIcon(int icon){
        this.icon = icon;
    }

    }

