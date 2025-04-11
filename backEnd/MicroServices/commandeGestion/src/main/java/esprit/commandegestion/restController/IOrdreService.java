package esprit.commandegestion.restController;

import java.util.List;

public interface IOrdreService {
    List<Order> retrieveAllOrder();
    Order addOrder(Order c); // ajouter l’équipe avec son détail
    Order updateOrder (Order c);
    Order retrieveOrder (Integer id);
}
