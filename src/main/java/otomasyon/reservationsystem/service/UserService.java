package otomasyon.reservationsystem.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import otomasyon.reservationsystem.model.Enum.Role;
import otomasyon.reservationsystem.model.Enum.Status;
import otomasyon.reservationsystem.model.User;
import otomasyon.reservationsystem.util.HibernateUtil;

public class UserService {
    public void registerUser(String name, String surname, String email, String phoneNumber, String password, String role) {
        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setStatus(Status.PENDING);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean isEmailRegistered(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return user != null;
        }
    }

    public User logIn(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE email = :email AND password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();

            if (user != null) {
                System.out.println("Login successful for user: " + user.getEmail());
                return user;
            } else {
                System.out.println("Login failed: Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createAdminUser() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            User admin = new User();
            admin.setEmail("admin1@mail.com");
            admin.setPassword("123"); // Make sure to hash the password in a real application
            admin.setRole(Role.ADMIN);
            session.save(admin);
            session.getTransaction().commit();
        }
    }

    public void notifyUserApproval(String email) {
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
            } else {
                throw new IllegalArgumentException("User not found.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean resetPassword(String email, String newPassword) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            if (user != null) {
                user.setPassword(newPassword);
                session.update(user);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}