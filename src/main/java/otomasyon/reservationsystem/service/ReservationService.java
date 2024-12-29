package otomasyon.reservationsystem.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import otomasyon.reservationsystem.model.Reservation;
import otomasyon.reservationsystem.util.HibernateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ReservationService {

    public void createReservation(Long userId, Reservation reservation) {
        reservation.setUserId(userId);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(reservation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reservation WHERE reservationDate = :date", Reservation.class)
                    .setParameter("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Reservation findReservationByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reservation WHERE userId = :userId", Reservation.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Reservation> getReservationsByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reservation WHERE userId = :userId", Reservation.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}