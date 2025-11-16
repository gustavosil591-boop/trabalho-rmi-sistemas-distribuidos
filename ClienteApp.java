import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson; // Importando a biblioteca JSON
import com.google.gson.reflect.TypeToken; // isso é para listas
import java.lang.reflect.Type;


public class ClienteApp {

    private static IServicoLoja servicoRMI;
    private static int requestIDCounter = 1;

    public static void main(String[] args) {
        try {
            // Encontrar o rmiregistry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Procurar pelo objeto remoto com o nome LojaSuplementos
            servicoRMI = (IServicoLoja) registry.lookup("LojaSuplementos");

            System.out.println("Cliente conectado ao servidor!");

            // aqui se inicia os testes

            // Teste 1: Listar Suplementos
            System.out.println("\n--- Teste 1: Listando Suplementos ---");
            
            byte[] respostaListar = doOperation("ServidorImpl", "listarSuplementos", null);
            
            // aqui desempacota a resposta
            String jsonRespostaListar = new String(respostaListar);
            Type listaSuplementoTipo = new TypeToken<ArrayList<Suplemento>>(){}.getType();
            
            System.out.println(jsonRespostaListar); 


            // Teste 2: Cadastrar um Cliente
            System.out.println("\n--- Teste 2: Cadastrando Cliente ---");
            Cliente cli = new Cliente();
            cli.setNome("Cliente 01");
            
            // Serializa o objeto Cliente para JSON
            Gson gson = new Gson(); // aqui cria o gson
            String jsonCliente = gson.toJson(cli);
            
            byte[] respostaCliente = doOperation("ServidorImpl", "cadastrarCliente", jsonCliente.getBytes());
            String jsonRespostaCliente = new String(respostaCliente);
            Cliente clienteCadastrado = gson.fromJson(jsonRespostaCliente, Cliente.class);
            System.out.println("Cliente cadastrado com sucesso! ID: " + clienteCadastrado.getId());


            // Teste 3: Realizar Pedido
            System.out.println("\n--- Teste 3: Realizando Pedido ---");
            Pedido p = new Pedido();
            p.setCliente(clienteCadastrado);
            
            String jsonPedido = gson.toJson(p); 
            byte[] respostaPedido = doOperation("ServidorImpl", "realizarPedido", jsonPedido.getBytes());
            String jsonRespostaPedido = new String(respostaPedido);
            Pedido pedidoFeito = gson.fromJson(jsonRespostaPedido, Pedido.class);
            System.out.println("Pedido realizado com sucesso! ID: " + pedidoFeito.getId());

            // Teste 4: Verificar Estoque 
            System.out.println("\n--- Teste 4: Verificando Estoque ---");
            
            // aqui verifica o estoque do suplemento com ID = 1 que é o whey
            int idParaVerificar = 1;

            // Serializa o ID para JSON 
            String jsonId = gson.toJson(idParaVerificar);

            byte[] respostaEstoque = doOperation("ServidorImpl", "verificarEstoque", jsonId.getBytes());
            
            // Desempacotar a resposta
            String jsonRespostaEstoque = new String(respostaEstoque);
            int estoqueAtual = gson.fromJson(jsonRespostaEstoque, Integer.class);
            System.out.println("Estoque do item 1: " + estoqueAtual + " unidades.");


        } catch (Exception e) {
            System.err.println("Erro no Cliente: " + e.toString());
            e.printStackTrace();
        }
    }

   
    // Este método monta o pacote e chama o RMI
    private static byte[] doOperation(String objectRef, String methodId, byte[] arguments) throws Exception {
        Gson gson = new Gson();

        // aqui monta a mensagem de Requisição Request
        Mensagem msgRequest = new Mensagem();
        msgRequest.setMessageType(0); // 0 = Request
        msgRequest.setRequestID(requestIDCounter++);
        msgRequest.setObjectReference(objectRef);
        msgRequest.setMethodID(methodId);
        msgRequest.setArguments(arguments);

        // aqui serializar a mensagem para JSON
        String jsonRequest = gson.toJson(msgRequest);

        //aqui Chama o RMI ou seja ennviar pacote
        System.out.println("Cliente: Enviando requisição para " + methodId);
        byte[] respostaSerializada = servicoRMI.despachar(jsonRequest.getBytes());
        System.out.println("Cliente: Resposta recebida!");

        // aqui desempacotar a mensagem de resposta (Reply)
        String jsonReply = new String(respostaSerializada);
        Mensagem msgReply = gson.fromJson(jsonReply, Mensagem.class);

        // aqui retornar apenas os dados da resposta
        return msgReply.getArguments();
    }
}