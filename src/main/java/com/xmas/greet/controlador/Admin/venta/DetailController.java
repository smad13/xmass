package com.xmas.greet.controlador.Admin.venta;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xmas.greet.modelo.venta.Detail;
import com.xmas.greet.servicio.venta.DetailService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/saleDetail")
public class DetailController {

    private final DetailService detailService;

    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping("/{sale_id}")
    public ResponseEntity<List<Detail>> getDetailBySale(@PathVariable("sale_id")Long id){
        return new ResponseEntity<>(this.detailService.getDetailbySale(id), HttpStatus.OK);
        
    }


}
