package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDaoPsql implements IReizigerDao {

    private Connection connection;
    private IOvChipkaartDao ovChipkaartDao;
    private IAdresDao adresDao;
    private IProductDao productDao;

    public ReizigerDaoPsql(Connection connection) {
        this.connection = connection;
        this.adresDao = new AdresDaoPsql(connection);
        this.ovChipkaartDao = new OvChipkaartDaoPsql(connection);
        this.productDao = new ProductDaoPsql(connection);
    }

    @Override
    public void save(Reiziger reiziger) throws SQLException {
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reiziger.getReizigerId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboortedatum());
            preparedStatement.executeUpdate();
        }

        Adres adres = reiziger.getAdres();
        if (adres != null) {
            adres.setReiziger(reiziger);
            adresDao.save(adres);
        }

        for (OvChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
            ovChipkaart.setReiziger(reiziger);
            ovChipkaartDao.save(ovChipkaart);

            for (Product product : ovChipkaart.getProducten()) {
                productDao.save(product);
                String joinQuery = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
                try (PreparedStatement joinStatement = connection.prepareStatement(joinQuery)) {
                    joinStatement.setInt(1, ovChipkaart.getKaartNummer());
                    joinStatement.setInt(2, product.getProductNummer());
                    joinStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, reiziger.getVoorletters());
            preparedStatement.setString(2, reiziger.getTussenvoegsel());
            preparedStatement.setString(3, reiziger.getAchternaam());
            preparedStatement.setDate(4, reiziger.getGeboortedatum());
            preparedStatement.setInt(5, reiziger.getReizigerId());
            preparedStatement.executeUpdate();
        }

        Adres adres = reiziger.getAdres();
        if (adres != null) {
            adres.setReiziger(reiziger);
            adresDao.update(adres);
        }

        for (OvChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
            ovChipkaart.setReiziger(reiziger);
            ovChipkaartDao.update(ovChipkaart);

            for (Product product : ovChipkaart.getProducten()) {
                productDao.update(product);
            }
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        List<OvChipkaart> ovChipkaarten = ovChipkaartDao.findByReiziger(reiziger);
        for (OvChipkaart ovChipkaart : ovChipkaarten) {
            ovChipkaartDao.delete(ovChipkaart);
        }

        Adres adres = reiziger.getAdres();
        if (adres != null) {
            adres.setReiziger(reiziger);
            adresDao.delete(adres);
        }

        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, reiziger.getReizigerId());
        preparedStatement.executeUpdate();
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Reiziger reiziger = new Reiziger();
            reiziger.setReizigerId(resultSet.getInt("reiziger_id"));
            reiziger.setVoorletters(resultSet.getString("voorletters"));
            reiziger.setTussenvoegsel(resultSet.getString("tussenvoegsel"));
            reiziger.setAchternaam(resultSet.getString("achternaam"));
            reiziger.setGeboortedatum(resultSet.getDate("geboortedatum"));

            Adres adres = adresDao.findByReiziger(reiziger);
            reiziger.setAdres(adres);

            List<OvChipkaart> ovChipkaarten = ovChipkaartDao.findByReiziger(reiziger);
            reiziger.setOvChipkaart(ovChipkaarten);

            return reiziger;
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(Date date) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Reiziger> reizigers = new ArrayList<>();
        while (resultSet.next()) {
            Reiziger reiziger = new Reiziger();
            reiziger.setReizigerId(resultSet.getInt("reiziger_id"));
            reiziger.setVoorletters(resultSet.getString("voorletters"));
            reiziger.setTussenvoegsel(resultSet.getString("tussenvoegsel"));
            reiziger.setAchternaam(resultSet.getString("achternaam"));
            reiziger.setGeboortedatum(resultSet.getDate("geboortedatum"));
            reizigers.add(reiziger);
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String query = "SELECT * FROM reiziger";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Reiziger> reizigers = new ArrayList<>();
        while (resultSet.next()) {
            Reiziger reiziger = new Reiziger();
            reiziger.setReizigerId(resultSet.getInt("reiziger_id"));
            reiziger.setVoorletters(resultSet.getString("voorletters"));
            reiziger.setTussenvoegsel(resultSet.getString("tussenvoegsel"));
            reiziger.setAchternaam(resultSet.getString("achternaam"));
            reiziger.setGeboortedatum(resultSet.getDate("geboortedatum"));
            reizigers.add(reiziger);
        }
        return null;
    }

    public void setAdresDao(IAdresDao adresDaoPsql) {
        this.adresDao = adresDaoPsql;
    }

    public void setOvChipkaartDao(IOvChipkaartDao ovChipkaartDaoPsql) {
        this.ovChipkaartDao = ovChipkaartDaoPsql;
    }
}