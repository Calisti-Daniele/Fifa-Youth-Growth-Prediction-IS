package model.bean;

public class FifaPlayer {
    private String nome;
    private String overall;
    private String shooting;
    private String passing;
    private String dribbling;
    private String defending;
    private String physic;
    private String longName;
    private String playerUrl;
    private String shortName;
    private String playerPositions;
    private String nationalityName;
    private String preferredFoot;

    public FifaPlayer() {}

    public FifaPlayer(String nome, String overall, String shooting, String passing, String dribbling,
                      String defending, String physic, String longName, String playerUrl, String shortName,
                      String playerPositions, String nationalityName, String preferredFoot) {
        this.nome = nome;
        this.overall = overall;
        this.shooting = shooting;
        this.passing = passing;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physic = physic;
        this.longName = longName;
        this.playerUrl = playerUrl;
        this.shortName = shortName;
        this.playerPositions = playerPositions;
        this.nationalityName = nationalityName;
        this.preferredFoot = preferredFoot;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getOverall() { return overall; }
    public void setOverall(String overall) { this.overall = overall; }

    public String getShooting() { return shooting; }
    public void setShooting(String shooting) { this.shooting = shooting; }

    public String getPassing() { return passing; }
    public void setPassing(String passing) { this.passing = passing; }

    public String getDribbling() { return dribbling; }
    public void setDribbling(String dribbling) { this.dribbling = dribbling; }

    public String getDefending() { return defending; }
    public void setDefending(String defending) { this.defending = defending; }

    public String getPhysic() { return physic; }
    public void setPhysic(String physic) { this.physic = physic; }

    public String getLongName() { return longName; }
    public void setLongName(String longName) { this.longName = longName; }

    public String getPlayerUrl() { return playerUrl; }
    public void setPlayerUrl(String playerUrl) { this.playerUrl = playerUrl; }

    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }

    public String getPlayerPositions() { return playerPositions; }
    public void setPlayerPositions(String playerPositions) { this.playerPositions = playerPositions; }

    public String getNationalityName() { return nationalityName; }
    public void setNationalityName(String nationalityName) { this.nationalityName = nationalityName; }

    public String getPreferredFoot() { return preferredFoot; }
    public void setPreferredFoot(String preferredFoot) { this.preferredFoot = preferredFoot; }

    @Override
    public String toString() {
        return "FifaPlayer{" +
                "nome='" + nome + '\'' +
                ", overall='" + overall + '\'' +
                ", shooting='" + shooting + '\'' +
                ", passing='" + passing + '\'' +
                ", dribbling='" + dribbling + '\'' +
                ", defending='" + defending + '\'' +
                ", physic='" + physic + '\'' +
                ", longName='" + longName + '\'' +
                ", playerUrl='" + playerUrl + '\'' +
                ", shortName='" + shortName + '\'' +
                ", playerPositions='" + playerPositions + '\'' +
                ", nationalityName='" + nationalityName + '\'' +
                ", preferredFoot='" + preferredFoot + '\'' +
                '}';
    }
}
