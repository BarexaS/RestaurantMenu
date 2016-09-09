import javax.persistence.*;

@Entity
@Table(name = "MenuItems")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;
    private double weight;
    private boolean discont;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MenuItem() {
    }

    public MenuItem(String name, double price, double weight, boolean discont) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.discont = discont;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isDiscont() {
        return discont;
    }

    public void setDiscont(boolean discont) {
        this.discont = discont;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                " name='" + name + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", discont=" + discont +
                '}';
    }
}
