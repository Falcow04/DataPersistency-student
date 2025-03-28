package infra.hibernate;

import domain.IReizigerDao;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ReizigerHibernate implements IReizigerDao {

    private EntityManager entityManager;

    public ReizigerHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Reiziger reiziger) throws SQLException {
        try {
            entityManager.persist(reiziger);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error saving Reiziger", e);
        }
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        try {
            entityManager.merge(reiziger);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error updating Reiziger", e);
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        try {
            entityManager.remove(reiziger);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error deleting Reiziger", e);
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        return entityManager.find(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(Date date) {
        return entityManager.createQuery("SELECT r FROM Reiziger r WHERE r.geboortedatum = :date", Reiziger.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        return entityManager.createQuery("SELECT r FROM Reiziger r", Reiziger.class).getResultList();
    }
}