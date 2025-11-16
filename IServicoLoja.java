import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface RMI que é o "Contrato"
// aqui só tem um método, que despacha as mensagens
public interface IServicoLoja extends Remote {
    // aqui o cliente envia a mensagem serializada e recebe uma resposta serializada
    byte[] despachar(byte[] mensagemSerializada) throws RemoteException;
}