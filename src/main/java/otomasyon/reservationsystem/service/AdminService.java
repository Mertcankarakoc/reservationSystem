package otomasyon.reservationsystem.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import otomasyon.reservationsystem.model.Enum.Role;
import otomasyon.reservationsystem.model.Enum.Status;
import otomasyon.reservationsystem.model.User;
import otomasyon.reservationsystem.util.HibernateUtil;

public class AdminService {
    public void approveUser(String email) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (user != null) {
                user.setStatus(Status.APPROVED);
                session.update(user);
                transaction.commit();
                System.out.println("User " + email + " has been approved.");
                sendApprovalNotification(user);
            } else {
                System.out.println("User with email " + email + " not found.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private void sendApprovalNotification(User user) {
        // Implement the logic to send a notification to the user
        System.out.println("Notification sent to user: " + user.getEmail() + " - You can now log in.");
    }

    public void createUser(String email, String password) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.ADMIN);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

}
