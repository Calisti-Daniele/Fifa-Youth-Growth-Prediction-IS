package model.bean;

import java.time.Instant;

public class Utente {
    private int idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Instant dataIscrizione;

    public Utente() {}

    public Utente(int idUtente, String nome, String cognome, String email, String password, Instant dataIscrizione) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.dataIscrizione = dataIscrizione;
    }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Instant getDataIscrizione() { return dataIscrizione; }
    public void setDataIscrizione(Instant dataIscrizione) { this.dataIscrizione = dataIscrizione; }

    @Override
    public String toString() {
        return "Utente{" +
                "idUtente=" + idUtente +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", dataIscrizione=" + dataIscrizione +
                '}';
    }
}
