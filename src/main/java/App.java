import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.awt.SystemColor.menu;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAMenu");
        EntityManager em = emf.createEntityManager();
        Session session = em.unwrap(Session.class);

        try (Scanner sc = new Scanner(System.in)) {
            Menu menu = new Menu();
            initMenu(em, menu);
            while (true) {
                System.out.println("Input '1' to add new Item to Menu");
                System.out.println("Input '2' for search by min/max price");
                System.out.println("Input '3' for discount search");
                System.out.println("Input '4' selection at 15 kg");

                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        addNewItem(sc, em, menu);
                        break;
                    case "2":
                        searchMinMax(sc, session);
                        break;
                    case "3":
                        searchDiscount(session);
                        break;
                    case "4":
                        searchWeight(em, session);
                        break;
                    default:
                        return;
                }
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void initMenu(EntityManager em, Menu menu) {
        em.getTransaction().begin();
        try {
            for (int i = 1; i < 15; i++) {
                menu.addMenuItem(new MenuItem("name" + i, i * 50.5, (i*0.3), true));
            }
            menu.addMenuItem(new MenuItem("name", 50.5, 4, false));
            em.persist(menu);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    private static void searchWeight(EntityManager em,Session session) {
        double leftWeight = 15;
        List<MenuItem> result = new ArrayList<>();

        Query query = em.createQuery("SELECT count(id) AS id FROM MenuItem");
        Long count =(Long) query.getSingleResult();
        long i = 0;
        while (true){

            long randId = (long)(Math.random()*count+1);

            MenuItem temp = em.find(MenuItem.class,randId);
            if (leftWeight >= temp.getWeight()){
                result.add(temp);
                leftWeight=leftWeight-temp.getWeight();
            }
            i++;
            if (i==(count*10)) break;
        }

        for (MenuItem item : result) {
            System.out.println(item);
        }
    }

    private static void searchDiscount(Session session) {
        List<MenuItem> result = session.createCriteria(MenuItem.class)
                .add(Restrictions.eq("discont",true))
                .list();
        for (MenuItem item:  result) {
            System.out.println(item);
        }
    }

    private static void searchMinMax(Scanner sc, Session session) {
        System.out.println("Input min price");
        Double minPrice = Double.parseDouble(sc.nextLine());
        System.out.println("Input max price");
        Double maxPrice = Double.parseDouble(sc.nextLine());

        List<MenuItem> result = session.createCriteria(MenuItem.class)
                .add(Restrictions.between("price",minPrice,maxPrice))
                .list();
        for (MenuItem item:  result) {
            System.out.println(item);
        }

    }

    private static void addNewItem(Scanner sc, EntityManager em, Menu menu) {
        em.getTransaction().begin();
        try {
            System.out.println("Input Item name :");
            String name = sc.nextLine();
            System.out.println("Input Item price");
            Double price = Double.parseDouble(sc.nextLine());
            System.out.println("Input Item weight");
            Double weight = Double.parseDouble(sc.nextLine());
            System.out.println("Item with discount? input Y/N");
            boolean discount;
            while (true) {
                String ans = sc.nextLine();
                if ((ans.equalsIgnoreCase("y")) || (ans.equalsIgnoreCase("n"))) {
                    if (ans.equalsIgnoreCase("y")) {
                        discount = true;
                    } else {
                        discount = false;
                    }
                    break;
                } else {
                    System.out.println("Wrong input");
                }
            }
            menu.addMenuItem(new MenuItem(name, price, weight, discount));

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return;
        }
    }
}
