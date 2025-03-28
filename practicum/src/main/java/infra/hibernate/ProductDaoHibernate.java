package infra.hibernate;

import domain.IProductDao;
import domain.OvChipkaart;
import domain.Product;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class ProductDaoHibernate implements IProductDao {

    private EntityManager entityManager;

    // Constructor om de EntityManager in te stellen
    public ProductDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Product product) throws SQLException {
        // Implementatie voor opslaan van product
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(product);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error saving product", e);
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        // Implementatie voor bijwerken van product
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(product);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error updating product", e);
        }
    }

    @Override
    public void delete(Product product) throws SQLException {
        // Implementatie voor verwijderen van product
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(product) ? product : entityManager.merge(product));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting product", e);
        }
    }

    @Override
    public Product findById(int id) throws SQLException {
        try {
            return entityManager.find(Product.class, id);
        } catch (Exception e) {
            throw new SQLException("Error finding product by ID", e);
        }
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        try {
            return entityManager.createQuery("SELECT p FROM Product p WHERE p.ovChipkaart = :ovChipkaart", Product.class)
                    .setParameter("ovChipkaart", ovChipkaart)
                    .getResultList();
        } catch (Exception e) {
            throw new SQLException("Error finding products by OvChipkaart", e);
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try {
            return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        } catch (Exception e) {
            throw new SQLException("Error finding all products", e);
        }
    }
}
