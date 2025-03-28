package infra.hibernate;

import domain.IOvChipkaartDao;
import domain.IProductDao;
import domain.OvChipkaart;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class OvChipkaartDaoHibernate implements IOvChipkaartDao {

    private final EntityManager entityManager;

    public OvChipkaartDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(OvChipkaart ovChipkaart) throws SQLException {
        try {
            entityManager.persist(ovChipkaart);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error saving OvChipkaart", e);
        }
    }

    @Override
    public void update(OvChipkaart ovChipkaart) throws SQLException {
        try {
            entityManager.merge(ovChipkaart);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error updating OvChipkaart", e);
        }
    }

    @Override
    public void delete(OvChipkaart ovChipkaart) throws SQLException {
        try {
            entityManager.remove(ovChipkaart);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error deleting OvChipkaart", e);
        }
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        try {
            return entityManager.find(OvChipkaart.class, id);
        } catch (Exception e) {
            throw new SQLException("Error finding OvChipkaart by ID", e);
        }
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        try {
            return entityManager.createQuery("SELECT o FROM OvChipkaart o WHERE o.reiziger = :reiziger", OvChipkaart.class)
                    .setParameter("reiziger", reiziger)
                    .getResultList();
        } catch (Exception e) {
            throw new SQLException("Error finding OvChipkaart by Reiziger", e);
        }
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        try {
            return entityManager.createQuery("SELECT o FROM OvChipkaart o", OvChipkaart.class)
                    .getResultList();
        } catch (Exception e) {
            throw new SQLException("Error finding all OvChipkaarten", e);
        }
    }

    @Override
    public void setProductDao(IProductDao productDao) {

    }
}