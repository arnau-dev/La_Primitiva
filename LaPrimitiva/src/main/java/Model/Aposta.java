package Model;

public class Aposta {
    private int identificador;
    private Combinacio combinacio;

    public Aposta(Combinacio combinacio, int identificador) {
        this.combinacio = combinacio;
        this.identificador = identificador;
    }
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }
    public int getIdentificador() {
        return identificador;
    }
    public Combinacio getCombinacio() {
        return combinacio;
    }
}
