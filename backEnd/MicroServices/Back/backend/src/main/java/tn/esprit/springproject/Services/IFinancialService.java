package tn.esprit.springproject.Services;

import tn.esprit.springproject.Entities.Financial;

import java.util.List;

public interface IFinancialService {
 List<Financial> getAllFinancials();
 Financial getbyid(Integer financialId);
 Financial modifyFinancial(Financial financial);
 void deleteFinancial(Integer idfinancial);
 Financial addFinancialToProject(Financial financial, Integer projectId);
 List<Financial> getFinancialsByProjectId(Integer projectId); // Updated to return List
}


