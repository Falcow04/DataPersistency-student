package infra.hibernate;

import domain.Adres;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class AdresHibernate implements domain.IAdresDao {

    private final EntityManager entityManager;

    public AdresHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Adres adres) throws SQLException {
        try {
            entityManager.persist(adres);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error saving Adres", e);
        }
    }

    @Override
    public void update(Adres adres) throws SQLException {
        try {
            entityManager.merge(adres);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error updating Adres", e);
        }
    }

    @Override
    public void delete(Adres adres) throws SQLException {
        try {
            entityManager.remove(adres);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error deleting Adres", e);
        }
    }

    @Override
    public Adres findById(int id) throws SQLException {
        return entityManager.find(Adres.class, id);
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        return entityManager.createQuery("SELECT a FROM Adres a WHERE a.reiziger = :reiziger", Adres.class)
                .setParameter("reiziger", reiziger)
                .getSingleResult();
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        return entityManager.createQuery("SELECT a FROM Adres a", Adres.class)
                .getResultList();
    }
}