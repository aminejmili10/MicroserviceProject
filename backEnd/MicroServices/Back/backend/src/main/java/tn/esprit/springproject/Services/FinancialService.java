package tn.esprit.springproject.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entities.Financial;
import tn.esprit.springproject.Entities.Project;
import tn.esprit.springproject.Repository.FinancialRepository;
import tn.esprit.springproject.Repository.ProjectRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FinancialService implements IFinancialService {
    FinancialRepository financialRepository;
    ProjectRepository projectRepository;

    public List<Financial> getAllFinancials() {
        return financialRepository.findAll();
    }

    public Financial getbyid(Integer financialId) {
        return financialRepository.findById(financialId).get();
    }

    public Financial modifyFinancial(Financial financial) {
        return financialRepository.save(financial);
    }

    public void deleteFinancial(Integer idfinancial) {
        financialRepository.deleteById(idfinancial);
    }

    public Financial addFinancialToProject(Financial financial, Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("❌ Aucun projet trouvé avec l'ID : " + projectId));
        financial.setProject(project);
        return financialRepository.save(financial);
    }

    public List<Financial> getFinancialsByProjectId(Integer projectId) {
        return financialRepository.findAllByProjectId(projectId);
    }

}
