package modelDominio;
import java.io.Serializable;

public class Usuario implements Serializable{
    private static final long serialVersionUID = 123l;
    private int codUsuario;
    private String nome;
    private String login;
    private String senha;
    private String endereco;

    @Override
    public boolean equals(Object obj) {
        boolean resultado;
        
        if (obj instanceof Usuario) {
            Usuario usr = (Usuario) obj;
            resultado = this.codUsuario == usr.getCodUsuario();
        } else
            resultado = false;
        
        return resultado;
    }

    @Override
    public String toString() {
        return  "Usuario{" + "codUsuario=" + codUsuario + ", nomeUsuario=" + nome + ", login=" + login + ", senha=" + senha + ", endereco=" + endereco + '}';
                }  
    
    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Usuario(String nome, String login, String senha, String endereco) {
        //INSERT
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.endereco = endereco;
    }

    public Usuario(int codUsuario, String nome, String login, String senha, String endereco) {
       //SELECTS e UPDATES
        this.codUsuario = codUsuario;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.endereco = endereco;
    }

    public Usuario(String login, String senha) {
        //LOGIN
        this.login = login;
        this.senha = senha;
    }

    public Usuario(int codUsuario) {
        //DELETES
        this.codUsuario = codUsuario;
    }

    public Usuario(int codUsuario, String nome) {
        //PARA O OBJETO DE VENDA
        this.codUsuario = codUsuario;
        this.nome = nome;
    }

    public Usuario(int codUsuario, String nome, String endereco) {
        //PARA A VISUALIZAÇÃO DE CLIENTES POR PARTE DO VENDEDOR
        this.codUsuario = codUsuario;
        this.nome = nome;
        this.endereco = endereco;
    }
    
}
