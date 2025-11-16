import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson; // Importando  a biblioteca JSON

// Esta é a classe que implementa a interface RMI
public class ServidorImpl extends UnicastRemoteObject implements IServicoLoja {
    
    // aqui usei um banco de dados simples
    private List<Cliente> clientes = new ArrayList<>();
    private List<Suplemento> suplementos = new ArrayList<>();
    private int clienteIdCounter = 1;
    private int pedidoIdCounter = 1;

    protected ServidorImpl() throws RemoteException {
        super();
        // aqui cadastra alguns suplementos para teste
        SuplementoProteico whey = new SuplementoProteico();
        whey.setId(1);
        whey.setNome("Whey 100%");
        whey.setPreco(120.0);
        whey.setSabor("Baunilha");
        suplementos.add(whey);

        Vitaminico multi = new Vitaminico();
        multi.setId(2);
        multi.setNome("Multivitamínico A-Z");
        multi.setPreco(80.0);
        multi.setPrincipaisVitaminas("A, C, D, E, K");
        suplementos.add(multi);
    }

    // Este é o único método RMI que o cliente chama
    @Override
    public byte[] despachar(byte[] mensagemSerializada) throws RemoteException {
        // aqui desempacotar a mensagem do cliente
        String jsonRequest = new String(mensagemSerializada);
        Gson gson = new Gson();
        Mensagem msgRequest = gson.fromJson(jsonRequest, Mensagem.class);

        // aqui chama o método getRequest
        byte[] dadosResposta = getRequest(msgRequest.getMethodID(), msgRequest.getArguments());

        // aqui chama o sendReply para empacotar a resposta
        Mensagem msgReply = sendReply(msgRequest.getRequestID(), dadosResposta);

        // aqui serializar a mensagem de resposta e envia
        String jsonReply = gson.toJson(msgReply);
        return jsonReply.getBytes();
    }

    private byte[] getRequest(String methodID, byte[] arguments) {
        Gson gson = new Gson();
        System.out.println("Servidor: Recebido pedido para o método: " + methodID);

        // O "roteador" do servidor
        switch (methodID) {
            case "cadastrarCliente": {
                String jsonArgs = new String(arguments);
                Cliente c = gson.fromJson(jsonArgs, Cliente.class);
                c.setId(clienteIdCounter++);
                clientes.add(c);
                System.out.println("Cliente cadastrado: " + c.getNome());
                // Resposta serializada
                String jsonResposta = gson.toJson(c); // Retorna o cliente com ID
                return jsonResposta.getBytes();
            }

            case "listarSuplementos": {
                // Resposta serializada
                String jsonResposta = gson.toJson(suplementos);
                return jsonResposta.getBytes();
            }

            case "realizarPedido": {
                String jsonArgs = new String(arguments);
                Pedido p = gson.fromJson(jsonArgs, Pedido.class);
                p.setId(pedidoIdCounter++);
                System.out.println("Pedido realizado para: " + p.getCliente().getNome());
                // Resposta serializada
                String jsonResposta = gson.toJson(p); // Retorna o pedido com ID
                return jsonResposta.getBytes();
            }
            
            case "verificarEstoque": {
                // aqui pega os argumentos que é o ID do suplemento em JSON
                String jsonArgs = new String(arguments);
                // aqui desserializa o ID que é um simples int
                int suplementoId = gson.fromJson(jsonArgs, Integer.class);

                // aqui procura o suplemento
                int estoqueEncontrado = 0; // usa 0 por padrão
                for (Suplemento s : suplementos) {
                    if (s.getId() == suplementoId) {
                        estoqueEncontrado = 100; 
                        break;
                    }
                }
                
                System.out.println("Servidor: Verificando estoque do ID " + suplementoId + ". Encontrado: " + estoqueEncontrado);

                // Resposta serializada
                String jsonResposta = gson.toJson(estoqueEncontrado);
                return jsonResposta.getBytes();
            }

            default:
                String jsonResposta = gson.toJson("Erro: Método não encontrado");
                return jsonResposta.getBytes();
        }
    }
    private Mensagem sendReply(int requestID, byte[] replyData) {
        Mensagem msgReply = new Mensagem();
        msgReply.setMessageType(1); 
        msgReply.setRequestID(requestID); // aqui é a responde ao ID da requisição
        msgReply.setObjectReference("ServidorImpl");
        msgReply.setMethodID("reply");
        msgReply.setArguments(replyData);
        return msgReply;
    }
}