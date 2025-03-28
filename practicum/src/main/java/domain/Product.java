package domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column (name = "product_nummer")
    private int productNummer;
    @Column (name = "naam")
    private String naam;
    @Column (name = "beschrijving")
    private String beschrijving;
    @Column (name = "prijs")
    private BigDecimal prijs;
    @ManyToMany(mappedBy = "producten", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<OvChipkaart> ovChipKaarten;

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public void setPrijs(BigDecimal prijs) {
        this.prijs = prijs;
    }

    public List<OvChipkaart> getOvChipKaarten() {
        return ovChipKaarten;
    }

    public void setOvChipKaarten(List<OvChipkaart> ovChipKaarten) {
        this.ovChipKaarten = ovChipKaarten;
    }
}