package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import model.Processador;
import model.Processo;
import view.telaPrincipal;

public class Escalonador implements Runnable {

    private final Processador xeon;
    private final ArrayList<Processo> listaDeProcessos;
    private final ArrayList<Processo> processosAtivos;
    private final ArrayList<Processo> processosFinalizados;
    private int tempoAtual;
    private int trocandoDeContexto;
    private int tamanhoTrocaDeContexto;
    private telaPrincipal tp;
    private int escalonador;
    private final Comparador comparador;
    private int numeroTrocaDeContexto;
    private int tempoManutencao;
    private Processo proximo;

    public void setTp(telaPrincipal tp) {
        this.tp = tp;
    }

    public void setEscalonador(int escalonador) {
        this.escalonador = escalonador;
    }

    public Escalonador() {
        xeon = new Processador();
        listaDeProcessos = new ArrayList<>();
        processosAtivos = new ArrayList<>();
        processosFinalizados = new ArrayList<>();
        tempoAtual = 0;
        trocandoDeContexto = 0;
        tamanhoTrocaDeContexto = 0;
        comparador = new Comparador();
    }

    public void setTamanhoTrocaDeContexto(int tamanhoTrocaDeContexto) {
        this.tamanhoTrocaDeContexto = tamanhoTrocaDeContexto;
    }

    public void umClock() {
        System.out.println("Tempo: " + tempoAtual + " Tamanho ativos " + processosAtivos.size() + " Trocando de contexto " + trocandoDeContexto);

        if (!listaDeProcessos.isEmpty()) {
            while (listaDeProcessos.get(0).getTempoChegada() == tempoAtual) {
                processosAtivos.add(listaDeProcessos.remove(0));
                if (listaDeProcessos.isEmpty()) {
                    break;
                }
            }
        }

        if (escalonador == 5) {

            comparador.loteria(processosAtivos);
        } else {
            comparador.comparar(processosAtivos, escalonador);
        }

        if (trocandoDeContexto == 0) {
            if (xeon.isVazio()) {
                if (!processosAtivos.isEmpty()) {
                    if (proximo != null) {
                        xeon.setExecutando(proximo);
                        proximo = null;
                    } else {

                        xeon.setExecutando(processosAtivos.remove(0));
                    }

                } else {
                    xeon.setVazio(true);
                    xeon.processa();
                }
            } else {
                Processo executando = xeon.processa();
                if (executando != null) {
                    if (escalonador == 2) {
                        xeon.setVazio(false);
                    }

                    if (processosAtivos.isEmpty()) {
                        if (executando.getRestante() > 0) {
                            xeon.setExecutando(executando);
                        } else {
                            xeon.setVazio(true);
                        }
                    } else {

                        if (executando.getRestante() == 0) {
                            xeon.setExecutando(processosAtivos.remove(0));
                            processosFinalizados.add(executando);
                        } else {
                            trocandoDeContexto = tamanhoTrocaDeContexto;
                            numeroTrocaDeContexto++;

                            if (trocandoDeContexto > 0) {
                                trocandoDeContexto--;
                            }
                            if (escalonador != 2) {
                                xeon.setVazio(true);
                            }
                            executando.setPrioridadeRelativa(executando.getPrioridadeOriginal());
                            processosAtivos.add(executando);

                            if (tamanhoTrocaDeContexto == 0) {
                                comparador.comparar(processosAtivos, escalonador);
                                xeon.setExecutando(processosAtivos.remove(0));
                            }

                        }
                    }

                }
            }
        } else {

            trocandoDeContexto -= 1;
            xeon.processa();
            if (tamanhoTrocaDeContexto == 0) {

                comparador.comparar(processosAtivos, escalonador);
                if (!processosAtivos.isEmpty()) {
                    xeon.setExecutando(processosAtivos.remove(0));
                }
            }

//            if (!processosAtivos.isEmpty()) {
//                xeon.setExecutando(processosAtivos.remove(0));
//            }
        }

        processosAtivos.forEach((s) -> {
            s.setEspera(s.getEspera() + 1);
        });

        if (escalonador == 4) {
            ajustaPrioridade();
        }

        tempoAtual++;

    }

    public void setTempoManutencao(int tempoManutencao) {
        this.tempoManutencao = tempoManutencao;
    }

    public void setTrocandoDeContexto(int trocandoDeContexto) {
        this.trocandoDeContexto = trocandoDeContexto;
    }

    public ArrayList<Processo> getListaDeProcessos() {
        return listaDeProcessos;
    }

    public Processador getXeon() {
        return xeon;
    }

    public void executar() {

        while (!(listaDeProcessos.isEmpty() && processosAtivos.isEmpty() && xeon.isVazio())) {
            umClock();
        }

    }

    public void leituraDoArquivo() {
        tempoAtual = 0;
        listaDeProcessos.clear();
        processosAtivos.clear();
        JFileChooser jfAbrir = new JFileChooser();
//        jfAbrir.setCurrentDirectory(new File("C:\\Users\\Cristiano\\Dropbox\\Programas\\NetBeansProjects\\Protótipo 1.0"));
        jfAbrir.setCurrentDirectory(new File("C:\\Users\\Cristiano\\Desktop"));

        int retorno = jfAbrir.showOpenDialog(null);
        String caminho = null;

        if (retorno == JFileChooser.APPROVE_OPTION) {
            caminho = jfAbrir.getSelectedFile().getAbsolutePath();
            String aux = jfAbrir.getSelectedFile().getName();

            //this.Ativo = aux2[0];
            try {
                FileInputStream arquivo = new FileInputStream(caminho);
                leituraDosDados(arquivo);

            } catch (FileNotFoundException ex) {
                System.out.println(ex.toString());
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

        } else {
            System.out.println("Nenhum arquivo selecionado");
        }

    }

    public int getTempoAtual() {
        return tempoAtual;
    }

    private void leituraDosDados(FileInputStream arquivo) throws IOException {

        InputStreamReader input = new InputStreamReader(arquivo);
        BufferedReader ler = new BufferedReader(input);

        while (true) {
            String linha = ler.readLine();
            if (!(linha == null)) {
                String split[] = linha.split(", ");

                Processo novo = new Processo();

                novo.setTempoChegada(Integer.parseInt(split[0].replace(" ", "")));
                novo.setTempoServico(Integer.parseInt(split[1].replace(" ", "")));
                novo.setPrioridadeOriginal(Integer.parseInt(split[2].replace(" ", "")));
                novo.setRestante(novo.getTempoServico());
                novo.setPrioridadeRelativa(novo.getPrioridadeOriginal());

                listaDeProcessos.add(novo);

            } else {
                break;
            }
        }

    }

    public ArrayList<Processo> getProcessosAtivos() {
        return processosAtivos;
    }

    @Override
    public void run() {

        while (!(listaDeProcessos.isEmpty() && processosAtivos.isEmpty() && xeon.isVazio())) {
            umClock();
            tp.atualizaTabela();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Escalonador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void ajustaPrioridade() {

        for (Processo p : processosAtivos) {
            if (p.getEspera() >= tempoManutencao) {

                int acrescenta = (int) p.getEspera() / tempoManutencao;

                if (p.getPrioridadeRelativa() < 4) {
                    p.setPrioridadeRelativa(p.getPrioridadeOriginal() + acrescenta);
                }
            }
        }
    }

    public void resultados() {

        ArrayList<Integer> seila = xeon.getTempoOcioso();

        int maiorTempoOcioso = 0;
        int totalOciosoCPU = 0;

        for (Integer i : seila) {
            totalOciosoCPU += i;
            if (i > maiorTempoOcioso) {
                maiorTempoOcioso = i;
            }
        }

        int maiorEsperaDeUmProcesso = 0;
        float mediaEsperaFila = 0;
        for (Processo s : processosFinalizados) {
            mediaEsperaFila += s.getTotalEspera();
            if (maiorEsperaDeUmProcesso < s.getMaximaEspera()) {
                maiorEsperaDeUmProcesso = s.getMaximaEspera();
            }
        }

        mediaEsperaFila = (mediaEsperaFila / processosFinalizados.size());

        String resposta = "";
        resposta += "Tempo total da simulacao " + (tempoAtual - 1) + "\n";
        resposta += "Tempo máximo de espera na fila para execucao " + maiorEsperaDeUmProcesso + "\n";
        resposta += "Tempo médio de espera de um processo na fila para execução " + mediaEsperaFila + "\n";
        resposta += "Número total de trocas de contexto " + numeroTrocaDeContexto + "\n";
        resposta += "Tempo total de ociosidade da CPU " + totalOciosoCPU + "\n";
        resposta += "Tempo máximo de ociosidade da CPU " + maiorTempoOcioso + "\n";
        resposta += "Tempo ocioso total " + xeon.getOciosoTotal() + "\n";
        resposta += "" + "\n";

        System.out.println(resposta);

    }

}
