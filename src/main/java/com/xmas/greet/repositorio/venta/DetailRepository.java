package com.xmas.greet.repositorio.venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmas.greet.modelo.venta.Detail;

public interface DetailRepository extends JpaRepository<Detail, Long>{
List<Detail> findBySale_Id(Long saleId);

}
