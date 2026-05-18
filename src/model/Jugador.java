package model;

import java.util.*;
public class Jugador {
    private String        nombre;
    private int           lp = 8000;
    private List<Carta>   mazo   = new ArrayList<>();
    private List<Carta>   mano   = new ArrayList<>();
    private List<Monstruo> campo  = new ArrayList<>();
    private List<Trampa>  trampas = new ArrayList<>(); 
    public Jugador(String nombre) { this.nombre = nombre; }
    public String         getNombre()  { return nombre; }
    public int            getLp()      { return lp; }
    public List<Carta>    getMano()    { return mano; }
    public List<Monstruo> getCampo()  { return campo; }
    public List<Carta>    getMazo()   { return mazo; }
    public List<Trampa>   getTrampas(){ return trampas; }
    public boolean estaVivo()       { return lp > 0; }
    public boolean tieneMazo()      { return !mazo.isEmpty(); }
    public boolean tieneMano()      { return !mano.isEmpty(); }
    public boolean tieneMonstruos() { return !campo.isEmpty(); }
    public boolean tieneTrampas()   { return !trampas.isEmpty(); }
    public void agregarAlMazo(Carta c) { mazo.add(c); }
    public boolean robarCarta() {
        if (mazo.isEmpty()) return false;
        mano.add(mazo.remove(0));
        return true;
    }
    public void invocarMonstruo(Monstruo m) { campo.add(m); }
    public void removerMonstruo(Monstruo m) { campo.remove(m); }
    public void limpiarCampo()              { campo.clear(); }
    public void agotarAtaquesMonstruos() {
        for (Monstruo m : campo) m.agotarAtaque();
    }

    public void colocarTrampa(Trampa t) { trampas.add(t); }
    public void removerTrampa(Trampa t) { trampas.remove(t); }
    public void recibirDanio(int cant)  { lp -= cant; if (lp < 0) lp = 0; }
    public void recuperarLp(int cant)   { lp += cant; }
}
