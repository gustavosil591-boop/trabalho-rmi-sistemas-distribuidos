import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    public static void main(String[] args) {
        try {
            // aqui criar o objeto do servidor
            ServidorImpl servico = new ServidorImpl();

            // aqui iniciar o rmiregistry que é o serviço de nomes
            Registry registry = LocateRegistry.createRegistry(1099); // Porta padrão
            
            // aqui registrar o objeto com um nome
            registry.bind("LojaSuplementos", servico); 

            System.out.println("Servidor da Loja pronto!");
            System.out.println("Aguardando conexões...");

        } catch (Exception e) {
            //a mensagem abaixo aparece se der erro no servidor
            System.err.println("Erro no Servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}