package Modelo;

public class SearchModelo {
    private static SearchModelo instance;
    private int id;
    private String username;
    private String password;
    private String Funcao;

    private SearchModelo(){
    }

    // Classe Sigleton assinatura do id na demais estruturas
    public static SearchModelo getInstance() {
        if (instance == null) {
            instance = new SearchModelo();
        }
        return instance;
    }

    public SearchModelo(String user, String password){
        this.username = user;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFuncao() {
        return Funcao;
    }

    public void setFuncao(String funcao) {
        Funcao = funcao;
    }

    public int SearchId() {
        int id = getId();
        return id;
    }
}
