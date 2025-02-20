package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDaoPsql implements IReizigerDao {

    private Connection connection;
    private IOvChipkaartDao ovChipkaartDao;
    private IAdresDao adresDao;

    public ReizigerDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Reiziger reiziger) throws SQLException {
        String sql = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reiziger.getReizigerId());
            pstmt.setString(2, reiziger.getVoorletters());
            pstmt.setString(3, reiziger.getTussenvoegsel());
            pstmt.setString(4, reiziger.getAchternaam());
            pstmt.setDate(5, reiziger.getGeboortedatum());

            pstmt.executeUpdate();
        }

        // Save address if present
        if (reiziger.getAdres() != null && adresDao != null) {
            adresDao.save(reiziger.getAdres());
        }

        // Save OV-chipkaarten if present
        if (reiziger.getOvChipkaart() != null && ovChipkaartDao != null) {
            for (OvChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
                ovChipkaartDao.save(ovChipkaart);
            }
        }
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reiziger.getVoorletters());
            pstmt.setString(2, reiziger.getTussenvoegsel());
            pstmt.setString(3, reiziger.getAchternaam());
            pstmt.setDate(4, reiziger.getGeboortedatum());
            pstmt.setInt(5, reiziger.getReizigerId());

            pstmt.executeUpdate();
        }

        // Update address if present
        if (reiziger.getAdres() != null && adresDao != null) {
            adresDao.update(reiziger.getAdres());
        }

        // Update OV-chipkaarten if present
        if (reiziger.getOvChipkaart() != null && ovChipkaartDao != null) {
            for (OvChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
                ovChipkaartDao.update(ovChipkaart);
            }
        }
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        // Delete dependent records first
        if (adresDao != null && reiziger.getAdres() != null) {
            adresDao.delete(reiziger.getAdres());
        }

        if (ovChipkaartDao != null && reiziger.getOvChipkaart() != null) {
            for (OvChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
                ovChipkaartDao.delete(ovChipkaart);
            }
        }

        String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reiziger.getReizigerId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String sql = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToReiziger(rs);
            }
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(Date date) throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String sql = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, date);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reizigers.add(mapResultSetToReiziger(rs));
            }
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String sql = "SELECT * FROM reiziger";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reizigers.add(mapResultSetToReiziger(rs));
            }
        }
        return reizigers;
    }

    // Helper method to map a ResultSet row to a Reiziger object
    private Reiziger mapResultSetToReiziger(ResultSet rs) throws SQLException {
        Reiziger reiziger = new Reiziger();
        reiziger.setReizigerId(rs.getInt("reiziger_id"));
        reiziger.setVoorletters(rs.getString("voorletters"));
        reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
        reiziger.setAchternaam(rs.getString("achternaam"));
        reiziger.setGeboortedatum(rs.getDate("geboortedatum"));

        // Fetch related entities if DAOs are provided
        if (adresDao != null) {
            reiziger.setAdres(adresDao.findByReiziger(reiziger));
        }
        if (ovChipkaartDao != null) {
            reiziger.setOvChipkaart(ovChipkaartDao.findByReiziger(reiziger));
        }

        return reiziger;
    }

    public void setAdresDao(IAdresDao adresDaoPsql) {
        this.adresDao = adresDaoPsql;
    }

    public void setOvChipkaartDao(IOvChipkaartDao ovChipkaartDaoPsql) {
        this.ovChipkaartDao = ovChipkaartDaoPsql;
    }
}
