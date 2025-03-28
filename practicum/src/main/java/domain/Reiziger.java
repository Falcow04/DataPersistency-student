package domain;

import jakarta.persistence.*;

import java.sql.Date;
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

    // One-to-One relationship with Adres
    @OneToOne(mappedBy = "reiziger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Adres adres;

    // One-to-Many relationship with OvChipkaart
    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OvChipkaart> ovChipkaart;

    // Default constructor
    public Reiziger() {
    }

    // Getters and Setters
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

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OvChipkaart> getOvChipkaart() {
        return ovChipkaart;
    }

    public void setOvChipkaart(List<OvChipkaart> ovChipkaart) {
        this.ovChipkaart = ovChipkaart;
    }
}
