package domain;

import jakarta.persistence.*;

@Entity
@Table(name = "adres")
public class Adres {
    @Id
    @Column(name = "adres_id")
    private int adresId;
    @Column(name = "postcode")
    private String postcode;
    @Column(name = "huisnummer")
    private String huisnummer;
    @Column(name = "straat")
    private String straat;
    @Column(name = "woonplaats")
    private String woonplaats;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    // Constructor
    public Adres(int adresId, String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger) {
        this.adresId = adresId;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public Adres() {

    }

    // Getters
    public int getAdresId() {
        return adresId;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    // Setters
    public void setAdresId(int adresId) {
        this.adresId = adresId;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        return "Adres{" +
                "adresId=" + adresId +
                ", postcode='" + postcode + '\'' +
                ", huisnummer='" + huisnummer + '\'' +
                ", straat='" + straat + '\'' +
                ", woonplaats='" + woonplaats + '\'' +
                ", reizigerId=" + (reiziger != null ? reiziger.getReizigerId() : "null") +
                '}';
    }
}
