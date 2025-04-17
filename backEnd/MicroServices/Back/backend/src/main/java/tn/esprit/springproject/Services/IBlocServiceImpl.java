package tn.esprit.springproject.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entities.Bloc;
import tn.esprit.springproject.Repository.BlocReopsitory;

import java.util.List;
@AllArgsConstructor
@Service
public class IBlocServiceImpl implements IBlocService{
    BlocReopsitory blocReopsitory;
    @Override
    public Bloc addBloc(Bloc bloc) {
        return blocReopsitory.save(bloc);
    }

    @Override
    public List<Bloc> getBlocks() {
        return blocReopsitory.findAll();
    }

    @Override
    public Bloc modifyBloc(Bloc bloc) {
        Bloc existingBloc = blocReopsitory.findById(bloc.getIdBloc()).orElse(null);

        throw new IllegalArgumentException("Bloc with ID " + bloc.getIdBloc() + " not found");
    }

    @Override
    public void deleteBloc(Long idBloc) {
      blocReopsitory.deleteById(idBloc);
    }

    @Override
    public Bloc retrieveBloc(Long idBloc) {
        return blocReopsitory.findById(idBloc).orElseThrow(()->new IllegalArgumentException("Block with ID " + idBloc + " not found"));
    }
}
