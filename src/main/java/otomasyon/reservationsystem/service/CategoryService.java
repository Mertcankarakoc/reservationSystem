package otomasyon.reservationsystem.service;

import org.hibernate.Session;
import otomasyon.reservationsystem.model.Category;
import otomasyon.reservationsystem.util.HibernateUtil;

import java.util.List;

public class CategoryService {

    public List<Category> getAllCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Category", Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}