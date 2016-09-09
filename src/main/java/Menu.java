import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuItem> list = new ArrayList<>();

    public Menu() {
    }

    public List<MenuItem> getList() {
        return list;
    }

    public void setList(List<MenuItem> list) {
        this.list = list;
    }

    public void addMenuItem(MenuItem item){
        item.setMenu(this);
        list.add(item);
    }

}
