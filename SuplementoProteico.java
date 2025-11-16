// Herança 1
public class SuplementoProteico extends Suplemento {
    private String sabor;

    public String getSabor() { return sabor; }
    public void setSabor(String sabor) { this.sabor = sabor; }

    @Override
    public String toString() {
        return super.toString() + ", Sabor: " + sabor;
    }
}