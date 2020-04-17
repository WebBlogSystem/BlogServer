package service;

import domain.EssayCate;

public interface EssayCateService {
    public boolean addEssayCate(int essayId,int cateId);
    public EssayCate queryEssayCate(int essayId,int cateId);
    public boolean deleteEssayCate(int essayId,int cateId);
}
