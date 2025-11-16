// Herança 2
public class Vitaminico extends Suplemento {
    private String principaisVitaminas;

    public String getPrincipaisVitaminas() { return principaisVitaminas; }
    public void setPrincipaisVitaminas(String v) { this.principaisVitaminas = v; }

    @Override
    public String toString() {
        return super.toString() + ", Vitaminas: " + principaisVitaminas;
    }
}