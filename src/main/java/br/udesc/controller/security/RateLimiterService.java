package br.udesc.controller.security;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RateLimiterService {

    private static final int MAX_TENTATIVAS = 5;
    private static final long JANELA_MS = 60_000L; // 1 minuto

    private final ConcurrentHashMap<String, Deque<Long>> historico = new ConcurrentHashMap<>();

    public boolean isPermitido(String chave) {
        long agora = System.currentTimeMillis();
        Deque<Long> tentativas = historico.compute(chave, (k, fila) -> {
            Deque<Long> f = (fila != null) ? fila : new ArrayDeque<>();
            while (!f.isEmpty() && agora - f.peekFirst() > JANELA_MS) {
                f.pollFirst();
            }
            f.addLast(agora);
            return f;
        });
        return tentativas.size() <= MAX_TENTATIVAS;
    }
}
