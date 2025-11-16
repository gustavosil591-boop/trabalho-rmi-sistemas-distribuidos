import java.io.Serializable;

// Esta classe é o "pacote" que vai e volta como JSON
public class Mensagem implements Serializable {
    private int messageType; 
    private int requestID;
    private String objectReference;
    private String methodID;
    // Esses são argumentos em JSON
    private byte[] arguments; 

    // Getters e Setters para todos os campos
    public int getMessageType() { return messageType; }
    public void setMessageType(int m) { this.messageType = m; }
    public int getRequestID() { return requestID; }
    public void setRequestID(int r) { this.requestID = r; }
    public String getObjectReference() { return objectReference; }
    public void setObjectReference(String o) { this.objectReference = o; }
    public String getMethodID() { return methodID; }
    public void setMethodID(String m) { this.methodID = m; }
    public byte[] getArguments() { return arguments; }
    public void setArguments(byte[] a) { this.arguments = a; }
}