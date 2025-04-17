package tn.esprit.springproject.Services;

import tn.esprit.springproject.Entities.Bloc;

import java.util.List;

public interface IBlocService {
    Bloc addBloc(Bloc bloc);
    List<Bloc> getBlocks();
    Bloc modifyBloc(Bloc bloc);
    void deleteBloc(Long idBloc);
    Bloc retrieveBloc(Long idBloc);
}
