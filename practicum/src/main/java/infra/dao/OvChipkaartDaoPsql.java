package infra.dao;

import domain.*;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaartDaoPsql implements IOvChipkaartDao {

    private final Connection connection;
    private IProductDao productDao;

    public OvChipkaartDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(OvChipkaart ovChipkaart) throws SQLException {
        String query = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ovChipkaart.getKaartNummer());
        preparedStatement.setDate(2, ovChipkaart.getGeldigTot());
        preparedStatement.setObject(3, new BigInteger(String.valueOf(ovChipkaart.getKlasse())));
        preparedStatement.setBigDecimal(4, ovChipkaart.getSaldo());
        preparedStatement.setInt(5, ovChipkaart.getReiziger().getReizigerId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(OvChipkaart ovChipkaart) throws SQLException {
        String query = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, ovChipkaart.getGeldigTot());
        preparedStatement.setObject(2, new BigInteger(String.valueOf(ovChipkaart.getKlasse())));
        preparedStatement.setBigDecimal(3, ovChipkaart.getSaldo());
        preparedStatement.setInt(4, ovChipkaart.getReiziger().getReizigerId());
        preparedStatement.setInt(5, ovChipkaart.getKaartNummer());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(OvChipkaart ovChipkaart) throws SQLException {
        String deleteOvcProductQuery = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        try (PreparedStatement preparedStatement1 = connection.prepareStatement(deleteOvcProductQuery)) {
            preparedStatement1.setInt(1, ovChipkaart.getKaartNummer());
            preparedStatement1.executeUpdate();
        }

        String deleteOvChipkaartQuery = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement preparedStatement2 = connection.prepareStatement(deleteOvChipkaartQuery)) {
            preparedStatement2.setInt(1, ovChipkaart.getKaartNummer());
            preparedStatement2.executeUpdate();
        }
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            OvChipkaart ovChipkaart = new OvChipkaart();
            ovChipkaart.setKaartNummer(resultSet.getInt("kaart_nummer"));
            ovChipkaart.setGeldigTot(resultSet.getDate("geldig_tot"));
            ovChipkaart.setKlasse(BigInteger.valueOf(resultSet.getLong("klasse")));
            ovChipkaart.setSaldo(resultSet.getBigDecimal("saldo"));
            return ovChipkaart;
        }
        return null;
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, reiziger.getReizigerId());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<OvChipkaart> ovChipkaarten = new ArrayList<>();
        while (resultSet.next()) {
            OvChipkaart ovChipkaart = new OvChipkaart();
            ovChipkaart.setKaartNummer(resultSet.getInt("kaart_nummer"));
            ovChipkaart.setGeldigTot(resultSet.getDate("geldig_tot"));
            ovChipkaart.setKlasse(BigInteger.valueOf(resultSet.getLong("klasse")));
            ovChipkaart.setSaldo(resultSet.getBigDecimal("saldo"));

            List<Product> producten = productDao.findByOvChipkaart(ovChipkaart);
            ovChipkaart.setProducten(producten);

            ovChipkaarten.add(ovChipkaart);
        }
        return ovChipkaarten;
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        String query = "SELECT * FROM ov_chipkaart";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<OvChipkaart> ovChipkaarten = new ArrayList<>();
        while (resultSet.next()) {
            OvChipkaart ovChipkaart = new OvChipkaart();
            ovChipkaart.setKaartNummer(resultSet.getInt("kaart_nummer"));
            ovChipkaart.setGeldigTot(resultSet.getDate("geldig_tot"));
            ovChipkaart.setKlasse(BigInteger.valueOf(Long.parseLong(resultSet.getString("klasse"))));
            ovChipkaart.setSaldo(resultSet.getBigDecimal("saldo"));
            ovChipkaarten.add(ovChipkaart);
        }
        return ovChipkaarten;
    }

    @Override
    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }
}