package service;

import domain.Up;

public interface UpService {
    public int getUps(int essayId,int flag);
    public Up queryUpByUserIdAndEssayId(int userId,int essayId);
    public void addUp(int userId,int essayId,int flag);
    public void updateUp(int id,int flag);
}
