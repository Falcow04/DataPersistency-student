package infra.hibernate;

import domain.IReizigerDao;
import domain.Reiziger;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.persistence.TypedQuery;

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
            throw new SQLException("Error while saving Reiziger: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        try {
            entityManager.merge(reiziger);
        } catch (Exception e) {
            throw new SQLException("Failed to update Reiziger", e);
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        try {
            Reiziger managedReiziger = entityManager.find(Reiziger.class, reiziger.getReizigerId());
            if (managedReiziger != null) {
                entityManager.remove(managedReiziger);
            }
        } catch (Exception e) {
            throw new SQLException("Failed to delete Reiziger", e);
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        return entityManager.find(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(Date date) {
        TypedQuery<Reiziger> query = entityManager.createQuery(
                "SELECT r FROM Reiziger r WHERE r.geboortedatum = :geboortedatum", Reiziger.class);
        query.setParameter("geboortedatum", date);
        return query.getResultList();
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        TypedQuery<Reiziger> query = entityManager.createQuery(
                "SELECT r FROM Reiziger r", Reiziger.class);
        return query.getResultList();
    }
}
