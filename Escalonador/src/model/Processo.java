package model;

public class Processo {

    private int tempoChegada, tempoServico, prioridadeOriginal, restante, espera, maximaEspera, totalEspera, prioridadeRelativa;

    public int getPrioridadeOriginal() {
        return prioridadeOriginal;
    }

    public void setPrioridadeOriginal(int prioridadeOriginal) {
        this.prioridadeOriginal = prioridadeOriginal;
    }

    public int getPrioridadeRelativa() {
        return prioridadeRelativa;
    }

    public void setPrioridadeRelativa(int prioridadeRelativa) {
        this.prioridadeRelativa = prioridadeRelativa;
    }

    public int getTempoChegada() {
        return tempoChegada;
    }

    public void setTempoChegada(int tempoChegada) {
        this.tempoChegada = tempoChegada;
    }

    public int getTempoServico() {
        return tempoServico;
    }

    public void setTempoServico(int tempoServico) {
        this.tempoServico = tempoServico;
    }



    public int getRestante() {
        return restante;
    }

    public void setRestante(int restante) {
        this.restante = restante;
    }

    public int getEspera() {
        return espera;
    }

    public void setEspera(int espera) {
        this.espera = espera;
    }

    public int getMaximaEspera() {
        return maximaEspera;
    }

    public void setMaximaEspera(int maximaEspera) {
        this.maximaEspera = maximaEspera;
    }

    public int getTotalEspera() {
        return totalEspera;
    }

    public void setTotalEspera(int totalEspera) {
        this.totalEspera = totalEspera;
    }

}
