package id.co.nds.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.co.nds.catalogue.entities.SaleEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.models.ResponseModel;
import id.co.nds.catalogue.models.SaleModel;
import id.co.nds.catalogue.services.SaleService;

@RestController
@RequestMapping("/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> postSale(@RequestBody SaleModel saleModel) {
        try {

            SaleEntity saleEntity = saleService.doSale(saleModel);

            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("New sale is successfully added");
            responseModel.setData(saleEntity);
            return ResponseEntity.ok(responseModel);
        } catch (ClientException e) {
            ResponseModel response = new ResponseModel();
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseModel response = new ResponseModel();
            response.setMessage("Sorry, there is a failure on our server.");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
