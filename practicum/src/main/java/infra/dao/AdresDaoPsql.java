package infra.dao;

import domain.Adres;
import domain.IAdresDao;
import domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDaoPsql implements IAdresDao {
    private Connection connection;

    public AdresDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Adres adres) throws SQLException {
        String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, adres.getAdresId());
            pstmt.setString(2, adres.getPostcode());
            pstmt.setString(3, adres.getHuisnummer());
            pstmt.setString(4, adres.getStraat());
            pstmt.setString(5, adres.getWoonplaats());
            pstmt.setInt(6, adres.getReiziger().getReizigerId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void update(Adres adres) throws SQLException {
        String sql = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ? WHERE adres_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, adres.getPostcode());
            pstmt.setString(2, adres.getHuisnummer());
            pstmt.setString(3, adres.getStraat());
            pstmt.setString(4, adres.getWoonplaats());
            pstmt.setInt(5, adres.getAdresId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Adres adres) throws SQLException {
        String sql = "DELETE FROM adres WHERE adres_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, adres.getAdresId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public Adres findById(int id) throws SQLException {
        String sql = "SELECT * FROM adres WHERE adres_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAdres(rs);
            }
        }
        return null;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reiziger.getReizigerId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAdres(rs);
            }
        }
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        String sql = "SELECT * FROM adres";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                adressen.add(mapResultSetToAdres(rs));
            }
        }
        return adressen;
    }

    private Adres mapResultSetToAdres(ResultSet rs) throws SQLException {
        return new Adres(
                rs.getInt("adres_id"),
                rs.getString("postcode"),
                rs.getString("huisnummer"),
                rs.getString("straat"),
                rs.getString("woonplaats"),
                null // Reiziger wordt apart opgehaald
        );
    }
}
