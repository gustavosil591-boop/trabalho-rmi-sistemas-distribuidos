import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    private int id;
    // Agregação 2 tem um Cliente
    private Cliente cliente;
    // Agregação 3 tem vários Itens
    private List<ItemPedido> itens;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente c) { this.cliente = c; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}