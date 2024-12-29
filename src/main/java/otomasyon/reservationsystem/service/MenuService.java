package otomasyon.reservationsystem.service;

import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import org.hibernate.Session;
import otomasyon.reservationsystem.util.HibernateUtil;

import java.awt.*;
import java.util.List;

public class MenuService {

    public void createMenu(otomasyon.reservationsystem.model.MenuItem menuItem) throws SystemException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = (Transaction) session.beginTransaction();
            session.save(menuItem);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateMenu(otomasyon.reservationsystem.model.MenuItem menuItem) throws SystemException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = (Transaction) session.beginTransaction();
            session.update(menuItem);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteMenu(otomasyon.reservationsystem.model.MenuItem menuItem) throws SystemException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = (Transaction) session.beginTransaction();
            session.delete(menuItem);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<otomasyon.reservationsystem.model.MenuItem> getMenuItems(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "FROM MenuItem AS mi left join Category as c on c.id = mi.category.id where c.name='" + category + "'";
            return session.createQuery(query, otomasyon.reservationsystem.model.MenuItem.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
