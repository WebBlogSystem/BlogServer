package service;

import domain.Attention;
import org.springframework.stereotype.Service;


public interface AttentionService {
    public boolean deleteAttention(Attention attention);
    public boolean addAttention(Attention attention);
    public Attention getAttention(Attention attention);
}
