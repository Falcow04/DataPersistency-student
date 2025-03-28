package infra.hibernate;

import domain.IProductDao;
import domain.OvChipkaart;
import domain.Product;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class ProductDaoHibernate implements IProductDao {

    private final EntityManager entityManager;

    public ProductDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Product product) throws SQLException {
        try {
            entityManager.persist(product);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error saving Product", e);
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        try {
            entityManager.merge(product);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error updating Product", e);
        }
    }

    @Override
    public void delete(Product product) throws SQLException {
        try {
            product.getOvChipKaarten().forEach(ovChipkaart -> ovChipkaart.getProducten().remove(product));
            entityManager.remove(product);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SQLException("Error deleting Product", e);
        }
    }

    @Override
    public Product findById(int id) throws SQLException {
        try {
            return entityManager.find(Product.class, id);
        } catch (Exception e) {
            throw new SQLException("Error finding Product by ID", e);
        }
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        try {
            return entityManager.createQuery("SELECT p FROM Product p WHERE :ovChipkaart MEMBER OF p.ovChipKaarten", Product.class)
                    .setParameter("ovChipkaart", ovChipkaart)
                    .getResultList();
        } catch (Exception e) {
            throw new SQLException("Error finding Products by OvChipkaart", e);
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try {
            return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                    .getResultList();
        } catch (Exception e) {
            throw new SQLException("Error finding all Products", e);
        }
    }
}