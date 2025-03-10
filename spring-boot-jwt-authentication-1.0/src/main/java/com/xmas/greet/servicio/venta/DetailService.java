package com.xmas.greet.servicio.venta;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xmas.greet.modelo.venta.Detail;
import com.xmas.greet.repositorio.venta.DetailRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DetailService {

    private DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }
    public void createDetail(Detail detail){
        this.detailRepository.save(detail);
    } 
    public List<Detail> getDetailbySale(Long saleId){
        return this.detailRepository.findBySale_Id(saleId) ;
    }
    public List<Detail> findDetailAll(){
        return  this.detailRepository.findAll();
    }



}
