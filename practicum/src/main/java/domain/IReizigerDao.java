package domain;

import java.sql.*;
import java.util.List;

public interface IReizigerDao {

    void save(Reiziger reiziger) throws SQLException;

    void update(Reiziger reiziger) throws SQLException;

    void delete(Reiziger reiziger) throws SQLException;

    Reiziger findById(int id) throws SQLException;

    List<Reiziger> findByGeboorteDatum(Date date) throws SQLException;

    List<Reiziger> findAll() throws SQLException;
}
