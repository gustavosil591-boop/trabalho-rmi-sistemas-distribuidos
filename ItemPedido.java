import java.io.Serializable;

public class ItemPedido implements Serializable {
    private Suplemento suplemento; 
    private int quantidade;

    public Suplemento getSuplemento() { return suplemento; }
    public void setSuplemento(Suplemento s) { this.suplemento = s; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int q) { this.quantidade = q; }
}