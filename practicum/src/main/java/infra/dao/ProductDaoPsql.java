package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoPsql implements IProductDao {

    private Connection connection;

    public ProductDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product product) throws SQLException {
        String query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, product.getProductNummer());
        preparedStatement.setString(2, product.getNaam());
        preparedStatement.setString(3, product.getBeschrijving());
        preparedStatement.setBigDecimal(4, product.getPrijs());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Product product) throws SQLException {
        String query = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, product.getNaam());
        preparedStatement.setString(2, product.getBeschrijving());
        preparedStatement.setBigDecimal(3, product.getPrijs());
        preparedStatement.setInt(4, product.getProductNummer());
        preparedStatement.executeUpdate();
    }

    public void delete(Product product) throws SQLException {
        String deleteOvcProductQuery = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(deleteOvcProductQuery);
        preparedStatement1.setInt(1, product.getProductNummer());
        preparedStatement1.executeUpdate();

        String deleteProductQuery = "DELETE FROM product WHERE product_nummer = ?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(deleteProductQuery);
        preparedStatement2.setInt(1, product.getProductNummer());
        preparedStatement2.executeUpdate();
    }

    @Override
    public Product findById(int id) throws SQLException {
        String query = "SELECT * FROM product WHERE product_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Product product = new Product();
            product.setProductNummer(resultSet.getInt("product_nummer"));
            product.setNaam(resultSet.getString("naam"));
            product.setBeschrijving(resultSet.getString("beschrijving"));
            product.setPrijs(resultSet.getBigDecimal("prijs"));
            return product;
        }

        return null;
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        String query = "SELECT * FROM product p INNER JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer WHERE ocp.kaart_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ovChipkaart.getKaartNummer());
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Product> producten = new ArrayList<>();
        while (resultSet.next()) {
            Product product = new Product();
            product.setProductNummer(resultSet.getInt("product_nummer"));
            product.setNaam(resultSet.getString("naam"));
            product.setBeschrijving(resultSet.getString("beschrijving"));
            product.setPrijs(resultSet.getBigDecimal("prijs"));
            producten.add(product);
        }

        return producten;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String query = "SELECT * FROM product";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Product> producten = new ArrayList<>();
        while (resultSet.next()) {
            Product product = new Product();
            product.setProductNummer(resultSet.getInt("product_nummer"));
            product.setNaam(resultSet.getString("naam"));
            product.setBeschrijving(resultSet.getString("beschrijving"));
            product.setPrijs(resultSet.getBigDecimal("prijs"));
            producten.add(product);
        }

        return producten;
    }
}