package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
@Entity
@Table(name = "reiziger")
public class Reiziger {
    @Id
    @Column(name = "reiziger_id")
    private int reizigerid;
    @Column(name = "voorletters")
    private String voorletters;
    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;
    @Column(name = "achternaam")
    private String achternaam;
    @Column(name = "geboortedatum")
    private Date geboortedatum;

    public Reiziger() {
    }

    public int getReizigerId() {
        return reizigerid;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerid = reizigerId;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }


    public domain.Adres getAdres() {
    return null;
    }

    public void setAdres(domain.Adres adres) {

    }

    public List<OvChipkaart> getOvChipkaart() {
        return null;
    }

    public void setOvChipkaart(List<OvChipkaart> ovChipkaart) {
    }
}
